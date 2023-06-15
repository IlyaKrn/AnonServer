package com.example.anonserver.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "reports")
@AllArgsConstructor
@NoArgsConstructor
public class ReportModel {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long authorId;
    private String text;
    private ReportType type;
    private long resourceId;
    private boolean isChecked;
    private boolean isViolation;
    private long uploadTime;


}
