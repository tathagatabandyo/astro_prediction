package com.techtechnicworld.astroPrediction.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.techtechnicworld.astroPrediction.entity.Role;
import com.techtechnicworld.enums.RoleName;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);

    @Query("SELECT r.name FROM Role r WHERE r.name IN :names")
    Set<RoleName> findExistingByNames(Set<RoleName> names);
}
