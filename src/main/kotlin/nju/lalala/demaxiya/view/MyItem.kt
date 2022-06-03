package nju.lalala.demaxiya.view

import javafx.beans.property.SimpleIntegerProperty
import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.control.ContentDisplay
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.control.Tab
import javafx.scene.image.ImageView
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.layout.FlowPane
import tornadofx.imageview


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
    }

    fun add(node: Node) {
        //添加一个node
        ((this.content as ScrollPane).content as FlowPane).children.add(node)
    }

    fun removeAt(index: Int) {
        //删除一个node
        ((this.content as ScrollPane).content as FlowPane).children.removeAt(index)
    }

    fun changeNumber(labelIndex: Int, change: Int) {
        //先removeAt。再add（int，ele）新的
        val _text = (((this.content as ScrollPane).content as FlowPane).children[labelIndex] as MyLabel).text
        val _spaceIndex = _text.indexOf(" ")
        (((this.content as ScrollPane).content as FlowPane).children[labelIndex] as MyLabel).text =
            _text.substring(0, _spaceIndex) + " " +
                    ((_text.substring(_spaceIndex + 1, _text.length) as String).toInt() + change).toString()
    }

}

class MyLabel(_string: String, _idNumber: Long, _isthick:Boolean,_type:String, _tabIndex: Int = 0) : Label() {
    var tabIndex = _tabIndex
    val id_number = _idNumber

    constructor(_item:ItemData ,_tabIndex: Int) : this("${_item.description} ${_item.number}", _item.id_number,_item.isthick,_item.type,_tabIndex)

    init {
        this.apply {
            text = _string

            addEventHandler(MouseEvent.MOUSE_CLICKED, EventHandler {
                //左键
                if (it.button.name == MouseButton.PRIMARY.name) {
                    UIView.changeInfo[0] = tabIndex
                    UIView.changeInfo_idNumber.value = id_number
                    if (tabIndex != 0) {
//                        println("$id_number From $tabIndex To 0")
                        UIView.changeInfo[1] = 0

                    } else {
//                        println("$id_number From $tabIndex To " + UIView.selectedTabIndex.value)
                        UIView.changeInfo[1] = UIView.selectedTabIndex.value

                    }
                    //传递改变的信号
                    UIView.changeFlag.value = -1 * UIView.changeFlag.value

                } else if (it.button.name == MouseButton.SECONDARY.name) {

                }
            })
        }
        when(_isthick){
            true -> this.style = "-fx-background-color : #FFA07A"
            else -> this.style = "-fx-background-color : #87CEFA"
        }
        this.graphic = ImageView(ItemData.map["袜子"]).apply {}
//        this.graphic.style = "-fx-alignment : center"
//        label.setContentDisplay(ContentDisplay.TOP)
        this.contentDisplay = ContentDisplay.TOP

    }

}

