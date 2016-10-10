package puzzle.stage

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import puzzle.game.GameSound
import puzzle.game.GameStage
import puzzle.game.GameSystem
import puzzle.util.Ease
import puzzle.util.EasingManager

class StageManager(stageData: GameStage.Data, initIndex: Int) {

    private val stageData: GameStage.Data = stageData

    private lateinit var nowStage: Stage
    private lateinit var nextStage: Stage
    private var nowIndex: Int = initIndex

    init {
        require(!stageData.pathList.isEmpty())
        require(0<=nowIndex && nowIndex<stageData.pathList.size)
        nowStage = loadStage(nowIndex)
    }

    private var changingRightFlg: Boolean = false
    private var changingLeftFlg: Boolean = false
    private var changingEase: EasingManager = Ease.Quad.InOut.build()
    private val changingDuration: Int = 60

    var isFinished: Boolean = false
        private set

    fun update() {
        if (changingRightFlg) {
            changingEase.step()
        } else if (changingLeftFlg) {
            changingEase.step()
        } else {
            nowStage.update()
            if (nowStage.restartFlg) {
                nowStage = loadStage(nowIndex)
            } else if (shouldChangeStageRight()) {
                if (nowIndex == stageData.pathList.size-1) {
                    isFinished = true
                } else {
                    changingRightFlg = true
                    changingEase.set(0f, GameSystem.WINDOW_W.toFloat(), changingDuration)
                    nextStage = loadStage(nowIndex+1)
                    GameSound.SE.stageMoveSE.play()
                }
            } else if (shouldChangeStageLeft()) {
                changingLeftFlg = true
                changingEase.set(0f, GameSystem.WINDOW_W.toFloat(), changingDuration)
                nextStage = loadStage(nowIndex-1)
                GameSound.SE.stageMoveSE.play()
            }
        }
    }

    fun draw(batch: SpriteBatch) {
        if (changingRightFlg) {
            nowStage.draw(batch, GameStage.LEFT-changingEase.get(), GameStage.BOTTOM, 1f, 1f)
            nextStage.draw(batch, GameStage.LEFT+GameSystem.WINDOW_W - changingEase.get(), GameStage.BOTTOM, 1f, 1f)
            if (changingEase.done) {
                nowStage = nextStage
                nowIndex++
                if (nowIndex == stageData.pathList.size-1) {
                    GameSound.SE.clearSE.play()
                }
                changingRightFlg = false
            }
        } else if (changingLeftFlg) {
            nowStage.draw(batch, GameStage.LEFT+changingEase.get(), GameStage.BOTTOM, 1f, 1f)
            nextStage.draw(batch, GameStage.LEFT-GameSystem.WINDOW_W + changingEase.get(), GameStage.BOTTOM, 1f, 1f)
            if (changingEase.done) {
                nowStage = nextStage
                nowIndex--
                changingLeftFlg = false
            }
        } else {
            nowStage.draw(batch, GameStage.LEFT, GameStage.BOTTOM, 1f, 1f)
        }
    }

    private fun shouldChangeStageRight(): Boolean
            = nowStage.isFinished || nowStage.player.changingStageRightFlg

    private fun shouldChangeStageLeft(): Boolean
            = nowStage.player.changingStageLeftFlg && nowIndex>0

    private fun loadStage(index: Int): Stage
            = StageLoader.load(getStageName(index), stageData.pathList[index])

    private fun getStageName(index: Int): String
            = if (index==stageData.pathList.size-1) "" else stageData.name + "-" + (index+1)

    fun invisualizeStageName() {
        nowStage.isForeVisual = false
    }

    fun visualizeStageName() {
        nowStage.isForeVisual = true
    }

    fun shouldExit(): Boolean {
        return nowStage.player.exitFlg
    }

}