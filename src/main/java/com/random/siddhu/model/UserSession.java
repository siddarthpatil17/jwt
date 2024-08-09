package com.random.siddhu.model;

public class UserSession {

	 	private int userId; // Unique user ID
	    private String sessionId; // Unique session ID
	    private boolean isActive;
		public int getUserId() {
			return userId;
		}
		public void setUserId(int userId) {
			this.userId = userId;
		}
		public String getSessionId() {
			return sessionId;
		}
		public void setSessionId(String sessionId) {
			this.sessionId = sessionId;
		}
		public boolean isActive() {
			return isActive;
		}
		public void setActive(boolean isActive) {
			this.isActive = isActive;
		}
		public UserSession(int userId, String sessionId, boolean isActive) {
			super();
			this.userId = userId;
			this.sessionId = sessionId;
			this.isActive = isActive;
		}

}
