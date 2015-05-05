package org.bulletproof.candidates.harold;

import org.springframework.stereotype.Service;

/**
 * Contract with the auto increment service component, a default implementation for this class is provided via configuration at {@link org.bulletproof.candidates.harold.BatchConfiguration#DefaultIncrementService()}
 * @author Harold
 *
 */
@Service
public interface AutoIncrementService {
	public Long getNextConsecutive();
}
