# Start from an official base image
FROM mcr.microsoft.com/windows/nanoserver:ltsc2022

# Set environment variables
ENV JAVA_HOME="C:\\openjdk-11.0.11_9"

# Install Common Lisp (SBCL), Java 11, and other necessary tools
RUN apt-get update

RUN apt-get install -y --no-install-recommends \
        build-essential \
        curl \
        wget \
        libncurses-dev \
        libc6-dev \
        git \
        rlwrap

RUN apt-get install -y --no-install-recommends \
    software-properties-common \
    && add-apt-repository -y ppa:openjdk-r/ppa \
    && apt-get update \
    && apt-get install -y --no-install-recommends \
    openjdk-11-jdk \
    xorg \
    libxaw7 \
    libxrender1 \
    libxtst6 \
    libxi6

RUN apt-get install -y --no-install-recommends \
    x11vnc \
    xvfb

RUN rm -rf /var/lib/apt/lists/*

#RUN rm /run/reboot-required*
#RUN echo "/usr/sbin/lightdm" > /etc/X11/default-display-manager
#RUN echo "\
#[LightDM]\n\
#[Seat:*]\n\
#type=xremote\n\
#xserver-hostname=host.docker.internal\n\
#xserver-display-number=0\n\
#autologin-user=root\n\
#autologin-user-timeout=0\n\
#" > /etc/lightdm/lightdm.conf.d/lightdm.conf


# Set environment variables
ENV ALLEGRO_CL_VERSION 10.1
ENV ALLEGRO_CL_FILE acl${ALLEGRO_CL_VERSION}express-linux-x64.tbz2
ENV ALLEGRO_CL_URL https://franz.com/ftp/pub/acl${ALLEGRO_CL_VERSION}express/linuxamd64.64/${ALLEGRO_CL_FILE}

# Download and install Allegro Common Lisp Express
RUN wget --no-check-certificate ${ALLEGRO_CL_URL}
RUN tar jxf ${ALLEGRO_CL_FILE} -C /usr/local && \
        rm ${ALLEGRO_CL_FILE} && \
        ln -s /usr/local/acl${ALLEGRO_CL_VERSION}express.64/alisp /usr/local/bin/alisp

RUN echo '#!/bin/bash\n\
\n\
# Start the X11 server\n\
Xvfb :1 -screen 0 1024x768x16 &\n\
\n\
# Start the VNC server\n\
x11vnc -display :1 -nopw -listen localhost -xkb -ncache 10 -ncache_cr -forever &\n\
\n\
# Set the DISPLAY environment variable\n\
export DISPLAY=:1\n\
\n\
# Start the Java application\n\
java -jar /path/to/your/java/application.jar\n'\
> /usr/local/bin/start-vnc.sh && chmod +x /usr/local/bin/start-vnc.sh

# Optional: Install Quicklisp for easier Common Lisp package management
#RUN curl -o /tmp/quicklisp.lisp https://beta.quicklisp.org/quicklisp.lisp && \
#    sbcl --load /tmp/quicklisp.lisp \
#         --eval '(quicklisp-quickstart:install)' \
#         --eval '(ql:add-to-init-file)' \
#         --eval '(quit)' && \
#    rm /tmp/quicklisp.lisp
ENV DISPLAY=:1

# Set PATH to include Java and SBCL
RUN setx PATH "%PATH%;%JAVA_HOME%\\bin;C:\\Program Files\\Steel Bank Common Lisp\\1.5.6\\sbcl.exe"

# clone https://github.com/SNePS/SNePS2.git into /app/sneps

RUN git clone https://github.com/SNePS/SNePS2.git /app/sneps

# Copy your project files into the container (optional)
#COPY "./Sneps-2.7.0" "/app/sneps"

COPY "/out/artifacts/internal_server_jar/internal-server.jar" "/app/internal-server.jar"

# Set working directory
WORKDIR /app

COPY "/out/artifacts/new_jlinker_jar/jlinker.jar" "/usr/local/acl10.1express.64/jlinker/jlinker.jar"

#COPY "/out/artifacts/jlinker_jar/jlinker.jar" "/app/sneps/Jlinker/jlinker.jar"

COPY "/out/artifacts/SNePSGUIShow_jar/SNePSGUIShow.jar" "/app/sneps/SnepsGUI/SnepsGUIShow.jar"

COPY "/Sneps-2.7.0/load-sneps.lisp" "/app/sneps/load-sneps.lisp"

COPY "/Sneps-2.7.0/sneps_config.lisp" "/app/sneps/sneps_config.lisp"

COPY "/Sneps-2.7.0/snepslog-helper.lisp" "/app/sneps/snepslog-helper.lisp"

COPY "/Sneps-2.7.0/Jlinker/jl-config.cl" "/app/sneps/Jlinker/jl-config.cl"

COPY "/Sneps-2.7.0/sneps/fns/dd.lisp" "/app/sneps/sneps/fns/dd.lisp"

# Expose any necessary ports (optional)
EXPOSE 7000

EXPOSE 5900

# Set the command to run when starting the container
CMD /usr/local/bin/start-vnc.sh; java -jar internal-server.jar