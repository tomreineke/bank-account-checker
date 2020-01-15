import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource
import kotlin.system.exitProcess

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

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        System.err.println("Use one of the following parameters:\n-c: for Commerzbank\n-p: for Postbank\n-s: for Sparkasse")
        exitProcess(-1)
    }

    val raw = File("src/resources/input.xml")
    val validXmlLines = raw.readLines().asSequence().map {
        it.replace("<br>", "<br/>")
            .replace("&nbsp;", "")
            .replace("""<input.*">""".toRegex(), "")
            .replace("""<use.*>""".toRegex(), "")
            .replace("<wbr>", "")
    }

    val transformFile = when {
        args[0] == "-c" -> File("src/resources/xslt/commerz.xslt")
        args[0] == "-p" -> File("src/resources/xslt/post.xslt")
        args[0] == "-s" -> File("src/resources/xslt/spaka.xslt")
        else -> throw IllegalAccessException("Invalid parameter")
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
                transformFile,
                tmpFile
            ) // after conversion remove superfluous whitespaces and a XML tag => we want nice typescript file
                .replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "")
                .replace("""\n|\r|\t|\v""".toRegex(), "")
                .replace("( ){2,}".toRegex(), " ")
                .replace("€ ", " EUR")
                .replace("{ }, ", "")
                .replace("""\{ amount: '(\w|\.|,|\ )*' \}, """.toRegex(), "")
        )
    }

    tmpFile.deleteOnExit()
}