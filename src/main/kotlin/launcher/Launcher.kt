package launcher

import graphics.data.Frame
import graphics.network.NetworkEngine
import graphics.sample.SampleLogic
import java.awt.GridLayout
import java.awt.Toolkit
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JTextField

class Launcher : JFrame("ARC Launcher") {
    private val settings = ArrayList<Pair<JLabel, JTextField>>()
    init {
        val dim = Toolkit.getDefaultToolkit().screenSize
        setLocation(dim.width / 2 - this.size.width / 2, dim.height / 2 - this.size.height / 2)
        setBounds(0, 0, 750, 500)
        defaultCloseOperation = EXIT_ON_CLOSE
        layout = GridLayout(15, 1)
        val title = JLabel("DON'T CHANGE THE VALUES IF YOU ARE NOT SURE")
        title.isVisible = true
        add(title)
        val ver = JLabel("ARC LAUNCHER v0.1 ALPHA")
        ver.isVisible = true
        add(ver)
        param("Window width", "680")
        param("Window height", "400")
        param("Server IP", "46.17.104.16")
        param("Server port", "1488")
        param("Field of view (in radians)", "60")
        param("Camera speed", "0.05")
        param("Mouse sensitivity", "0.05")
        param("Z-near limit", "0.01")
        param("Z-far limit (view distance)", "1000")
        param("Shader folder path", "resources\\shaders\\")
        param("Model folder path", "resources\\models\\")
        param("Camera filename", "camera")
        param("Server timeout (ms)", "10")
        val button = JButton("LAUNCH")
        button.isVisible = true
        button.addActionListener {
            dispose()
            val frame = Frame("ARC", param(0).toInt(), param(1).toInt(), true)
            Settings.load(
                param(2), param(3).toInt(), Math.toRadians(param(4).toDouble()).toFloat(),
                param(5).toFloat(), param(6).toFloat(), param(7).toFloat(),
                param(8).toFloat(), param(9), param(10), param(11),
                param(12).toInt()
            )
            NetworkEngine(frame, SampleLogic(frame))
        }
        add(button)
        val ex = JLabel("EXIT")
        ex.isVisible = true
        add(ex)
        isVisible = true
    }

    private fun param(index: Int): String {
        return settings[index].second.text
    }

    private fun param(name: String, type: String) {
        val label = JLabel(name)
        label.isVisible = true
        add(label)
        val field = JTextField(type)
        field.isVisible = true
        add(field)
        settings.add(Pair(label, field))
    }
}