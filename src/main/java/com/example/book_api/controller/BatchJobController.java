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
    // Constructor con inyección de dependencias
    public BatchJobController(JobLauncher jobLauncher, Job importBookJob) {
        this.jobLauncher = jobLauncher;
        this.importBookJob = importBookJob;
    }

    @GetMapping("/start-batch") // Crea un endpoint HTTP GET para lanzar el batch
    public String startBatchJob() {
        try {
            // Lanza el job con parámetros (en este caso, un timestamp único)
            jobLauncher.run(importBookJob, new JobParametersBuilder()
                    .addLong("startAt", System.currentTimeMillis()) // Evita errores por ejecución duplicada
                    .toJobParameters());
            return "¡Batch Job iniciado correctamente!";
        } catch (Exception e) {
            e.printStackTrace(); // Muestra el error en consola si falla
            return "Error al iniciar el Batch Job: " + e.getMessage();
        }
    }
}