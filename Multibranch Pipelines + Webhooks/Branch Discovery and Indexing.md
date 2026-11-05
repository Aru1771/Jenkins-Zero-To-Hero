Branch Discovery and Indexing:
==============================

* Today's goal is to understand how Jenkins discovers Git branches, identifies their Jenkinsfiles, and creates or removes branch pipeline jobs.

What is branch discovery?
-------------------------
* Yesterday, we learned that a Multibranch Pipeline can automatically create separate pipeline jobs for eligible Git branches.

But an important question is: How does Jenkins know which branches exist in your Git repository?

* It uses a process called Branch Indexing.

For example, suppose your Git repository contains:

    empower-application/
    │
    ├── main
    ├── develop
    ├── feature/login
    └── feature/payment

* When Jenkins scans this repository, it discovers the available branches and checks which ones contain a valid Jenkinsfile.

* It then creates branch pipeline jobs for eligible branches.

How branch indexing works
--------------------------

Understand this workflow:

<img width="957" height="690" alt="image" src="https://github.com/user-attachments/assets/67e7bc23-2100-4673-acd8-98d94ad50f64" />

What happens during branch indexing?
-------------------------------------

Let's understand the process in more detail.

    Connect to the Git repository: Jenkins uses the configured Branch Source to access your repository.
    
    Discover branches: Jenkins retrieves the branches available in the repository.
    
    Check the Jenkinsfile: Jenkins checks the configured script path (by default, Jenkinsfile in the repository root).
    
    Create or update branch jobs: Eligible branches get their own pipeline jobs.
    
    Remove obsolete jobs: If a branch is deleted, Jenkins can remove its corresponding job according to the configured orphaned-item strategy.

<img width="987" height="423" alt="image" src="https://github.com/user-attachments/assets/9e0897bb-6250-4eb1-98d1-6c2ee4f444b1" />

When does branch indexing happen?
---------------------------------

There are several ways to trigger indexing:

<img width="968" height="701" alt="image" src="https://github.com/user-attachments/assets/cf95a390-2390-4432-a771-ffd071dc40ce" />

Remember the difference:

    - Branch indexing discovers repository changes and updates branch jobs.
    - A pipeline build executes the Jenkinsfile.

They are related, but they are not the same operation.

Practical example
------------------

Imagine your team has this repository:

    empower-application/
    │
    ├── main/
    │   └── Jenkinsfile
    │
    ├── develop/
    │   └── Jenkinsfile
    │
    ├── feature/login/
    │   └── Jenkinsfile
    │
    └── feature/payment/

Assume feature/payment does not contain a Jenkinsfile.

<img width="872" height="696" alt="image" src="https://github.com/user-attachments/assets/81a71cfb-e056-4e26-a61c-afbe398cffc1" />


Today's hands-on lab:
----------------------

Use your existing Jenkins environment.

Task 1: Test branch discovery

      1. Create a Git repository called empower-branch-lab.
      2. Create three branches: main, develop, and feature/login.
      3. Add a Jenkinsfile to main and develop.
      4. Initially, do not add a Jenkinsfile to feature/login.
      5. Configure a Multibranch Pipeline in Jenkins.
      6. Run Scan Multibranch Pipeline Now.
      7. Verify which branch jobs Jenkins creates.
      8. Add a Jenkinsfile to feature/login and scan again.
      9. Verify whether Jenkins discovers the new branch job.
      You should be able to explain why the branch was missing initially and why it appeared after the next scan.
