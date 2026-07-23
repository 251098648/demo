pipeline {
    agent {
        // 使用预装JDK、Maven、Playwright浏览器依赖的镜像
        docker {
            image 'mcr.microsoft.com/playwright/java:v1.45.0-jammy'
            reuseNode true
        }
    }

    stages {
        stage('打印环境信息') {
            steps {
                sh 'java -version'
                sh 'mvn -v'
            }
        }

        stage('编译 & 执行UI自动化') {
            steps {
                sh '''
                    mvn clean test \
                    -Dheadless=true
                '''
                //test only
            }
        }
    }

    post {
        always {
            // 收集测试报告，Jenkins展示结果
            //测试一下
            junit testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true
            archiveArtifacts artifacts: 'target/screenshot/**', allowEmptyArchive: true
        }
        success {
            echo "✅ UI自动化执行成功"
        }
        failure {
            echo "❌ UI自动化有用例失败，请查看报告"
        }
    }
}