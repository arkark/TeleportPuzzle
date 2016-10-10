package puzzle.stage.effect

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import puzzle.game.GameSound
import puzzle.game.GameStage
import puzzle.game.GameSystem
import puzzle.util.*

class PlayerCollapseEffect() : Effect() {

    private val sprite: Sprite = Sprite(GameStage.Texture.player)

    private var time: Int = 0

    private val ease: EasingManager = Ease.Linear.InOut.build()

    private lateinit var centerPos: Vector2

    fun set(duration: Int, centerPos: Vector2) {
        GameSound.SE.collapseSE.play()
        ease.set(1f, 0f, duration)
        this.centerPos = centerPos
    }

    override fun step() {
        ease.step()
        time++
    }

    override fun draw(batch: SpriteBatch, left: Float, bottom: Float, angle: Float, scale: Float, alpha: Float) {
        val w = GameStage.Tile.W * scale
        val h = GameStage.Tile.H * scale
        val pos = centerPos * scale
        sprite.setSize(w, h)
        sprite.setAlpha(ease.get()*alpha)
        sprite.setPosition(pos.x - sprite.width/2f + left, pos.y - sprite.height/2f + bottom)
        sprite.setOrigin(GameSystem.WINDOW_W/2f - sprite.x, GameSystem.WINDOW_H/2f - sprite.y)
        sprite.rotation = angle
        sprite.draw(batch)
        sprite.rotation = 0f
    }

    override fun done(): Boolean {
        return ease.done
    }

}