package com.payments.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "payment_id", nullable = false, updatable = false)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private PaymentType type;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 3)
    private Currency currency;

    @Column(name = "debtor_iban", nullable = false, length = 34)
    private String debtorIban;

    @Column(name = "creditor_iban", nullable = false, length = 34)
    private String creditorIban;

    @Column(columnDefinition = "TEXT")
    private String details;

    @Column(name = "creditor_bic", length = 11)
    private String creditorBic;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private boolean canceled;

    @Column(name = "cancel_fee", precision = 19, scale = 2)
    private BigDecimal cancelFee;

    @Column(name = "canceled_at")
    private LocalDateTime canceledAt;

    @Column(name = "client_ip", length = 45)
    private String clientIp;

    @Column(name = "client_country", length = 2)
    private String clientCountry;

    @Version
    @Column(nullable = false)
    private long version;

    @OneToMany(mappedBy = "payment",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Notification> notifications = new ArrayList<>();;

    @PrePersist
    public void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isCancelableAt(LocalDateTime now) {
        if (canceled) return false;
        return createdAt.toLocalDate().equals(now.toLocalDate());
    }
}
