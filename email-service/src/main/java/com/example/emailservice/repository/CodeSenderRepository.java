package com.example.emailservice.repository;

import com.example.emailservice.model.CodeSender;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CodeSenderRepository extends JpaRepository<CodeSender, Long> {

}
