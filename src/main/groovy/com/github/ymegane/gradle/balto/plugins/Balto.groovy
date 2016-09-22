package com.github.ymegane.gradle.balto.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import com.github.ymegane.gradle.balto.plugins.entities.BaltoExtension
import com.github.ymegane.gradle.balto.plugins.entities.DeployTarget
import com.github.ymegane.gradle.balto.plugins.tasks.UploadTask

class Balto implements Plugin<Project> {
    HashSet<String> tasksToCreate

    void apply(Project project) {
        tasksToCreate = new HashSet<>()
        setupExtension project
        project.afterEvaluate { prj ->
            if (['com.android.application', 'android'].any { prj.plugins.hasPlugin(it) }) {
                createBaltoTasks prj
            }
        }
    }

    def setupExtension(Project project) {
        def apkTargets = project.container(DeployTarget)
        apkTargets.all {
            tasksToCreate.add name
        }
        project.extensions.add 'baltoUpload', new BaltoExtension(apkTargets)
    }

    def createBaltoTasks(project) {
        createMultipleUploadTask(project, tasksToCreate)

        // @see ApplicationVariantFactory#createVariantData
        // variant is for applicationFlavors
        project.android.applicationVariants.all { variant ->
            // variant is for splits
            variant.outputs.eachWithIndex { output, idx ->
                createTask(project, output)
                tasksToCreate.remove output.name
            }
        }

        tasksToCreate.each { name ->
            createTask(project, name)
        }
    }

    private void createTask(project, output) {
        def name
        def signingReady = true
        def isUniversal = true
        def assemble = null
        def outputFile = null

        if (output instanceof String) {
            name = output
        } else {
            name = output.name
            signingReady = output.variantOutputData.variantData.signed
            isUniversal = output.outputs.get(0).filters.size() == 0
            assemble = output.assemble
            outputFile = output.outputFile
        }

        def capitalized = name.capitalize()
        def taskName = "uploadBalto${capitalized}"
        project.task(taskName,
                type: UploadTask,
                dependsOn: ([assemble] - null),
                overwrite: true) {

            def desc = "Deploy assembled ${capitalized} to Balto"

            // require signing config to build a signed APKs
            if (!signingReady) {
                desc += " (requires valid signingConfig setting)"
            }

            // universal builds show in Balto group
            if (isUniversal) {
                group 'Balto'
            }

            description desc
            outputName name
            hasSigningConfig signingReady

            defaultSourceFile outputFile
        }
    }

    def createMultipleUploadTask(Project project, HashSet<String> dependsOn) {
        if (dependsOn.empty) return
        project.task 'uploadBalto',
                dependsOn: dependsOn.toArray().collect { "uploadBalto${it.capitalize()}" },
                description: 'Upload all builds defined in build.gradle to Balto',
                group: 'Balto'
    }
}
