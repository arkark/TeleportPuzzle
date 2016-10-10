package puzzle.stage

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import puzzle.game.GameSound
import puzzle.game.GameStage
import puzzle.stage.entity.mover.Mover
import puzzle.stage.entity.mover.Player
import puzzle.stage.entity.tile.Tile
import puzzle.util.Ease
import puzzle.util.EasingManager

class Stage(tileList: Array<Array<out Tile>>, player: Player, moverList: Array<out Mover>, background: StageBackground, goalNum: Int, stageName: String, stagePath: String) {

    internal val name: String = stageName
    internal val stagePath: String = stagePath
    internal val tileList: Array<Array<out Tile>> = tileList
    internal val player: Player = player
    internal val moverList: Array<out Mover> = moverList
    private val background: StageBackground = background
    private val foreground: StageForeground = StageForeground(this)

    internal var goalCount: Int = goalNum
        private set
    fun decreaseGoalCount() {
        require(goalCount > 0)
        goalCount--
    }

    private val rotatingEase: EasingManager = Ease.Quad.InOut.build()
    var isRotating: Boolean = false
        get() = !rotatingEase.done || shouldRotateStage()
        private set
    init {
        rotatingEase.set(0f, 0f, 1)
        rotatingEase.terminate()
    }

    var isFinished: Boolean = false
        get() = goalCount==0 && !isRotating && player.isFinished
        private set
    var restartFlg: Boolean = false
        get() = player.restartFlg
        private set
    var isForeVisual: Boolean = true


    fun update() {
        tileList.forEach { it.forEach { it.update() } }
        moverList.forEach { it.update() }
        player.update()

        if (shouldRotateStage()) setStageRotation()
        if (isRotating) rotatingEase.step()
    }

    private fun shouldRotateStage(): Boolean = rotatingEase.done && player.rotatingStageFlg

    private fun setStageRotation() {
        val beginAngle = (rotatingEase.get()+360f) % 360f
        val endAngle = when (player.gravityDir) {
            GameStage.Direction.DOWN -> 0f
            GameStage.Direction.LEFT -> 90f
            GameStage.Direction.UP -> 180f
            GameStage.Direction.RIGHT -> 270f
        }.let {
            if (it < beginAngle) it + 360f else it
        }.let {
            if (it-beginAngle > 180f) it-360f else it
        }
        getRotatingTime(Math.abs(endAngle - beginAngle)).let { duration ->
            if (duration > 0) {
                rotatingEase.set(beginAngle, endAngle, duration)
                GameSound.SE.rotatingSE.play(0.2f, 1.8f, 1.0f)
            }
        }
        player.rotatingStageFlg = false
    }

    private fun getRotatingTime(diffAngle: Float): Int {
        return diffAngle.toInt() / 3
    }

    fun draw(batch: SpriteBatch, left: Float, bottom: Float, scale: Float, alpha: Float) {
        val angle = getStageAngle()
        background.draw(batch, left, bottom, angle, scale, alpha, isRotating)
        tileList.forEach { it.forEach { it.draw(batch, left, bottom, angle, scale, alpha) } }
        moverList.forEach { it.draw(batch, left, bottom, angle, scale, alpha) }
        player.draw(batch, left, bottom, angle, scale, alpha)
        if (isForeVisual) drawForeground(batch, left, bottom, angle, scale, alpha)
    }

    fun drawForeground(batch: SpriteBatch, left: Float, bottom: Float, angle: Float, scale: Float, alpha: Float) {
        foreground.draw(batch, left, bottom, angle, scale, alpha)
    }

    fun getStageAngle(): Float {
        return rotatingEase.get()
    }

}