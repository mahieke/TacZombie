package taczombie.test.model

import taczombie.model.GameFactory
import taczombie.model.GameField
import taczombie.model.MoveDown
import taczombie.model.MoveLeft
import taczombie.model.MoveRight

object HelloGame {
  
  def printGameField(gameField : GameField) {
    for(x <- 0 to gameField.levelHeight) {
      printf("%2d ", x)
      for(y <- 0 to gameField.levelWidth) {
        val cell = {
          if (gameField.gameFieldCells.contains(x,y))
            gameField.gameFieldCells(x,y)
          else
            null
        }
        val char = {
          if (cell == null) ""
          else if (cell.isEmpty) "   "            
          else if (cell.containsWall) "███"
          else if (cell.containsZombieToken) " Z " //"😈"
          else if (cell.containsHumanToken) " H " //"😃"
          else if (cell.containsCoin) " • "
          else if (cell.containsPowerup) " ★ "
          else ""
        }
        print(char)
      }
      print('\n')
    }
  }

	def main(args: Array[String]) {
	  var game = GameFactory.newGame(false, humans = 1, zombies = 1)
	  
  	printGameField(game.gameField)
  	
  	  
		game = game.executeCommand(MoveLeft)
		game = game.executeCommand(MoveLeft)
		game = game.executeCommand(MoveLeft)
		game = game.executeCommand(MoveLeft)
		game = game.executeCommand(MoveDown)
		game = game.executeCommand(MoveDown)
		game = game.executeCommand(MoveRight)
		game = game.executeCommand(MoveRight)
		game = game.executeCommand(MoveRight)
		game = game.executeCommand(MoveRight)
		
		printGameField(game.gameField)
	}
}