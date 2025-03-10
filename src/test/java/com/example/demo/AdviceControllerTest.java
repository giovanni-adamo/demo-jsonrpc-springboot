package com.example.demo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.lessThanOrEqualTo;


@SpringBootTest
@AutoConfigureMockMvc
public class AdviceControllerTest {

	private final String API = "/advice";

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testGiveMeAdviceWithAmount() throws Exception {

		// Payload with topic and amount params
		String requestJson = """
				{
				  "id":1,
				  "jsonrpc":"2.0",
				  "method":"GiveMeAdvice",
				  "params":{
				    "topic":"love",
				    "amount":2
				  }
				}
				""";

		// Execute test and check response
		mockMvc.perform(post(API)
						.contentType(MediaType.APPLICATION_JSON)
						.characterEncoding("UTF-8")
						.content(requestJson))
				.andExpect(status().isOk());
	}

	@Test
	public void testGiveMeAdviceWithoutAmount() throws Exception {

		// Payload without amount param
		String requestJson = """
				{
				  "id":1,
				  "jsonrpc":"2.0",
				  "method":"GiveMeAdvice",
				  "params":{
				    "topic":"cars"
				  }
				}
				""";

		// Execute test and check response
		mockMvc.perform(post(API)
						.contentType(MediaType.APPLICATION_JSON)
						.characterEncoding("UTF-8")
						.content(requestJson))
				.andExpect(status().isOk());
	}

	@Test
	public void testWithOtherRpcMethod() throws Exception {

		// Payload with RPC method that not exists
		String requestJson = """
				{
				  "id":1,
				  "jsonrpc":"2.0",
				  "method":"OtherRpcMethod",
				  "params":{
				    "topic":"cars"
				  }
				}
				""";

		// Execute test and check response
		mockMvc.perform(post(API)
						.contentType(MediaType.APPLICATION_JSON)
						.characterEncoding("UTF-8")
						.content(requestJson))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.error").exists());
	}

	@Test
	public void testGiveMeAdviceWithAmountLessThanOrEqualToResponse() throws Exception {

		// Payload with topic and amount params
		String requestJson = """
            {
              "id":1,
              "jsonrpc":"2.0",
              "method":"GiveMeAdvice",
              "params":{
                "topic":"love",
                "amount":3
              }
            }
            """;

		// Extract amount value
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode requestNode = objectMapper.readTree(requestJson);
		int amount = requestNode.path("params").path("amount").asInt();

		// Execute test and check response
		mockMvc.perform(post(API)
						.contentType(MediaType.APPLICATION_JSON)
						.characterEncoding("UTF-8")
						.content(requestJson))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.result.length()").value(lessThanOrEqualTo(amount)));
	}
}