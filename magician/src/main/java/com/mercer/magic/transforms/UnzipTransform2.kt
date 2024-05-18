package com.mercer.magic.transforms

import org.gradle.api.artifacts.transform.TransformAction
import org.gradle.api.artifacts.transform.TransformOutputs
import org.gradle.api.artifacts.transform.TransformParameters

/**
 * author:  mercer
 * date:    2024/5/16
 * desc:
 *
 */
abstract class UnzipTransform2 : TransformAction<TransformParameters.None> {

    override fun transform(outputs: TransformOutputs) {
        println("压缩的 transform.")
    }

}