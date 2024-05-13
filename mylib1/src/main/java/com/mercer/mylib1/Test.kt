package com.mercer.mylib1

/**
 * author:  mercer
 * date:    2024/5/14
 * desc:
 *
 */
object Test {

    fun work() {
        println(this::class.java.`package`)
        com.mercer.mylib2.Test.work()
        com.mercer.mylib3.Test.work()
    }

}