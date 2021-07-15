import com.intellij.openapi.externalSystem.autoimport.AutoImportProjectTracker
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ModuleRootManager

fun countDependencies(project: Project): Int = project.modules.sumOf { countLibraries(it) }

private val Project.modules: Array<Module>
    get() = ModuleManager.getInstance(this).modules

private fun countLibraries(module: Module): Int {
    var nLibraries = 0
    ModuleRootManager.getInstance(module).orderEntries().forEachLibrary {
        nLibraries++
        true
    }
    return nLibraries
}
