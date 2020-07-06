package com.aliengame.server.repository;

import com.aliengame.server.entity.Role;
import com.aliengame.server.entity.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByRoleType(RoleType roleType);
}
