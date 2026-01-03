package com.mrakshith21.programming_judge.repositories;

import com.mrakshith21.programming_judge.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
}
