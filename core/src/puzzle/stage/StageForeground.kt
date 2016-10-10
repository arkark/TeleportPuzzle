package puzzle.stage

import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Align
import puzzle.game.GameFont
import puzzle.game.GameStage
import puzzle.game.GameSystem
import puzzle.util.*

class StageForeground(val stage: Stage) {

    fun draw(batch: SpriteBatch, left: Float, bottom: Float, angle: Float, scale: Float, alpha: Float) {
        val x = (28f + left) * scale
        val y = (GameSystem.WINDOW_H-10f - GameStage.Tile.H + bottom) * scale
        val width = GameSystem.WINDOW_W*scale
        font.setColor(0f, 0f, 0f, 0.4f*alpha)
        3f.let { d -> arrayOf(Vector2(-d, 0f), Vector2(d, 0f), Vector2(0f, -d), Vector2(0f, 1f)) }.forEach {
            diff -> font.draw(batch, stage.name, x+diff.x, y+diff.y, width, Align.left, true)
        }
        font.setColor(1f, 1f, 1f, 1f*alpha)
        font.draw(batch, stage.name, x, y, width, Align.left, true)
    }

    companion object {
        private val font: BitmapFont = GameFont.font
    }

}