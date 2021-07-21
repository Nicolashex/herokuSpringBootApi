/*
 * Copyright (c) 2021. Wings Design.
 */

package com.wings.designs.ProyectoFraude.security;

import com.wings.designs.ProyectoFraude.persistence.model.User;
import com.wings.designs.ProyectoFraude.persistence.model.Role;
import com.wings.designs.ProyectoFraude.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service("userDetailsService")
@Transactional
public class UserDetailsServiceImp implements UserDetailsService {

    /**
     * the service that allow to make request to the database.
     */
    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(final String rut)
            throws UsernameNotFoundException {
        try {
            final Optional<User> user = userService.findUsersByRut(rut);
            if (!user.isPresent()) {
                throw new UsernameNotFoundException(
                        "No user found with rut: " + rut);
            }
            return new org.springframework.security.core.userdetails.User(
                    user.get().getRut(), user.get().getPassword(), true,
                    true, true, true,
                    getAuthorities(user.get().getRole())
            );
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Collection<? extends GrantedAuthority> getAuthorities(
            final Role role) {
        return getGrantedAuthorities(getPrivileges(role));
    }

    private List<String> getPrivileges(final Role role) {
        List<String> privileges = new ArrayList<>();
        privileges.add(role.getName().name());
        return privileges;
    }

    private List<GrantedAuthority> getGrantedAuthorities(
            final List<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }

}
