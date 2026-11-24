Practice playground: https://onecompiler.com/groovy?utm_source=chatgpt.com

Day 1:
======

What is Groovy?

* Groovy is a programming language that runs on the Java Virtual Machine (JVM). It is designed to work closely with Java, but its syntax is generally shorter and more flexible.

Let's write a basic Groovy program:

    println "Welcome to Groovy"
    println "Learning Jenkins Pipeline Coding"

println prints a message and moves to the next line.

<img width="550" height="216" alt="image" src="https://github.com/user-attachments/assets/8588acae-8d7a-43c6-b7d7-15a14232e3e1" />

Day-2:
======

Groovy Variables, Data Types and Strings

<img width="991" height="537" alt="image" src="https://github.com/user-attachments/assets/6b6e07bb-2f10-4667-98c9-fe0a8bc7b966" />

<img width="1010" height="747" alt="image" src="https://github.com/user-attachments/assets/9bde7b98-696d-4b6c-b41c-fcf1f234f947" />

<img width="857" height="445" alt="image" src="https://github.com/user-attachments/assets/30207f50-bc0c-4075-89ef-2dd6a168e66b" />

Remember this Jenkins distinction:

    - Groovy double-quoted strings can interpolate variables.
    - Groovy single-quoted strings don't interpolate variables.
    - Jenkins sh is a pipeline step that executes shell commands.

def appName = 'empower'

Single quotes: println 'Application name is ${appName}' 

Double quotes: println "Application name is ${appName}"

Double-quoted strings support interpolation and produce a GString when interpolation is used.

What is script {}?

* In Declarative Jenkins Pipeline, script {} allows us to write imperative Groovy code inside a stage.

* Remember the distinction:

        - def – declares a Groovy variable.
        - environment – defines environment variables for Jenkins pipeline execution.
        - env.APP_NAME – accesses a Jenkins environment variable from Groovy code.
