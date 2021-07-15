import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ReadAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import java.io.File

fun slowOpenProject(projectFile: File): Project? {
    var project: Project? = null
    ApplicationManager.getApplication().invokeAndWait {
        project = openProject(projectFile)
    }
    return project
}

val Project.psiFiles: List<PsiFile>
    get() {
        val virtualFiles = ProjectRootManager
            .getInstance(this)
            .contentRoots
            .flatMap { root ->
                VfsUtil.collectChildrenRecursively(root)
            }

        val psiFiles = virtualFiles.map { file ->
            ReadAction.compute<PsiFile?, Exception> {
                PsiManager.getInstance(this).findFile(file)
            }
        }

        return psiFiles.filterNotNull()
    }
