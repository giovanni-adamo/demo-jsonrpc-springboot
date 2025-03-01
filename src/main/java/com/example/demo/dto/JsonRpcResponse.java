package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JsonRpcResponse {

    @NotNull(message = "jsonrpc must not be null")
    private final String jsonrpc = "2.0";

    @NotNull(message = "id must not be null")
    private int id;

    @NotNull(message = "result must not be null")
    private Result<?> result;

    public JsonRpcResponse(int id, List<String> adviceList) {
        this.id = id;
        this.result = new Result(adviceList);
    }

    @Data
    @AllArgsConstructor
    public static class Result<T> {

        private T adviceList;
    }
}