package com.example.demo.web;

import com.example.demo.dto.JsonRpcError;
import com.example.demo.dto.JsonRpcRequestDto;
import com.example.demo.dto.JsonRpcResponseDto;
import com.example.demo.service.AdviceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class AdviceController {

    @Autowired
    private AdviceService adviceService;

    @Autowired
    private ObjectMapper objectMapper;

    private final Validator validator; // Iniettato da Spring se vuoi

    @Autowired
    public AdviceController(Validator validator) {
        this.validator = validator;
    }

    @PostMapping(
            value = "advice",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "L'API fornisce un elenco di frasi riguardanti un determinato argomento")
    @ApiResponses(value = {
            @ApiResponse(
                    description = "Successful Operation",
                    responseCode = "200",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = JsonRpcResponseDto.class))),
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
    public ResponseEntity<?> giveMeAdvice(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Esempio di richiesta per la chiamata al metodo RPC GiveMeAdvice",
                    required = true,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = JsonRpcRequestDto.class),
                            examples = @ExampleObject(value = "{\n" +
                                    "   \"id\":1,\n" +
                                    "   \"jsonrpc\":\"2.0\",\n" +
                                    "   \"method\":\"GiveMeAdvice\",\n" +
                                    "   \"params\":{\n" +
                                    "      \"topic\":\"love\",\n" +
                                    "      \"amount\":2\n" +
                                    "   }\n" +
                                    "}\n")))
            @RequestBody String jsonRequest) {

        JsonRpcRequestDto jsonRpcRequestDto = null;

        try {

            // Convert JSON string to DTO and validate attributes
            jsonRpcRequestDto = convertRequestToDto(jsonRequest, true);
            String jsonRpcRequest = objectMapper.writeValueAsString(jsonRpcRequestDto);

            // Call RPC method
            String jsonResponse = adviceService.callRpcMethod(jsonRpcRequest);

            // Rebuild JSON response
            String jsonResponseRebuilded = rebuildJsonResponse(jsonResponse);

            // Calculate Hash by response
            String validationHash = adviceService.calculateSHA256Hash(jsonResponseRebuilded);

            // Add validationHash header to response
            HttpHeaders headers = new HttpHeaders();
            headers.add("validationHash", validationHash);

            // Return response
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(jsonResponseRebuilded);

        } catch (Exception e) {

            // Return error response
            return buildResponseError(jsonRpcRequestDto, jsonRequest, e);
        }
    }

    /**
     * Converte un JSON di input nel corrispondente DTO e lo valida
     *
     * @param jsonRequest
     * @return
     * @throws JsonProcessingException
     */
    private JsonRpcRequestDto convertRequestToDto(String jsonRequest, boolean validate) throws Exception {

        // JSON String to DTO
        JsonRpcRequestDto jsonRpcRequestDto = objectMapper.readValue(jsonRequest, JsonRpcRequestDto.class);

        // DTO validation
        if (validate) {

            Set<ConstraintViolation<JsonRpcRequestDto>> violations = validator.validate(jsonRpcRequestDto);
            if (!violations.isEmpty()) {

                String violationMessages = violations.stream().map(v -> v.getMessage()).collect(Collectors.joining(","));
                String message = String.format("%s", violationMessages);

                throw new RuntimeException(message);
            }
        }

        return jsonRpcRequestDto;
    }

    /**
     * Modifica il JSON di risposta sostituendo l'elemento 'result' con 'adviceList'
     *
     * @param jsonResponse
     * @return
     */
    private String rebuildJsonResponse(String jsonResponse) {

        String jsonResponseRebuilded;

        try {

            JsonNode rootNode = objectMapper.readTree(jsonResponse);

            // Trasforma il campo 'result' da array -> {"adviceList": [...]}
            JsonNode resultNode = rootNode.get("result");
            if (resultNode != null && resultNode.isArray()) {

                // Creiamo un oggetto con adviceList
                ObjectNode adviceListObj = objectMapper.createObjectNode();
                adviceListObj.set("adviceList", resultNode);

                // Sostituzione di 'adviceList' con 'result'
                ((ObjectNode) rootNode).set("result", adviceListObj);
            }

            jsonResponseRebuilded = objectMapper.writeValueAsString(rootNode);

        } catch (Exception e) {
            throw new RuntimeException("Errore nella costruzione del JSON di risposta");
        }

        return jsonResponseRebuilded;
    }

    /**
     * Costruisce l'oggetto di risposta che definisce l'errore
     *
     * @param jsonRpcRequestDto
     * @param jsonRequest
     * @param throwable
     * @return
     */
    private ResponseEntity<?> buildResponseError(JsonRpcRequestDto jsonRpcRequestDto, String jsonRequest, Throwable throwable) {

        Integer jsonRpcId = -1;
        String errorMessage = "Invalid Request";

        try {

            if (jsonRpcRequestDto == null && jsonRequest != null) {

                JsonNode rootNode = objectMapper.readTree(jsonRequest);
                if (rootNode != null && rootNode.has("id")) {
                    jsonRpcId = rootNode.get("id").asInt();
                }
            }

            if (throwable != null && throwable.getMessage() != null) {
                errorMessage = String.format("Invalid Request: %s", throwable.getMessage());
            }
        } catch (Exception e) {
            jsonRpcId = -1;
        }

        return ResponseEntity
                .badRequest()
                .body(
                        new JsonRpcError(
                                jsonRpcId,
                                -32600,
                                errorMessage
                        )
                );
    }
}
