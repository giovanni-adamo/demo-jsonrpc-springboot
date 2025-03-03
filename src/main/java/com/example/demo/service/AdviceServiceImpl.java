package com.example.demo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.jsonrpc4j.JsonRpcServer;
import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@Service
@AutoJsonRpcServiceImpl
public class AdviceServiceImpl implements AdviceService {

    @Value("${demo.advice-api-endpoint}")
    private String adviceApiEndpoint;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    @Cacheable(value = "adviceCache", key = "#topic", unless = "#result.size() == 0")
    public List<String> giveMeAdvice(String topic, Integer amount) {

        List<String> adviceList = null;

        try {

            if (amount == null) {
                amount = 1; // valore di default
            }

            if (amount != null && amount <= 0) {
                amount = 0;
            }

            adviceList = new ArrayList<>();

            // Call API
            ResponseEntity<String> response = restTemplate.getForEntity(adviceApiEndpoint + topic, String.class);

            // Get API result
            JsonNode jsonNode = objectMapper.readTree(response.getBody());

            // Read API result and build advice list
            int elem = jsonNode.path("slips").size();
            if (elem > 0) {

                int limit = Math.min(elem, amount);

                IntStream.range(0, limit)
                        .mapToObj(i -> jsonNode.path("slips").get(i).path("advice").asText())
                        .filter(advice -> advice != null)
                        .forEach(adviceList::add);
            }

            return adviceList;

        } catch (Exception e) {

            log.error("giveMeAdvice error", e);
            return adviceList;
        }
    }

    @Override
    public String callRpcMethod(String jsonRpcRequest) throws Exception {

        ByteArrayInputStream inputStream = new ByteArrayInputStream(jsonRpcRequest.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        JsonRpcServer jsonRpcServer = new JsonRpcServer(
                objectMapper,        // Jackson mapper
                this,         // oggetto con i metodi
                AdviceService.class  // interfaccia/metodo su cui fare reflection
        );

        // Invoke RPC method
        jsonRpcServer.handleRequest(inputStream, outputStream);

        // Get response by RPC method
        String jsonRpcResponse = outputStream.toString(StandardCharsets.UTF_8);

        return jsonRpcResponse;
    }

    @Override
    public String calculateSHA256Hash(String data) throws NoSuchAlgorithmException {

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));

        // Convert byte array to hex string
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}