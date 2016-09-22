package com.github.ymegane.gradle.balto.plugins.entities

import org.gradle.api.NamedDomainObjectContainer
import com.github.ymegane.gradle.balto.plugins.Config

public class BaltoExtension {
    def String userToken
    def String endpoint = Config.BALTO_ROOT

    def NamedDomainObjectContainer<DeployTarget> apks

    public BaltoExtension(NamedDomainObjectContainer<DeployTarget> apkTargets) {
        this.apks = apkTargets
    }

    public apks(Closure closure) {
        apks.configure(closure)
    }
}
