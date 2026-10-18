package com.example.scheme.repository;

import com.example.scheme.model.Scheme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Spring Data JPA repository providing CRUD database operations for Scheme entities.
@Repository
public interface SchemeRepository extends JpaRepository<Scheme, Long> {
}
