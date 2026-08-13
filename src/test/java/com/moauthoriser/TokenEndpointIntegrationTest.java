package com.moauthoriser;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestDataConfiguration.class)
class TokenEndpointIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void tokenEndpoint_withValidCredentials_returnsToken() throws Exception {
		mockMvc.perform(post("/oauth2/token")
				.with(httpBasic("test-client", "test-secret"))
				.param("grant_type", "client_credentials"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.access_token").exists())
			.andExpect(jsonPath("$.token_type").value("Bearer"));
	}

	@Test
	void tokenEndpoint_withWrongSecret_returns401() throws Exception {
		mockMvc.perform(post("/oauth2/token")
				.with(httpBasic("test-client", "wrong-secret"))
				.param("grant_type", "client_credentials"))
			.andExpect(status().isUnauthorized())
			.andExpect(jsonPath("$.error").value("invalid_client"));
	}

	@Test
	void tokenEndpoint_withUnknownClient_returns401() throws Exception {
		mockMvc.perform(post("/oauth2/token")
				.with(httpBasic("unknown-client", "secret"))
				.param("grant_type", "client_credentials"))
			.andExpect(status().isUnauthorized())
			.andExpect(jsonPath("$.error").value("invalid_client"));
	}

	@Test
	void tokenEndpoint_withInactiveClient_returns401() throws Exception {
		mockMvc.perform(post("/oauth2/token")
				.with(httpBasic("inactive-client", "test-secret"))
				.param("grant_type", "client_credentials"))
			.andExpect(status().isUnauthorized())
			.andExpect(jsonPath("$.error").value("invalid_client"));
	}

	@Test
	void jwksEndpoint_returnsPublicKeySet() throws Exception {
		mockMvc.perform(get("/oauth2/jwks"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.keys").isArray())
			.andExpect(jsonPath("$.keys[0].kty").value("RSA"))
			.andExpect(jsonPath("$.keys[0].use").value("sig"));
	}

	@Test
	void wellKnownEndpoint_returnsAuthorizationServerMetadata() throws Exception {
		mockMvc.perform(get("/.well-known/oauth-authorization-server"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.issuer").value("http://localhost:9000"))
			.andExpect(jsonPath("$.token_endpoint", containsString("/oauth2/token")))
			.andExpect(jsonPath("$.jwks_uri", containsString("/oauth2/jwks")));
	}

}
