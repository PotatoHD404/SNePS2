# sneps-test

## The code is intended to rune through docker container

## Start

```bash
docker-compose up
```

## Building image

1. Create a directory on your local machine and save the Dockerfile in it.
2. Open a terminal and navigate to the directory where the Dockerfile is saved.
3. Run the following command to build the Docker image:

   ```bash
   docker build -t potatohd/sneps2:latest .
   ```

   Replace `<dockerhub_username>` with your Docker Hub username, `<image_name>` with the name you want to give your Docker image, and `<tag>` with the version number (e.g. "latest", "1.0", etc.). The `.` at the end specifies the build context as the current directory.

4. Wait for the build to complete.
5. Run the following command to verify that the Docker image was created:

   ```bash
   docker images
   ```

   You should see your image listed.

6. Login to your Docker Hub account using the following command:

   ```bash
   docker login
   ```

7. Enter your Docker Hub username and password when prompted.
8. Push the Docker image to Docker Hub using the following command:

   ```bash
   docker push potatohd/sneps2:latest
   ```

   This will upload the image to your Docker Hub account.

9. Wait for the upload to complete.

Your Docker image is now available on Docker Hub and can be pulled by other users using the command 

```bash
docker pull potatohd/sneps2:latest
```
