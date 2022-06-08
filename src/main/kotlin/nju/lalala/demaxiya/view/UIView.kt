package nju.lalala.demaxiya.view

import javafx.application.Application
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleLongProperty
import javafx.collections.FXCollections

import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType


import javafx.scene.control.TabPane
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination

import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import javafx.stage.Stage
import tornadofx.vgrow


class UIView : Application() {
    companion object {
        //用来跟踪是哪个面板被选中
        var selectedTabIndex : SimpleIntegerProperty? = SimpleIntegerProperty(1)
        // 左键则是从哪个tab到哪个tab，右键改变的则为在哪个tab
        var changeInfo = SimpleListProperty(FXCollections.observableList(listOf(0, 0)))
        //点击物品的id
        var changeInfo_idNumber = SimpleLongProperty(0)
        // 跟踪是否左键点击
        var clickFlag = SimpleIntegerProperty(1)
        // 跟踪是否需要改变信息
        var changeFlag = SimpleIntegerProperty(1)
        // 存储改变信息的item信息
        lateinit var changeItem :ItemData

    }

    // 创建上面的Tab 板块
    val tabpaneUp = TabPane().apply {
        vgrow = Priority.ALWAYS
        tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE

    }

    // 创建下面的Tab 模块
    val tabpaneDown = TabPane().apply {
        vgrow = Priority.ALWAYS
        tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE

    }

    // 创建tab图形化列表
    var tabList = mutableListOf<MyTab>()

    var tabNameList = mutableListOf<String>()
    // 数据管理列表
    var itemList = mutableListOf<MutableList<ItemData>>()

    private fun initializeUI() {
        //根据itemList初始化UI
        tabpaneUp.tabs.add(MyTab(" ",0))
        tabpaneUp.tabs.removeAll(tabList)
        tabList.clear()
        tabpaneDown.tabs.clear()
        // 根据itemList中的内容来更新图形化界面
        for (i in 0 until itemList.size) {
            // 创建Tab
            val _tab = MyTab(MyController.tabNameList[i], i)
            itemList[i].forEach {
                _tab.add(MyLabel(it, i))
            }
//             在TabList中添加tab
            tabList.add(_tab)

            when (i) {
                0 -> tabpaneDown.tabs.add(tabList.last())
                else -> tabpaneUp.tabs.add(tabList.last())
            }
        }
        tabpaneUp.tabs.removeAt(0)
        //因为clear了tab，所以必须重置已选的tab
        selectedTabIndex?.value = 1

    }

    private fun update() {
        //移动事件后的更新
        val _fromTabIdex: Int = changeInfo[0]
        val _toTabIndex: Int = changeInfo[1]
        val _id_number: Long = changeInfo_idNumber.value

        val _fromIndex = itemList[_fromTabIdex].map { it.id_number }.indexOf(_id_number)
        val _item: ItemData = itemList[_fromTabIdex][_fromIndex]
//        println("item number ${_item.number}")
        when (_item.number) {
            1 -> {
                itemList[_fromTabIdex].remove(_item)
                tabList[_fromTabIdex].removeAt(_fromIndex)
            }
            else -> {
                _item.number -= 1
                tabList[_fromTabIdex].changeNumber(_fromIndex, -1)
            }

        }
        // 查找目标tab中是否有该元素
        val _toIndex = itemList[_toTabIndex].map { it.id_number }.indexOf(_id_number)
        when (_toIndex) {
            -1 -> {
                val newItem = _item.copy().apply { this.number = 1 }
                itemList[_toTabIndex] += newItem
                tabList[_toTabIndex].add(
                    MyLabel(newItem ,_toTabIndex)
                )
            }
            else -> {
                itemList[_toTabIndex][_toIndex].number += 1
                tabList[_toTabIndex].changeNumber(_toIndex, 1)

            }
        }
    }

    private fun updateData(){
        // 右键后直接更改信息
        val _fromTabIdex: Int = changeInfo[0]
        val _id_number: Long = changeItem.id_number

        val _fromIndex = itemList[_fromTabIdex].map { it.id_number }.indexOf(_id_number)

        itemList[_fromTabIdex][_fromIndex] = changeItem

        itemList.forEach{list->
            list.forEach{
                if (it.id_number == _id_number){
                    it.description = changeItem.description
                    it.thickness = changeItem.thickness
                    it.picPath = changeItem.picPath
                    it.type = changeItem.type
                }
            }

        }

    }

    private fun touchData(){
        itemList =MyController.itemList
        tabNameList  = MyController.tabNameList
    }


    private fun sortData(){
        // 根据薄-厚，类型名称进行排序
        itemList.forEach {tabList ->
            tabList.sortWith(Comparator{item1,item2->
                //厚薄升序排列，厚-薄
                if (item1.thickness != item2.thickness){
                    item1.thickness.compareTo(item2.thickness)
                }else{
                    item1.type.compareTo(item2.type)
                }

            })

        }
    }



    override fun start(primaryStage: Stage) {
        //模拟一下数据

//        val itemTabList1 = mutableListOf<ItemData>(
//            ItemData("毛衣", 2,true,"毛衣ssssssss")
//        )
//        val itemTabList2 = mutableListOf<ItemData>(
//            ItemData("袜子", 5,false,"袜子")
//        )
//        val itemTabList3 = mutableListOf<ItemData>(
//
//        )
//
//        // 数据管理列表
//        itemList.apply {
//            add(itemTabList1)
//            add(itemTabList2)
//            add(itemTabList3)
//        }
        //得到数据
        touchData()
        //根据数据生成ui
        initializeUI()

        //点击label事件的响应
        clickFlag.addListener { observable, oldValue, newValue ->
            update()
        }

        //更改数据的响应
        changeFlag.addListener{observable, oldValue, newValue ->
            updateData()
            MyController.writeInTo(tabNameList,itemList)
            initializeUI()

        }

        // 监听上面版选定的tab
        tabpaneUp.selectionModel.selectedItemProperty().addListener { observable, oldValue, newValue ->
            selectedTabIndex = (newValue as MyTab).index
        }



        // root
        val root = VBox().apply {
            spacing = 5.0
            //为避免初始化的时候没有tab内容而报错，不得不在最后才绑定
            children.add(tabpaneUp)
            children.add(tabpaneDown)
        }

        val scene = Scene(root)

        scene.root.stylesheets.add("file:data/config/UICSS.css")
        // 设置快捷键 Ctrl+ S 保存
        scene.accelerators.put(
            KeyCodeCombination(
                KeyCode.S,KeyCodeCombination.CONTROL_ANY
            )
        ){
            sortData()
            MyController.writeInTo(tabNameList,itemList)
            touchData()
            initializeUI()
            println("saved Successfully")
        }


        primaryStage.apply {
            title = "LifeAssist"
            minWidth = 570.0
            minHeight = 400.0

            this.scene = scene
            show()

            setOnCloseRequest {it ->
                Alert(Alert.AlertType.CONFIRMATION).apply {
                    width = 50.0
                    width = 50.0
                    title = "退出"
                    headerText = "是否保存?"
                    if (this.showAndWait().get() == ButtonType.OK){
                        sortData()
                        MyController.writeInTo(tabNameList,itemList)
                    }
                }
            }

        }
        tabpaneUp.prefHeightProperty().bind(scene.heightProperty().divide(2))
        tabpaneDown.prefHeightProperty().bind(scene.heightProperty().divide(2))

    }



}