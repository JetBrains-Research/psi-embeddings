package filter

import com.intellij.openapi.application.ReadAction
import com.intellij.psi.PsiElement
import psi.language.LanguageHandler

/**
 * Defines interface to validate trees
 */
abstract class Filter {

    /**
     * Custom method to define the logic of checking PSI tree
     * @param root: tree to validate
     * @return: `true` if tree is satisfy condition and should be kept and `false` otherwise
     */
    protected abstract fun isGoodTree(root: PsiElement, languageHandler: LanguageHandler): Boolean

    /**
     * Interface method to validate tree in thread safe mode
     * @param root: tree to validate
     * @return: `true` if tree is satisfy condition and should be kept and `false` otherwise
     */
    fun validateTree(root: PsiElement, languageHandler: LanguageHandler): Boolean =
        ReadAction.compute<Boolean, Exception> { isGoodTree(root, languageHandler) }
}