package net.asserts;

public class AssertResult {
	
	private boolean isSucc;
	private String message;

	public AssertResult(boolean isSucc, String message) {
		this.setSucc(isSucc);
		this.setMessage(message);
	}

	public boolean isSucc() {
		return isSucc;
	}

	public void setSucc(boolean isSucc) {
		this.isSucc = isSucc;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
