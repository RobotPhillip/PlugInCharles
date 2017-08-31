package org.robotsandpencils

import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.FileCollection

class CharlesPluginExtension {
    final org.gradle.api.provider.PropertyState<String> message
    final ConfigurableFileCollection outputFiles

    CharlesPluginExtension(Project project) {
        message = project.property(String)
        setMessage('Hello from CharlesPlugin')
        outputFiles = project.files()
    }

    String getMessage() {
        message.get()
    }

    org.gradle.api.provider.Provider<String> getMessageProvider() {
        message
    }

    void setMessage(String message) {
        this.message.set(message)
    }

    FileCollection getOutputFiles() {
        outputFiles
    }

    void setOutputFiles(FileCollection outputFiles) {
        this.outputFiles.setFrom(outputFiles)
    }
}
