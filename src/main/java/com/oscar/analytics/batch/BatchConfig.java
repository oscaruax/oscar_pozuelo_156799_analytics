package com.oscar.analytics.batch;

import com.oscar.analytics.model.Student;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {

    // ---------- READER ----------
    @Bean
    public FlatFileItemReader<Student> studentReader() {
        FlatFileItemReader<Student> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("students.csv"));
        reader.setLinesToSkip(1);

        DefaultLineMapper<Student> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames("id", "name", "averageGrade");

        BeanWrapperFieldSetMapper<Student> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Student.class);

        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        reader.setLineMapper(lineMapper);
        return reader;
    }

    // ---------- WRITER ----------
    @Bean
    public JpaItemWriter<Student> studentWriter(EntityManagerFactory emf) {
        JpaItemWriter<Student> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(emf);
        return writer;
    }

    // ---------- STEP ----------
    @Bean
    public Step importStudentsStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            FlatFileItemReader<Student> reader,
            JpaItemWriter<Student> writer) {

        return new StepBuilder("importStudentsStep", jobRepository)
                .<Student, Student>chunk(5, transactionManager)
                .reader(reader)
                .writer(writer)
                .build();
    }

    // ---------- JOB (ESTO ES LO CRÍTICO) ----------
    @Bean
    public Job importStudentsJob(
            JobRepository jobRepository,
            Step importStudentsStep) {

        return new JobBuilder("importStudentsJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(importStudentsStep)
                .build();
    }
}
