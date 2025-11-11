pipeline {
    agent any

    tools {
        maven 'Maven-3.9'
        jdk 'JDK-17'
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Obteniendo código del repositorio...'
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo 'Compilando la aplicación...'
                sh 'mvn clean compile'
            }
        }

        stage('Test') {
            steps {
                echo 'Ejecutando tests...'
                sh 'mvn test'
            }
        }

        stage('Package') {
            steps {
                echo 'Empaquetando aplicación...'
                sh 'mvn package -DskipTests'
            }
        }

        stage('Deploy') {
            steps {
                echo 'Desplegando aplicación...'
                sh 'java -jar target/mi-playlist-1.0.0.jar &'
            }
        }
    }

    post {
        success {
            echo '¡Pipeline ejecutado exitosamente! ✅'
        }
        failure {
            echo 'Pipeline falló ❌'
        }
    }
}