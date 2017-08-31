package org.robotsandpencils

import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

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
            logger.quiet "Writing port to manifiest"
//            it.text = getMessage()
            it.append(getMessage()+"\n")
        }
    }
}
