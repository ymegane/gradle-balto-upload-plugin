package com.github.ymeane.gradle.balto.plugins

import com.github.ymegane.gradle.balto.plugins.entities.DeployTarget
import org.junit.Test

class DeployTargetTest {
    @Test
    public void apkTest() {
        String name = "name"
        File file = null
        String projectToken = "project token"
        String releaseNote = "release note"
        String readyForReview = "1"

        DeployTarget apk = new DeployTarget(name: name, sourceFile: file, projectToken: projectToken, releaseNote: releaseNote, readyForReview: readyForReview)
        checkDeployTarget(apk, name, file, projectToken,releaseNote, readyForReview)
        checkParams(apk, projectToken, releaseNote, readyForReview)
    }

    @Test
    public void argsNullTest() {
        String name = "name"
        File file = null
        String projectToken = null
        String releaseNote = null
        String readyForReview = null
        DeployTarget apk = new DeployTarget(name)
        apk.sourceFile = file
        checkDeployTarget(apk, name, file, projectToken,releaseNote, readyForReview)
        checkParams(apk, projectToken, releaseNote, readyForReview)
    }

    public static void checkDeployTarget(DeployTarget apk, String name, File file, String projectToken, String releaseNote, String readyForReview) {
        assert apk instanceof DeployTarget
        assert apk.name == name
        assert apk.sourceFile == file
        assert apk.projectToken == projectToken
        assert apk.releaseNote == releaseNote
        assert apk.readyForReview == readyForReview
    }

    public static void checkParams(DeployTarget apk, String projectToken, String releaseNote, String readyForReview) {
        HashMap<String, String> params = apk.toParams()
        assert params["project_token"] == projectToken
        assert params["release_note"] == releaseNote
        assert params["ready_for_review"] == readyForReview
    }
}
