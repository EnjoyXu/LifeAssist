package nju.lalala.demaxiya.view

import kotlinx.serialization.json.*
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream



//数据储存的类
class ItemData(
    var type: String, //类型
    var number: Int, //数量
    var thickness : String,
    var description : String  = "", // 描述
    _idNumber : Long = (-1.0).toLong(),
    var picPath : String = ""
    ) {

    var id_number: Long = when(_idNumber){
        (-1).toLong() -> System.currentTimeMillis()
        else -> _idNumber
    }


    fun copy(): ItemData {
        val newItem = ItemData(this.type, this.number, this.thickness,this.description).apply {
            this.id_number = this@ItemData.id_number
        }
        return newItem
    }
}

object MyIconMap{
    private fun getMyJson(): JsonElement {
        val string = File("data/config/icon_config.json").readText()
//            println(string)
        return Json.parseToJsonElement(string)
    }

    private fun getAllMap(json: JsonElement):Triple<Map<String,String>,List<String>,List<List<String>>>{
        val map= mutableMapOf<String,String>()
        val firstLayerKeys = mutableListOf<String>()
        val secondLayerKeys = mutableListOf<List<String>>()
        for(firstLayer in json.jsonObject.keys){
            firstLayerKeys += firstLayer

            val _list = mutableListOf<String>()
            for(secondLayer in (json.jsonObject[firstLayer] as JsonElement).jsonObject.keys){
                _list += secondLayer
                map += secondLayer to ((json.jsonObject[firstLayer] as JsonElement).jsonObject[secondLayer].toString())
            }
            secondLayerKeys += _list
        }

        return Triple(map,firstLayerKeys.toList(),secondLayerKeys.toList())
    }

    val json = getMyJson()

    private val triple =  getAllMap(json)

    val typeToIconMap = triple.first
    val firstKeys = triple.second
    val secondKeys = triple.third

}

object MyController{
    private fun readExcel():Pair<MutableList<String>,MutableList<MutableList<ItemData>>>{
//        :MutableList<MutableList<ItemData>>

        val tabNameList = mutableListOf<String>()
        val itemList = mutableListOf<MutableList<ItemData>>()

        // 读取excel数据返回itemList
        val workbook = WorkbookFactory.create(FileInputStream("data/data.xlsx"))
        val iterator = workbook.sheetIterator()


        while (iterator.hasNext()){
            //遍历一个sheet内的内容
            val _sheet = iterator.next()

            val _tabItemList = mutableListOf<ItemData>()
            _sheet.rowIterator().asSequence().forEachIndexed{index,row->
                //不读取第一行title
                if (index != 0){
                    //获得一行的数据
                    //类型    描述	数量	    厚度  	id  图片path
                    val valuesSeq = row.cellIterator().asSequence().map{
                        when(it.cellType){
                            CellType.NUMERIC -> it.numericCellValue.toString()
                            else -> it.stringCellValue
                        }
                    }.toList()

                    //防止excel中使用清除数据留下没有数据的假行
                    if(valuesSeq.size>1){
                        //构建item
                        _tabItemList += ItemData(
                            type = valuesSeq[0] ,
                            number = valuesSeq[2].let{
                                when(it.contains(".")){
                                    true -> it.substringBefore(".").toInt()
                                    else -> it.toInt()
                                }
                            },
                            thickness = valuesSeq[3],
                            description = valuesSeq.getOrElse(1){""} as String,
                            _idNumber =   valuesSeq[4].let{
                                when(it.contains(".")){
                                    true -> it.substringBefore(".").toLong()
                                    else -> it.toLong()
                                }
                            },
                            picPath = valuesSeq.getOrElse(5){""}
                        )
                    }




                }
            }

            itemList += _tabItemList
            tabNameList += _sheet.sheetName
        }

        return Pair(tabNameList,itemList)
    }

    private var  excelData = readExcel()
    var tabNameList = excelData.first
    var itemList = excelData.second

    private fun update(){
        excelData = readExcel()
        tabNameList = excelData.first
        itemList = excelData.second
    }

    fun writeInTo(_tabNameList: MutableList<String>,_itemList: MutableList<MutableList<ItemData>>){

        val writeIntoPath = "data/data.xlsx"

        val workBook = XSSFWorkbook()
        //遍历不同的sheet
        _tabNameList.forEachIndexed{sheetIndex,sheetName->
            val _sheet = workBook.createSheet(sheetName).apply {
                //第一行先写title
                this.createRow(0).apply {
                    createCell(0).setCellValue("类型")
                    createCell(1).setCellValue("描述")
                    createCell(2).setCellValue("数量")
                    createCell(3).setCellValue("厚度")
                    createCell(4).setCellValue("id")
                    createCell(5).setCellValue("图片名")

                }
            }

            val _sheetItemList = _itemList[sheetIndex]

            //遍历sheet内不同的物品
            _sheetItemList.forEachIndexed { itemIndex, itemData ->
                //注意因为第一行是title，所以写入时index+1
                _sheet.createRow(itemIndex+1).apply {
                    createCell(0).setCellValue(itemData.type)
                    createCell(1).setCellValue(itemData.description)
                    createCell(2).setCellValue((itemData.number).toString())
                    createCell(3).setCellValue(itemData.thickness)
                    createCell(4).setCellValue(itemData.id_number.toString())
                    createCell(5).setCellValue(itemData.picPath)
                }
            }
        }

        val fileOutputStream = FileOutputStream(writeIntoPath)
        workBook.write(fileOutputStream)
        fileOutputStream.close()

        update()
    }

}