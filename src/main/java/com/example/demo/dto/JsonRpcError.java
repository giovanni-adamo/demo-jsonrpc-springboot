package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JsonRpcError {

    @NotNull(message = "jsonrpc must not be null")
    private final String jsonrpc = "2.0";

    @NotNull(message = "id must not be null")
    private int id;

    /**
     * Codice numerico d'errore.
     * Esempi tipici nello standard JSON-RPC:
     * -32600 -> Invalid Request
     * -32601 -> Method not found
     * -32602 -> Invalid params
     * -32603 -> Internal error
     * -32000 to -32099 -> Errori specifici definiti dal server
     */
    @NotNull(message = "code must not be null")
    private int code;

    /**
     * Breve messaggio di errore.
     */
    @NotNull(message = "message must not be null")
    private String message;

    public JsonRpcError(int id, int code, String message) {
        this.id = id;
        this.code = code;
        this.message = message;
    }
}