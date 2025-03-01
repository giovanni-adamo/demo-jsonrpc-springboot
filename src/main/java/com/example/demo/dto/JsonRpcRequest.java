package com.example.demo.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JsonRpcRequest {

    @NotNull(message = "id must not be null")
    private int id;

    @NotNull(message = "jsonrpc must not be null")
    private String jsonrpc;

    @NotNull(message = "method must not be null")
    private String method;

    @Valid
    private Params params;

    @Data
    public static class Params {

        @NotNull(message = "topic must not be null")
        private String topic;

        private Integer amount; // facoltativo
    }
}