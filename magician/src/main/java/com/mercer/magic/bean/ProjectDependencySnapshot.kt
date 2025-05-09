package com.mercer.magic.bean

/**
 * author:  Mercer
 * date:    2025/05/01.
 * desc:
 *   本地模块依赖替换信息的快照
 */
data class ProjectDependencySnapshot(
    val named: Named,
    val res: ModuleRes,
    val group: String,
) {
    override fun toString(): String {
        val group = group
        val artifact = res.named.artifactName
        val version = res.lastModifiedTime
        return arrayOf(group, artifact, version).joinToString(":")
    }
}