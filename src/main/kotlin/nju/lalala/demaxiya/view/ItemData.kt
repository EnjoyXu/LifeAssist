package nju.lalala.demaxiya.view

//数据储存的类
class ItemData(
    val type: String,
    var number: Int,

    ) {
    companion object {
        // 静态变量，类变量，每次构造时加1，用来标志不同的物体
        var ID_NUMBER = 0
    }

    init {
        val id_number = ID_NUMBER
        ID_NUMBER += 1
    }


}

