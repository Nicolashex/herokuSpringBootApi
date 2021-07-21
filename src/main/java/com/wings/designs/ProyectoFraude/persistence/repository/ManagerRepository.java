/*
 * Copyright (c) 2021. Wings Design.
 */

package com.wings.designs.ProyectoFraude.persistence.repository;

import com.wings.designs.ProyectoFraude.persistence.model.Manager;
import com.wings.designs.ProyectoFraude.persistence.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface ManagerRepository extends JpaRepository<Manager, Long> {
    /**
     * Search for the manager with the given email and returns
     * and Optional object to check if the manager is present or not.
     *
     * @param email The email of the manager wanted.
     * @return An optional instance in any case.
     */
    @Query("SELECT m FROM manager m WHERE m.email=?1")
    Optional<Manager> findManagerByEmail(String email);

    /**
     * Given the user of a manager, gives that manager.
     *
     * @param user the User of the manager that is looked.
     * @return the Manager with the given user. If there's no manager that
     * is related to the user given, then returns null.
     */
    @Query("SELECT m FROM manager m WHERE m.user.id = :#{#user.id}")
    Manager getManagerByUser(@Param("user") User user);

    /**
     * Return a manager with the rut given.
     * @param rut rut of the manager wanted.
     * @return a manager if found, or null in the
     * other case.
     */
    @Query("SELECT m FROM manager m WHERE m.rut=?1")
    Manager getManagerByRut(String rut);

    /**
     * Return a manager with the id given.
     * @param managerId the id of the manager wanted.
     * @return a manager if found, or null in the
     * other case.
     */
    @Query("SELECT m FROM manager m WHERE m.id=?1")
    Manager getManagerById(Long managerId);
}
