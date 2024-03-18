package com.example.emailservice.repositories;

import com.example.emailservice.domains.model.CodeSender;
import org.springdoc.core.providers.JavadocProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CodeSenderRepository extends JpaRepository<CodeSender, Long> {

    @Query("SELECT c from CodeSender c where c.code = :code")
    Optional<CodeSender> findCodeSenderByCode(@Param("code") Integer code);
}
