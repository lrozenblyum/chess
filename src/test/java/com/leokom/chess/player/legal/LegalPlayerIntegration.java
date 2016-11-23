package com.leokom.chess.player.legal;

import com.leokom.chess.engine.Position;

/**
 * Expose more details about LegalPlayer than are accessible
 * through its public interface
 * Author: Leonid
 * Date-time: 10.02.16 22:57
 */
public class LegalPlayerIntegration extends LegalPlayer {
	@Override
	public Position getPosition() {
		return super.getPosition();
	}
}
