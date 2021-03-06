package taczombie.client.model

import taczombie.model.util.JsonHelper._
import taczombie.client.util.Observable
import spray.json._
import spray.json.DefaultJsonProtocol._
import scala.swing.event.Event
import scala.collection.SortedMap
import taczombie.client.util.ViewHelper.charName2Wrapper
import taczombie.model.util.JsonHelper.ErrorJsonProtocol.ErrorFormat
import taczombie.model.util.JsonHelper.GameDataJsonProtocol.DataFormat
import taczombie.model.util.JsonHelper.GameDataJsonProtocol.GameDataFormat
import taczombie.model.util.JsonHelper.GameDataJsonProtocol.listFormat

case object gameUpdated extends Event

class ViewModel extends Observable {
  var cmd = ""
  var gameState = " "
  var currentPlayerToken = " "
  var deadTokens = 0
  var totalTokens = 0
  var lifes = 0
  var movesRemaining = 0
  var coins = 0
  var score = 0
  var powerUp = 0
  var levelWidth = 0
  var levelHeight = 0
  var cells = SortedMap[(Int, Int), (Char, Boolean)]()
  var humanTokens = SortedMap[(Int, Int), (Boolean)]()
  var frozenTime = 0
  var log = List[String]()
  var gameMessage = " "

  def toObject(json: JsValue) {
    if (json.toString.contains("all") || json.toString.contains("updated"))
      toData(json)
    else if (json.toString.contains("error"))
      toError(json)
  }

  import taczombie.model.util.JsonHelper.GameDataJsonProtocol._
  private def toData(json: JsValue) {
    val data: Data = json.convertTo[Data]
    val gameData = data.gameData.convertTo[GameData]
    val updatedCells = data.cells.convertTo[List[Cell]]

    updatedCells.foreach { x =>
      cells += (x.x, x.y) -> (x.token, x.isHighlighted)
    }

    val humanTokensTmp = data.humanTokens.convertTo[List[HumanTokens]]
    humanTokensTmp.foreach { x =>
      humanTokens += (x.x, x.y) -> (x.powerUp)
    }

    cmd = data.cmd
    gameState = gameData.gameState.toString()
    import taczombie.client.util.ViewHelper._
    currentPlayerToken = gameData.currentPlayer.toName
    coins = gameData.coins
    lifes = gameData.lifes
    totalTokens = gameData.totalTokens
    deadTokens = gameData.deadTokens
    movesRemaining = gameData.movesRemaining
    score = gameData.score
    powerUp = gameData.powerUp
    levelWidth = gameData.levelWidth
    levelHeight = gameData.levelHeight
    frozenTime = gameData.frozenTime
    log = data.log
    gameMessage = data.gameMessage

    notifyObservers
  }

  import taczombie.model.util.JsonHelper.ErrorJsonProtocol._
  private def toError(json: JsValue) {
    val error: Error = json.convertTo[Error]
    gameMessage = error.message

    notifyObservers
  }
}