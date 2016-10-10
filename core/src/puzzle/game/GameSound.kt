package puzzle.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound

object GameSound {

    object SE {
        var stageMoveSE: Sound = Gdx.audio.newSound(Gdx.files.internal("sounds/se_maoudamashii_se_ignition01.mp3"))
        var collapseSE: Sound = Gdx.audio.newSound(Gdx.files.internal("sounds/se_maoudamashii_system45.mp3"))
        var selectSE: Sound = Gdx.audio.newSound(Gdx.files.internal("sounds/se_maoudamashii_system49.mp3"))
        var cancelSE: Sound = Gdx.audio.newSound(Gdx.files.internal("sounds/se_maoudamashii_system36.mp3"))
        var cursorSE: Sound = Gdx.audio.newSound(Gdx.files.internal("sounds/se_maoudamashii_se_ignition01.mp3"))
        var hitGoalPointSE: Sound = Gdx.audio.newSound(Gdx.files.internal("sounds/se_maoudamashii_system46.mp3"))
        var landSE: Sound = Gdx.audio.newSound(Gdx.files.internal("sounds/se_maoudamashii_battle07.mp3"))
        var rotatingSE: Sound = Gdx.audio.newSound(Gdx.files.internal("sounds/se_maoudamashii_se_paper01.mp3"))
        var clearSE: Sound = Gdx.audio.newSound(Gdx.files.internal("sounds/Puzzle_clear_2.wav"))

        fun init() {}
    }

    object BGM {
        //var testBGM: Music = Gdx.audio.newMusic(Gdx.files.internal("sounds/se_maoudamashii_battle07.mp3"))

        fun init() {
            //testBGM.isLooping = true
            //testBGM.play()
        }
    }

    fun init() {
        SE.init()
        BGM.init()
    }

    fun dispose() {
        SE.stageMoveSE.dispose()
        SE.collapseSE.dispose()
        SE.selectSE.dispose()
        SE.cancelSE.dispose()
        SE.cursorSE.dispose()
        SE.hitGoalPointSE.dispose()
        SE.landSE.dispose()
        SE.rotatingSE.dispose()
        SE.clearSE.dispose()
        //BGM.testBGM.dispose()
    }
}