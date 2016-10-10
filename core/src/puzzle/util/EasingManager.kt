package puzzle.util

class EasingManager(private val _func: (Float)->Float) {
    private var _duration: Int = -1
    private var _start: Float = 0f
    private var _end: Float = 0f

    private var _nowTime: Int = 0

    fun set(start: Float, end: Float, duration: Int) {
        require(duration > 0)
        _start = start
        _end = end
        _duration = duration
        _nowTime = 0
    }

    fun get(): Float {
        require(_duration > 0)
        return (_nowTime / _duration.toFloat()).let {
            _func(Math.max(0f, Math.min(1f, it)))
        }.let {
            _start*(1f-it) + _end*it
        }
    }

    fun step() = _nowTime++

    var done: Boolean = true
        get() = _nowTime >= _duration
        private set

    fun terminate() {
        _nowTime = _duration
    }
}