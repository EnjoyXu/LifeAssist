package nju.lalala.demaxiya.view

import javafx.application.Application
import javafx.beans.property.SimpleIntegerProperty

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

    private fun update(isFirst: Boolean = false) {
        // 根据itemList中的内容来更新图形化界面
        for (i in 0 until itemList.size) {
            // 创建Tab
            val _tab = MyTab("$i", i)
            itemList[i].forEach {
                _tab.add(MyLabel("${it.number}", i))
            }
            // 在TabList中添加tab
            tabList.add(_tab)

            if (isFirst) {
                //将Tab置于不同的tabpane面板中
                if (i == 0) {
                    tabpaneDown.tabs.add(_tab)
                } else {
                    tabpaneUp.tabs.add(_tab)
                }
            }

        }
    }


    override fun start(primaryStage: Stage) {


        var itemTabList1 = mutableListOf<ItemData>(
            ItemData("毛衣", 2),
            ItemData("袜子", 5)
        )
        var itemTabList2 = mutableListOf<ItemData>(
            ItemData("毛衣", 2),
            ItemData("袜子", 5)
        )
        var itemTabList3 = mutableListOf<ItemData>(
            ItemData("毛衣", 2),
            ItemData("袜子", 5)
        )

        // 数据管理列表
        itemList.apply {
            add(itemTabList1)
            add(itemTabList2)
            add(itemTabList3)
        }

        update(true)


        // root
        val root = VBox().apply {
            spacing = 5.0
            //为避免初始化的时候没有tab内容而报错，不得不在最后才绑定
            children.add(tabpaneUp.apply { selectedTabIndex.bind((this.selectionModel.selectedItem as MyTab).index) })
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


    }
}