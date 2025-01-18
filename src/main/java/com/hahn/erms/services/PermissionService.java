package com.hahn.erms.services;

import com.hahn.erms.entities.Permission;

import java.util.List;
import java.util.Optional;

public interface PermissionService {
    List<Permission> getAllPermissions();
    Permission createPermission(Permission permission);
    Optional<Permission> getPermissionById(Long id);
    Permission updatePermission(Long id, Permission permissionDetails);
    void deletePermission(Long id);
}
