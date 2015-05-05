package org.bulletproof.candidates.harold;


/**
 * Each row of the file is mapped to this object for processing
 * @author Harold
 */
public class CSVRowData {

	private String firstColumn;
	private String secondColumn;
	private Long generatedUniqueID;
	
    public CSVRowData() {

    }
	public CSVRowData(String firstColumn, String secondColumn) {
		super();
		this.firstColumn = firstColumn;
		this.secondColumn = secondColumn;
	}

	public String getFirstColumn() {
		return firstColumn;
	}

	public void setFirstColumn(String firstColumn) {
		this.firstColumn = firstColumn;
	}

	public String getSecondColumn() {
		return secondColumn;
	}

	public void setSecondColumn(String secondColumn) {
		this.secondColumn = secondColumn;
	}

	public Long getGeneratedUniqueID() {
		return generatedUniqueID;
	}

	public void setGeneratedUniqueID(Long generatedUniqueID) {
		this.generatedUniqueID = generatedUniqueID;
	}
}
