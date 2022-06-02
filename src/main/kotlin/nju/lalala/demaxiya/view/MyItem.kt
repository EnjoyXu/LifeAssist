package nju.lalala.demaxiya.view

import javafx.scene.Node
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.control.Tab
import javafx.scene.layout.FlowPane


class MyTab(string : String): Tab(string) {
    init {
        this.apply {
            text = string
            content = ScrollPane().apply {
                content = FlowPane()
                (this.content as FlowPane).prefHeightProperty().bind(this.heightProperty())
                (this.content as FlowPane).prefWidthProperty().bind(this.widthProperty())
            }
        }
    }

    fun add(node: Node){
        ((this.content as ScrollPane).content as FlowPane).children.add(node)
    }

    fun removeAt(index:Int){
        ((this.content as ScrollPane).content as FlowPane).children.removeAt(index)
    }


}

class MyLable {

}

