package com.mercer.magic.bean

/**
 * @author      Mercer
 * @Created     2025/05/05.
 * @Description:
 *   名字
 */
data class Named(
    val name: String,
    val path: String,
    val artifactName: String = path.split(":").filter(String::isNotBlank).joinToString("_"),
)