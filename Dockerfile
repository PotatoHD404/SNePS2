# Start from an official base image
FROM ubuntu:20.04

# Set environment variables
ENV DEBIAN_FRONTEND=noninteractive \
    JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64

# Install Common Lisp (SBCL), Java 11, and other necessary tools
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
        build-essential \
        curl \
#     Install Common Lisp (SBCL) here
        sbcl \
        openjdk-11-jdk-headless \
        rlwrap \
    && rm -rf /var/lib/apt/lists/*

# Optional: Install Quicklisp for easier Common Lisp package management
#RUN curl -o /tmp/quicklisp.lisp https://beta.quicklisp.org/quicklisp.lisp && \
#    sbcl --load /tmp/quicklisp.lisp \
#         --eval '(quicklisp-quickstart:install)' \
#         --eval '(ql:add-to-init-file)' \
#         --eval '(quit)' && \
#    rm /tmp/quicklisp.lisp

# Set working directory
WORKDIR /app

# Copy your project files into the container (optional)
COPY "./Sneps-2.7.0" /app/sneps

COPY /out/artifacts/internal_server_jar/internal-server.jar /app/internal-server.jar

COPY "/out/artifacts/jung_jar/jung-1.7.6.jar" "/app/sneps/SnepsGUI/SnepsGUIMods/JungFiles/JUNG/jung-1.7.6/jung-1.7.6.jar"

COPY /out/artifacts/SNePSGUIShow_jar/SNePSGUIShow.jar /app/sneps/SnepsGUI/SNePSGUIShow.jar

# Expose any necessary ports (optional)
EXPOSE 7000

# Set the command to run when starting the container
CMD ["java", "-jar", "internal-server.jar"]