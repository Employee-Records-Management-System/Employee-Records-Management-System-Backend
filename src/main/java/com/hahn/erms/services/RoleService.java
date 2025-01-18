package com.hahn.erms.services;

import com.hahn.erms.entities.Permission;
import com.hahn.erms.entities.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    List<Role> getAllRoles();
    Optional<Role> getRoleById(Long id);
    Role createRole(Role role);
    Role updateRole(Long id, Role roleDetails);
    void deleteRole(Long id);
    void assignPermissionToRole(Role role, Permission permission);
}
