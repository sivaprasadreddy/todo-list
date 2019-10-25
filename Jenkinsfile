#!groovy
@Library('jenkins-shared-library')
import com.sivalabs.JenkinsSharedLib

properties([
    parameters([
        booleanParam(defaultValue: false, name: 'PUBLISH_TO_DOCKERHUB', description: 'Publish Docker Image to DockerHub?'),
        booleanParam(defaultValue: false, name: 'DEPLOY_ON_HEROKU', description: 'Should deploy on Heroku?'),
        booleanParam(defaultValue: false, name: 'RUN_PERF_TESTS', description: 'Should run Performance Tests?')
    ])
])

env.DOCKER_USERNAME = 'sivaprasadreddy'
env.APPLICATION_NAME = 'todo-list'

def utils = new JenkinsSharedLib(this, env, params, scm, currentBuild)

node {

    try {
        utils.checkout()
        dir("todo-api-spring-boot") {
            utils.runMavenTests()
            utils.runOWASPChecks()
            utils.publishDockerImage()
            utils.deployOnHeroku()
        }
        dir("todo-ui-react") {
            utils.npmBuild()
            utils.npmTest()
        }
        dir("todo-list-gatling-perf-tests") {
            utils.runMavenGatlingTests()
        }
    }
    catch(err) {
        echo "ERROR: ${err}"
        currentBuild.result = currentBuild.result ?: "FAILURE"
    }
}