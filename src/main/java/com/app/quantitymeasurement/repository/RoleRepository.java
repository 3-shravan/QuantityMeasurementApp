package com.app.quantitymeasurement.repository;

import com.app.quantitymeasurement.domain.Role;
import com.app.quantitymeasurement.domain.Role.ERole;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(ERole name);
}
