package com.mrakshith21.programming_judge.controllers;

import com.mrakshith21.programming_judge.models.Problem;
import com.mrakshith21.programming_judge.services.ProblemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/problems")
public class ProblemsController {

    @Autowired
    private ProblemsService problemsService;

    @GetMapping
    public Page<Problem> getAllProblems(
            @PageableDefault(size = 10) Pageable pageable) {
        return problemsService.getAllProblems(pageable);
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
