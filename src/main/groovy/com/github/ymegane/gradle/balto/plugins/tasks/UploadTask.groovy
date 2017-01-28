package com.github.ymegane.gradle.balto.plugins.tasks

import groovyx.net.http.ContentType
import groovyx.net.http.HttpResponseDecorator
import groovyx.net.http.Method
import org.apache.http.entity.mime.MultipartEntity
import org.apache.http.entity.mime.content.FileBody
import org.apache.http.entity.mime.content.StringBody
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction
import com.github.ymegane.gradle.balto.plugins.entities.DeployTarget
import com.github.ymegane.gradle.balto.plugins.utils.HTTPBuilderFactory

import java.nio.charset.Charset

class UploadTask extends DefaultTask {
    String outputName
    boolean hasSigningConfig
    File defaultSourceFile

    @TaskAction
    def upload() {
        if (!hasSigningConfig)
            throw new GradleException('Cannot upload a build without code signature to Balto')

        DeployTarget target = findTarget()
        if (!target.sourceFile?.exists())
            throw new GradleException("APK file not found")

        uploadProject(project, target)
    }

    private DeployTarget findTarget() {
        DeployTarget target = project.baltoUpload.apks.findByName(outputName)
        if (!target)
            target = new DeployTarget(outputName)
        if (!target.sourceFile)
            target.sourceFile = defaultSourceFile
        fillFromEnv(target)
        target
    }

    private def fillFromEnv(DeployTarget target) {
        target.with {
            sourceFile = sourceFile ?: project.file(System.getenv('BALTO_SOURCE_FILE'))
            projectToken = projectToken ?: System.getenv('BALTO_PROJECT_TOKEN')
            releaseNote = releaseNote ?: System.getenv('BALTO_RELEASE_NOTE')
            readyForReview = readyForReview ?: System.getenv('BALTO_READY_FOR_REVIEW')
        }
    }

    def uploadProject(Project project, DeployTarget apk) {
        String token = getToken(project)

        def result = postApk(token, apk)
        errorHandling(apk, result)

        result.data
    }

    private static void errorHandling(apk, result) {
        if (result.status != 200 || result.data.error) {
            throw new GradleException("${apk.name} error message: ${result.data.message}")
        }
    }

    private static String getToken(Project project) {
        String token = project.baltoUpload.userToken ?: System.getenv('BALTO_USER_TOKEN')
        if (!token?.trim()) {
            throw new GradleException('user_token is missing. Please enter the user_token.')
        }
        token
    }

    private HttpResponseDecorator postApk(String token, DeployTarget apk) {
        MultipartEntity entity = new MultipartEntity()
        Charset charset = Charset.forName('UTF-8')

        File file = apk.sourceFile
        entity.addPart("package", new FileBody(file.getAbsoluteFile()))
        entity.addPart("user_token", new StringBody(token, charset))

        HashMap<String, String> params = apk.toParams()
        for (String key : params.keySet()) {
            entity.addPart(key, new StringBody(params.get(key), charset))
        }

        HTTPBuilderFactory.restClient(project.baltoUploadPlugin.endpoint).request(Method.POST, ContentType.JSON) { req ->
            uri.path = " /api/v2/builds/upload"
            req.entity = entity
        } as HttpResponseDecorator
    }
}
