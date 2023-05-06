# Start from an official base image
FROM mcr.microsoft.com/windows/nanoserver:ltsc2022

# Set environment variables
ENV JAVA_HOME="C:\\openjdk-11.0.11_9"

# Install Chocolatey package manager
RUN powershell -Command \
        iex ((New-Object System.Net.WebClient).DownloadString('https://chocolatey.org/install.ps1'))

# Install OpenJDK 11 and SBCL
RUN powershell -Command \
        choco install openjdk11 -y --version 11.0.11 && \
        choco install sbcl -y

# Set PATH to include Java and SBCL
RUN setx PATH "%PATH%;%JAVA_HOME%\\bin;C:\\Program Files\\Steel Bank Common Lisp\\1.5.6\\sbcl.exe"

# Create directories for the application and copy files
RUN powershell -Command \
        New-Item -ItemType Directory -Path C:\\app\\sneps; \
        New-Item -ItemType Directory -Path C:\\app\\sneps\\SnepsGUI\\SnepsGUIMods\\JungFiles\\JUNG\\jung-1.7.6

COPY "./Sneps-2.7.0" "C:\\app\\sneps"
COPY "/out/artifacts/internal_server_jar/internal-server.jar" "C:\\app\\internal-server.jar"
COPY "/out/artifacts/jung_jar/jung-1.7.6.jar" "C:\\app\\sneps\\SnepsGUI\\SnepsGUIMods\\JungFiles\\JUNG\\jung-1.7.6\\jung-1.7.6.jar"
COPY "/out/artifacts/SNePSGUIShow_jar/SNePSGUIShow.jar" "C:\\app\\sneps\\SnepsGUI\\SNePSGUIShow.jar"

# Set working directory
WORKDIR /app

# Expose any necessary ports (optional)
EXPOSE 7000

# Set the command to run when starting the container
CMD ["java", "-jar", "internal-server.jar"]