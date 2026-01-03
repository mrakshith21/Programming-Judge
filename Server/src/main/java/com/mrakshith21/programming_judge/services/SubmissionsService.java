package com.mrakshith21.programming_judge.services;

import com.mrakshith21.programming_judge.models.Submission;
import com.mrakshith21.programming_judge.repositories.SubmissionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SubmissionsService {

    private static final Logger log = LoggerFactory.getLogger(SubmissionsService.class);
    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.submission.exchangeName}")
    private String exchangeName;

    @Value("${rabbitmq.submission.routingKey}")
    private String routingKey;

    @Value("${storage.submissions.path}")
    private String submissionsPath;

    public List<Submission> getAllSubmissions() {
        return submissionRepository.findAll();
    }

    public Optional<Submission> getSubmissionById(Long id) {
        return submissionRepository.findById(id);
    }

    public Submission createSubmission(Submission submission, MultipartFile file) throws IOException {
        submission.setSubmitted(new Date());
        submission.setVerdict("Not Started");
        Submission savedSubmission = submissionRepository.save(submission);

        Path root = Paths.get(submissionsPath);
        if (!Files.exists(root)) {
            Files.createDirectories(root);
        }
        Files.copy(file.getInputStream(), root.resolve(savedSubmission.getSubmissionId().toString()));
        log.info("Saved submission file to {}", root.resolve(savedSubmission.getSubmissionId().toString()));

        rabbitTemplate.convertAndSend(exchangeName, routingKey, savedSubmission);
        log.info("Submitted request id: {} to queue", savedSubmission.getSubmissionId());
        return savedSubmission;
    }

    public Submission updateSubmission(Long id, Submission submissionDetails) {
        Submission submission = submissionRepository.findById(id).orElseThrow(() -> new RuntimeException("Submission not found"));
        submission.setUsername(submissionDetails.getUsername());
        submission.setSubmitted(submissionDetails.getSubmitted());
        submission.setVerdict(submissionDetails.getVerdict());
        return submissionRepository.save(submission);
    }
}
