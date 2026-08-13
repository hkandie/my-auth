package com.moauthoriser.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ClientCredentialsJpaRepository extends JpaRepository<ClientCredentialsEntity, String> {

	Optional<ClientCredentialsEntity> findByClientId(String clientId);

}
