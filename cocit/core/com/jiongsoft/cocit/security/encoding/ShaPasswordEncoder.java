package com.jiongsoft.cocit.security.encoding;

public class ShaPasswordEncoder extends MessageDigestPasswordEncoder {

	public ShaPasswordEncoder() {
		this(1);
	}

	public ShaPasswordEncoder(int strength) {
		super("SHA-" + strength);
	}
}