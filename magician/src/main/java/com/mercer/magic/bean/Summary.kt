package com.mercer.magic.bean

import com.mercer.magic.extensions.ModuleExtensions
import com.mercer.magic.md5
import org.gradle.api.Project
import java.io.File


/**
 * @author :Mercer
 * @Created on 2024/5/16 14:04.
 * @Description:
 *      概略
 */
data class Summary(
    val group: String,
    val artifact: String,
    val version: String,
    val date: Long,
    val contents: List<Content>,
) {
    constructor(
        extension: ModuleExtensions,
        project: Project,
        contents: List<File>
    ) : this(
        group = extension.groupId,
        artifact = project.name,
        version = extension.version,
        date = System.currentTimeMillis(),
        contents = contents.map {
            Content(
                value = it.absolutePath,
                name = it.name,
                md5 = it.md5(),
                lastModified = it.lastModified()
            )
        }
    )
}

data class Content(
    val value: String,
    val name: String,
    val md5: String,
    val lastModified: Long
)