/*
 * Copyright (c) 2021. Wings Design.
 */

package com.wings.designs.ProyectoFraude.persistence.repository;

import com.wings.designs.ProyectoFraude.persistence.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Obtain the role that matches the rol name given.
     *
     * @param name Is the name of the role wanted.
     * @return null if theres's no role with that name, if not
     * returns the role found.
     */
    Role findByName(Role.enumRole name);
}
