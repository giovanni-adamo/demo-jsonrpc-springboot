package com.example.demo.web;

import com.example.demo.dto.JsonRpcError;
import com.example.demo.dto.JsonRpcRequest;
import com.example.demo.dto.JsonRpcResponse;
import com.example.demo.service.AdviceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AdviceController {

    @Autowired
    private AdviceService adviceService;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping(
            value ="giveMeAdvice",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "L'API fornisce un elenco di frasi riguardanti un determinato argomento")
    @ApiResponses(value = {
            @ApiResponse(
                    description = "Successful Operation",
                    responseCode = "200",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = JsonRpcResponse.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request",
                    content = @Content(
                            schema = @Schema(implementation = JsonRpcError.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found",
                    content = @Content(
                            schema = @Schema(implementation = JsonRpcError.class)))})
    public ResponseEntity<?> giveMeAdvice(@Valid @RequestBody JsonRpcRequest request) {

        try {

            String topic = request.getParams().getTopic();
            Integer amount = request.getParams().getAmount() != null ? Integer.valueOf(request.getParams().getAmount()) : 1;

            List<String> adviceList = adviceService.giveMeAdvice(topic, amount);

            // Converti l'oggetto di risposta in JSON
            JsonRpcResponse jsonRpcResponse = new JsonRpcResponse(request.getId(), adviceList);
            String jsonResponse = objectMapper.writeValueAsString(jsonRpcResponse);

            String validationHash = adviceService.calculateSHA256Hash(jsonResponse);

            // Add validationHash header to response
            HttpHeaders headers = new HttpHeaders();
            headers.add("validationHash", validationHash);

            // Return response
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(jsonRpcResponse);

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            new JsonRpcError(
                                    request.getId(),
                                    -32600,
                                    "Invalid Request"
                            )
                    );
        }
    }
}
