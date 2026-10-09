pipeline {
agent any

```
stages {
    stage('Clone Code') {
        steps {
            echo 'Code cloned successfully'
        }
    }

    stage('Build Docker Image') {
        steps {
            sh 'sudo docker build -t gayatri2002/digital-banking-portal .'
        }
    }

    stage('Test Docker Image') {
        steps {
            sh 'sudo docker image inspect gayatri2002/digital-banking-portal'
        }
    }

    stage('Deploy') {
        steps {
            sh '''
                sudo docker stop digital-banking-portal || true
                sudo docker rm digital-banking-portal || true
                sudo docker run -d --name digital-banking-portal -p 8000:8080 gayatri2002/digital-banking-portal
            '''
        }
    }

    stage('Monitor') {
        steps {
            sh 'sudo docker ps'
        }
    }
}
```

}
