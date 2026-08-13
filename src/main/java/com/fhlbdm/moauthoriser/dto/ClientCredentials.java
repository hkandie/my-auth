package com.fhlbdm.moauthoriser.dto;

import lombok.Data;

@Data
public class ClientCredentials {

	private String memberId;

	private String clientId;

	private String clientSecret;

	private String status;

	private String createdAt;

}
