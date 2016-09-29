package com.github.ymegane.gradle.balto.plugins.entities

import org.gradle.api.Named

class DeployTarget implements Named {
    String name

    File sourceFile
    String projectToken
    String releaseNote
    String readyForReview

    DeployTarget() {}

    DeployTarget(String name) {
        this.name = name
    }

    public HashMap<String, String> toParams() {
        HashMap<String, String> params = new HashMap<String, String>()
        if (projectToken != null) {
            params.put("project_token", projectToken)
        }
        if (readyForReview != null) {
            params.put("ready_for_review", readyForReview)
        }
        if (releaseNote != null) {
            params.put("release_note", releaseNote)
        }
        return params
    }
}
