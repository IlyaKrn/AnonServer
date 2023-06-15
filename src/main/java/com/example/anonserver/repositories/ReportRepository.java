package com.example.anonserver.repositories;

import com.example.anonserver.domain.models.PostModel;
import com.example.anonserver.domain.models.ReportModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<ReportModel, Long> {
}
