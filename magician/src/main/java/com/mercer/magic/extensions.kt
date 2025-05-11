package com.mercer.magic

import org.gradle.api.Project

/**
 * @author      Mercer
 * @Created     2025/05/05.
 * @Description:
 *   扩展
 */
inline fun <reified E> Collection<E>.onEach(crossinline action: E.() -> Unit) {
    forEach {
        it.action()
    }
}

inline fun Project.afterEvaluate(crossinline action: Project.() -> Unit) {
    afterEvaluate {
        action()
    }
}

inline fun Collection<Project>.afterEvaluate(crossinline action: Project.() -> Unit) {
    forEach { value ->
        value.afterEvaluate {
            it.action()
        }
    }
}