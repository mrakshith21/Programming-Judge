package com.mrakshith21.programming_judge.controllers;

import com.mrakshith21.programming_judge.models.Submission;
import com.mrakshith21.programming_judge.services.SubmissionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/submissions")
public class SubmissionsController {

    @Autowired
    private SubmissionsService submissionsService;

    @GetMapping
    public List<Submission> getAllSubmissions() {
        return submissionsService.getAllSubmissions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Submission> getSubmissionById(@PathVariable Long id) {
        return submissionsService.getSubmissionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Submission createSubmission(
            @RequestPart("submission") Submission submission,
            @RequestPart("file") MultipartFile file) throws IOException {
        return submissionsService.createSubmission(submission, file);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Submission> updateSubmission(@PathVariable Long id, @RequestBody Submission submissionDetails) {
        try {
            return ResponseEntity.ok(submissionsService.updateSubmission(id, submissionDetails));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
