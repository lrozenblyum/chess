package com.leokom.games.commons.brain.normalized.range;

/**
 * Double Interval (inclusive) [ minValue, maxValue ]
 */
public class Range {
    private final double minValue;
    private final double maxValue;

    public Range( double minValue, double maxValue ) {
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
    public double convert( Range targetRange, double value ) {
        if ( !contains( value ) ) {
            throw new IllegalArgumentException( String.format( "The value %s is out of range [ %s, %s ] ", value, minValue, maxValue ) );
        }

        double ourPosition = ( value - minValue ) / length();

        return targetRange.length() * ourPosition + targetRange.minValue;
    }

    private double length() {
        return this.maxValue - this.minValue;
    }

    public boolean contains( double value ) {
        return value >= this.minValue && value <= this.maxValue;
    }
}
