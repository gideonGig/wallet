# Use the official OpenJDK image as the base image
FROM openjdk:24-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the jar file of your Java application into the container
COPY /target/exercise-wallet-0.0.2.jar exercise-wallet-0.0.2.jar

# Expose port 8080 for the application
EXPOSE 8090

# Command to run the Java application
CMD ["java", "-jar", "exercise-wallet-0.0.2.jar"]