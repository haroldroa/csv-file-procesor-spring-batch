package org.bulletproof.candidates.harold;

import java.io.File;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.annotation.AfterWrite;
import org.springframework.batch.test.AssertFile;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CsvFileProcessorApplication.class)
public class CsvFileProcessorApplicationTests {

	private static final String EXPECTED_FILE = "src" + File.separator
			+ "test" + File.separator
			+ "resources" + File.separator
			+ "test-expected-data.csv";
	private static final String OUTPUT_FILE = "test-out-data.csv";

	/**
	 * Overwrite application.properties to use a testing set of files
	 */
	@BeforeClass
	public static void setParameters() {
		System.setProperty("csv.processor.input.filename",
				"test-input-data.csv");
		System.setProperty("csv.processor.output.filename", "test-out-data.csv");
		System.setProperty("csv.processor.chunk.size", "2");
	}

	/**
	 * Compare the generated file against an expected example
	 * 
	 * @throws Exception
	 */
	@Test
	@AfterWrite
	public void outputHasExpectedContent() throws Exception {
		AssertFile.assertFileEquals(new FileSystemResource(EXPECTED_FILE),
				new FileSystemResource(OUTPUT_FILE));
	}
}
