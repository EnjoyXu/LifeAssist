package nju.lalala.demaxiya.view

import kotlinx.serialization.json.*
import java.io.File
import java.io.StringReader
import kotlin.reflect.typeOf

//数据储存的类
class ItemData(
    val type: String, //类型
    var number: Int, //数量
    val isthick : Boolean,
    var description : String  = "" // 描述
    ) {

    companion object{
        // 创建type-图片url键值对
        val map = mapOf<String,String>("袜子" to "C:/Users/80605/Downloads/icons8-socks-32.png")
    }


    var id_number: Long = System.currentTimeMillis()
    fun copy(): ItemData {
        val newItem = ItemData(this.type, this.number, this.isthick,this.description).apply {
            this.id_number = this@ItemData.id_number
        }
        return newItem
    }
}

object MyMap{
    private fun getMyJson(): JsonElement {
        val string = File("src/data/config/icon_config.json").readText()
//            println(string)
        return Json.parseToJsonElement(string)
    }

    private fun getAllMap(json: JsonElement):Map<String,String>{
        val map= mutableMapOf<String,String>()
        for(firstLayer in json.jsonObject.keys){
            println(firstLayer)
            for(secondLayer in (json.jsonObject[firstLayer] as JsonElement).jsonObject.keys){
                println((json.jsonObject[firstLayer] as JsonElement).jsonObject[secondLayer])
                map += secondLayer to ((json.jsonObject[firstLayer] as JsonElement).jsonObject[secondLayer].toString())
            }
        }

        return map
    }

    val json = getMyJson()

    val map = getAllMap(json)







}