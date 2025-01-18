package com.hahn.erms.services;

import com.hahn.erms.entities.Role;
import com.hahn.erms.repositories.RoleRepository;
import com.hahn.erms.services.impl.RoleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceTests {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    private Role testRole;

    @BeforeEach
    void setUp() {
        testRole = Role.builder()
                .id(1L)
                .name("ADMIN")
                .permissions(new HashSet<>())
                .build();
    }

    @Test
    void getAllRoles_ShouldReturnListOfRoles() {
        when(roleRepository.findAll()).thenReturn(Collections.singletonList(testRole));

        List<Role> roles = roleService.getAllRoles();

        assertNotNull(roles);
        assertEquals(1, roles.size());
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void createRole_ShouldReturnCreatedRole() {
        when(roleRepository.save(any(Role.class))).thenReturn(testRole);

        Role created = roleService.createRole(testRole);

        assertNotNull(created);
        assertEquals(testRole.getName(), created.getName());
        verify(roleRepository, times(1)).save(any(Role.class));
    }
}
