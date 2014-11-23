package com.cruciform;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.cruciform.utils.SafeObjectMap;

public class TestClass {
	
	public static class ChildClass extends TestClass { }

	//public static class Baz<@Nullable T extends TestClass> { }
	
	public enum TestEnum {
		ONE
	}

	public static class GenericClass<T extends TestClass> { 
//	public static <@Nullable T extends @Nullable Component> ComponentMapper<@Nullable T> getFor (Class<@Nullable T> componentClass) {
		public static <T extends TestClass> GenericClass<T> create (Class<T> creationClass) {
			return new GenericClass<T>(creationClass);
		}
		
		public GenericClass(Class<T> creationClass) { 
		}
	}
	
	public static class GenericClassNullable<T extends @Nullable TestClass> { 
//	public static <@Nullable T extends @Nullable Component> ComponentMapper<@Nullable T> getFor (Class<@Nullable T> componentClass) {
//		public static <T extends TestClass> GenericClassNullable<T> create (Class<T> creationClass) {
//			return new GenericClass<T>(creationClass);
//		}
		
		public GenericClassNullable(Class<@Nullable T> creationClass) { 
		}
	}
	
	public static void acceptEnum(TestEnum val) {
		
	}
	
	public static void acceptClass(Class<? extends @Nullable TestClass> type) {
		
	}
	

	public static void test() {
		acceptEnum(TestEnum.ONE);
		GenericClass<ChildClass> genericObj = new GenericClass<ChildClass>(ChildClass.class);
		GenericClassNullable<ChildClass> genericObj2 = new GenericClassNullable<ChildClass>(ChildClass.class);
		SafeObjectMap<TestEnum, String> map = new SafeObjectMap<>();
		@NonNull final String blah = map.getSafe(TestEnum.ONE, "blah");
		System.out.println(blah);
		Class<? extends TestClass> type1 = ChildClass.class;
		Class<? extends @Nullable TestClass> type2 = ChildClass.class; // No warning
		Class<@Nullable ? extends @Nullable TestClass> type3 = ChildClass.class;
		Class<@Nullable ? extends TestClass> type4 = ChildClass.class;
		
		//public static class Baz<@Nullable T extends TestClass> { } // This throws a null pointer exception in Eclipse's compiler... oops.
		acceptClass(ChildClass.class);
	}
}
