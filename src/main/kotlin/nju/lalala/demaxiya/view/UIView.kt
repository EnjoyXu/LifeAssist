package nju.lalala.demaxiya.view

import javafx.application.Application
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleLongProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList

import javafx.scene.Scene
import javafx.scene.control.Label

import javafx.scene.control.TabPane

import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import javafx.stage.Stage
import tornadofx.vgrow


class UIView : Application() {
    companion object {
        //用来跟踪是哪个面板被选中
        var selectedTabIndex = SimpleIntegerProperty(1)
        var changeInfo = SimpleListProperty(FXCollections.observableList(listOf(0, 0)))
        var changeInfo_idNumber = SimpleLongProperty(0)
        var changeFlag = SimpleIntegerProperty(1)

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

    // 数据管理列表
    var itemList = mutableListOf<MutableList<ItemData>>()

    private fun initializeUI() {
        //初始化UI
        tabList.clear()
        // 根据itemList中的内容来更新图形化界面
        for (i in 0 until itemList.size) {
            // 创建Tab
            val _tab = MyTab("$i", i)
            itemList[i].forEach {
                _tab.add(MyLabel(it, i))
            }
//             在TabList中添加tab
            tabList.add(_tab)

            when (i) {
                0 -> tabpaneDown.tabs.add(_tab)
                else -> tabpaneUp.tabs.add(_tab)
            }
        }

    }

    private fun update() {
        //更新
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
    override fun start(primaryStage: Stage) {
        //模拟一下数据

        val itemTabList1 = mutableListOf<ItemData>(
            ItemData("毛衣", 2,true,"毛衣ssssssss")
        )
        val itemTabList2 = mutableListOf<ItemData>(
            ItemData("袜子", 5,false,"袜子")
        )
        val itemTabList3 = mutableListOf<ItemData>(

        )

        // 数据管理列表
        itemList.apply {
            add(itemTabList1)
            add(itemTabList2)
            add(itemTabList3)
        }
        //根据数据生成ui
        initializeUI()

        changeFlag.addListener { observable, oldValue, newValue ->
            update()
        }

        tabpaneUp.selectionModel.selectedItemProperty().addListener { observable, oldValue, newValue ->

            Companion.selectedTabIndex = (newValue as MyTab).index
        }

        // root
        val root = VBox().apply {
            spacing = 5.0
            //为避免初始化的时候没有tab内容而报错，不得不在最后才绑定
            children.add(tabpaneUp)
            children.add(tabpaneDown)
        }

        val scene = Scene(root)
        scene.root.stylesheets.add("file:src/main/kotlin/nju/lalala/demaxiya/css/UICSS.css")
//        scene.stylesheets.add(f.toURI().toExternalForm() )

        primaryStage.apply {
            title = "LifeAssist"
            minWidth = 570.0
            minHeight = 400.0

            this.scene = scene
            show()
        }
        tabpaneUp.prefHeightProperty().bind(scene.heightProperty().divide(2))
        tabpaneDown.prefHeightProperty().bind(scene.heightProperty().divide(2))


    }

}