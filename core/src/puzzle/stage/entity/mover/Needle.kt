package puzzle.stage.entity.mover

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector2
import puzzle.game.GameStage

class Needle(indexX: Int, indexY: Int): Mover(indexX, indexY) {
    init {
        sprite = Sprite(GameStage.Texture.needle)
    }

    fun getCenter(): Vector2 {
        return Vector2(
                (indexX + 0.5f)*GameStage.Tile.W + diffX,
                (indexY + 0.5f)*GameStage.Tile.H + diffY
        )
    }
}