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
        this.setLocation(dim.width / 2 - this.size.width / 2, dim.height / 2 - this.size.height / 2)
        setBounds(0, 0, 750, 500)
        defaultCloseOperation = EXIT_ON_CLOSE
        layout = GridLayout(18, 1)
        val title = JLabel("ЛАУНЧЕР. НЕ МЕНЯЙТЕ ЗНАЧЕНИЯ, ЕСЛИ НЕ УВЕРЕНЫ.")
        title.isVisible = true
        add(title)
        val ver = JLabel("ARC LAUNCHER v0.1 ALPHA")
        ver.isVisible = true
        add(ver)
        param("WIDTH # ширина окна игры", "680")
        param("HEIGHT # высота окна игры", "400")
        param("IP # айпи сервера", "46.17.104.16")
        param("PORT # порт сервера", "1488")
        param("FOV # поле зрения", "60")
        param("CAMERA STEP", "0.05")
        param("MOUSE SENSITIVITY", "0.05")
        param("Z NEAR", "0.01")
        param("Z FAR", "1000")
        param("PATH", "resources\\")
        param("SHADER PATH", "resources\\shaders\\")
        param("TEXTURE PATH", "resources\\textures\\")
        param("MODEL PATH", "resources\\models\\")
        param("CAMERA MESH NAME", "camera.obj")
        param("CAMERA TEXTURE NAME", "camera.png")
        param("SOCKET TIMEOUT MS", "10")
        val button = JButton("LAUNCH")
        button.isVisible = true
        button.addActionListener {
            dispose()
            val frame = Frame("ARC", param(0).toInt(), param(1).toInt(), true)
            Settings.load(
                param(2), param(3).toInt(), Math.toRadians(param(4).toDouble()).toFloat(),
                param(5).toFloat(), param(6).toFloat(), param(7).toFloat(),
                param(8).toFloat(), param(9), param(10), param(11), param(12),
                param(13), param(14), param(15).toInt()
            )
            NetworkEngine(frame, SampleLogic(frame))
        }
        add(button)
        val ex = JLabel("ВЫЙТИ ИЗ ПРОГРАММЫ - КЛАВИША ESC")
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