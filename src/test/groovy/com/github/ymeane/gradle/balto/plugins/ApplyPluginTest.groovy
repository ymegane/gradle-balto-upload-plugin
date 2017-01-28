package com.github.ymeane.gradle.balto.plugins

import com.github.ymegane.gradle.balto.plugins.entities.BaltoExtension
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test

import static org.junit.Assert.assertTrue

class ApplyPluginTest {

    @Test
    public void checkTask() {
        Project target = ProjectBuilder.builder().build();
        target.apply plugin: 'com.github.ymegane.balto'

        assertTrue(target.extensions.baltoUploadPlugin instanceof BaltoExtension)
    }
}
