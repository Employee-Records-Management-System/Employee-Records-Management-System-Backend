package com.hahn.erms.services.impl;

import com.hahn.erms.entities.Permission;
import com.hahn.erms.entities.Role;
import com.hahn.erms.repositories.PermissionRepository;
import com.hahn.erms.repositories.RoleRepository;
import com.hahn.erms.services.RoleService;
import com.hahn.erms.utils.EntityUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public RoleServiceImpl(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Optional<Role> getRoleById(Long id) {
        return roleRepository.findById(id);
    }

    @Transactional
    public void assignPermissionToRole(Role role, Permission permission) {
        Role managedRole = roleRepository.findById(role.getId())
                .orElseThrow(() -> new IllegalArgumentException("Role not found with ID: " + role.getId()));

        Permission managedPermission = permissionRepository.findById(permission.getId())
                .orElseThrow(() -> new IllegalArgumentException("Permission not found with ID: " + permission.getId()));

        managedRole.getPermissions().add(managedPermission);

        roleRepository.save(managedRole);
    }
    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    public Role updateRole(Long id, Role roleDetails) {
        Role role = roleRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Role not found"));
        EntityUtils.copyNonNullProperties(roleDetails, role);

        return roleRepository.save(role);
    }

    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }
}
