# Accelerate server for Zhihu Daily Purify
This directory contains the source code for the Zhihu Daily Purify accelerate server hosted on Heroku.  

When a request hits the server, it will fetch the content using Zhihu Daily's API.  
It will filter out the content that does not contain a valid Zhihu question, then store the news in a mongodb instance from [mlab](mlab.com).

## First thing first.
`cp database.ini.example database.ini`  

Get a mongodb instance from [mlab](mlab.com), or install it locally. Fill in the necessary information.
Or, otherwise, bazel will refuse to build the code.

## Build docker image without using docker?
Yes, you can.
In the `BUILD` file, by using the `py_image` rule and the `container_image` rule, you can build a __hermit__ docker image using bazel without even need to invoke docker to do the building.

Though, one caveat might be that the `CMD` instruction has to be present in the `Dockerfile` in order to deploy this image to heroku.
Which means, at the end of the `container_image` rule, you have to add and empty string as the sole element to the `cmd` attribute.

## Build the docker image
`bazel build //server:zhihu_daily_purify_server_docker`

## Deploy to Heroku
`bazel run //server:zhihu_daily_purify_server_docker`  

`docker tag bazel/server:zhihu_daily_purify_server_docker registry.heroku.com/<APP_NAME>/<WORKER_TYPE>`
