package org.bulletproof.candidates.harold;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
/**
 * This class does transform each row of the file by appending a unique identifier to the data.
 * @author Harold
 *
 */
@DependsOn("counterBean")
public class AddUniqueIdentifierProcessor implements
		ItemProcessor<CSVRowData, CSVRowData> {
	
	@Autowired
	private AutoIncrementService counterBean;
	
	
	@Override
	public CSVRowData process(final CSVRowData fileRow) throws Exception {
		final CSVRowData processedData = new CSVRowData(fileRow.getFirstColumn(), fileRow.getSecondColumn());
		/**
		 * Uses a service that encapsulates the logic to generate unique ids for this purpose. 
		 */
		processedData.setGeneratedUniqueID(counterBean.getNextConsecutive());
		return processedData;
	}

}
