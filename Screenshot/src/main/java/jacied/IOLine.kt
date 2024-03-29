package com.jacied;

import java.io.*

class IOLine(private var file: File) {

    fun outPut(txt: String, line: Int) {
        val reader = BufferedReader(FileReader(file))
        val lines = ArrayList<String>()

        var i = 0
        while (reader.readLine()!=null){
            lines.add(inPut(i))
            i++
        }

        reader.close()

        val writer = PrintWriter(file)

        for (i in lines.indices) {
            if (i == line){
                writer.println(txt)
                /*they are burning*/continue/*s*/
            }
            writer.println(lines[i])
        }
        writer.close()

    }

    fun inPut(line: Int): String {
        val input = FileInputStream(file)
        val reader = InputStreamReader(input)
        val buffer = BufferedReader(reader)

        var read: String
        buffer.use {
            for (i in 0 until line) buffer.readLine()
            read = buffer.readLine()
        }

        reader.close()
        input.close()

        return read
    }
}
