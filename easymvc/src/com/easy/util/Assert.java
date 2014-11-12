/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.easy.util;


public abstract class Assert {
	public static void isTrue(boolean expression,
			String message) {
		if (!(expression))
			throw new IllegalArgumentException(message);
	}

	public static void isTrue(boolean expression) {
		isTrue(expression, "[Assertion failed] - this expression must be true");
	}

	public static void isNull(Object object,
			String message) {
		if (object != null)
			throw new IllegalArgumentException(message);
	}

	public static void isNull(Object object) {
		isNull(object, "[Assertion failed] - the object argument must be null");
	}

	public static void notNull(Object object,
			String message) {
		if (object == null)
			throw new IllegalArgumentException(message);
	}

	public static void notNull(Object object) {
		notNull(object,
				"[Assertion failed] - this argument is required; it must not be null");
	}

	public static void hasLength(String text,
			String message) {
		if (!(StringUtils.hasLength(text)))
			throw new IllegalArgumentException(message);
	}

	public static void hasLength(String text) {
		hasLength(text,
				"[Assertion failed] - this String argument must have length; it must not be null or empty");
	}

	public static void hasText(String text,
			String message) {
		if (!(StringUtils.hasText(text)))
			throw new IllegalArgumentException(message);
	}

	public static void hasText(String text) {
		hasText(text,
				"[Assertion failed] - this String argument must have text; it must not be null, empty, or blank");
	}

	public static void doesNotContain(String textToSearch,
			String substring,
			String message) {
		if ((!(StringUtils.hasLength(textToSearch)))
				|| (!(StringUtils.hasLength(substring)))
				|| (!(textToSearch.contains(substring))))
			return;
		throw new IllegalArgumentException(message);
	}

	public static void doesNotContain(String textToSearch,
			String substring) {
		doesNotContain(textToSearch,
				substring,
				new StringBuilder().append("[Assertion failed] - this String argument must not contain the substring [")
						.append(substring)
						.append("]")
						.toString());
	}

	public static void notEmpty(Object[] array,
			String message) {
		if (ObjectUtils.isEmpty(array))
			throw new IllegalArgumentException(message);
	}

	public static void notEmpty(Object[] array) {
		notEmpty(array,
				"[Assertion failed] - this array must not be empty: it must contain at least 1 element");
	}

	public static void noNullElements(Object[] array,
			String message) {
		if (array != null)
			for (Object element : array)
				if (element == null)
					throw new IllegalArgumentException(message);
	}

	public static void noNullElements(Object[] array) {
		noNullElements(array,
				"[Assertion failed] - this array must not contain any null elements");
	}

	public static void isInstanceOf(Class<?> clazz,
			Object obj) {
		isInstanceOf(clazz, obj, "");
	}

	public static void isInstanceOf(Class<?> type,
			Object obj,
			String message) {
		notNull(type, "Type to check against must not be null");
		if (type.isInstance(obj)) {
			return;
		}
		throw new IllegalArgumentException(new StringBuilder().append((StringUtils.hasLength(message)) ? new StringBuilder().append(message)
				.append(" ")
				.toString()
				: "")
				.append("Object of class [")
				.append((obj != null) ? obj.getClass().getName() : "null")
				.append("] must be an instance of ")
				.append(type)
				.toString());
	}

	public static void isAssignable(Class<?> superType,
			Class<?> subType) {
		isAssignable(superType, subType, "");
	}

	public static void isAssignable(Class<?> superType,
			Class<?> subType,
			String message) {
		notNull(superType, "Type to check against must not be null");
		if ((subType == null) || (!(superType.isAssignableFrom(subType))))
			throw new IllegalArgumentException(new StringBuilder().append(message)
					.append(subType)
					.append(" is not assignable to ")
					.append(superType)
					.toString());
	}

	public static void state(boolean expression,
			String message) {
		if (!(expression))
			throw new IllegalStateException(message);
	}

	public static void state(boolean expression) {
		state(expression,
				"[Assertion failed] - this state invariant must be true");
	}
}