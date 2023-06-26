package com.example.dentistClinic.repository;

import com.example.dentistClinic.domain.UserAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface UserAdminRepository extends JpaRepository<UserAdmin,Long> {
    Optional<UserAdmin> findByUsername(String username);
    void deleteByUsername(String username);


    @Transactional
    @Modifying
    @Query("UPDATE UserAdmin u " + "SET u.enabled = TRUE WHERE u.username = ?1")
    int enableUser(String email);
}
