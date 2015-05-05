package org.bulletproof.candidates.harold;

import java.io.File;
import java.util.concurrent.atomic.AtomicLong;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

/**
 * This class does configure all required beans to process the CSV using Spring Batch
 * 
 * @author Harold
 * 
 */
@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

	@Autowired
	Environment environment;

	/**
	 * Default implementation for AutoIncrementService service. This
	 * implementation aims to be thread safe in case multiple jobs are created
	 * to work in parallel for example if using something like
	 * org.springframework.batch.core.partition.support.MultiResourcePartitioner
	 * 
	 * @return
	 */
	@Bean
	@Scope("singleton")
	AutoIncrementService DefaultIncrementService() {
		return new AutoIncrementService() {
			private AtomicLong consecutive = new AtomicLong();

			@Override
			public Long getNextConsecutive() {
				return consecutive.incrementAndGet();
			}
		};
	}

	@Bean
	public ItemReader<CSVRowData> reader() {
		FlatFileItemReader<CSVRowData> reader = new FlatFileItemReader<CSVRowData>();
		reader.setResource(new ClassPathResource(environment
				.getRequiredProperty("csv.processor.input.filename",
						String.class)));
		reader.setLineMapper(new DefaultLineMapper<CSVRowData>() {
			{
				setLineTokenizer(new DelimitedLineTokenizer() {
					{
						setNames(new String[] { "firstColumn", "secondColumn" });
					}
				});
				setFieldSetMapper(new BeanWrapperFieldSetMapper<CSVRowData>() {
					{
						setTargetType(CSVRowData.class);
					}
				});
			}
		});
		return reader;
	}

	@Bean
	public ItemProcessor<CSVRowData, CSVRowData> processor() {
		return new AddUniqueIdentifierProcessor();
	}

	@Bean
	public ItemWriter<CSVRowData> writer(DataSource dataSource) {
		FlatFileItemWriter<CSVRowData> writer = new FlatFileItemWriter<CSVRowData>();
		String outputFileName = environment.getRequiredProperty(
				"csv.processor.output.filename", String.class);

		File file = new File(outputFileName);
		writer.setResource(new FileSystemResource(file));
		/**
		 * For simplicity RowData.toString() does know the format of the output
		 * CSV file.
		 */
		writer.setLineAggregator(new LineAggregator<CSVRowData>() {
			@Override
			public String aggregate(CSVRowData item) {
				return item.getFirstColumn() + DelimitedLineTokenizer.DELIMITER_COMMA + item.getSecondColumn() + DelimitedLineTokenizer.DELIMITER_COMMA + item.getGeneratedUniqueID();
			}
		});
		return writer;
	}

	@Bean
	public Job processCSVJob(JobBuilderFactory jobs, Step s1) {
		return jobs.get("processCSVJob").incrementer(new RunIdIncrementer())
				.flow(s1).end().build();
	}

	@Bean
	public Step step1(StepBuilderFactory stepBuilderFactory,
			ItemReader<CSVRowData> reader, ItemWriter<CSVRowData> writer,
			ItemProcessor<CSVRowData, CSVRowData> processor) {
		return stepBuilderFactory
				.get("step1")
				.<CSVRowData, CSVRowData> chunk(
						environment.getRequiredProperty(
								"csv.processor.chunk.size", Integer.class))
				.reader(reader).processor(processor).writer(writer).build();
	}
}
