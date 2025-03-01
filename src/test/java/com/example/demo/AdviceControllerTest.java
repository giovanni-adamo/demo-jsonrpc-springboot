package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class AdviceControllerTest {

	private final String API = "/giveMeAdvice";

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testGiveMeAdvice() throws Exception {

		// Simula il payload della richiesta JSON-RPC
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

		// Esegui la richiesta e verifica lo stato della risposta
		mockMvc.perform(post(API)
						.contentType(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.characterEncoding("UTF-8")
						.content(requestJson))
				.andExpect(status().isOk());

		/*assertThat(mvc.post().uri(API)
				.contentType(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.characterEncoding("UTF-8")
				.content(requestJson))
				.hasStatusOk();
		 */
	}

	@Test
	public void testGiveMeAdviceWithAmount() throws Exception {

		// Simula il payload della richiesta JSON-RPC
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

		// Esegui la richiesta e verifica lo stato della risposta
		mockMvc.perform(post(API)
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson)
						.characterEncoding("UTF-8")
						.content(requestJson))
				.andExpect(status().isOk());
	}
}