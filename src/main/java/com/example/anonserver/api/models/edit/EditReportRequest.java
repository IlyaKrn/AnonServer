package com.example.anonserver.api.models.edit;

import com.example.anonserver.domain.models.ReportType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditReportRequest {
    private String text;

}
