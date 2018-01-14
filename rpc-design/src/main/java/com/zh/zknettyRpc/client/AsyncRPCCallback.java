package com.zh.zknettyRpc.client;

public interface AsyncRPCCallback {

    void success(Object result);

    void fail(Exception e);

}
