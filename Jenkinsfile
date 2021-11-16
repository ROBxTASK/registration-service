node('robxtask-jenkins-slave') {

    // -----------------------------------------------
    // --------------- Staging Branch ----------------
    // -----------------------------------------------

    if (env.BRANCH_NAME == 'staging') {

        stage('Clone and Update') {
            git(url: 'https://github.com/ROBxTASK/registration-service.git', branch: env.BRANCH_NAME)
        }

        stage('Build Application - staging') {
            sh 'mvn clean install -Denv=staging'
        }

        stage('Build Docker - staging') {
            sh 'docker build -t robxtask/registration-service:staging ./target'
        }

        stage('Push Docker - staging') {
            withCredentials([usernamePassword(credentialsId: 'docker-login-credentials', usernameVariable: 'USER', passwordVariable: 'PASS')]) {
                sh 'docker login --username $USER --password $PASS'
                sh 'docker push robxtask/registration-service:staging'
            }
        }

        stage('Deploy - staging') {
            sh 'ssh staging "cd /srv/docker-setup/staging/ && ./run-staging.sh restart-single registration-service"'
        }
    }

    // -----------------------------------------------
    // ---------------- Release Tags -----------------
    // -----------------------------------------------
    if( env.TAG_NAME ==~ /^\d+.\d+.\d+$/) {

        stage('Clone and Update') {
            git(url: 'https://github.com/ROBxTASK/registration-service.git', branch: 'master')
        }

        stage('Set version') {
            sh 'mvn versions:set -DnewVersion=' + env.TAG_NAME
        }

        stage('Build Application - ' + env.TAG_NAME) {
            sh 'mvn clean install -Denv=prod'
        }

        stage('Build Docker - ' + env.TAG_NAME) {
            sh 'docker build -t robxtask/registration-service ./target'
        }

        stage('Push Docker - ' + env.TAG_NAME) {
            sh 'docker push robxtask/registration-service:latest'
            sh 'docker tag robxtask/registration-service:latest robxtask/registration-service:' + env.TAG_NAME
            sh 'docker push robxtask/registration-service:' + env.TAG_NAME
        }

        stage('Deploy - ' + env.TAG_NAME) {
            // FIXME: sh 'ssh robxtask "cd /data/deployment_setup/prod/ && export COMPOSE_HTTP_TIMEOUT=600 && sudo ./run-prod.sh restart-single registration-service"'
        }

    }
}
