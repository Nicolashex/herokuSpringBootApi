/*
 * Copyright (c) 2021. Wings Design.
 */

package com.wings.designs.ProyectoFraude.service;

import com.wings.designs.ProyectoFraude.persistence.model.Role;
import com.wings.designs.ProyectoFraude.persistence.repository.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    /**
     * Main constructor.
     * @param roleRepository used to make queries or retrieve information
     *                       to the role table in the database.
     */
    public RoleService(final RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     * Given a role name, search and give the role
     * with that name.
     * @param name name of the role wanted.
     * @return a role instance if found, null if not.
     */
    public Role findRoleByName(final Role.enumRole name) {
        return this.roleRepository.findByName(name);
    }

    /**
     * Save in to the database the role given.
     * @param role role to be saved.
     * @return the role saved.
     */
    public Role save(Role role) {
        return this.roleRepository.save(role);
    }
}
