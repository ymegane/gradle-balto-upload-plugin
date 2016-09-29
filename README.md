# Gradle Balto Upload Plugin

This is the Balto upload plugin for the Gradle.  
You can build and deploy your apps to Balto by running a single task.

Balto [https://www.balto.io/](https://www.balto.io/)

Getting started
-------

1) Open your `build.gradle` on your project root and add a dependency.

```gradle
buildscript {
    repositories {
        maven {
            url 'https://ymegane.github.io/gradle-balto-upload-plugin/repository'
        }
    }
    dependencies {
        classpath 'com.github.ymegane.balto:balto-upload:0.1.+'
    }
}
```

2) Open your module build script file and add the following scripts just after apply plugin: 'com.android.application'.

```gradle
apply plugin: 'com.github.ymegane.balto'

// Optional configurations
baltoUpload {
    userToken = "[your user token]"

    // You can also specify additional options for each flavor.
    apks {
       // this correspond to `debug` flavor and used for `uploadBaltoDebug` task
        debug {
            sourceFile = file("./build/outputs/apk/app-debug.apk")
            projectToken = "[your project token]"
            releaseNote = "Upload from gradle plugin"
            // API default value is "1"
            readyForReview = "0"
        }
    }
}

```
Usage
-------
### Tasks
Run ./gradlew tasks on your project root to see all available tasks.
* uploadBalto[FlavorName] - Build and upload app of [FlavorName]

If you define flavors in apks section, there will also be uploadBalto task which can upload all the flavors at once.

### Environment Variables
If you are using Continuous Integration, you can set these environment variables to provide default values for Balto upload Plugin instead of writing in build.gradle.

- BALTO_USER_TOKEN
- BALTO_SOURCE_FILE
- BALTO_PROJECT_TOKEN
- BALTO_RELEASE_NOTE
- BALTO_READY_FOR_REVIEW

License
-------
```
This project was created based on the gradle-deploygate-plugin

Copyright 2016 ymegane

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

---------------------------------------------------------------------------------------------
License for gradle-deploygate-plugin:
Copyright 2015 DeployGate Inc.

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
```
