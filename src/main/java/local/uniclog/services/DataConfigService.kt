package local.uniclog.services

import local.uniclog.ui.controlls.model.MacrosItem
import local.uniclog.utils.ConfigConstants.TEMPLATE_CONFIG_PATH
import local.uniclog.services.support.FileServiceWrapper.loadObjectFromJson as load
import local.uniclog.services.support.FileServiceWrapper.saveObjectAsJson as save

class DataConfigService {

    private data class DataConfig(val items: MutableList<MacrosItem> = arrayListOf())

    private val config: DataConfig

    init {
        config = loadConfig() ?: saveConfig(DataConfig())
    }

    private fun saveConfig(config: DataConfig) = save(TEMPLATE_CONFIG_PATH, config, DataConfig::class.java)
    private fun loadConfig(): DataConfig? = load(TEMPLATE_CONFIG_PATH, DataConfig::class.java)

    /**
     * Получение конфига
     *
     * @return коллекция обьектов макросов
     */
    fun getItems(): List<MacrosItem> = config.items

    /**
     * Замена элемента по индексу
     *
     * @param index index
     * @param item расположение файла
     */
    fun modifyItemByIndex(index: Int, item: MacrosItem) {
        config.items[index] = item
        saveConfig(config)
    }

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

}
