import com.intellij.openapi.project.Project
import com.intellij.psi.PsiJavaCodeReferenceElement
import com.intellij.psi.PsiJavaFile
import com.intellij.psi.util.descendantsOfType

interface Metric {
    val name: String
    fun calculate(project: Project): Double
}

abstract class ReferencesMetric : Metric {
    fun findPercentageOfResolvedReferences(references: Sequence<PsiJavaCodeReferenceElement>): Double {
        var nReferences = 0
        var nResolvedReferences = 0
        for (reference in references) {
            nReferences += 1
            if (reference.resolve() != null) {
                nResolvedReferences += 1
            }
        }
        return if (nReferences > 0) {
                (nResolvedReferences.toDouble() / nReferences.toDouble()) * 100
        } else 100.0
    }

    abstract fun getReferences(psiJavaFile: PsiJavaFile): Sequence<PsiJavaCodeReferenceElement>

    override fun calculate(project: Project): Double {
        val references = project.psiFiles.asSequence().filterIsInstance<PsiJavaFile>().flatMap { getReferences(it) }
        return findPercentageOfResolvedReferences(references)
    }
}

object ResolvedReferencesMetric : ReferencesMetric() {
    override val name: String = "resolved_references"
    override fun getReferences(psiJavaFile: PsiJavaFile): Sequence<PsiJavaCodeReferenceElement> =
        psiJavaFile.descendantsOfType<PsiJavaCodeReferenceElement>()
}

object ResolvedImportsMetric : ReferencesMetric() {
    override val name: String = "resolved_imports"
    override fun getReferences(psiJavaFile: PsiJavaFile): Sequence<PsiJavaCodeReferenceElement> =
        psiJavaFile.importList?.allImportStatements?.asSequence()?.mapNotNull { it.importReference } ?: emptySequence()
}

object ResolvedDependenciesMetric : Metric {
    override val name: String = "resolved_dependencies"
    override fun calculate(project: Project): Double = countDependencies(project).toDouble()
}
