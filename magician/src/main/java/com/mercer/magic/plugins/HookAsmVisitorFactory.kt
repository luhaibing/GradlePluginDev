package com.mercer.magic.plugins

import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.android.build.api.instrumentation.InstrumentationParameters
import org.objectweb.asm.ClassVisitor


/**
 * @author :Mercer
 * @Created on 2024/5/16 15:08.
 * @Description:
 *
 */
abstract class HookAsmVisitorFactory: AsmClassVisitorFactory<InstrumentationParameters.None> {
    override fun createClassVisitor(
        classContext: ClassContext,
        nextClassVisitor: ClassVisitor
    ): ClassVisitor {
        println()
        println()
        // Logger.error(classContext.currentClassData.className)
        return HookClassVisitor(
            nextVisitor = nextClassVisitor,
            ctx = classContext,
        )
    }

    override fun isInstrumentable(classData: ClassData): Boolean {
        return true
    }

}