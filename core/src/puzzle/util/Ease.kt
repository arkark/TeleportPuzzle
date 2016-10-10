package puzzle.util

// E.g.
//  val e: EasingManager = Ease.Quad.InOut.build()

object Ease {
    private lateinit var f: (Float)->Float
    private lateinit var g: ((Float)->Float)->((Float)->Float)

    val Linear: Ease
        get() {
            f = { t -> t }
            return this
        }
    val Quad: Ease
        get() {
            f = { t -> t*t }
            return this
        }
    val Cubic: Ease
        get() {
            f = { t -> t*t*t }
            return this
        }
    val Quart: Ease
        get() {
            f = { t -> t*t*t*t }
            return this
        }
    val Quint: Ease
        get() {
            f = { t -> t*t*t*t*t }
            return this
        }
    val Sine: Ease
        get() {
            f = { t -> (1.0-Math.cos(t*Math.PI/2)).toFloat() }
            return this
        }
    val Circ: Ease
        get() {
            f = { t -> (1.0-Math.sqrt(1.0-t*t)).toFloat() }
            return this
        }
    val Exp: Ease
        get() {
            f = { Math.pow(2.0, -(1.0-it)*10.0).toFloat() }
            return this
        }
    val Back: Ease
        get() {
            f = { t -> t*t*(2.70158f*t-1.70158f) }
            return this
        }
    val SoftBack: Ease
        get() {
            f = { t -> t * t * (2f * t - 1f) }
            return this
        }

    val In: Ease
        get() {
            g = { a -> { t -> a(t) } }
            return this
        }
    val Out: Ease
        get() {
            g = { a -> { t -> 1f-a(1f-t) } }
            return this
        }
    val InOut: Ease
        get() {
            g = { a -> { t -> if (t<0.5) a(2f*t)/2f else 1f-a(2f-2f*t)/2f } }
            return this
        }

    fun build(): EasingManager = EasingManager(g(f))
}
