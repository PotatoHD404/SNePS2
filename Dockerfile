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
        wget \
        libncurses-dev \
        libc6-dev \
        git \
#     Install Common Lisp (SBCL) here
        openjdk-11-jdk-headless \
        rlwrap \
    && rm -rf /var/lib/apt/lists/*

# Set environment variables
ENV ALLEGRO_CL_VERSION 10.1
ENV ALLEGRO_CL_FILE acl${ALLEGRO_CL_VERSION}express-linux-x64.tbz2
ENV ALLEGRO_CL_URL https://franz.com/ftp/pub/acl${ALLEGRO_CL_VERSION}express/linuxamd64.64/${ALLEGRO_CL_FILE}

# Download and install Allegro Common Lisp Express
RUN wget --no-check-certificate ${ALLEGRO_CL_URL}
RUN tar jxf ${ALLEGRO_CL_FILE} -C /usr/local && \
        rm ${ALLEGRO_CL_FILE} && \
        ln -s /usr/local/acl${ALLEGRO_CL_VERSION}express.64/alisp /usr/local/bin/alisp


# Optional: Install Quicklisp for easier Common Lisp package management
#RUN curl -o /tmp/quicklisp.lisp https://beta.quicklisp.org/quicklisp.lisp && \
#    sbcl --load /tmp/quicklisp.lisp \
#         --eval '(quicklisp-quickstart:install)' \
#         --eval '(ql:add-to-init-file)' \
#         --eval '(quit)' && \
#    rm /tmp/quicklisp.lisp

# Set working directory
WORKDIR /app

# clone https://github.com/SNePS/SNePS2.git into /app/sneps

RUN git clone https://github.com/SNePS/SNePS2.git /app/sneps

# Copy your project files into the container (optional)
#COPY "./Sneps-2.7.0" "/app/sneps"

COPY "/out/artifacts/internal_server_jar/internal-server.jar" "/app/internal-server.jar"

COPY "/out/artifacts/jung_jar/jung-1.7.6.jar" "/app/sneps/SnepsGUI/SnepsGUIMods/JungFiles/JUNG/jung-1.7.6/jung-1.7.6.jar"

COPY "/out/artifacts/SNePSGUIShow_jar/SNePSGUIShow.jar" "/app/sneps/SnepsGUI/SNePSGUIShow.jar"

COPY "/Sneps-2.7.0/load-sneps.lisp" "/app/sneps/load-sneps.lisp"

COPY "/Sneps-2.7.0/sneps_config.lisp" "/app/sneps/sneps_config.lisp"

COPY "/Sneps-2.7.0/snepslog-helper.lisp" "/app/sneps/snepslog-helper.lisp"

COPY "/Sneps-2.7.0/Jlinker/jl-config.cl" "/app/sneps/Jlinker/jl-config.cl"

# Expose any necessary ports (optional)
EXPOSE 7000

# Set the command to run when starting the container
CMD ["java", "-jar", "internal-server.jar"]