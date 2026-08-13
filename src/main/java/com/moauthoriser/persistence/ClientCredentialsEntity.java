package com.moauthoriser.persistence;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "client_credentials")
@Getter
@Setter
public class ClientCredentialsEntity {

	@Id
	@Column(name = "client_id")
	private String clientId;

	@Column(name = "member_id")
	private String memberId;

	@Column(name = "client_secret")
	private String clientSecret;

	@Column(name = "status")
	private String status;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

}
