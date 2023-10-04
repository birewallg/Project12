package local.uniclog.services

import local.uniclog.services.entity.DataConfig
import local.uniclog.ui.model.MacrosItem
import local.uniclog.utils.ConfigConstants.CONFIG_MODIFY_PERIOD
import local.uniclog.utils.ConfigConstants.TEMPLATE_CONFIG_PATH
import kotlin.concurrent.fixedRateTimer
import local.uniclog.services.support.FileServiceWrapper.loadObjectFromJson as load
import local.uniclog.services.support.FileServiceWrapper.saveObjectAsJson as save

/**
 * Сервис управляющий конфигурацией
 */
class DataConfigService {

    @Volatile
    private var config: DataConfig

    init {
        config = loadConfig() ?: saveConfig(DataConfig())

        fixedRateTimer(
            name = "save-config-timer", daemon = true,
            initialDelay = CONFIG_MODIFY_PERIOD, period = CONFIG_MODIFY_PERIOD
        )
        { if (config.changed) config.save(saveConfig(config)) }
    }

    private fun saveConfig(config: DataConfig) = save(TEMPLATE_CONFIG_PATH, config)
    private fun loadConfig(): DataConfig? = load(TEMPLATE_CONFIG_PATH, DataConfig::class.java)

    fun forceSaveConfiguration(): Boolean = saveConfig(config) is DataConfig
    fun getItemsClone(): List<MacrosItem> = config.clone
    fun modifyItemByIndex(index: Int, item: MacrosItem) = config.modifyItemByIndex(index, item)
    fun addItem(item: MacrosItem) = config.addItem(item)
    fun removeItem(item: MacrosItem) = config.removeItem(item)
    fun clearItems() = config.clearAllItems()

}
