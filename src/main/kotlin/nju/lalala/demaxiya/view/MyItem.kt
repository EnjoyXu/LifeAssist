package nju.lalala.demaxiya.view

import javafx.beans.property.IntegerProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.ChoiceBox
import javafx.scene.control.ContentDisplay
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.control.Separator
import javafx.scene.control.Tab
import javafx.scene.control.TextFormatter
import javafx.scene.image.ImageView
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.layout.FlowPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.stage.FileChooser
import javafx.stage.Stage
import javafx.util.StringConverter
import tornadofx.*


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

class MyLabel(val item : ItemData,  var tabIndex: Int = 0) : Label() {


    init {
        this.apply {
            text = item.description+ " " + item.number.toString()

            addEventHandler(MouseEvent.MOUSE_CLICKED, EventHandler {
                //左键
                if (it.button.name == MouseButton.PRIMARY.name) {
                    if (it.clickCount == 1){
                        UIView.changeInfo[0] = tabIndex
                        UIView.changeInfo_idNumber.value = item.id_number
                        if (tabIndex != 0) {
//                        println("$id_number From $tabIndex To 0")
                            UIView.changeInfo[1] = 0

                        } else {
//                        println("$id_number From $tabIndex To " + UIView.selectedTabIndex.value)
                            UIView.changeInfo[1] = UIView.selectedTabIndex?.value

                        }
                        //传递改变的信号
                        UIView.clickFlag.value *= -1
                    }


                } else if (it.button.name == MouseButton.SECONDARY.name) {
                    if (it.clickCount == 1){
                        MyPopUpStage(this@MyLabel).show()

//                        UIView.changeFlag.value *= -1

                    }

                }
            })
        }
        //设置背景颜色
        when(item.thickness){
            "厚" -> this.style = "-fx-background-color : #FFA07A"
            "薄" -> this.style = "-fx-background-color : #87CEFA"
        }

        //设置icon
        if (MyIconMap.typeToIconMap.contains(item.type)){
            this.graphic = ImageView("file:data/icons/"+MyIconMap.typeToIconMap[item.type].toString().replace("\"",""))
        }
        this.graphic = ImageView("file:data/icons/"+MyIconMap.typeToIconMap.getOrElse(item.type){
            MyIconMap.typeToIconMap["其他"]
        }.toString().replace("\"",""))
        this.contentDisplay = ContentDisplay.TOP

    }

}

class MyPopUpStage(_myLabel:MyLabel) : Stage(){

    // Item数据
    private var item = _myLabel.item
    //tabIndex
    val tabIndex = _myLabel.tabIndex
    // 更改的图片路径
    private var changePicturePath  = item.picPath


    //面板UI
    private val root = VBox(10.0)

    private val typeChoiceBox = MyTypeChoiceBox(item.type)

    private val numberTextField = textfield (item.number.toString() ).apply {
        // 设置>0
        filterInput {change->
            !change.isAdded || change.controlNewText.let {
                it.toInt() > 0
            }
        }
    }
    private val descriptionTextField = textfield ( item.description)

    private val thickChoiceBox = ChoiceBox<String>().apply {
        items.addAll("厚","薄")
        value = item.thickness
    }

    private val choosePicture = Button("选择图片").apply {
        setOnAction {
            changePicturePath = item.picPath
            val pictureFile = FileChooser().apply {
                title = "选择图片"
            }.showOpenDialog(this@MyPopUpStage)
//            pictureFile.copyTo()

            //判断是否有选择图片
            if(pictureFile != null){
                //判断确实是图片格式
                if (pictureFile.absolutePath.substringAfter(".").lowercase() in listOf<String>("jpg","png","tif","bmp")){
                    //判断目前是否有图片显示
                    if (changePicturePath != ""){
                        root.children.removeAt(5)
                    }

                    root.children.add(5,
                        ScrollPane().apply {
                            content =  ImageView("file:" + pictureFile.absolutePath.replace("\\","/"))
                        }
                    )
                }
                changePicturePath = pictureFile.absolutePath
            }
        }
    }
    private val confirm = Button("确认").apply {
        setOnAction { confirmInfo() }
    }
    private fun confirmInfo(){
        var _isChanged = false
        if (typeChoiceBox.value as String != item.type){
            item.type  = typeChoiceBox.value as String
            _isChanged = true
        }

        if (numberTextField.text.toInt() != item.number){
            item.number = numberTextField.text.toInt()
            _isChanged = true
        }
        if (descriptionTextField.text != item.description){
            item.description = descriptionTextField.text
            _isChanged = true
        }
        if(changePicturePath != item.picPath){
            item.picPath = changePicturePath
            _isChanged = true
        }

        if(thickChoiceBox.value != item.thickness){
            item.thickness = thickChoiceBox.value
            _isChanged = true
        }

        if(_isChanged){
            UIView.changeItem = item
            UIView.changeInfo[0] = tabIndex
            UIView.changeFlag *= -1
        }

    }
    init {
        this.apply {
            title = "物品信息"
            width = 300.0
            height = 400.0
        }

        root.apply {
            padding = Insets(5.0,20.0,0.0,20.0)
            children.add(
                HBox(10.0).apply {
                    children.addAll(Label("类别:"),typeChoiceBox )
                }
            )
            children.add(
                HBox(10.0).apply {
                    children.addAll(Label("所在地:"+MyController.tabNameList[_myLabel.tabIndex]))
                }
            )
            children.add(
                HBox(10.0).apply {
                    children.addAll(Label("数量:") , numberTextField)
                }
            )
            children.add(
                HBox(10.0).apply {
                    children.addAll(Label("描述:") , descriptionTextField)
                }
            )
            children.add(
                HBox(10.0).apply {
                    children.addAll(Label("厚薄:"), thickChoiceBox)
                }
            )
            children.addAll(
                choosePicture
            )

        }
        if (_myLabel.item.picPath != ""){
            root.children.addAll(
                imageview("file:data/pictures/" +item.picPath.replace("\"",""))
            )
        }

        root.children.add(confirm)



        this.scene = Scene(root)

    }
}


class MyTypeChoiceBox(selectedType:String):ChoiceBox<Any>(){
    init {
//        MyIconMap.firstKeys.forEach{firstKey->
//            MyIconMap.secondKeys.forEachIndexed { index, secondKey ->
//                if (index == 0){
//                    this.items.add(secondKey)
//                }else{
//                    this.items.add(secondKey)
//                }
//            }
//        }
        MyIconMap.secondKeys.forEachIndexed { firstIndex, secondKeyList ->
            secondKeyList.forEachIndexed{index,secondKey->
                if (index==0){
                    this.items.add(Separator())
                    this.items.add(secondKey)
                }else{
                    this.items.add(secondKey)
                }
            }

        }
        this.value = selectedType



    }
}