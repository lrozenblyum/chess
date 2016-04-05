package com.leokom.chess.utils;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

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
	 * Get an unmodifiable set of enum values of a particular enum class
	 * @param clazz enum class
	 * @param <T> enum
	 * @return unmodifiable set of enum values
	 */
	public static <T extends Enum< T >> Set< T > enums( Class< T > clazz ) {
		return Collections.unmodifiableSet( EnumSet.allOf( clazz ) );
	}

	/**
	 * Get stream of entry sets of the map
	 * for which values suit the predicate
	 * @param map map to be searched
	 * @param valuePredicate predicate to be applied to values
	 * @param <K> map key
	 * @param <V> map value
	 * @return stream of filtered entry sets
	 */
	public static < K, V > Stream< Map.Entry< K, V > >
	filterMapByValues( Map< K, V > map, Predicate< V > valuePredicate ) {
		return map.entrySet().stream().filter( entry -> valuePredicate.test( entry.getValue() ) );
	}

}
