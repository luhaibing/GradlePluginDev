package com.mercer.magic

import java.text.SimpleDateFormat

/**
 * @author :Mercer
 * @Created on 2025/05/05.
 * @Description:
 *   全局常量
 */
@Suppress("SimpleDateFormat")
val dataFormat = SimpleDateFormat("yyyyMMddHHmmSSS")

const val MAIN = "main"
const val DEBUG = "debug"

val ASSEMBLE_REGEX = ":?[a-zA-Z0-9]+:assemble(.*)(debug|trial|release)".toRegex(RegexOption.IGNORE_CASE)
val PUBLISH_REGEX = "publish((.*Publications?ToMaven(Repository|Local))|ToMavenLocal)?".toRegex(RegexOption.IGNORE_CASE)