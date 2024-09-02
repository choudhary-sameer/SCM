package com.scm.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.scm.entities.Contact;
import com.scm.entities.User;

public interface ContactRepo extends JpaRepository<Contact, String> {
    // Custom finder method
    List<Contact> findByUser(User user);

    // Custom query method
    @Query("SELECT c FROM Contact c WHERE c.user.id = :userId")
    List<Contact> findByUserId(@Param("userId") String userId);

}
