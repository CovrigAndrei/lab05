pipeline {
  agent { label 'ansible-agent' }
  environment {
    ANSIBLE_DIR = '/ansible'
    REPO_URL = 'https://github.com/CovrigAndrei/lab05.git'
  }
  stages {
    stage('Checkout') {
      steps {
        checkout([$class: 'GitSCM', branches: [[name: '*/main']], userRemoteConfigs: [[url: env.REPO_URL]]])
      }
    }
    stage('Run Ansible Playbook') {
      steps {
        sh """
          ansible-playbook -i ${ANSIBLE_DIR}/hosts.ini \
          ${ANSIBLE_DIR}/setup_test_server.yml \
          --private-key /secrets/ansible_to_test_ssh_key \
          -u ansible --ssh-extra-args='-o StrictHostKeyChecking=no'
        """
      }
    }
  }
}
