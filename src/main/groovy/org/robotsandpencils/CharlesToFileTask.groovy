package org.robotsandpencils

import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.util.GFileUtils

class CharlesToFileTask extends DefaultTask {

    final org.gradle.api.provider.PropertyState<String> message = project.property(String)
    final ConfigurableFileCollection outputFiles = project.files()

    String getGroup() {
        "Charles to File"
    }

    String getDescription() {
        "Write Charles proxy settings to file"
    }

    @Input
    String getMessage() {
        message.get()
    }

    void setMessage(String message) {
        this.message.set(message)
    }

    void setMessage(org.gradle.api.provider.Provider<String> message) {
        this.message.set(message)
    }

    FileCollection getOutputFiles() {
        outputFiles
    }

    void setOutputFiles(FileCollection outputFiles) {
        this.outputFiles.setFrom(outputFiles)
    }

    @TaskAction
    def configureManifest() {
        getOutputFiles().each {
            logger.quiet "Writing port to manifest"
//            it.text = getMessage()
            it.append(getMessage() + "\n")
        }

        new File("charlesLib/src/main/res/xml").mkdirs()

        def gitIgnoreFile = new File('charlesLib', '.gitignore')
        gitIgnoreFile.text = ''
        gitIgnoreFile << '''/build
'''

        def buildGradleFile = new File('charlesLib', 'build.gradle')
        buildGradleFile.text = ''
        buildGradleFile << '''apply plugin: \'com.android.library\'
android {
    compileSdkVersion 26
    buildTypes {
        debug {}
        releaseApp {}
        releaseSdk {}
    }
    defaultConfig {
        minSdkVersion 14
    }
}
'''

        def androidManifestFile = new File('charlesLib/src/main', 'AndroidManifest.xml')
        androidManifestFile.text = ''
        androidManifestFile << '''<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
          package="com.robotandpencils.charleslib">
    <application
        android:networkSecurityConfig="@xml/network_security_config">
    </application>
</manifest>
'''

        def networkSecurityConfigFile = new File('charlesLib/src/main/res/xml', 'network_security_config.xml')
        networkSecurityConfigFile.text = ''
        networkSecurityConfigFile << '''<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <debug-overrides>
        <trust-anchors>
            <!-- Trust user added CAs while debuggable only -->
            <certificates src="user" />
        </trust-anchors>
    </debug-overrides>
</network-security-config>
'''

        def settingsGradleFile = new File('settings.gradle')
        if (!settingsGradleFile.getText('UTF-8').contains('charlesLib')) {
            settingsGradleFile << ", ':charlesLib'"
        }

        def appBuildGradleFile = new File('app/build.gradle')
        if (!GFileUtils.readFile(appBuildGradleFile).contains('charlesLib')) {
            new File('app/tmp.build.gradle').withWriter { w ->
                new File('app/build.gradle').eachLine { line ->
                    w << line.replaceAll('dependencies \\{', 'dependencies \\{\n    implementation project(\':charlesLib\')') + System.getProperty("line.separator")
                }
            }
            new File('app/build.gradle').renameTo('app/old.build.gradle')
            new File('app/tmp.build.gradle').renameTo('app/build.gradle')
        }

    }

}
