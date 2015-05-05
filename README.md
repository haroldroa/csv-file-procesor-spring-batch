# csv-file-procesor-spring-batch
This application does process a huge CSV file using Spring Batch, it can be buid/tested using Maven.

It does require the following properties/parameters to be passed:

--csv.processor.chunk.size={size of chunks to process}
--csv.processor.input.filename={csv input file name, must be in classpath}
--csv.processor.output.filename={csv output file name}

Resources provided:

- sample-data.csv : default sample file with the valid format, this file has 245.000 lines
- test-expected-data.csv: Valid output to compare againts for unit testing
- test-input-data.csv: Small sample file with 10 rows used as input for unit testing
