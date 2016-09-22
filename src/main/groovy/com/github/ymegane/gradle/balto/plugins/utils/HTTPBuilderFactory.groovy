package com.github.ymegane.gradle.balto.plugins.utils

import groovyx.net.http.HTTPBuilder
import groovyx.net.http.RESTClient
import org.apache.http.impl.conn.ProxySelectorRoutePlanner
import com.github.ymegane.gradle.balto.plugins.Config

class HTTPBuilderFactory {
    static HTTPBuilder setDefaultProxy(HTTPBuilder httpBuilder) {
        httpBuilder.client.routePlanner = new ProxySelectorRoutePlanner(
                httpBuilder.client.connectionManager.schemeRegistry,
                ProxySelector.default
        )
        httpBuilder
    }

    static HTTPBuilder setDefaultRequestHeaders(HTTPBuilder httpBuilder) {
        httpBuilder.headers = [
                'User-Agent': Config.USER_AGENT
        ]
        httpBuilder
    }

    static HTTPBuilder httpBuilder(endpoint) {
        setDefaultProxy(setDefaultRequestHeaders(new HTTPBuilder(endpoint)))
    }

    static RESTClient restClient(endpoint) {
        setDefaultProxy(setDefaultRequestHeaders(new RESTClient(endpoint))) as RESTClient
    }
}
