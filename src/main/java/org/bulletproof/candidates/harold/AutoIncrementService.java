package org.bulletproof.candidates.harold;

import org.springframework.stereotype.Service;

/**
 * Contract with the auto increment service component.
 * @author Harold
 *
 */
@Service
public interface AutoIncrementService {
	public Long getNextConsecutive();
}
