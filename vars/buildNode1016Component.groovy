/*
 * Toolform-compatible Jenkins 2 Pipeline build step for NodeJS 10.16 apps using the node1016 builder
 */

def call(Map config) {

  def artifactDir = "${config.project}-${config.component}-artifacts"
  def testOutput = "${config.project}-${config.component}-tests.xml"

  final yarn = { cmd ->
    ansiColor('xterm') {
      dir(config.baseDir) {
        sh "NODE_OPTIONS=--max-old-space-size=4096 JEST_JUNIT_OUTPUT=${testOutput} yarn ${cmd}"
      }
    }
  }
  

    stage('Build Details') {
      echo "Project:   ${config.project}"
      echo "Component: ${config.component}"
      echo "BuildNumber: ${config.buildNumber}"
    }

    stage('Install dependencies') {
      yarn "install"
    }

    stage('Test') {
      yarn 'test --ci --testResultsProcessor="jest-junit"'
      junit allowEmptyResults: true, testResults: testOutput
    }

    stage('Build') {
      yarn "build"
    }
   


}
