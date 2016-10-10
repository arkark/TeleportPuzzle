package puzzle.stage.effect

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import puzzle.game.GameStage
import puzzle.game.GameSystem
import puzzle.util.*

class GoalPointBackEffect(centerPos: Vector2) : Effect() {

    private val centerPos: Vector2 = centerPos

    private val sprite: Sprite = Sprite(GameStage.Texture.goalPointBackground)

    private var time: Int = 0

    private val ease: EasingManager = Ease.Cubic.build()
    init {
        ease.set(1f, 1f, 1)
    }

    override fun step() {
        ease.step()
        time++
    }

    override fun draw(batch: SpriteBatch, left: Float, bottom: Float, angle: Float, scale: Float, alpha: Float) {
        val pos = centerPos*scale
        val w = GameStage.Tile.W * 4f * scale
        val h = GameStage.Tile.H * 4f * scale
        sprite.setSize(w, h)
        batch.setBlendModeToAdd()
        sprite.setPosition(pos.x-sprite.width/2f+left, pos.y-sprite.height/2f+bottom)
        val a = (ease.get() * (MathUtils.sin(time * MathUtils.PI / 60f)*0.15f+0.15f+0.7f))*alpha
        sprite.setAlpha(a)
        sprite.setOrigin(GameSystem.WINDOW_W/2f - sprite.x, GameSystem.WINDOW_H/2f - sprite.y)
        sprite.rotation = angle
        sprite.draw(batch)
        sprite.rotation = 0f
        batch.setBlendModeToBlend()
    }

    override fun done(): Boolean {
        return ease.done
    }

    fun kill(duration: Int) {
        ease.set(1f, 0f, duration)
    }

}