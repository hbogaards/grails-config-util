package org.grails.plugins.config.util

import grails.plugins.Plugin

class ConfigUtilGrailsPlugin extends Plugin {

    def grailsVersion = "3.1.10 > *"

    def pluginExcludes = [
        "grails-app/views/error.gsp"
    ]

    def title = "Grails Config Util" // Headline display name of the plugin
    def author = "Hans Bogaards"
    def authorEmail = "hbogaards@gmail.com"
    def description = '''\
Brief summary/description of the plugin.
'''

    // URL to the plugin's documentation
    def documentation = "http://hbogaards.github.io/grails-config-util/latest/"

    // License: one of 'APACHE', 'GPL2', 'GPL3'
    def license = "APACHE"

    def organization = [ name: "ANWB B.V.", url: "http://www.anwb.nl/" ]

    def developers = [ [ name: "Jos Annevelink", email: "jannevelink@anwb.nl" ]]

    // Location of the plugin's issue tracker.
    def issueManagement = [ system: "GITHUB", url: "https://github.com/hbogaards/grails-config-util/issues" ]

    // Online location of the plugin's browseable source code.
    def scm = [ url: "https://github.com/hbogaards/grails-config-util/" ]
}
