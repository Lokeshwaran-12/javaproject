#!/bin/bash

# Script to test and run the Java Sentiment Analysis application

echo "===== Testing Java Sentiment Analysis Application ====="

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "Maven is not installed. Installing Maven..."
    sudo apt-get update
    sudo apt-get install -y maven
fi

# Check if MySQL is installed
if ! command -v mysql &> /dev/null; then
    echo "MySQL is not installed. Installing MySQL..."
    sudo apt-get update
    sudo apt-get install -y mysql-server
    
    # Start MySQL service
    sudo service mysql start
    
    # Create database and user for the application
    echo "Setting up MySQL database..."
    sudo mysql -e "CREATE DATABASE IF NOT EXISTS sentiment_feedback;"
    sudo mysql -e "CREATE USER IF NOT EXISTS 'sentiment_user'@'localhost' IDENTIFIED BY 'password';"
    sudo mysql -e "GRANT ALL PRIVILEGES ON sentiment_feedback.* TO 'sentiment_user'@'localhost';"
    sudo mysql -e "FLUSH PRIVILEGES;"
fi

# Navigate to project directory
cd /home/ubuntu/sentiment_analysis_java

# Update database connection settings if needed
echo "Checking database connection settings..."
sed -i 's/jdbc:mysql:\/\/localhost:3306\/sentiment_feedback/jdbc:mysql:\/\/localhost:3306\/sentiment_feedback/' src/main/java/com/sentiment/util/DatabaseUtil.java
sed -i 's/root/sentiment_user/' src/main/java/com/sentiment/util/DatabaseUtil.java

# Build the project
echo "Building the project with Maven..."
mvn clean package

# Check if build was successful
if [ $? -eq 0 ]; then
    echo "Build successful!"
    echo "To run the application, use: java -jar target/sentiment-analysis-1.0-SNAPSHOT.jar"
else
    echo "Build failed. Please check the error messages above."
fi

echo "===== Testing Complete ====="
