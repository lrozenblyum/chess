package com.leokom.chess.player.legal.brain.normalized;

/**
 * Double Interval (inclusive) [ minValue, maxValue ]
 */
class Range {
    private final double minValue;
    private final double maxValue;

    Range( double minValue, double maxValue ) {
        if ( minValue >= maxValue ) {
            throw new IllegalArgumentException( String.format( "[%s, %s] cannot be created", minValue, maxValue ) );
        }

        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    /**
     * Convert value from current range to the target one in proportional manner.
     *
     * Current: [ 0, 100 ], Target: [ 10, 20 ], value = 30 -> 13
     *
     * @param targetRange target range to put the value into
     * @param value value inside current range
     * @return value put proportionally in target range
     */
    double convert( Range targetRange, double value ) {
        if ( value < this.minValue || value > this.maxValue ) {
            throw new IllegalArgumentException( String.format( "The value %s is out of range [ %s, %s ] ", value, minValue, maxValue ) );
        }

        double ourPosition = ( value - minValue ) / ( maxValue - minValue );

        return ( targetRange.maxValue - targetRange.minValue ) * ourPosition + targetRange.minValue;
    }
}
