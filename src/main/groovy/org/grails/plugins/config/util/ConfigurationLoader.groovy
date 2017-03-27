package org.grails.plugins.config.util

import grails.boot.config.GrailsAutoConfiguration
import groovy.util.logging.Slf4j
import org.grails.core.io.DefaultResourceLocator
import org.springframework.core.env.Environment
import org.springframework.core.env.MapPropertySource

/**
 * TBD
 * <p>
 * From
 * <tt>http://grails.1312388.n4.nabble.com/Grails-3-External-config-td4658823.html</tt>,
 * where the class is named <tt>ApplicationConfigurationLoader</tt>.
 * Modified and extended.
 */
@Slf4j
class ConfigurationLoader {

    private static final String APPLICATION_GROOVY = 'application.groovy'
    private static final String RUNTIME_GROOVY = 'runtime.groovy'

    private GrailsAutoConfiguration _app
    private Environment _env

    ConfigurationLoader(GrailsAutoConfiguration app, Environment env) {
        _app = app
        _env = env

        //_logEnvironment(env)
    }

    private _logEnvironment(Environment env) {
        for (Object obj in env.systemProperties) {
            log.info("Env.systemProperty: ${obj}")
        }
        for (Object obj in env.systemEnvironment) {
            log.info("Env.systemEnvironment: ${obj}")
        }
    }

    public load() {
        if (!_app || !_env) {
            return
        }

        DefaultResourceLocator resourceLocator = new DefaultResourceLocator()
        def loader = _app.getClass().classLoader
        def appGroovy = loader.getResource(APPLICATION_GROOVY)
        if (!appGroovy) {
            log.info("Not found: '${APPLICATION_GROOVY}'")
            return
        }

        def envName = grails.util.Environment.current.name
        log.info("envName='${envName}'")
        def appConf = new ConfigSlurper(envName).parse(appGroovy)
        def confLocs = appConf.grails?.config?.locations
        if (!confLocs) {
            def runGroovy = loader.getResource(RUNTIME_GROOVY)
            if (runGroovy) {
                def runConf = new ConfigSlurper(envName).parse(runGroovy)
                confLocs = runConf.grails?.config?.locations
            }
        }
        if (!confLocs) {
            log.info("Not present: grails.config.locations")
            return
        }

        for (String confLoc in confLocs) {
            def confRes = resourceLocator.findResourceForURI(confLoc)
            if (!confRes) {
                log.info("Not found: '${confLoc}': skipping")
                continue
            }
            def url = confRes.getURL()
            log.info("Reading: '${url}")
            def config = new ConfigSlurper(envName).parse(url)
            def props = new MapPropertySource(confLoc, config)
            _env.propertySources.addFirst(props)
        }
    }
}
