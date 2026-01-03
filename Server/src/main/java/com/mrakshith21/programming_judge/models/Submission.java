package com.mrakshith21.programming_judge.models;

import com.mrakshith21.programming_judge.enums.Language;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "submissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Submission implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long submissionId;

    private Long problemId;

    private String username;

    @Enumerated(EnumType.STRING)
    private Language language;

    private Date submitted;

    private String verdict;
}
