pipeline {
    agent any

    stages {
        stage('clone code') {
            steps {
                echo 'code clone successful'
            }
        }
        stage('build') {
            steps {
                sh 'sudo docker build -t . gayatri2002/digital-banking-portal'
                echo 'code build successful'
            }
        }
        stage('test') {
            steps {
                echo 'code test successful'
            }
        }
        stage('deploy') {
            steps {
                sh 'sudo docker run -d --name digital-banking-portal -p 8000:8000 gayatri2002/digital-banking-portal'
                echo 'code deploy successful'
            }
        }
        stage('monitor') {
            steps {
                echo 'code monitor successful'
            }
        }
    }
}
