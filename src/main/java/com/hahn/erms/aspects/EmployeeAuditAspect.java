package com.hahn.erms.aspects;

import com.hahn.erms.entities.Employee;
import com.hahn.erms.models.UserDetailsModel;
import com.hahn.erms.repositories.AuditRepository;
import com.hahn.erms.entities.Audit;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
public class EmployeeAuditAspect {

    private final AuditRepository auditRepository;

    public EmployeeAuditAspect(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    @AfterReturning(
            pointcut = "execution(* com.hahn.erms.services.EmployeeService.createEmployee(..)) && args(employee)",
            returning = "result",
            argNames = "joinPoint,employee,result")
    public void logCreateEmployee(JoinPoint joinPoint, Employee employee, Employee result) {
        logAudit(
                "CREATE",
                result.getId(),
                String.format("Created employee with ID: %d, Name: %s.", result.getId(), employee.getFullName())
        );
    }

    @Before(value = "execution(* com.hahn.erms.services.EmployeeService.updateEmployeeWithRoleCheck(..)) && args(id, employeeDetails, userDetails)", argNames = "joinPoint, id, employeeDetails, userDetails")
    public void logUpdateEmployee(JoinPoint joinPoint, Long id, Employee employeeDetails, UserDetailsModel userDetails) {
        if (employeeDetails != null) {
            logAudit(
                    "UPDATE",
                    id,
                    String.format("Updated employee with ID: %d by user %s.", id, userDetails != null ? userDetails.getUsername() : "Unknown")
            );
        } else {
            logAudit(
                    "UPDATE",
                    id,
                    String.format("User %s attempted to update employee with ID: %d but no details were provided.",
                            userDetails != null ? userDetails.getUsername() : "Unknown", id)
            );
        }
    }

    @AfterReturning(
            pointcut = "execution(* com.hahn.erms.services.EmployeeService.deleteEmployee(..)) && args(id)",
            returning = "result",
            argNames = "joinPoint,id,result")
    public void logDeleteEmployee(JoinPoint joinPoint, Long id, boolean result) {
        if(result){
            logAudit(
                    "DELETE",
                    id,
                    String.format("Deleted employee with ID: %d.", id)
            );
        }
        else{
            logAudit(
                    "DELETE",
                    id,
                    String.format("User attempted to update employee with ID: %d.", id)
            );
        }

    }

    @AfterReturning(
            pointcut = "execution(* com.hahn.erms.services.EmployeeService.getEmployeeById(..)) && args(id)",
            returning = "result",
            argNames = "joinPoint,id,result")
    public void logViewEmployee(JoinPoint joinPoint, Long id, Object result) {
        if (result != null) {
            logAudit(
                    "VIEW",
                    id,
                    String.format("Viewed employee with ID: %d.", id)
            );
        }
    }

    @AfterReturning(
            pointcut = "execution(* com.hahn.erms.services.EmployeeService.getAllEmployees(..))",
            returning = "result",
            argNames = "joinPoint,result")
    public void logViewEmployees(JoinPoint joinPoint, Object result) {
        if (result != null) {
            logAudit(
                    "VIEW",
                    0L,
                    "Viewed all employees."
            );
        }
    }


    private void logAudit(String action, Long entityId, String details) {
        Audit audit = Audit.builder()
                .action(action)
                .timestamp(LocalDateTime.now())
                .entityId(entityId)
                .entityName("Employee")
                .details(details)
                .performedBy(getCurrentUser())
                .build();

        auditRepository.save(audit);
    }

    private String getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return "Guest";
    }
}