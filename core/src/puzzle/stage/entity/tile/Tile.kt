package puzzle.stage.entity.tile

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import puzzle.game.GameStage
import puzzle.game.GameSystem
import puzzle.stage.entity.Entity

abstract class Tile(indexX: Int, indexY: Int) : Entity(indexX, indexY) {

    override fun update() {

    }

    override fun draw(batch: SpriteBatch, left: Float, bottom: Float, angle: Float, scale: Float, alpha: Float) {
        val w = GameStage.Tile.W * scale
        val h = GameStage.Tile.H * scale
        val x = w * indexX + left
        val y = h * indexY + bottom
        sprite.setAlpha(alpha)
        sprite.setSize(w, h)
        sprite.setPosition(x, y)
        sprite.setOrigin(GameSystem.WINDOW_W/2f - sprite.x, GameSystem.WINDOW_H/2f - sprite.y)
        sprite.rotation = angle
        sprite.draw(batch)
        sprite.rotation = 0f
    }

}