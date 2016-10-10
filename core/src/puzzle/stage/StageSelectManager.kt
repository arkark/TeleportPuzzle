package puzzle.stage

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import puzzle.game.GameSound
import puzzle.game.GameStage
import puzzle.game.GameStage.Direction
import puzzle.game.GameSystem
import puzzle.scene.SceneBase
import puzzle.scene.SceneStage
import puzzle.scene.SceneTitle
import puzzle.util.*

class StageSelectManager() {

    private val mostLeft: Float = 40f
    private val scale: Float = 0.25f
    private val movingDuration: Int = 8

    var isFinished: Boolean = false
        private set
    var isMoving: Boolean = false
        get() = !movingEase.done
        private set

    private var nextSceneStageFlg: Boolean = false
    private var nextSceneTitleFlg: Boolean = false

    private val movingEase: EasingManager = Ease.Quint.InOut.build()
    private var movingDirection: Direction = Direction.DOWN
    private fun isHorizontalMoving(): Boolean = isMoving && (movingDirection==Direction.LEFT || movingDirection==Direction.RIGHT)
    private fun isVerticalMoving(): Boolean = isMoving && (movingDirection==Direction.UP || movingDirection==Direction.DOWN)

    private val stageList: Array<Array<Stage>> = GameStage.DataList.map {
        it.pathList.mapIndexed { i, str ->
            StageLoader.load(it.name +"-"+ (i+1), str)
        }.toTypedArray()
    }.reversed().toTypedArray()

    private val stageNameList: Array<String> = GameStage.DataList.map { it.name }.toTypedArray()

    // staticに値を持つことで最後に選択したステージを保持する
    companion object {
        private var indexY: Int = GameStage.DataList.size-1
    }
    private var indexXList: Array<Int> = Array(stageList.size) {0}

    private val fogSprite: Sprite = Sprite(GameStage.Texture.fade)
    private val selectorSprite: Sprite = Sprite(GameStage.Texture.stageSelector)

    init {
        movingEase.set(0f, 0f, 1)
        movingEase.terminate()

        stageList.forEach { it.forEach { it.isForeVisual = false } }
    }

    fun update() {
        if (!isMoving) input()
        movingEase.step()
    }

    fun draw(batch: SpriteBatch) {
        drawBackground(batch)
        drawStage(batch)
        drawSelector(batch)
        drawForeground(batch)
    }

    private fun drawStage(batch: SpriteBatch) {
        val dy = if (isVerticalMoving()) movingEase.get() else 0f

        for(y in -2..2) {
            val bottom = (GameSystem.WINDOW_H / 3f).let { d ->
                d * (y+1 + dy) + GameStage.BOTTOM*scale + (d-GameSystem.WINDOW_H*scale)/2f
            }

            val dx = if (y==0 && isHorizontalMoving()) movingEase.get() else 0f
            val w = GameSystem.WINDOW_W * scale
            val i = (indexY + y + stageList.size) % stageList.size
            stageList[i].forEachIndexed po@{
                j, stage ->
                if (j==stageList[i].size-1) return@po
                val left = mostLeft + GameStage.LEFT*scale + w * (j - indexXList[i] + dx)
                if (left < -w || left > GameSystem.WINDOW_W) return@po // 画面外を描画しない
                val po = 0.4f
                val alpha = (if (i==indexY && j==indexXList[indexY]) {
                    if (isMoving) 1f-Math.abs(movingEase.get()) else 1f
                } else if (stage===preStage) {
                    if (isMoving) Math.abs(movingEase.get()) else 0f
                } else  0f)*po + (1f-po)
                stage.draw(batch, left, bottom, scale, alpha)
            }
        }
    }

    private var preStage: Stage? = null
    private fun drawBackground(batch: SpriteBatch) {
        val nowStage = stageList[indexY][indexXList[indexY]]
        if (preStage !== nowStage && !isMoving) {
            preStage = nowStage
        }
//        preStage?.draw(batch, GameStage.LEFT,GameStage.BOTTOM, 1f, 1f)
//        val r = if (isMoving) 1f-Math.abs(movingEase.get()) else 1f
//        nowStage.draw(batch, GameStage.LEFT,GameStage.BOTTOM, 1f, r)
        nowStage.draw(batch, GameStage.LEFT,GameStage.BOTTOM, 1f, 1f)

        batch.setBlendModeToSubtract()
        fogSprite.setAlpha(0.5f)
        fogSprite.setSize(GameSystem.WINDOW_W*1f, GameSystem.WINDOW_H*1f)
        fogSprite.draw(batch)
        batch.setBlendModeToBlend()
    }

    private fun drawForeground(batch: SpriteBatch) {
        val nowStage = stageList[indexY][indexXList[indexY]]
        val left = GameStage.LEFT + 10f
        val bottom = GameStage.BOTTOM - GameSystem.WINDOW_H/3f + 30f
        nowStage.drawForeground(batch, left, bottom, 0f, 1f, 1f)
    }

    private fun drawSelector(batch: SpriteBatch) {
        val po = scale*1.25f
        val w = GameSystem.WINDOW_W*po
        val h = GameSystem.WINDOW_H*po
        val x = mostLeft-GameSystem.WINDOW_W*(po-scale)/2f
        val y = GameSystem.WINDOW_H/2f - h/2
        batch.setBlendModeToAdd()
        selectorSprite.setSize(w, h)
        selectorSprite.setPosition(x, y)
        selectorSprite.draw(batch)
        batch.setBlendModeToBlend()
    }

    private fun input() {
        val leftPressed = Gdx.input.isKeyPressed(Input.Keys.LEFT)
        val rightPressed = Gdx.input.isKeyPressed(Input.Keys.RIGHT)
        val upPressed = Gdx.input.isKeyPressed(Input.Keys.UP)
        val downPressed = Gdx.input.isKeyPressed(Input.Keys.DOWN)

        val zJustPressed = Gdx.input.isKeyJustPressed(Input.Keys.Z)
        val xJustPressed = Gdx.input.isKeyJustPressed(Input.Keys.X)
        val enterJustPressed = Gdx.input.isKeyJustPressed(Input.Keys.ENTER)
        val spaceJustPressed = Gdx.input.isKeyJustPressed(Input.Keys.SPACE)
        val backSpaceJustPressed = Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)
        val escapeJustPressed = Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)

        if (leftPressed && !isMoving) setMoveLeft()
        if (rightPressed && !isMoving) setMoveRight()
        if (upPressed && !isMoving) setMoveUp()
        if (downPressed && !isMoving) setMoveDown()

        if ((zJustPressed || xJustPressed || enterJustPressed || spaceJustPressed) && !isMoving) setNextSceneStage()
        if ((backSpaceJustPressed || escapeJustPressed) && !isMoving) setNextSceneTitle()
    }

    private fun setMoveLeft() {
        if (indexXList[indexY] > 0) {
            GameSound.SE.cursorSE.play(0.05f)
            movingDirection = Direction.LEFT
            indexXList[indexY]--
            movingEase.set(-1f, 0f, movingDuration)
        }
    }

    private fun setMoveRight() {
        movingDirection = Direction.RIGHT
        if (indexXList[indexY]+2 < stageList[indexY].size) {
            GameSound.SE.cursorSE.play(0.05f)
            indexXList[indexY]++
            movingEase.set(1f, 0f, movingDuration)
        }
    }

    private fun setMoveUp() {
        GameSound.SE.cursorSE.play(0.05f)
        movingDirection = Direction.UP
        indexY = (indexY+1)%stageList.size
        movingEase.set(1f, 0f, movingDuration)
    }

    private fun setMoveDown() {
        GameSound.SE.cursorSE.play(0.05f)
        movingDirection = Direction.DOWN
        indexY = (indexY-1+stageList.size)%stageList.size
        movingEase.set(-1f, 0f, movingDuration)
    }

    private fun setNextSceneStage() {
        GameSound.SE.selectSE.play(0.1f)
        isFinished = true
        nextSceneStageFlg = true
    }

    private fun setNextSceneTitle() {
        GameSound.SE.cancelSE.play(0.1f)
        isFinished = true
        nextSceneTitleFlg = true
    }

    private fun getStageName(indexY: Int, indexX: Int): String
            = stageNameList[indexY] + "-" + (indexX+1)

    fun getNextScene(batch: SpriteBatch): SceneBase? {
        return if (nextSceneStageFlg) {
            SceneStage(batch, GameStage.DataList[GameStage.DataList.size-indexY-1], indexXList[indexY])
        } else if (nextSceneTitleFlg) {
            SceneTitle(batch)
        } else {
            require(false)
            null
        }
    }

    fun getSelectStageName(): String = getStageName(indexY, indexXList[indexY])
}