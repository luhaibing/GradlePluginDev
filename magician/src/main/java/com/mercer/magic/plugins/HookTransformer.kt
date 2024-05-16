package com.mercer.magic.plugins

import com.android.build.api.transform.Context
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.TransformInput
import com.android.build.api.transform.TransformInvocation
import com.android.build.api.transform.TransformOutputProvider

import com.android.build.api.transform.Transform

/**
 * @author :Mercer
 * @Created on 2024/5/16 14:58.
 * @Description:
 *
 */
class HookTransformer : Transform() {

    override fun getName(): String {
        return HookTransformer::class.java.name
    }

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> {
        return mutableSetOf(QualifiedContent.DefaultContentType.CLASSES)
    }

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
        return mutableSetOf(QualifiedContent.Scope.PROJECT, QualifiedContent.Scope.EXTERNAL_LIBRARIES)
    }

    override fun isIncremental(): Boolean {
        return true
    }

    override fun transform(
        context: Context?,
        inputs: MutableCollection<TransformInput>?,
        referencedInputs: MutableCollection<TransformInput>?,
        outputProvider: TransformOutputProvider?,
        isIncremental: Boolean
    ) {

//        Transformer()
//            .addInputs(
//                context,
//                inputs,
//                referencedInputs,
//                outputProvider,
//                isIncremental
//            )
//            .actions {
//                val writer = ClassWriter(0)
//                val reader = ClassReader(it)
//                reader.accept(HookClassVisitor(writer, ext.hooks), 0)
//                writer.toByteArray()
//            }
//            .transform()
//

    }

}