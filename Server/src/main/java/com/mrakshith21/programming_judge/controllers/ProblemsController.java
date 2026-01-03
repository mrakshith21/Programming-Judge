package com.mrakshith21.programming_judge.controllers;

import com.mrakshith21.programming_judge.models.Problem;
import com.mrakshith21.programming_judge.services.ProblemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/problems")
public class ProblemsController {

    @Autowired
    private ProblemsService problemsService;

    @GetMapping
    public List<Problem> getAllProblems() {
        return problemsService.getAllProblems();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Problem> getProblemById(@PathVariable Long id) {
        return problemsService.getProblemById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Problem createProblem(@RequestBody Problem problem) {
        return problemsService.createProblem(problem);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Problem> updateProblem(@PathVariable Long id, @RequestBody Problem problemDetails) {
        try {
            return ResponseEntity.ok(problemsService.updateProblem(id, problemDetails));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
