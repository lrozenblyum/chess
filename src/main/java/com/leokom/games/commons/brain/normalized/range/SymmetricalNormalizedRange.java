package com.leokom.games.commons.brain.normalized.range;


/**
 * Normalize move estimates that were calculated by a symmetrical evaluator.
 * Symmetrical denormalized evaluator can provide [min, max] ranges for index for OUR and THEIR position
 * Symmetrical denormalized evaluator behavior is based on a simple difference of the two indices.
 */
public class SymmetricalNormalizedRange {
    private static final Range NORMALIZED_RANGE = new Range( 0.0, 1.0 );
    private final Range range;

    //NOTE: the algorithm can be speed-up by simplifying the expressions below. Not a goal now.

    //the range will be [ -(max-min); (max-min) ]
    public SymmetricalNormalizedRange( double minimalPossibleValue, double maximalPossibleValue ) {
        this.range = new Range(
            minimalPossibleValue - maximalPossibleValue,
            maximalPossibleValue - minimalPossibleValue
        );

    }

    //convert advantage [ MINIMAL_ADV..MAXIMAL_ADV ] to value [ 0..1 ]
    public double normalize( double denormalizedValue ) {
        return this.range.convert( NORMALIZED_RANGE, denormalizedValue );
    }

}
