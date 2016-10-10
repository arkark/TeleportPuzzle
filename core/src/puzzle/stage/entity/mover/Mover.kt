package puzzle.stage.entity.mover

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import puzzle.game.GameStage
import puzzle.game.GameSystem
import puzzle.stage.entity.Entity

abstract class Mover(indexX: Int, indexY: Int): Entity(indexX, indexY) {
    internal var diffX: Float = 0f
    internal var diffY: Float = 0f

    override fun update() {

        while(diffX < -GameStage.Tile.W/2) {
            diffX += GameStage.Tile.W
            indexX--
        }
        while(diffX > GameStage.Tile.W/2) {
            diffX -= GameStage.Tile.W
            indexX++
        }
        while(diffY < -GameStage.Tile.H/2) {
            diffY += GameStage.Tile.H
            indexY--
        }
        while(diffY > GameStage.Tile.H/2) {
            diffY -= GameStage.Tile.H
            indexY++
        }

        indexX = (indexX + GameStage.NUM_X) % GameStage.NUM_X
        indexY = (indexY + GameStage.NUM_Y) % GameStage.NUM_Y

    }

    override fun draw(batch: SpriteBatch, left: Float, bottom: Float, angle: Float, scale: Float, alpha: Float) {
        val w = GameStage.Tile.W * scale
        val h = GameStage.Tile.H * scale
        val x = w * indexX + diffX + left
        val y = h * indexY + diffY + bottom
        sprite.setAlpha(alpha)
        sprite.setSize(w, h)
        sprite.setPosition(x, y)
        sprite.setOrigin(GameSystem.WINDOW_W/2f - sprite.x, GameSystem.WINDOW_H/2f - sprite.y)
        sprite.rotation = angle
        sprite.draw(batch)
        sprite.rotation = 0f
    }


}