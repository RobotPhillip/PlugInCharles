package org.robotsandpencils

import org.gradle.api.Project
import org.gradle.api.Plugin
import org.gradle.api.tasks.Copy

class CharlesPlugin implements Plugin<Project> {

    void apply(Project project) {
        def someTask = project.tasks.create('copyIml', Copy) {
            from project.zipTree('/Users/pwray/robots/plugins/repo/org/gradle/charlesPlugin/1.0/charlesPlugin-1.0.jar')
            include '**/charles.iml'
            into 'charlesLib'
        }
        someTask.dependsOn "consumeCharles"
        someTask.group = "Charles to File"
    }

}
