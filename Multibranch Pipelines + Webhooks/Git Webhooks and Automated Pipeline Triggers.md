Git Webhooks and Automated Pipeline Triggers:
============================================

Today we'll learn how to automatically trigger Jenkins pipelines when developers push code to GitHub.

This is an important real-world CI/CD concept.

Part 1 — What is a webhook?
------------------------------

* Imagine a developer updates the Empower application and pushes new code to GitHub.
* Without a webhook, Jenkins may need to scan the repository periodically to discover changes.
* With a webhook, GitHub can immediately send a notification to Jenkins when a configured event occurs.
* Webhook = Automatic notification from one application to another when an event happens.

Example:

    - Developer pushes code to GitHub.
    - GitHub sends an HTTP request to Jenkins.
    - Jenkins receives the notification.
    - Jenkins starts the relevant pipeline or triggers repository indexing, depending on the configuration.

Part 2 — How does a GitHub webhook work?:
-----------------------------------------

<img width="972" height="705" alt="image" src="https://github.com/user-attachments/assets/d5db6498-19d4-44e8-8e1a-13a522b8fbf3" />

Step-by-step explanation

    1. A developer commits and pushes code to GitHub.
    2. GitHub detects the configured event, such as a push.
    3. GitHub sends an HTTP POST request to the webhook URL.
    4. Jenkins receives the request through its configured integration.
    5. Jenkins triggers the relevant processing.

Important: A webhook is a notification mechanism. 
It does not itself build your application. Jenkins must have the appropriate job configuration and integration to respond to the event.

Part 3 — Webhook vs Polling:
----------------------------

<img width="886" height="507" alt="image" src="https://github.com/user-attachments/assets/0cc3b6d9-1f65-4d49-9f50-71884809f4e8" />

Part 4 — Configure a GitHub webhook:
------------------------------------

Let's understand the practical configuration.

<img width="2560" height="1671" alt="image" src="https://github.com/user-attachments/assets/ef8edbce-335d-47a3-85b9-07cffb8ceb19" />

Assume your Jenkins server is accessible through:

    https://jenkins.example.com

A common Jenkins webhook endpoint used by the GitHub integration is:

    https://jenkins.example.com/github-webhook/

The trailing slash is important.

Step 1 — Jenkins configuration

For a GitHub Multibranch Pipeline:

    1. Install the GitHub Branch Source plugin.
    2. Open your Multibranch Pipeline configuration.
    3. Configure GitHub as the Branch Source.
    4. Add GitHub credentials if the repository or webhook registration requires them.
    5. Configure branch and PR discovery.
    6. Save the configuration.
The GitHub Branch Source integration can manage webhooks automatically when configured with suitable credentials and permissions. 
Manual webhook setup may be required in some environments.

Step 2 — GitHub configuration

Open your GitHub repository:

    1. Go to Settings.
    2. Select Webhooks.
    3. Click Add webhook.
    4. Enter your Jenkins webhook endpoint.
    5. Set Content type to application/json.
    6. Configure a secret if supported by your integration.
    7. Select the events you need, such as Push events and Pull Request events.
    8. Save the webhook.

<img width="1112" height="812" alt="image" src="https://github.com/user-attachments/assets/61fb7a22-69cf-4951-91e1-3d0e883edc06" />

Step 3 — Test the webhook

    1. Push a new commit to GitHub.
    2. Open the GitHub repository's webhook settings.
    3. Check the Recent Deliveries section.
    4. Verify that GitHub received a successful response from Jenkins.
    5. Check Jenkins for the resulting branch indexing or pipeline activity.

Remember: A successful HTTP delivery means Jenkins received the notification. It does not guarantee that a pipeline build succeeded.

Part 5 — Important networking requirement:
-------------------------------------------

This is especially important when Jenkins is running on an AWS EC2 instance.

GitHub must be able to reach Jenkins.

    GitHub
       |
       | HTTPS : 443
       |
    Internet
       |
       |
    AWS Load Balancer / Reverse Proxy
       |
       |
    Jenkins EC2
       |
       |
    Jenkins webhook endpoint

Check these things:

- Jenkins must have a publicly reachable endpoint or a secure network integration that allows GitHub to reach it.
- Use HTTPS.
- Allow the required inbound traffic through security groups and firewalls.
- Configure the reverse proxy to forward the webhook path correctly.
- Protect webhook endpoints and secrets appropriately.


Important: If Jenkins is accessible only through localhost or a private EC2 IP, GitHub cannot directly reach it over the public internet.


Part 6 — What happens after a webhook?
--------------------------------------

Let's connect everything from Phase 3.

<img width="807" height="510" alt="image" src="https://github.com/user-attachments/assets/c49a2266-7e3c-48f9-be1e-1a15a08f2b4c" />

<img width="866" height="366" alt="image" src="https://github.com/user-attachments/assets/102da949-c32a-4391-b8b9-7bb18b39a172" />

Part 7 — Today's hands-on practical:
-------------------------------------

Phase 3 — Day 5 practical
Use your existing empower-application GitHub repository and Jenkins Multibranch Pipeline.

Task: Configure automatic pipeline triggering.
1. Configure the GitHub Branch Source integration.
2. Configure the webhook between GitHub and Jenkins.
3. Push a new commit to the develop branch.
4. Verify the webhook delivery in GitHub.
5. Check Jenkins branch indexing and build activity.
6. Create a Pull Request and verify the PR-related event handling.
7. Confirm that the correct branch or PR pipeline executes.
Try to perform this using your own Jenkins EC2 environment.


