package com.leokom.chess.utils;

import java.util.Collection;

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
}
