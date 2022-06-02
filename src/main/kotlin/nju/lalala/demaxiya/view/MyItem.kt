package nju.lalala.demaxiya.view

import javafx.beans.property.SimpleIntegerProperty
import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.control.Tab
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.layout.FlowPane


class MyTab(_string: String, _index: Int) : Tab(_string) {
    //用来标志Tab的序号
    var index = SimpleIntegerProperty(_index)

    init {
        this.apply {
            text = _string
            content = ScrollPane().apply {
                content = FlowPane()
                (this.content as FlowPane).prefHeightProperty().bind(this.heightProperty())
                (this.content as FlowPane).prefWidthProperty().bind(this.widthProperty())
            }
        }

        // 绑定一个数据list

    }

    fun add(node: Node) {
        //添加一个node
        ((this.content as ScrollPane).content as FlowPane).children.add(node)
    }

    fun removeAt(index: Int) {
        //删除一个node
        ((this.content as ScrollPane).content as FlowPane).children.removeAt(index)
    }


}

class MyLabel(_string: String, _tabIndex: Int = 0) : Label() {
    var tabIndex = _tabIndex

    init {
        this.apply {
            text = _string

            addEventHandler(MouseEvent.MOUSE_CLICKED, EventHandler {
                //左键
                if (it.button.name == MouseButton.PRIMARY.name) {
                    println("left")
                    if (tabIndex != 0) {
                        println("From $tabIndex To 0")
                    } else {
                        println("From $tabIndex To " + UIView.selectedTabIndex.value)
                    }
                } else if (it.button.name == MouseButton.SECONDARY.name) {
                    println("Right")
                }
            })

        }
    }

}

