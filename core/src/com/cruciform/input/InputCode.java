package com.cruciform.input;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;

@NonNullByDefault
public class InputCode {
	/** Avoid hash collisions with key codes. **/
	private static final int MOUSE_BUTTON_OFFSET = 1000;
	protected final int code;
	private final boolean isKey;

	public static InputCode fromButton(int code) {
		return new InputCode(code + MOUSE_BUTTON_OFFSET, false);
	}
	
	public static InputCode fromKey(int code) {
		return new InputCode(code, true);
	}
	
	private InputCode(int code, boolean isKey) {
		this.code = code;
		this.isKey = isKey;
	}
	
	@Override
	public int hashCode() {
		return code;
	}

	@Override
	public boolean equals(@Nullable Object other) {
		InputCode otherCode = (InputCode) other;
		if (otherCode == null) {
			return false;
		}
		return this.code == otherCode.code;
	}

	@Override
	public @Nullable String toString() {
		if (isKey) {
			return Input.Keys.toString(code);
		} else {
			switch (code - MOUSE_BUTTON_OFFSET) {
			case Buttons.BACK:
				return "Back";
			case Buttons.FORWARD:
				return "Forward";
			case Buttons.LEFT:
				return "Mouse Left";
			case Buttons.RIGHT:
				return "Mouse Right";
			case Buttons.MIDDLE:
				return "Mouse Middle";
			default:
				return "Error: Unknown Mouse Button";
			}
		}
	}
	
}
