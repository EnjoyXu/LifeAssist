package nju.lalala.demaxiya.view
import javafx.application.Application
import javafx.beans.value.ChangeListener
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import javafx.scene.layout.FlowPane
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import javafx.stage.Stage
import tornadofx.vgrow
import nju.lalala.demaxiya.view.MyTab


class UIView :Application(){
    override fun start(primaryStage: Stage) {

        // 上面的Tab 板块
        val tabHome = MyTab("Home").apply {
            add(Label("1"))
            add(Label("2"))
            add(Label("3"))
            add(Label("4"))
            add(Label("5"))
        }

        val tabSchool = MyTab("School").apply {
            add(Label("1"))
            add(Label("2"))
            add(Label("3"))
            add(Label("4"))
            add(Label("5"))
        }


        val tabpane1 = TabPane().apply {
            vgrow = Priority.ALWAYS
            tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE
            tabs.addAll(tabHome,tabSchool)
        }

        //下面的Tab 模块

        val tabPackage = MyTab("Package").apply {
            add(Label("1"))
            add(Label("2"))
            add(Label("3"))
            add(Label("4"))
            add(Label("5"))
        }



        val tabpane2  = TabPane().apply {
            vgrow = Priority.ALWAYS
            tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE
            tabs.add(tabPackage)
        }

        // root
        val root = VBox().apply {
            spacing = 5.0
            children.add(tabpane1)
            children.add(tabpane2)
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