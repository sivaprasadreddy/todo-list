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
                    junit 'target/failsafe-reports/*.xml'

                    publishHTML(target:[
                         allowMissing: true,
                         alwaysLinkToLastBuild: true,
                         keepAll: true,
                         reportDir: 'target/site/jacoco-aggregate',
                         reportFiles: 'index.html',
                         reportName: "Jacoco Report"
                    ])
                }
            }
        }

        stage('OWASP Dependency Check') {
            steps {
                sh './mvnw dependency-check:check'
            }
            post {
                always {
                    publishHTML(target:[
                         allowMissing: true,
                         alwaysLinkToLastBuild: true,
                         keepAll: true,
                         reportDir: 'target',
                         reportFiles: 'dependency-check-report.html',
                         reportName: "OWASP Dependency Check Report"
                    ])
                }
            }
        }
    }
}
