package com.fhlbdm.moauthoriser;

import com.fhlbdm.moauthoriser.persistence.ClientCredentialsEntity;
import com.fhlbdm.moauthoriser.persistence.ClientCredentialsJpaRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@TestConfiguration
@Profile("test")
public class TestDataConfiguration {

	@Bean
	public TestDataInitializer testDataInitializer(ClientCredentialsJpaRepository repository, PasswordEncoder passwordEncoder) {
		return new TestDataInitializer(repository, passwordEncoder);
	}

	public static class TestDataInitializer {

		private final ClientCredentialsJpaRepository repository;
		private final PasswordEncoder passwordEncoder;

		public TestDataInitializer(ClientCredentialsJpaRepository repository, PasswordEncoder passwordEncoder) {
			this.repository = repository;
			this.passwordEncoder = passwordEncoder;
			initializeTestData();
		}

		private void initializeTestData() {
			String encodedSecret = passwordEncoder.encode("test-secret");

			ClientCredentialsEntity activeClient = new ClientCredentialsEntity();
			activeClient.setClientId("test-client");
			activeClient.setMemberId("member-123");
			activeClient.setClientSecret(encodedSecret);
			activeClient.setStatus("ACTIVE");
			activeClient.setCreatedAt(LocalDateTime.now());
			repository.save(activeClient);

			ClientCredentialsEntity inactiveClient = new ClientCredentialsEntity();
			inactiveClient.setClientId("inactive-client");
			inactiveClient.setMemberId("member-456");
			inactiveClient.setClientSecret(encodedSecret);
			inactiveClient.setStatus("INACTIVE");
			inactiveClient.setCreatedAt(LocalDateTime.now());
			repository.save(inactiveClient);
		}

	}

}
