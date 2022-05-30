package nju.lalala.demaxiya.view

import javafx.scene.control.TabPane
import javafx.scene.layout.FlowPane
import javafx.scene.layout.Priority
import nju.lalala.demaxiya.css.UICSS
import tornadofx.*

class UIView() : View() {
    init {
        title = "LifeAssist"
        // 设定正好一行最少10个元素
        primaryStage.minWidth = 570.0 + 1e-13
        primaryStage.minHeight = 400.0
    }

    override val root = vbox(spacing = 5) {
        tabpane {
            vgrow = Priority.ALWAYS
            tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE
            tab("Home") {
                // scroll pane + flow pane 构建一个可以滚动的自动折叠元素流的pane
                // 需要将外部scroll pane的可视区域大小时刻传给内部的flow pane
                scrollpane {
                    flowpane {
                        addClass(UICSS.flowPane)
                        label("item 1")
                        label("item 2")
                        label("item 3")
                    }
                }.viewportBoundsProperty().addListener { _, _, bounds ->
                    children.filterIsInstance<FlowPane>().apply {
                        prefWidth = bounds.width
                        prefHeight = bounds.height
                    }
                }
            }
            tab("School") {
                scrollpane {
                    flowpane {
                        addClass(UICSS.flowPane)
                        label("item 1")
                        label("item 2")
                        label("item 3")
                        label("item 4")
                        label("item 5")
                        label("item 6")
                        label("item 7")
                        label("item 8")
                        label("item 9")
                        label("item 10")
                        label("item 11")
                        label("item 12")
                    }
                }.viewportBoundsProperty().addListener { _, _, bounds ->
                    children.filterIsInstance<FlowPane>().apply {
                        prefWidth = bounds.width
                        prefHeight = bounds.height
                    }
                }
            }
        }
        tabpane {
            vgrow = Priority.ALWAYS
            tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE
            tab("Package") {
                scrollpane {
                    flowpane {
                        addClass(UICSS.flowPane)
                        label("item 1")
                        label("item 2")
                        label("item 3")
                        label("item 4")
                        label("item 5")
                        label("item 6")
                        label("item 7")
                        label("item 8")
                        label("item 9")
                        label("item 10")
                        label("item 11")
                        label("item 12")
                        label("item 13")
                    }
                }.viewportBoundsProperty().addListener { _, _, bounds ->
                    children.filterIsInstance<FlowPane>().apply {
                        prefWidth = bounds.width
                        prefHeight = bounds.height
                    }
                }
            }
        }
    }
}