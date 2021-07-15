import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ApplicationStarter
import java.io.File
import java.nio.file.Files
import kotlin.streams.toList
import kotlin.system.exitProcess


class MetricRunner : ApplicationStarter {
    override fun getCommandName(): String = "metric"

    private val metrics = listOf(ResolvedImportsMetric, ResolvedReferencesMetric, ResolvedDependenciesMetric)

    val inputDirectory = File("/Users/Egor.Porsev/Desktop/java_dataset_research/output")
    val outputFile = File("/Users/Egor.Porsev/Documents/psiminer/output/results.csv").apply { createNewFile() }

    override fun main(args: Array<out String>) = ApplicationManager.getApplication().runReadAction {
        ResultsTable(metrics, outputFile).use { resultsTable ->
            for (projectDirectory in inputDirectory.subdirectories) {
                println("Opening ${projectDirectory.name}")
                try {
                    val project = openProject(projectDirectory) ?: error("Project didnt open")
                    println("Project ${project.name} opened")
                    resultsTable.addProject(project)
                    closeProject(project)
                } catch (e: Exception) {
                    println("Failed to open project ${projectDirectory.name}")
                    println(e)
                    continue
                }
            }
        }

        exitProcess(0)
    }
}

val File.subdirectories: List<File>
    get() = Files.walk(this.toPath(), 1).filter { Files.isDirectory(it) && it != this.toPath() }.map { it.toFile() }
        .toList()
