package com.example.book_api.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BatchJobController {

    private final JobLauncher jobLauncher;

    private final Job importBookJob;

    public BatchJobController(JobLauncher jobLauncher, Job importBookJob) {
        this.jobLauncher = jobLauncher;
        this.importBookJob = importBookJob;
    }

    @GetMapping("/start-batch")
    public String startBatchJob() {
        try {
            jobLauncher.run(importBookJob, new JobParametersBuilder()
                    .addLong("startAt", System.currentTimeMillis())
                    .toJobParameters());
            return "¡Batch Job iniciado correctamente!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al iniciar el Batch Job: " + e.getMessage();
        }
    }
}