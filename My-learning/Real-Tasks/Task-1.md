The development team wants a production deployment pipeline with this flow:

    Build
      ↓
    Parallel Testing
     ├── Unit Test
     ├── SonarQube
     └── Trivy
      ↓
    Deploy to DEV
      ↓
    Production Approval
      ↓
    Production Deployment

Requirements

Your Jenkinsfile must support:

Parameters

    APP_VERSION — application version
    DEPLOY_ENV — dev, stage, prod
    DEPLOY — boolean
    PROD_APPROVAL — boolean

Environment

    Application name = Empower
    Docker image = Aru1771/Empower3

Pipeline requirements

Build:
    
    Print application and version.

Testing:

    Run these three in parallel:
    Unit Test
    SonarQube
    Trivy Scan

Deployment:

    Deployment stage should run only when:
    DEPLOY == true
    and environment is dev or stage.

Production Approval:

    Production should require input approval.
    Approval must have a 2-minute timeout.

Production Deployment:

    Run only when:
    DEPLOY == true
    DEPLOY_ENV == 'prod'
    PROD_APPROVAL == true

Retry:

    Make the deployment operation retry 3 times.

Post:

    On success → print deployment successful.
    On failure → print deployment failed.
    Always → print pipeline completed.
    Cleanup → print cleanup completed.

Matrix

    For testing, test these combinations:
    Environment: dev, stage
    Version:     1.0, 2.0

So you should get 4 matrix combinations.

Important

Use only the concepts you already completed:
    
    parameters
    environment
    when
    input
    timeout
    retry
    post
    parallel
    matrix

❌ Do NOT use Shared Libraries.

That's your one final Advanced Pipeline Syntax real-world task.

