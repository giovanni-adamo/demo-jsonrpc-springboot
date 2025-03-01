package com.example.demo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Service
//@AutoJsonRpcServiceImpl
public class AdviceServiceImpl implements AdviceService {

    private final String API_URL = "https://api.adviceslip.com/advice/search/";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    @Cacheable(value = "adviceCache", key = "#topic", unless = "#result.size() == 0")
    public List<String> giveMeAdvice(String topic, int amount) {

        List<String> adviceList = fetchAdvice(topic, amount);
        return adviceList;
    }

    public String calculateSHA256Hash(String data) throws NoSuchAlgorithmException {

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));

        // Converti l'array di byte in una stringa esadecimale
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private List<String> fetchAdvice(String topic, int amount) {

        try {

            List<String> adviceList = new ArrayList<>();
            ResponseEntity<String> response = restTemplate.getForEntity(API_URL + topic, String.class);
            JsonNode jsonNode = objectMapper.readTree(response.getBody());

            int elem = jsonNode.path("slips").size();
            if (elem >= amount) {
                for (int i = 0; i < amount; i++) {
                    String advice = jsonNode.path("slips").get(i).path("advice").asText();
                    if (advice != null) {
                        adviceList.add(advice);
                    }
                }
            }

            return adviceList;

        } catch (Exception e) {
            return null;
        }
    }
}