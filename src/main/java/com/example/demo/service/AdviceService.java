package com.example.demo.service;

import com.googlecode.jsonrpc4j.JsonRpcError;
import com.googlecode.jsonrpc4j.JsonRpcErrors;
import com.googlecode.jsonrpc4j.JsonRpcMethod;
import com.googlecode.jsonrpc4j.JsonRpcParam;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface AdviceService {

    @JsonRpcMethod(value = "GiveMeAdvice")
    @JsonRpcErrors({
            @JsonRpcError(
                    exception = Throwable.class,
                    code = -187)
    })
    List<String> giveMeAdvice(@JsonRpcParam(value = "topic") String topic, @JsonRpcParam(value = "amount") Integer amount);

    String calculateSHA256Hash(String data) throws NoSuchAlgorithmException;

    String callRpcMethod(String jsonRpcRequest) throws Exception;
}