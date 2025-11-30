pipeline {
  agent { label 'ansible-agent' }
  environment {
    REPO_URL = 'https://github.com/CovrigAndrei/php-jenkins.git'
    ANSIBLE_DIR = '/ansible'
  }
  stages {
    stage('Checkout') {
      steps { checkout([$class: 'GitSCM', branches: [[name: '*/main']], userRemoteConfigs: [[url: env.REPO_URL]]]) }
    }
    stage('Copy project to test server') {
      steps {
        sh '''
          ansible -i ${ANSIBLE_DIR}/hosts.ini testserver -m synchronize -a "src=. dest=/var/www/php-jenkins/ delete=yes" --private-key /secrets/ansible_to_test_ssh_key -u ansible || true
        '''
      }
    }
    stage('Set permissions') {
      steps {
        sh "ansible -i ${ANSIBLE_DIR}/hosts.ini testserver -m file -a \"path=/var/www/php-jenkins owner=www-data group=www-data recurse=yes\" --private-key /secrets/ansible_to_test_ssh_key -u ansible || true"
      }
    }
  }
}