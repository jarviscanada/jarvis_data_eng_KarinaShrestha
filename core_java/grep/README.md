# Introduction
The Java Grep application simulates the functionality of the Linux/Unix ```grep``` command-line tool 
which allows users to search for matching strings within a file, given three arguments: a text 
pattern, the directory path, and the output file. This project utilizes Lambda & Stream APIs and 
is developed in Java using IntelliJ. The technologies used to manage and package the grep application 
are Maven and Docker.
# Quick Start
The grep app can be launched by using one of the following two approaches:
1. Using the _.jar_ file
   ```
   # Compile and package the Java code
   mvn clean compile package
   
   # Launch a JVM and run the app (include three arguments)
   java -cp target/grep-1.0-SNAPSHOT.jar ca.jrvs.apps.grep.JavaGrepImp {regex} {rootPath} {outFile}
    ```
2. Using Docker
    ```
    # Pull the existing docker image from DockerHub
   docker pull localhostksh/grep
   
   # Run the Docker container
   docker run localhostksh/grep {regex} {rootPath} {outFile}
    ```

# Implementation
## Pseudocode
The ```process``` method is a high-level description of the implementation for the grep app.
The pseudocode is shown below:
```
matchedLines = []
for file in listFilesRecursively(rootDir)
  for line in readLines(file)
      if containsPattern(line)
        matchedLines.add(line)
writeToFile(matchedLines)
```

## Performance Issue
During execution, the app loads all the source data from the directory to the memory and recursively searches 
for the regex pattern line by line within all files in the directory. In such cases where the file is too large, 
there will be an ```OutOfMemoryError``` exception due to not enough heap memory being allocated to process
the data. To counter this problem, Lambda & Stream APIs were implemented in the place of lists, allowing the
grep app to process extensive data with a small amount of heap memory.

# Test
The app was tested manually using sample text files that consisted of varying text patterns and comparing the 
results with the results obtained from using the Linux ```grep``` command on the same text file and pattern.

# Deployment
For easier distribution, the app was deployed by creating a Docker image of the application and pushing the DockerFile
onto DockerHub.

# Improvement
* Add options to further mimic the ```grep``` command (e.g. print the line number with the output lines,
 display the count for lines that match the regex pattern, print only a count of selected lines per file)
* Display the memory usage each time the app is executed
* Search within both the directory specified and its subdirectories for the matching regex pattern