pipeline {
    agent any
    stages {
        stage('连通测试') {
            steps {
                echo "✅ Jenkinsfile 加载成功！链路通了"
            }
        }
    }
    post {
        always {
            echo "构建结束"
        }
    }
}