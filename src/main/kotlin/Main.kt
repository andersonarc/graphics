import data.Frame
import sample.SampleEngine
import sample.SampleLogic

fun main() {
    val frame = Frame("EEE", 500, 500, true)
    val engine = SampleEngine(frame, SampleLogic(frame))
    engine.run()
}