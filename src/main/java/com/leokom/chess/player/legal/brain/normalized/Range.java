package com.leokom.chess.player.legal.brain.normalized;


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

    double convert( Range targetRange, double value ) {
        if ( value < this.minValue || value > this.maxValue ) {
            throw new IllegalArgumentException( String.format( "The value %s is out of range [ %s, %s ] ", value, minValue, maxValue ) );
        }

        double ourPosition = ( value - minValue ) / ( maxValue - minValue );

        return ( targetRange.maxValue - targetRange.minValue ) * ourPosition + targetRange.minValue;
    }
}
