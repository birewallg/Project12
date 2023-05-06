package local.uniclog.services.entity

import local.uniclog.ui.model.MacrosItem

data class DataConfig(private val items: MutableList<MacrosItem> = mutableListOf()) {
    val clone: List<MacrosItem> get() = items.toList()

    @Transient
    var changed: Boolean = false

    fun addItem(item: MacrosItem) = action { items.add(item) }
    fun removeItem(item: MacrosItem) = action { items.remove(item) }
    fun clearAllItems() = action { items.clear() }
    fun modifyItemByIndex(index: Int, item: MacrosItem) =
        takeIf { index in items.indices }?.let { action { items[index] = item } }

    fun <T> save(config: T) = let { changed = config !is DataConfig }
    private fun action(action: () -> Unit) = action.invoke().let { changed = true }
}