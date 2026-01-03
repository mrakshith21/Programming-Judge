package com.mrakshith21.programming_judge.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "problems")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Problem {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long problemId;

    private String problemName;

    @Column(columnDefinition = "TEXT")
    private String problemDescription;

    private Double timeLimit;

    private Integer memoryLimit;

    private Date created;
}
