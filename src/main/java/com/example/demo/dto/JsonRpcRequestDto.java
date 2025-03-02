package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JsonRpcRequestDto {

    @NotNull(message = "id must not be null")
    private int id;

    @NotNull(message = "jsonrpc must not be null")
    @Pattern(regexp = "2.0", message = "jsonrpc must be '2.0'")
    private String jsonrpc;

    @NotNull(message = "method must not be null")
    @Schema(description = "Nome del metodo RPC da chiamare")
    private String method;

    @Valid
    private Params params;

    @Data
    public static class Params {

        @NotNull(message = "topic must not be null")
        @Schema(description = "Stringa contenente l'argomento")
        private String topic;

        @Schema(description = "Numero intero facoltativo che indica la quantità massima di frasi da restituire. Il default è 1")
        private Integer amount = 1; // facoltativo
    }
}