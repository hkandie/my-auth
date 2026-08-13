package com.moauthoriser.security;

import com.moauthoriser.persistence.ClientCredentialsEntity;
import com.moauthoriser.persistence.ClientCredentialsJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Repository;

@Repository("registeredClientRepository")
@RequiredArgsConstructor
public class MemberClientRepository implements RegisteredClientRepository {

	private final ClientCredentialsJpaRepository jpaRepository;
	private static final String ACTIVE_STATUS = "ACTIVE";

	@Override
	public void save(RegisteredClient registeredClient) {
		throw new UnsupportedOperationException(
			"Client provisioning is managed externally in MSSQL; this repository is read-only");
	}

	@Override
	public RegisteredClient findById(String id) {
		return findByClientId(id);
	}

	@Override
	public RegisteredClient findByClientId(String clientId) {
		var entity = jpaRepository.findByClientId(clientId);

		if (entity.isEmpty()) {
			return null;
		}

		ClientCredentialsEntity client = entity.get();

		// Only return clients with ACTIVE status
		if (!ACTIVE_STATUS.equals(client.getStatus())) {
			return null;
		}

		return buildRegisteredClient(client);
	}

	private RegisteredClient buildRegisteredClient(ClientCredentialsEntity entity) {
		return RegisteredClient.withId(entity.getClientId())
			.clientId(entity.getClientId())
			.clientSecret(entity.getClientSecret())
			.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
			.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
			.scope("api.access")
			.clientSettings(ClientSettings.builder()
				.setting("memberId", entity.getMemberId())
				.build())
			.tokenSettings(TokenSettings.builder()
				.build())
			.build();
	}

}
