package graphics.data.objects

class Face(v1: String, v2: String, v3: String) {
    val faceVertexIndices = arrayOf(parseLine(v1), parseLine(v2), parseLine(v3))

    private fun parseLine(line: String): IndexGroup {
        val lineTokens = line.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val length = lineTokens.size
        return IndexGroup(
            Integer.parseInt(lineTokens[0]) - 1,
            if (length > 1) {
                if (lineTokens[1].isNotEmpty()) lineTokens[1].toInt() - 1 else -1
            } else -1,
            if (length > 2) {
                lineTokens[2].toInt() - 1
            } else -1
        )
    }
}