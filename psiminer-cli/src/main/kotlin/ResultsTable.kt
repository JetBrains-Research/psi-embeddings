import com.intellij.openapi.project.Project
import java.io.Closeable
import java.io.File

class ResultsTable(private val metrics: List<Metric>, outputFile: File) : Closeable {
    private val writer = outputFile.printWriter().apply {
        val header = listOf("project name") + metrics.map { it.name }
        println(header.joinToString(","))
    }

    fun addProject(project: Project) {
        val row = CsvRow(project.name, calculateResults(project))
        println("Calculated row $row")
        writer.println(row.toCsvString())
        writer.flush()
    }

    override fun close() {
        writer.close()
    }

    data class CsvRow(val project: String, val results: List<Double>) {
        fun toCsvString() = (listOf(project) + results.map { it.toString() }).joinToString(",")
    }

    private fun calculateResults(project: Project): List<Double> = metrics.map { it.calculate(project) }
}
