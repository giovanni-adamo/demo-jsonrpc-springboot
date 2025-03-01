package com.example.demo.service;

/*
import com.googlecode.jsonrpc4j.JsonRpcMethod;
import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;
*/

import java.security.NoSuchAlgorithmException;
import java.util.List;

//@JsonRpcService("/api")
public interface AdviceService {

    /*@JsonRpcMethod(value = "giveMeAdvice")
    List<String> giveMeAdvice(@JsonRpcParam(value = "topic") String topic, @JsonRpcParam(value = "amount") int amount);*/

    List<String> giveMeAdvice(String topic, int amount);

    String calculateSHA256Hash(String data) throws NoSuchAlgorithmException;
}