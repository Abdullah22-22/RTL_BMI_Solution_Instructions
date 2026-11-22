pipeline {
    agent any

    tools {
        maven 'Maven3'   // Must match the name configured in Jenkins Global Tool Configuration
    }

    environment {
       PATH = "C:\\Program Files\\Docker\\Docker\\resources\\bin;${env.PATH}"
       JAVA_HOME = 'C:\\Program Files\\Eclipse Adoptium\\jdk-21.0.11.10-hotspot' // Adjust to your actual JDK path

        SONARQUBE_SERVER = 'SonarQubeServer'
        SONAR_TOKEN = credentials('SONAR_TOKEN')
        DOCKERHUB_CREDENTIALS_ID = 'dockerhub-credentials'
        DOCKERHUB_REPO = 'abdullah22hel/bmi_cal'
        DOCKER_IMAGE_TAG = 'latest'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/Abdullah22-22/RTL_BMI_Solution_Instructions.git'
            }
        }

        stage('Build') {
            steps {
                bat 'mvn clean install'
            }
        }

        stage('Test') {
            steps {
                bat 'mvn test'
            }
        }

        stage('SonarQube Analysis') {
             steps {
                  withSonarQubeEnv("${env.SONARQUBE_SERVER}") {
                  bat 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.1.2184:sonar -Dsonar.projectKey=BMI_Cal -Dsonar.projectName=BMI_Cal'
                   }
             }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    def app = docker.build("${DOCKERHUB_REPO}:${DOCKER_IMAGE_TAG}")
                    env.BUILT_IMAGE = app.id
                }
            }
        }

        stage('Push Docker Image to Docker Hub') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', DOCKERHUB_CREDENTIALS_ID) {
                       docker.image("${DOCKERHUB_REPO}:${DOCKER_IMAGE_TAG}").push()
                    }
                }
            }
        }
    }
}