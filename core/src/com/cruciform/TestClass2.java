package com.cruciform;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public class TestClass2 {
	public static class ChildClass extends TestClass2 {}
	
	@NonNullByDefault({})
	public class GameManager {
		public final Class<? extends TestClass2> type = ChildClass.class;
	}
	
	public class Cruciform {
		
	}
}
