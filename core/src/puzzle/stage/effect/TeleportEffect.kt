package puzzle.stage.effect

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import puzzle.game.GameStage
import puzzle.game.GameSystem
import puzzle.util.*

class TeleportEffect() : Effect() {

    private lateinit var startPos: Vector2
    private lateinit var endPos: Vector2

    fun setEndPos(pos: Vector2) {
        endPos = pos
    }

    private val sprite: Sprite = Sprite(GameStage.Texture.player)

    private val ease: EasingManager = Ease.Quad.InOut.build()

    fun set(startPos: Vector2, endPos: Vector2, duration: Int) {
        this.startPos = startPos
        this.endPos   = endPos
        ease.set(0f, 1f, duration)
    }

    override fun step() {
        ease.step()
    }

    override fun draw(batch: SpriteBatch, left: Float, bottom: Float, angle: Float, scale: Float, alpha: Float) {
        val sizeScale = ease.get().let { if (it<0.5f) 1f + it else 2.0f-it }.let { it*it }
        val w = GameStage.Tile.W * scale * sizeScale
        val h = GameStage.Tile.H * scale * sizeScale
        sprite.setSize(w, h)
        val pos = ease.get().let { startPos*(1f-it) + endPos*it }.let { it * scale }
        sprite.setPosition(pos.x-sprite.width/2f+left, pos.y-sprite.height/2f+bottom)
        sprite.setAlpha(alpha)
        sprite.setOrigin(GameSystem.WINDOW_W/2f - sprite.x, GameSystem.WINDOW_H/2f - sprite.y)
        sprite.rotation = angle
        sprite.draw(batch)
        sprite.rotation = 0f
    }

    override fun done(): Boolean {
        return ease.done
    }

}