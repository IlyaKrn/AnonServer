package com.example.anonserver.api.models.reports;

import com.example.anonserver.domain.models.ReportType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportAdminResponse {

    private long id;
    private long authorId;
    private String text;
    private ReportType type;
    private long resourceId;
    private long uploadTime;
}
