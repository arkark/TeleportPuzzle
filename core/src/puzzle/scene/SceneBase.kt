package puzzle.scene

import kotlin.collections.MutableMap

abstract class SceneBase() {
    protected var phase: Phase = Phase.OPENING
        set(value) {
            frameCount = -1
            field = value
        }
    protected val update: MutableMap<Phase, ()->SceneBase?> = mutableMapOf()

    protected var frameCount: Int = 0

    fun update(): SceneBase? {
        require(update.contains(phase))
        val scene = update[phase]?.invoke() ?: null
        frameCount++
        return scene
    }

    enum class Phase {
        OPENING,
        MIDDLE,
        END,
    }
}