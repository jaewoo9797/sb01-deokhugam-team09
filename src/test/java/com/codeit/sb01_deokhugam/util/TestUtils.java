package com.codeit.sb01_deokhugam.util;

import java.lang.reflect.Field;

public class TestUtils {

	public static void setField(Object target, String fieldName, Object value) {
		Class<?> clazz = target.getClass();
		while (clazz != null) {
			try {
				Field field = clazz.getDeclaredField(fieldName);
				field.setAccessible(true);
				field.set(target, value);
				return;
			} catch (NoSuchFieldException e) {
				clazz = clazz.getSuperclass();
			} catch (IllegalAccessException e) {
				throw new RuntimeException("필드 접근 실패: " + fieldName, e);
			}
		}
		throw new RuntimeException("필드를 찾을 수 없습니다: " + fieldName);
	}

	public static void setId(Object target, Object idValue) {
		setField(target, "id", idValue);
	}
}
