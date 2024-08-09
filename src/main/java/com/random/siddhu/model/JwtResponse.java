package com.random.siddhu.model;

import java.io.Serializable;

public class JwtResponse implements Serializable {

	private static final long serialVersionUID = -8091879091924046844L;
	private final String jwttoken;
	private final String refreshToken;

	public JwtResponse(String jwttoken,String refreshToken) {
		this.jwttoken = jwttoken;
		this.refreshToken = refreshToken;
	}

	public String getToken() {
		return this.jwttoken;
	}

    public String getRefreshToken() {
        return refreshToken;
    }
}