package local.uniclog.services

import local.uniclog.ui.controlls.model.MacrosItem
import local.uniclog.utils.ConfigConstants.TEMPLATE_CONFIG_PATH
import local.uniclog.services.support.FileServiceWrapper.loadObjectFromJson as load
import local.uniclog.services.support.FileServiceWrapper.saveObjectAsJson as save

class DataConfigService {

    private data class DataConfig(val items: MutableSet<MacrosItem>)

    private var config: DataConfig

    init {
        val createConfig: () -> DataConfig = {
            val config = DataConfig(hashSetOf())
            saveConfig(config)
            config
        }
        config = loadConfig() ?: createConfig()
    }

    /**
     * Получение конфига
     *
     * @return Set&lt;MacrosItem&gt;
     */
    fun getItems(): Set<MacrosItem> = config.items

    /**
     * Добавление нового конфига в список
     *
     * @param item расположение файла
     */
    fun addItem(item: MacrosItem) {
        config.items.add(item)
        saveConfig(config)
    }

    /**
     * Удаление конфига из списока
     *
     * @param item обьект конфига
     */
    fun removeItem(item: MacrosItem) {
        config.items.remove(item)
        saveConfig(config)
    }

    private fun saveConfig(config: DataConfig) = save(TEMPLATE_CONFIG_PATH, config, DataConfig::class.java)
    private fun loadConfig(): DataConfig? = load(TEMPLATE_CONFIG_PATH, DataConfig::class.java)

}
