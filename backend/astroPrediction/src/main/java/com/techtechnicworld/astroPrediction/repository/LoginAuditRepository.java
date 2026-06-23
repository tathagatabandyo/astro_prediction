package com.techtechnicworld.astroPrediction.repository;

import java.util.List;

import org.springframework.boot.data.autoconfigure.web.DataWebProperties.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techtechnicworld.astroPrediction.entity.LoginAudit;

@Repository
public interface LoginAuditRepository extends JpaRepository<LoginAudit, Long>{
    List<LoginAudit> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
}
