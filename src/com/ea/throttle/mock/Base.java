package com.ea.throttle.mock;

import java.util.Random;

public class Base {

	static Random r = new Random();

	static <T extends Enum<T>> T randomEnum(Class<T> cls){
		T[] values = cls.getEnumConstants();
		return values[r.nextInt(values.length)];
	}

}