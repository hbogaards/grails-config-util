package org.grails.plugins.config.util

import groovy.util.logging.Slf4j
import org.grails.config.CodeGenConfig

@Slf4j
class ConfigSlurperUtil {

    static GroovyClassLoader groovyClassLoader = new GroovyClassLoader(CodeGenConfig.getClassLoader())

    /**
     * Slurp a config groovy file. "Environment filtering" is applied.
     */
    static ConfigObject slurpGroovyFile(String path, String env) {
        log.info "Slurping config groovy file: '${path}' (env: '${env}')"
        def url = new File(path).toURI().toURL()
        def config = new ConfigSlurper(env).parse(url)
        config
    }

    /**
     * Slurp a config groovy resource. "Environment filtering" is applied.
     * <p>
     * (Running in IDEA or with "grails run-app",
     * slurping "application-config .groovy" picks up
     * "build/resources/main/application-config.groovy".)
     */
    static ConfigObject slurpGroovyResource(ClassLoader loader, String path, String env) {
        log.info "Slurping config properties resource: '${path}' (env: '${env}')"
        URL url = loader.getResource(path)
        if (!url) {
            throw new Error("Not found config groovy resource: '${path}'")
        }
        log.info "Slurping config groovy resource: '${url}' (env: '${env}')"
        def config = new ConfigSlurper(env).parse(url)
        config
    }

    /**
     * Slurp a config properties file.
     */
    static ConfigObject slurpPropertiesFile(String path) {
        log.info "Slurping config properties file: '${path}'"
        def props = new Properties()
        new File(path).withInputStream { props.load(it) }
        def config = new ConfigSlurper().parse(props)
        config
    }

    /**
     * Slurp a config properties resource.
     * <p>
     * (Running in IDEA or with "grails run-app",
     * slurping "application.properties" picks up
     * "build/resources/main/application.properties"
     */
    static ConfigObject slurpPropertiesResource(ClassLoader loader, String path) {
        log.info "Slurping config properties resource: '${path}'"
        URL url = groovyClassLoader.getResource(path)
        if (!url) {
            def fileConfig = slurpPropertiesFile(path)
            if (!fileConfig) {
                throw new Error("Not found config properties resource: '${path}'")
            }
            return fileConfig
        }
        URI uri = url.toURI()
        log.info "Slurping config properties resource: '${uri}'"
        def props = new Properties()
        new File(uri).withInputStream { props.load(it) }
        def configSlurper = new ConfigSlurper()
        configSlurper.classLoader = groovyClassLoader
        def config = configSlurper.parse(props)
        config
    }
}
