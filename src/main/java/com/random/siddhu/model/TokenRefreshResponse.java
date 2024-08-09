package com.random.siddhu.model;

public class TokenRefreshResponse {
	private String accessToken;
	private String refreshToken;
	private Long accessExpiry;
	private String tokenType = "Bearer";

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public Long getAccessExpiry() {
		return accessExpiry;
	}

	public void setAccessExpiry(Long accessExpiry) {
		this.accessExpiry = accessExpiry;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public TokenRefreshResponse(String accessToken, String refreshToken) {
	    this.accessToken = accessToken;
	    this.refreshToken = refreshToken;
	}

}
