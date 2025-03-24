package az.project.securityConfiguration.repository;

import az.project.securityConfiguration.entity.RoleEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    @Transactional
    @Modifying
    @Query(value = """
        INSERT INTO user_roles (user_id, role_id)
        SELECT u.id, r.id FROM users u
        JOIN roles r ON r.user = 1
        WHERE u.username = ?1
    """, nativeQuery = true)
    void assignUserRoles(String username);
}
