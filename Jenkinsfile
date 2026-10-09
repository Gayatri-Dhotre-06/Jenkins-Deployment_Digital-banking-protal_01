pipeline {
agent any

```
stages {
    stage('Clone Code') {
        steps {
            echo 'Code cloned successfully'
        }
    }

    stage('Build') {
        steps {
            sh 'sudo docker build -t gayatri2002/digital-banking-portal .'
            echo 'Docker image built successfully'
        }
    }

    stage('Test') {
        steps {
            echo 'Testing application...'
            sh 'sudo docker image inspect gayatri2002/digital-banking-portal'
        }
    }

    stage('Deploy') {
        steps {
            sh '''
                sudo docker stop digital-banking-portal || true
                sudo docker rm digital-banking-portal || true
                sudo docker run -d --name digital-banking-portal -p 8000:8000 gayatri2002/digital-banking-portal
            '''
            echo 'Application deployment command completed'
        }
    }

    stage('Monitor') {
        steps {
            sh 'sudo docker ps'
            echo 'Container status checked'
        }
    }
}
```

}
