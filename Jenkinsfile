pipeline {
    agent any

    triggers {
            pollSCM('*/3 * * * *')
    }

    stages {
        stage('Test') {
            steps {
                sh './mvnw clean verify'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
    }
}
