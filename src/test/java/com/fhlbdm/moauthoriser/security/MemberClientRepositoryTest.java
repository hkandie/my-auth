package com.fhlbdm.moauthoriser.security;

import com.fhlbdm.moauthoriser.persistence.ClientCredentialsEntity;
import com.fhlbdm.moauthoriser.persistence.ClientCredentialsJpaRepository;
import com.fhlbdm.moauthoriser.security.MemberClientRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberClientRepositoryTest {

	@Mock
	private ClientCredentialsJpaRepository jpaRepository;

	@InjectMocks
	private MemberClientRepository repository;

	@Test
	void findByClientId_withActiveClient_returnsRegisteredClient() {
		// Arrange
		ClientCredentialsEntity entity = new ClientCredentialsEntity();
		entity.setClientId("test-client");
		entity.setMemberId("member-123");
		entity.setClientSecret("$2a$10$abc123");
		entity.setStatus("ACTIVE");
		entity.setCreatedAt(LocalDateTime.now());

		when(jpaRepository.findByClientId("test-client")).thenReturn(Optional.of(entity));

		// Act
		RegisteredClient result = repository.findByClientId("test-client");

		// Assert
		assertThat(result).isNotNull();
		assertThat(result.getClientId()).isEqualTo("test-client");
		assertThat(result.getClientSecret()).isEqualTo("$2a$10$abc123");
		assertThat(result.getScopes()).containsExactly("api.access");
		assertThat((String) result.getClientSettings().getSetting("memberId")).isEqualTo("member-123");
	}

	@Test
	void findByClientId_withInactiveClient_returnsNull() {
		// Arrange
		ClientCredentialsEntity entity = new ClientCredentialsEntity();
		entity.setClientId("inactive-client");
		entity.setStatus("INACTIVE");

		when(jpaRepository.findByClientId("inactive-client")).thenReturn(Optional.of(entity));

		// Act
		RegisteredClient result = repository.findByClientId("inactive-client");

		// Assert
		assertThat(result).isNull();
	}

	@Test
	void findByClientId_withUnknownClient_returnsNull() {
		// Arrange
		when(jpaRepository.findByClientId("unknown-client")).thenReturn(Optional.empty());

		// Act
		RegisteredClient result = repository.findByClientId("unknown-client");

		// Assert
		assertThat(result).isNull();
	}

	@Test
	void findById_delegatesToFindByClientId() {
		// Arrange
		ClientCredentialsEntity entity = new ClientCredentialsEntity();
		entity.setClientId("test-client");
		entity.setMemberId("member-123");
		entity.setClientSecret("$2a$10$abc123def");
		entity.setStatus("ACTIVE");
		entity.setCreatedAt(LocalDateTime.now());

		when(jpaRepository.findByClientId("test-client")).thenReturn(Optional.of(entity));

		// Act
		RegisteredClient result = repository.findById("test-client");

		// Assert
		assertThat(result).isNotNull();
		assertThat(result.getClientId()).isEqualTo("test-client");
	}

	@Test
	void save_throwsUnsupportedOperationException() {
		// Act & Assert
		assertThatThrownBy(() -> repository.save(null))
			.isInstanceOf(UnsupportedOperationException.class)
			.hasMessageContaining("read-only");
	}

}
