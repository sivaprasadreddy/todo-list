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

def DOCKER_USERNAME = 'sivaprasadreddy'
def API_IMAGE_NAME = 'todo-api-spring-boot'
def UI_IMAGE_NAME = 'todo-ui-react'

def utils = new JenkinsSharedLib(this, env, params, scm, currentBuild)

node {

    try {
        utils.checkout()
        dir("todo-api-spring-boot") {
            utils.runMavenTests()
            utils.runOWASPChecks()
            utils.publishDockerImage(DOCKER_USERNAME, API_IMAGE_NAME)
            utils.deployOnHeroku()
        }
        dir("todo-ui-react") {
            utils.npmBuild()
            utils.npmTest()
            utils.publishDockerImage(DOCKER_USERNAME, UI_IMAGE_NAME)
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