package org.rangiffler.api;

import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.qameta.allure.grpc.AllureGrpc;
import org.rangiffler.config.Config;

public abstract class GrpcClient {

    protected static final Config CFG = Config.getConfig();
    private final String serviceUrl;
    private final int servicePort;

    protected final Channel CHANNEL;

    public GrpcClient(String serviceUrl, int servicePort) {
        this.serviceUrl = serviceUrl;
        this.servicePort = servicePort;
        CHANNEL = ManagedChannelBuilder
                .forAddress(this.serviceUrl, this.servicePort)
                .intercept(new AllureGrpc())
                .usePlaintext()
                .build();
    }

}
