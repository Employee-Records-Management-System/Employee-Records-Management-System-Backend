package com.hahn.erms.controllers;

import com.hahn.erms.entities.Audit;
import com.hahn.erms.repositories.AuditRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/audit-logs")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AuditLogController {

    private final AuditRepository auditRepository;

    public AuditLogController(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    @GetMapping
    public List<Audit> getAllAuditLogs() {
        return auditRepository.findAll();
    }
}
