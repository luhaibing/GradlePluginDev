package com.mercer.magic.bean

/**
 * author:  Mercer
 * date:    2025/05/01.
 * desc:
 *   名字的快照
 */
data class Named(
    val name: String,
    val path: String,
    val artifactName: String = path.split(":").filter(String::isNotBlank).joinToString("_"),
)

data class Named2(
    val group: String,
    val name: String,
    val version: String,
)