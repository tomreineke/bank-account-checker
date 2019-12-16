import java.io.*
import java.nio.charset.StandardCharsets
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource

object XMLTransformer {
    fun transform(style: File?, input: File): String {
        val inFile = StreamSource(InputStreamReader(FileInputStream(input), StandardCharsets.UTF_8))
        val s = ByteArrayOutputStream()
        val outFile = StreamResult(s)
        val factory = TransformerFactory.newInstance()
        val transformer = factory.newTransformer(StreamSource(style))
        transformer.setParameter(OutputKeys.INDENT, "no")
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8")
        transformer.transform(inFile, outFile)
        return String(s.toByteArray(), StandardCharsets.UTF_8)
    }
}

fun main() {
    val raw = File("src/resources/input.xml")
    val validXmlLines = raw.readLines().asSequence().map {
        it.replace("<br>", "<br/>")
            .replace("&nbsp;", "")
            .replace("""<input.*">""".toRegex(), "")
    }

    // convert input.xml to valid XML
    val tmpFile = File("src/resources/tmp.xml")
    tmpFile.printWriter().use { out ->
        validXmlLines.forEach { out.println(it) }
    }

    // output file for angular app
    val outFile = File("src/resources/transactions.ts")
    outFile.printWriter().use {
        it.println(
            XMLTransformer.transform(
                File("src/resources/transform.xslt"),
                tmpFile
            ) // after conversion remove superfluous whitespaces and a XML tag => we want nice typescript file
                .replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "")
                .replace("""\n|\r|\t|\v""".toRegex(), "")
                .replace("( ){2,}".toRegex(), " ")
        )
    }

    tmpFile.deleteOnExit()
}