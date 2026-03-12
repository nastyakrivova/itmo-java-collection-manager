# MusicBand Collection Manager

Console application for managing a collection of MusicBand objects. Developed as Lab 5 for ITMO University.

## Features
- Load/save collection from/to CSV file
- 15 interactive commands (add, update, remove, filter, etc.)
- Data validation and error handling
- Script execution with recursion protection
- Javadoc documentation

## Requirements
- Java 17+
- Maven 3.6+
- Environment variable `DATA` pointing to CSV file

## Quick Start

```bash
# Clone
git clone https://github.com/nastyakrivova/itmo-java-collection-manager.git
cd itmo-java-collection-manager

# Build
mvn clean compile

# Create data.csv
echo "Queen,100,200,5,15,ROCK,EMI,8" > data.csv

# Run (Windows)
set DATA=data.csv
mvn exec:java -Dexec.mainClass="com.myorg.lab5.App"

# Run (Linux/Mac)
export DATA=data.csv
mvn exec:java -Dexec.mainClass="com.myorg.lab5.App"