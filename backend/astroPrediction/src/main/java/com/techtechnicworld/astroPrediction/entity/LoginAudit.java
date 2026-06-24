package com.techtechnicworld.astroPrediction.entity;

import com.techtechnicworld.enums.AuditEventType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "login_audits", indexes = {
        @Index(name = "idx_audit_user_id", columnList = "user_id"),
        @Index(name = "idx_audit_user_event", columnList = "user_id, event_type"),
        @Index(name = "idx_audit_user_created", columnList = "user_id, created_at"),
        @Index(name = "idx_audit_ip", columnList = "ip_address"),
})
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginAudit extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false, length = 30)
    private AuditEventType eventType;

    @Column(name = "device_type", length = 50)
    private String deviceType;

    @Column(length = 100)
    private String browser;

    @Column(length = 100)
    private String os;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(length = 100)
    private String country;

    @Column(length = 100)
    private String city;

    @Column(length = 10)
    private String language;

    @Column(columnDefinition = "TEXT")
    private String details;
}
