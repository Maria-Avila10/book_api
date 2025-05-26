package com.example.book_api.batch;

import com.example.book_api.model.BookEntity;
import com.example.book_api.repository.BookRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.batch.core.step.builder.StepBuilder;


@Configuration
@EnableBatchProcessing
public class BatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final BookRepository bookRepository;

    // Constructor con inyección de dependencias
    public BatchConfig(JobRepository jobRepository,
                       PlatformTransactionManager transactionManager,
                       BookRepository bookRepository) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.bookRepository = bookRepository;
    }

    @Bean
    public Job processBookJob() {
        // Define el Job "processBookJob" y lo asocia con el Step definido abajo
        return new JobBuilder("processBookJob", jobRepository)
                .start(processBookStep()) // El job ejecuta un único step
                .build();
    }

    @Bean
    public Step processBookStep() {
        // Define el Step que leerá, procesará y escribirá objetos BookEntity
        return new StepBuilder("processBookStep", jobRepository)
                .<BookEntity, BookEntity>chunk(10, transactionManager) // Procesa los datos en bloques de 10
                .reader(bookItemReader())
                .processor(bookItemProcessor())
                .writer(bookItemWriter()) // Escritor: guarda los libros procesados
                .build();
    }

    @Bean
    public FlatFileItemReader<BookEntity> bookItemReader() {
        FlatFileItemReader<BookEntity> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("import_books.csv")); // archivo dentro de resources
        reader.setLinesToSkip(1);

        DefaultLineMapper<BookEntity> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames("title", "author", "isbn", "publishedYear", "url"); // nombres de los atributos

        BeanWrapperFieldSetMapper<BookEntity> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(BookEntity.class);

        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        reader.setLineMapper(lineMapper);
        return reader;
    }

    @Bean
    public ItemProcessor<BookEntity, BookEntity> bookItemProcessor() {
        // Procesa cada libro validando que tenga un ISBN
        return book -> {
            if (book.getIsbn() == null || book.getIsbn().isEmpty()) {
                // Lanza error si no hay ISBN
                throw new IllegalArgumentException("El ISBN no puede estar vacío.");
            }
            return book; // Si es válido, lo devuelve sin cambios
        };
    }

    @Bean
    public ItemWriter<BookEntity> bookItemWriter() {
        // Guarda la lista de libros procesados en la base de datos
        return  books -> bookRepository.saveAll(books); // Escritura en lote
    }
}
