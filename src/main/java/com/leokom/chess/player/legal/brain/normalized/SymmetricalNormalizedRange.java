package com.leokom.chess.player.legal.brain.normalized;


/**
 * Normalize move estimates that were calculated by a symmetrical evaluator.
 * Symmetrical denormalized evaluator can provide [min, max] ranges for index for OUR and THEIR position
 * Symmetrical denormalized evaluator behavior is based on a simple difference of the two indices.
 */
class SymmetricalNormalizedRange {
    private final double maximalAdvantage;
    private final double minimalAdvantage;

    SymmetricalNormalizedRange( double minimalPossibleValue, double maximalPossibleValue ) {
        this.maximalAdvantage = maximalPossibleValue - minimalPossibleValue;
        this.minimalAdvantage = minimalPossibleValue - maximalPossibleValue;
    }

    //convert advantage [ MINIMAL_ADV..MAXIMAL_ADV ] to value [ 0..1 ]
    double normalize( double denormalizedValue ) {
        return ( denormalizedValue - minimalAdvantage ) /
                (maximalAdvantage - minimalAdvantage);
    }

}
