#!/bin/bash

# Fail the script if any command fails
set -e

# Run Maven tests
echo "Running Maven tests..."
mvn clean test
