package org.robotsandpencils

import org.gradle.api.Project
import org.gradle.api.Plugin
import org.robotsandpencils.CharlesManifest

class CharlesPlugin implements Plugin<Project> {

    void apply(Project project) {
        // Add the 'greeting' extension object
        def extension = project.extensions.create('charlesManifest', CharlesPluginExtension, project)
        // Add a task that uses the configuration
        project.task('initializeCharles', type:CharlesToFileTask) {
            message = extension.messageProvider
            outputFiles = extension.outputFiles
        }
    }
}
