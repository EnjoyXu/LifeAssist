package nju.lalala.demaxiya.css

import javafx.geometry.Pos
import javafx.scene.control.ScrollPane
import javafx.scene.layout.BorderStrokeStyle
import tornadofx.*

class UICSS : Stylesheet() {
    companion object {
        val flowPane by cssclass()
    }

    init {
        root{
            backgroundColor += c("#111111")
        }
        scrollPane {
            fitToWidth = true
            fitToHeight = true
            vBarPolicy = ScrollPane.ScrollBarPolicy.NEVER
        }
        flowPane {
            backgroundColor += c("#888888")
            padding = box(5.0.px)
            hgap = 5.0.px
            vgap = 5.0.px
            prefHeight = 50.0.px
        }
        label {
            backgroundColor += c("#cccccc")
            alignment = Pos.CENTER
            prefWidth = 50.0.px
            prefHeight = 50.0.px
            borderStyle += BorderStrokeStyle.SOLID
        }
    }
}