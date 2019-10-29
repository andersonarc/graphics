import data.Frame
import sample.SampleEngine
import sample.SampleLogic

fun main() {
    val frame = Frame("EEE", 1360, 800, true)
    val engine = SampleEngine(frame, SampleLogic(frame))
    engine.run()
}