package view

import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.Includes._
import scalafx.geometry.{ HPos, Pos, Insets }
import scalafx.scene.control.{ Button, TextField, Label, Separator }
import scalafx.scene.image.Image
import scalafx.scene.image.ImageView
import scalafx.scene.layout.ColumnConstraints
import scalafx.scene.layout.GridPane
import scalafx.scene.layout.Priority
import scalafx.scene.layout.RowConstraints
import scalafx.scene.layout.VBox
import scalafx.scene.control.ToolBar
import scalafx.scene.input.Mnemonic
import scalafx.scene.input.KeyCombination
import scalafx.scene.input.KeyCodeCombination
import scalafx.scene.input.KeyCode
import scalafx.event.EventHandler
import scalafx.event.EventHandler
import scalafx.scene.shape.Rectangle
import scalafx.scene.paint.Color
import scalafx.scene.layout.BorderPane
import scalafx.scene.control.SplitPane
import scalafx.scene.layout.HBox
import scalafx.scene.control.ContentDisplay
import scalafx.event.ActionEvent
import scalafx.scene.input.KeyEvent
import scalafx.event.EventHandler
import javax.swing.event.ChangeEvent
import scala.collection.mutable.ObservableMap
import scalafx.beans.property.IntegerProperty
import scalafx.beans.value.ObservableValue
import javafx.beans.binding.IntegerBinding
import javafx.beans.value.ObservableIntegerValue
import javafx.beans.value.ObservableNumberValue
import javafx.beans.value.ChangeListener
import scalafx.beans.property.StringProperty

object View extends JFXApp { 
  
  var map = scalafx.collections.ObservableMap[(Int,Int), Color]()
  var mapProperty = Map[(Int,Int), StringProperty]().empty
  
  for(row <- 0 to 10; col <- 0 to 10) {
    if (row % 2 == 0) {
      map += ((row,col) -> (Color.YELLOW))
      mapProperty += ((row,col) -> StringProperty("Zombie"))
  	}
    else if (col % 2 == 0) {
      map += ((row,col) -> (Color.GREEN))
      mapProperty += ((row,col) -> StringProperty("Human"))
    }
    else {
      map += ((row,col) -> (Color.RED))
      mapProperty += ((row,col) -> StringProperty("Coin"))
    }
  }	 
 
  
  
  val currentPlayerFromModel = new StringProperty("0")
  
  stage = new JFXApp.PrimaryStage {
    
    var row = 0
    var col = 0
    var cell = "Coin"
    height = 1024
    width = 1024
    title = "TacZombie"    
    scene = new Scene {
      onKeyPressed = {e:KeyEvent =>
        e.code match {
          case KeyCode.UP => 
            println("MoveUP")
            mapProperty(row,col).update(cell)
            row += 1; col += 1
            println("Row+Col: " + (row+col))
            println("Mapsize/Col: " +mapProperty.size/10 )
            if ((row >= mapProperty.size/col)) {
              row = 0; col = 0
              cell match {
                case "Coin" => cell = "Zombie"
                case "Zombie" => cell = "Human"
                case "Human" => cell = "Coin"
              }
            }
          case KeyCode.DOWN => 
            println("MoveDOWN")
            currentPlayerFromModel.update((currentPlayerFromModel.value.toInt + 1).toString)
          case KeyCode.LEFT => println("MoveLEFT")
          case KeyCode.RIGHT => println("MoveRIGHT")
          case KeyCode.SPACE => println("MoveYield")
          case _ => 
        }
      }
      
      root = {
   
        /***************
         * TOOLBAR --
         ***************/
        
        val newGameButton = new Button {
          text = "New Game"
          minWidth = 75
          onAction = (event: ActionEvent) => {
            println("NewGame clicked.")
          }
        }

        val quitGameButton = new Button {
          text = "Quit Game"
          minWidth = 75
          onAction = (event: ActionEvent) => {
            close()
            println("QuitGame clicked.")
          }
        }

        val toolBar = new ToolBar {
          content = List(newGameButton, quitGameButton)
        }
        
        /***************
         * -- TOOLBAR
         ***************/
        
        /***************
         * GAMEGRID --
         ***************/
        val gameGrid = new GridPane {
          mapProperty.foreach(key => add(new Rectangle() {
              height = 30
              width = 30
              stroke = Color.WHITE
              fill <== when (key._2.===("Zombie")) choose Color.GREEN otherwise Color.YELLOW
            }, key._1._1, key._1._2))
        }
          
//          for (row <- 0 until gridHeight; col <- 0 until gridWidth) {
//            add(new Rectangle() {
//              height = 30
//              width = 30
//              stroke = Color.WHITE
//            }, row,col)
//          }
//        }
        
        /***************
         * -- GAMEGRID
         ***************/    
        
        /***************
         * GAMESTATS --
         ***************/
        var row = 0
        val currentPlayer = new Label("CurrentPlayer:")
        GridPane.setConstraints(currentPlayer, 0, row)
        val currentPlayerValue = new Label{
          text <== currentPlayerFromModel
        }
        
        GridPane.setConstraints(currentPlayerValue, 2, row)
       
        
        row += 1
        println(row)
        val movesLeft = new Label("Moves Left:")
        GridPane.setConstraints(movesLeft, 0, row)
        val movesLeftValue = new Label("0")
        GridPane.setConstraints(movesLeftValue, 2, row)
        
        row += 1
        println(row)
        val lives = new Label("Lives:")
        GridPane.setConstraints(lives, 0, row)
      	val livesValue = new Label("0")
        GridPane.setConstraints(livesValue, 2, row)

        row += 1
        println(row)
        val coins = new Label("Coins:")
        GridPane.setConstraints(coins, 0, row)
        val coinsValue = new Label("0")
        GridPane.setConstraints(coinsValue, 2, row) 
        
        val rowInfo = new RowConstraints(minHeight = 30, prefHeight = 30, maxHeight = 30)
        val colInfo = new ColumnConstraints(minWidth = 90, prefWidth = 110, maxWidth = 130)
    
        val gameStats = new GridPane {
        	padding = Insets(15)
          for (row <- 0 until row) {
            rowConstraints.add(rowInfo)
            columnConstraints.add(colInfo)
          }
          children ++= Seq(currentPlayer, currentPlayerValue, movesLeft, movesLeftValue, lives, livesValue, coins, coinsValue)
        }
        
    
        /***************
         * -- GAMESTATS
         ***************/ 
        
        new VBox {
          vgrow = Priority.ALWAYS
          hgrow = Priority.ALWAYS
          spacing = 10
          content = List(
            new VBox { content = toolBar },
            new BorderPane {
              padding = Insets(20)
            	left = gameGrid  
            	right = gameStats
            })
        }
      }
    }
  }
}

