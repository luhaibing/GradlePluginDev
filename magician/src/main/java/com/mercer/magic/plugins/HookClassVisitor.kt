package com.mercer.magic.plugins

import com.android.build.api.instrumentation.ClassContext
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes


/**
 * @author :Mercer
 * @Created on 2024/5/16 15:09.
 * @Description:
 *
 */
class HookClassVisitor(
    nextVisitor: ClassVisitor,
    private val ctx: ClassContext? = null,
) : ClassVisitor(Opcodes.ASM9, nextVisitor) {

    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        println()
        return object :MethodVisitor(Opcodes.ASM9){}
    }


}