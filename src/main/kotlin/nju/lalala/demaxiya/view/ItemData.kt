package nju.lalala.demaxiya.view

//数据储存的类
class ItemData(
    val type: String,
    var number: Int,

    ) {

    var id_number: Long = System.currentTimeMillis()


    fun copy(): ItemData {
        val newItem = ItemData(this.type, this.number).apply {
            this.id_number = this@ItemData.id_number
        }
        return newItem
    }
}
