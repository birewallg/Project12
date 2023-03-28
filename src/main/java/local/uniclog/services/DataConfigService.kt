package local.uniclog.services

import local.uniclog.ui.model.MacrosItem
import local.uniclog.utils.ConfigConstants.CONFIG_MODIFY_PERIOD
import local.uniclog.utils.ConfigConstants.TEMPLATE_CONFIG_PATH
import lombok.EqualsAndHashCode
import kotlin.concurrent.fixedRateTimer
import local.uniclog.services.support.FileServiceWrapper.loadObjectFromJson as load
import local.uniclog.services.support.FileServiceWrapper.saveObjectAsJson as save

class DataConfigService {
    @EqualsAndHashCode
    private data class DataConfig(val items: MutableList<MacrosItem> = mutableListOf()) {
        @Transient
        var changed: Boolean = false
        fun modifyItemByIndex(index: Int, item: MacrosItem) = action { items[index] = item }
        fun addItem(item: MacrosItem) = action { items.add(item) }
        fun removeItem(item: MacrosItem) = action { items.remove(item) }
        fun clearItems() = action { items.clear() }
        fun <T> save(config: T) = run { changed = config !is DataConfig }

        private fun action(action: () -> Unit) {
            action()
            changed = true
        }
    }

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

    private fun saveConfig(config: DataConfig) = save(TEMPLATE_CONFIG_PATH, config, DataConfig::class.java)
    private fun loadConfig(): DataConfig? = load(TEMPLATE_CONFIG_PATH, DataConfig::class.java)

    fun forceSaveConfiguration(): Boolean = saveConfig(config) is DataConfig
    fun getItemsClone(): MutableList<MacrosItem> = config.items.toMutableList()
    fun modifyItemByIndex(index: Int, item: MacrosItem) = config.modifyItemByIndex(index, item)
    fun addItem(item: MacrosItem) = config.addItem(item)
    fun removeItem(item: MacrosItem) = config.removeItem(item)
    fun clearItems() = config.clearItems()

}
