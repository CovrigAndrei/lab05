pipeline {
    agent { label 'ansible-agent' }

    environment {
        PHP_PROJECT_DIR = 'php-jenkins'
    }

    stages {
        stage('Checkout PHP project') {
            steps {
                dir("${env.PHP_PROJECT_DIR}") {
                    git url: 'https://github.com/CovrigAndrei/php-jenkins.git', branch: 'main'
                }
            }
        }

        stage('Install PHP Dependencies') {
            steps {
                dir("${env.PHP_PROJECT_DIR}") {
                    sh 'composer install --no-interaction --prefer-dist'
                }
            }
        }

        stage('Run PHP Tests') {
            steps {
                dir("${env.PHP_PROJECT_DIR}") {
                    sh './vendor/bin/phpunit --colors=always tests/'
                }
            }
        }
    }

    post {
        success {
            echo 'PHP Build & Test Pipeline finished successfully!'
        }
        failure {
            echo 'PHP Build & Test Pipeline failed. Verificați logurile!'
        }
        always {
            echo 'Pipeline-ul s-a terminat.'
        }
    }
}