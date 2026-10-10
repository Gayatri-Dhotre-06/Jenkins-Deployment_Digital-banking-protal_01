pipeline {
agent any

stages {
    stage('Clone Code') {
        steps {
            echo 'Code checkout handled by Jenkins'
        }
    }

    stage('Build Docker Image') {
        steps {
            sh 'docker build -t gayatri2002/digital-banking-portal .'
            echo 'Docker Image build successful'
        }
    }

    stage('Test Image') {
        steps {
            sh 'docker image inspect gayatri2002/digital-banking-portal'
            echo 'Docker image is clear/ no issues'
        }
    }

    stage('Deploy') {
        steps {
            sh  'docker stop digital-banking-portal || true'
            sh  'docker rm digital-banking-portal || true'
            sh  'docker run -d --name digital-banking-portal -p 8000:8080 gayatri2002/digital-banking-portal'
            echo 'Docker container is created successful'
        }
    }

    stage('Monitor') {
        steps {
            sh 'docker ps'
            echo 'Docker files are running successful'
        }
    }
}

}
