package com.example.book_api.batch;

import com.example.book_api.model.BookEntity;
import com.example.book_api.repository.BookRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.support.IteratorItemReader;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.batch.core.step.builder.StepBuilder;

import java.util.List;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final BookRepository bookRepository;

    public BatchConfig(JobRepository jobRepository,
                       PlatformTransactionManager transactionManager,
                       BookRepository bookRepository) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.bookRepository = bookRepository;
    }

    @Bean
    public Job processBookJob() {
        return new JobBuilder("processBookJob", jobRepository)
                .start(processBookStep())
                .build();
    }

    @Bean
    public Step processBookStep() {
        return new StepBuilder("processBookStep", jobRepository)
                .<BookEntity, BookEntity>chunk(10, transactionManager)
                .reader(bookItemReader())
                .processor(bookItemProcessor())
                .writer(bookItemWriter())
                .build();
    }

    @Bean
    public ItemReader<BookEntity> bookItemReader() {
        List<BookEntity> books = bookRepository.findAll();
        return new ListItemReader<>(books);
    }

    @Bean
    public ItemProcessor<BookEntity, BookEntity> bookItemProcessor() {
        return book -> {
            if (book.getIsbn() == null || book.getIsbn().isEmpty()) {
                throw new IllegalArgumentException("El ISBN no puede estar vacío.");
            }
            return book;
        };
    }

    @Bean
    public ItemWriter<BookEntity> bookItemWriter() {
        return books -> bookRepository.saveAll(books);
    }
}
