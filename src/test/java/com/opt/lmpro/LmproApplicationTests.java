package com.opt.lmpro;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class LmproApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void contextLoads() {
	}

	@Test
	@DisplayName("returns standardized business error for missing user")
	void returnsStandardizedBusinessError() throws Exception {
		mockMvc.perform(post("/api/accounts/transfer")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "fromUserId": "U9",
								  "toUserId": "U2",
								  "amount": 10
								}
								"""))
				.andExpect(status().isNotFound())
				.andExpect(header().exists("X-Trace-Id"))
				.andExpect(jsonPath("$.code").value(4004))
				.andExpect(jsonPath("$.errorType").value("USER_NOT_FOUND"))
				.andExpect(jsonPath("$.message").value("User not found with ID: U9"))
				.andExpect(jsonPath("$.traceId").isNotEmpty())
				.andExpect(jsonPath("$.details.userId").value("U9"));
	}

	@Test
	@DisplayName("returns standardized validation error for empty payload fields")
	void returnsValidationError() throws Exception {
		mockMvc.perform(post("/api/accounts/transfer")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "fromUserId": "",
								  "toUserId": "U2",
								  "amount": 0
								}
								"""))
				.andExpect(status().isBadRequest())
				.andExpect(header().exists("X-Trace-Id"))
				.andExpect(jsonPath("$.code").value(7003))
				.andExpect(jsonPath("$.errorType").value("VALIDATION_ERROR"))
				.andExpect(jsonPath("$.details.fromUserId").value("fromUserId is required"))
				.andExpect(jsonPath("$.details.amount").value("amount must be greater than 0"));
	}

	@Test
	@DisplayName("returns standardized bad request for invalid transfer rule")
	void returnsBadRequestForSameUserTransfer() throws Exception {
		mockMvc.perform(post("/api/accounts/transfer")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "fromUserId": "U1",
								  "toUserId": "U1",
								  "amount": 10
								}
								"""))
				.andExpect(status().isBadRequest())
				.andExpect(header().exists("X-Trace-Id"))
				.andExpect(jsonPath("$.code").value(7001))
				.andExpect(jsonPath("$.errorType").value("INVALID_TRANSFER_REQUEST"))
				.andExpect(jsonPath("$.details.reason").value("fromUserId and toUserId must be different"));
	}

}
