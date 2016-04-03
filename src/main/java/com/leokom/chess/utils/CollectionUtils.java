package com.leokom.chess.utils;

import java.util.*;

/**
 * Author: Leonid
 * Date-time: 07.07.13 22:25
 */
public final class CollectionUtils {
	private CollectionUtils() {}

	//TODO: reuse some library for this?
	// so far I'm trying to keep as less dependencies as possible
	public static < T > void addIfNotNull( Collection<T> collection, T toAdd ) {
		if ( toAdd != null ) {
			collection.add( toAdd );
		}
	}

	/**
	 * Return set that is intersection of the arguments
	 * @param first first argument
	 * @param second second argument
	 * @param <T> generic type of data
	 * @return intersection
	 */
	public static < T > Set< T > intersect( Set< T > first, Set< T > second ) {
		Set< T > result = new HashSet<>( first );
		result.retainAll( second );
		return result;
	}

	/**
	 * Get an unmodifiable set of enum values of a particular enum class
	 * @param clazz enum class
	 * @param <T> enum
	 * @return unmodifiable set of enum values
	 */
	public static <T extends Enum< T >> Set< T > enums( Class< T > clazz ) {
		return Collections.unmodifiableSet( EnumSet.allOf( clazz ) );
	}
}
