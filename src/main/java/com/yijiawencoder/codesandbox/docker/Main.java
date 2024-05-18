package com.yijiawencoder.codesandbox.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.PingCmd;
import com.github.dockerjava.core.DockerClientBuilder;

public class Main {
    public static void main(String[] args) {

        DockerClient dockerClient = DockerClientBuilder.getInstance().build();
        // Do something with the Docker client
        PingCmd pingCmd = dockerClient.pingCmd();

    }
}
