package com.mrakshith21.programming_judge.repositories;

import com.mrakshith21.programming_judge.models.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {
}
