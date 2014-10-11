package com.cruciform.input;

public class InputCode {
	/** Avoid hash collisions with key codes. **/
	private static final int MOUSE_BUTTON_OFFSET = 1000;
	protected final int code;

	public static InputCode fromButton(int code) {
		return new InputCode(code + MOUSE_BUTTON_OFFSET);
	}
	
	public static InputCode fromKey(int code) {
		return new InputCode(code);
	}
	
	private InputCode(int code) {
		this.code = code;
	}
	
	@Override
	public int hashCode() {
		return code;
	}

	@Override
	public boolean equals(Object other) {
		InputCode otherCode = (InputCode) other;
		if (otherCode == null) {
			return false;
		}
		return this.code == otherCode.code;
	}

	@Override
	public String toString() {
		return "InputCode: [" + code + "]";
	}
	
}
