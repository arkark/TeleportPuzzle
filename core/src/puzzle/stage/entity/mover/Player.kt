package puzzle.stage.entity.mover

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import puzzle.game.GameSound
import puzzle.game.GameStage
import puzzle.stage.Stage
import puzzle.stage.effect.PlayerCollapseEffect
import puzzle.stage.effect.TeleportEffect
import puzzle.stage.entity.tile.Block
import puzzle.util.*

class Player(indexX: Int, indexY: Int): Mover(indexX, indexY) {
    private val EPS: Float = 5f
    private lateinit var stage: Stage

    var isFinished: Boolean = false
        get() = stage.goalCount==0 && stage.moverList.all { if (it is GoalPoint) it.isFinished else true }
                private set
    var isActive: Boolean = true
        private set
    var isInputting: Boolean = false // 入力を引き受けるかどうか
        get() = !stage.isRotating && isActive
        private set
    var isJumpping: Boolean = false
        private set
    var isTeleporting: Boolean = false
        private set
    var isCollided: Boolean = false
        private set
    var isLanded: Boolean = false
        private set
    var isCollapsing: Boolean = false
        get() = !collapseEffect.done()
        private set

    private val collapseEffect: PlayerCollapseEffect = PlayerCollapseEffect()

    var changingStageRightFlg: Boolean = false
        private set
    var changingStageLeftFlg: Boolean = false
        private set
    var restartFlg: Boolean = false
        private set
    var exitFlg: Boolean = false
        private set
    var rotatingStageFlg: Boolean = false

    private val teleportEffect: TeleportEffect = TeleportEffect()

    private val jumpSpeed: Float = 8f

    private val moveSpeed: Float = 4f

    private var velocity: Vector2 = Vector2.Zero

    var gravityDir: GameStage.Direction = GameStage.Direction.DOWN
        private set

    private val dummyPlayer: DummyPlayer = DummyPlayer(this)

    init {
        sprite = Sprite(GameStage.Texture.player)
    }

    override fun update() {
        if (isInputting) input()
        act()
        super.update()
        if (isActive) collide()

        dummyPlayer.update()
    }

    override fun draw(batch: SpriteBatch, left: Float, bottom: Float, angle: Float, scale: Float, alpha: Float) {
        val a = (if (isTeleporting) 0.4f else 1f) * alpha
        if (!isCollapsing) super.draw(batch, left, bottom, angle, scale, a)
        if (!isCollapsing) dummyPlayer.draw(batch, left, bottom, angle, scale, a)
        drawEffects(batch, left, bottom, angle, scale, a)
    }

    private fun drawEffects(batch: SpriteBatch, left: Float, bottom: Float, angle: Float, scale: Float, alpha: Float) {
        if (!teleportEffect.done()) teleportEffect.draw(batch, left, bottom, angle, scale, alpha)
        if (isCollapsing) collapseEffect.draw(batch, left, bottom, angle, scale, alpha)
    }

    fun input() {
        val leftPressed  = Gdx.input.isKeyPressed(Keys.LEFT)
        val rightPressed = Gdx.input.isKeyPressed(Keys.RIGHT)
        val zJustPressed = Gdx.input.isKeyJustPressed(Keys.Z)
        val xJustPressed = Gdx.input.isKeyJustPressed(Keys.X)

        val qJustPressed = Gdx.input.isKeyJustPressed(Keys.Q)
        val wJustPressed = Gdx.input.isKeyJustPressed(Keys.W)
        val eJustPressed = Gdx.input.isKeyJustPressed(Keys.E)
        val backSpaceJustPressed = Gdx.input.isKeyJustPressed(Keys.BACKSPACE)
        val escapeJustPressed = Gdx.input.isKeyJustPressed(Keys.ESCAPE)

        if (leftPressed == rightPressed) {
            endMoving()
        } else if (leftPressed) {
            setMovingLeft()
        } else if (rightPressed) {
            setMovingRight()
        }

        if (xJustPressed && canTeleport()) setTeleportion()
        if (zJustPressed && canJump()) setJump()

        // debug keys
        if (qJustPressed) restartFlg = true
//        if (wJustPressed) changingStageLeftFlg = true
//        if (eJustPressed) changingStageRightFlg = true
        if (backSpaceJustPressed || escapeJustPressed) exitFlg = true
//        if (Gdx.input.isKeyJustPressed(Keys.A)) setStageRotation(GameStage.Direction.LEFT)
//        if (Gdx.input.isKeyJustPressed(Keys.S)) setStageRotation(GameStage.Direction.RIGHT)
//        if (Gdx.input.isKeyJustPressed(Keys.D)) setStageRotation(GameStage.Direction.UP)
//        if (Gdx.input.isKeyJustPressed(Keys.F)) setStageRotation(GameStage.Direction.DOWN)
    }

    fun act() {
        if (isTeleporting) teleport()
        if (isJumpping) jump()
        if (!isLanded && !stage.isRotating) gravitate()
        if (isCollapsing) collapse()

        limitVelocity()

        if (isActive) {
            diffX += velocity.x
            diffY += velocity.y
        }
    }

    fun limitVelocity() {
        val maxSpeed = 12f
        val size = velocity.len()
        if (size > maxSpeed) {
            velocity *= maxSpeed / size
        }
    }

    private fun setMovingLeft() {
        when(gravityDir) {
            GameStage.Direction.DOWN -> velocity.x = -moveSpeed
            GameStage.Direction.LEFT -> velocity.y = moveSpeed
            GameStage.Direction.UP -> velocity.x = moveSpeed
            GameStage.Direction.RIGHT -> velocity.y = -moveSpeed
        }
    }

    private fun setMovingRight() {
        when(gravityDir) {
            GameStage.Direction.DOWN -> velocity.x = moveSpeed
            GameStage.Direction.LEFT -> velocity.y = -moveSpeed
            GameStage.Direction.UP -> velocity.x = -moveSpeed
            GameStage.Direction.RIGHT -> velocity.y = moveSpeed
        }
    }

    private fun endMoving() {
        when(gravityDir) {
            GameStage.Direction.UP, GameStage.Direction.DOWN -> velocity.x = 0f
            GameStage.Direction.LEFT, GameStage.Direction.RIGHT -> velocity.y = 0f
        }
    }


    private fun setStageRotation(direction: GameStage.Direction) {
        gravityDir = direction
        rotatingStageFlg = true
        velocity.x = 0f
        velocity.y = 0f
    }

    private fun setCollapse() {
        isActive = false
        val collapsingDuration = 30
        collapseEffect.set(collapsingDuration, getCenter())
    }

    private fun collapse() {
        collapseEffect.step()
        if (collapseEffect.done()) {
            restartFlg = true
        }
    }

    private fun setTeleportion() {
        isTeleporting = true
        val startPos = getCenter()
        indexY = (indexY + GameStage.NUM_Y/2) % GameStage.NUM_Y
        val endPos = getCenter()
        val teleportingDuration = 10
        teleportEffect.set(startPos, endPos, teleportingDuration)
    }

    private fun teleport() {
        teleportEffect.step()
        teleportEffect.setEndPos(getCenter())
        if (teleportEffect.done()) {
            isTeleporting = false
        }
    }

    private fun setJump() {
        isJumpping = true
        when(gravityDir) {
            GameStage.Direction.UP -> velocity.y -= jumpSpeed
            GameStage.Direction.DOWN -> velocity.y += jumpSpeed
            GameStage.Direction.LEFT -> velocity.x += jumpSpeed
            GameStage.Direction.RIGHT -> velocity.x -= jumpSpeed
        }
    }

    private fun jump() {
        isJumpping = false
    }

    private fun canTeleport(): Boolean {
        return !dummyPlayer.isCollide()// && !isTeleporting
    }

    private fun canJump(): Boolean {
        return isLanded && !isJumpping && !isTeleporting
    }

    private fun gravitate() {
        when(gravityDir) {
            GameStage.Direction.DOWN  -> velocity.y -= 0.5f
            GameStage.Direction.UP    -> velocity.y += 0.5f
            GameStage.Direction.LEFT  -> velocity.x -= 0.5f
            GameStage.Direction.RIGHT -> velocity.x += 0.5f
        }
    }

    private fun collide() {
        collideBlock()
        collideGoalPoint()
        collideGravitater()
        collideNeedle()
    }

    private fun collideGoalPoint() {
        stage.moverList.forEach {
            if (it is GoalPoint && !it.isKilled) {
                val dist = (this.getCenter()-it.getCenter()).len()
                val po = (GameStage.Tile.W+GameStage.Tile.H)/2.5f
                if (dist < po) {
                    it.kill()
                    GameSound.SE.hitGoalPointSE.play(0.5f)
                    if (stage.goalCount == 0) {
                        diffX -= getCenter().x - it.getCenter().x
                        diffY -= getCenter().y - it.getCenter().y
                        isActive = false
                        setStageRotation(GameStage.Direction.DOWN)
                    }
                }
            }
        }
    }

    private fun collideGravitater() {
        stage.moverList.forEach {
            if (it is Gravitater && it.isActive) {
                val dist = (this.getCenter()-it.getCenter()).len()
                val po = (GameStage.Tile.W+GameStage.Tile.H)/3f
                if (dist < po) {
                    it.enactivate()
                    diffX -= getCenter().x - it.getCenter().x
                    diffY -= getCenter().y - it.getCenter().y
                    setStageRotation(it.direction)
                }
            }
        }
    }

    private fun collideNeedle() {
        if (isCollapsing) return
        stage.moverList.forEach {
            if (it is Needle) {
                val dist = (this.getCenter()-it.getCenter()).len()
                val po = (GameStage.Tile.W+GameStage.Tile.H)/2.5f
                if (dist < po) {
                    setCollapse()
                }
            }
        }
    }

    private fun collideBlock() {
        val left = isCollideLeftBlock()
        val right = isCollideRightBlock()
        val up = isCollideUpBlock()
        val down = isCollideDownBlock()
        // println("left: ${left}, right: ${right}, up: ${up}, down: ${down}")

        var soundFlg = !isLanded
        isLanded = when(gravityDir) {
            GameStage.Direction.LEFT -> left
            GameStage.Direction.RIGHT -> right
            GameStage.Direction.UP -> up
            GameStage.Direction.DOWN -> down
        }
        if (soundFlg && isLanded) GameSound.SE.landSE.play(0.02f, 1.9f, 0f)

        if (isCollided) {
            if (!left && !right && !up && !down) {
                isCollided = false
            }
        } else {
            if (up || down) {
                diffY = 0f
                velocity.y = 0f
            }
            if (left || right) {
                diffX = 0f
                velocity.x = 0f
            }
        }
    }

    private fun isCollideLeftBlock(): Boolean {
        if (diffX > 0) return false
        if (stage.tileList[indexY][indexX-1] is Block) return true
        if (diffY<-EPS && stage.tileList[indexY-1][indexX-1] is Block) return true
        if (diffY>EPS && stage.tileList[indexY+1][indexX-1] is Block) return true
        return false
    }

    private fun isCollideRightBlock(): Boolean {
        if (diffX < 0) return false
        if (stage.tileList[indexY][indexX+1] is Block) return true
        if (diffY<-EPS && stage.tileList[indexY-1][indexX+1] is Block) return true
        if (diffY>EPS && stage.tileList[indexY+1][indexX+1] is Block) return true
        return false
    }

    private fun isCollideUpBlock(): Boolean {
        if (diffY < 0) return false
        if (stage.tileList[indexY+1][indexX] is Block) return true
        if (diffX<-EPS && stage.tileList[indexY+1][indexX-1] is Block) return true
        if (diffX>EPS && stage.tileList[indexY+1][indexX+1] is Block) return true
        return false
    }

    private fun isCollideDownBlock(): Boolean {
        if (diffY > 0) return false
        if (stage.tileList[indexY-1][indexX] is Block) return true
        if (diffX<-EPS && stage.tileList[indexY-1][indexX-1] is Block) return true
        if (diffX>EPS && stage.tileList[indexY-1][indexX+1] is Block) return true
        return false
    }

    fun getCenter(): Vector2 {
        return Vector2(
                (indexX + 0.5f)*GameStage.Tile.W + diffX,
                (indexY + 0.5f)*GameStage.Tile.H + diffY
        )
    }

    fun setStage(stage: Stage) {
        this.stage = stage
        dummyPlayer.setStage(stage)
    }

}