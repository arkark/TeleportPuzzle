package puzzle.stage.entity.mover

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import puzzle.game.GameStage
import puzzle.stage.Stage
import puzzle.stage.effect.GoalPointBackEffect
import puzzle.util.*

class GoalPoint(indexX: Int, indexY: Int): Mover(indexX, indexY) {

    var isKilled: Boolean = false
        private set
    var isFinished: Boolean = false
        private set
    private var killingEase: EasingManager = Ease.Quad.InOut.build()

    private lateinit var stage: Stage


    private val ease: EasingManager = Ease.Cubic.build()

    private val backEffect: GoalPointBackEffect = GoalPointBackEffect(getCenter())

    init {
        sprite = Sprite(GameStage.Texture.goalPoint)
        ease.set(1f, 1f, 1)
    }

    override fun update() {
        super.update()
        backEffect.step()
        if (!isFinished && isKilled) {
            killingEase.step()
            if (killingEase.done) {
                sprite.setAlpha(0f)
                isFinished = true
            }
        }
        ease.step()
    }

    override fun draw(batch: SpriteBatch, left: Float, bottom: Float, angle: Float, scale: Float, alpha: Float) {
        backEffect.draw(batch, left, bottom, angle, scale, alpha)
        val a = ease.get()*alpha
        super.draw(batch, left, bottom, angle, scale, a)
    }

    fun getCenter(): Vector2 {
        return Vector2(
                (indexX + 0.5f)*GameStage.Tile.W + diffX,
                (indexY + 0.5f)*GameStage.Tile.H + diffY
        )
    }

    fun kill() {
        require(!isKilled)
        isKilled = true
        killingEase.set(0f, 1f, 30)
        backEffect.kill(30)
        ease.set(1f, 0f, 30)
        stage.decreaseGoalCount()
    }

    fun setStage(stage: Stage) {
        this.stage = stage
    }

}