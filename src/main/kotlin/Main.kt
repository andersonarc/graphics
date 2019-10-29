import data.Frame
import sample.SampleEngine
import sample.SampleLogic

fun main() {
    val frame = Frame("EEE", 600, 480, true)
    val engine = SampleEngine(frame, SampleLogic(frame))
    engine.run()
}