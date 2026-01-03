package com.mrakshith21.programming_judge.services;

import com.mrakshith21.programming_judge.models.Problem;
import com.mrakshith21.programming_judge.repositories.ProblemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProblemsService {

    @Autowired
    private ProblemRepository problemRepository;

    public List<Problem> getAllProblems() {
        return problemRepository.findAll();
    }

    public Optional<Problem> getProblemById(Long id) {
        return problemRepository.findById(id);
    }

    public Problem createProblem(Problem problem) {
        return problemRepository.save(problem);
    }

    public Problem updateProblem(Long id, Problem problemDetails) {
        Problem problem = problemRepository.findById(id).orElseThrow(() -> new RuntimeException("Problem not found"));
        problem.setProblemName(problemDetails.getProblemName());
        problem.setProblemDescription(problemDetails.getProblemDescription());
        problem.setTimeLimit(problemDetails.getTimeLimit());
        problem.setMemoryLimit(problemDetails.getMemoryLimit());
        // problem.setCreated(problemDetails.getCreated()); // Usually created date shouldn't be updated
        return problemRepository.save(problem);
    }
}
