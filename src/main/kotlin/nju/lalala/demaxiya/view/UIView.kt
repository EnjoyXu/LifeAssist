package nju.lalala.demaxiya.view
import javafx.application.Application
import javafx.beans.value.ChangeListener
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import javafx.scene.layout.FlowPane
import javafx.scene.layout.VBox
import javafx.stage.Stage
import java.io.File
import kotlin.io.path.toPath


class UIView :Application(){
    override fun start(primaryStage: Stage) {

        // 上面的Tab 板块
        val tabHome = Tab("Home").apply {
            content = ScrollPane().apply {
                content = FlowPane().apply {

                    children.addAll(
                            Label("1"),
                            Label("2"),
                            Label("3"),
                            Label("4"))
                }

                viewportBoundsProperty().addListener(ChangeListener { observable, oldValue, newValue ->
                    println(observable)
                })

//                viewportBoundsProperty().addListener { _, _, bounds ->
//                    this@apply.children.filterIsInstance<FlowPane>().apply {
//                        prefWidth = bounds.width
//                        prefHeight = bounds.height
//                    }
//                }
            }
        }

        val tabSchool = Tab("School").apply {
            content = ScrollPane().apply {
                content = FlowPane().apply {

                    children.addAll(
                        Label("1"),
                        Label("2"),
                        Label("3"),
                        Label("4"))

                }
            }
        }


        val tabpane1 = TabPane().apply { tabs.addAll(tabHome,tabSchool) }

        //下面的Tab 模块


        val root = VBox().apply {

            children.add(tabpane1)
        }



        val scene = Scene(root)
        scene.root.stylesheets.add("file:src/main/kotlin/nju/lalala/demaxiya/css/UICSS.css")
//        scene.stylesheets.add(f.toURI().toExternalForm() )

        primaryStage.apply {
            minWidth = 570.0
            minHeight = 400.0

            this.scene = scene
            show()
        }



    }
}