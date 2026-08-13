package com.moauthoriser.security;

import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.stereotype.Component;

@Component
public class MemberIdTokenCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {

	@Override
	public void customize(JwtEncodingContext context) {
		String memberId = context.getRegisteredClient()
			.getClientSettings()
			.getSetting("memberId");
		if (memberId != null) {
			context.getClaims().claim("memberId", memberId);
		}
	}

}
