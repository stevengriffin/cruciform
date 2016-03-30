package com.cruciform.utils;

import java.util.Arrays;
import java.util.stream.Collector;
import java.util.stream.Stream;

import com.badlogic.gdx.utils.Array;

public class CollectionUtils {
	public static <T> Stream<T> toStream(Array<T> array) {
		return Arrays.asList(array.toArray()).stream();
	}

	public static <T> Collector<T,?,Array<T>> toArray() {
		return Collector.of(Array::new, Array::add,
				(left, right) -> {left.addAll(right); return left; }); 
	}

}
