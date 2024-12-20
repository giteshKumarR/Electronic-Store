package com.lcwd.electronic.store.ElectronicStore.entities;

import com.lcwd.electronic.store.ElectronicStore.enums.OrderStatus;
import com.lcwd.electronic.store.ElectronicStore.enums.PaymentStatus;
import com.lcwd.electronic.store.ElectronicStore.enums.ShippingMethod;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "orders")
public class Order {
    @Id
    private String orderId;

    @Column(nullable = false, unique = true)
    private String OrderNumber;  // to uniquely identity an order

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "order",
    fetch = FetchType.EAGER,
    cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Column(nullable = false)
    private Double totalBeforeTax;

    @Column(nullable = false)
    private Double tax;

    @Column(nullable = false)
    private Double shippingCost;

    @Column(nullable = false)
    private Double totalAmountAfterTax;

    @Column(nullable = false)
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Embedded
    private Address shippingAddress;

    @Enumerated(EnumType.STRING)
    private ShippingMethod shippingMethod;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "street", column = @Column(name = "billing_street")),
            @AttributeOverride(name = "city", column = @Column(name = "billing_city")),
            @AttributeOverride(name = "state", column = @Column(name = "billing_state")),
            @AttributeOverride(name = "country", column = @Column(name = "billing_country")),
            @AttributeOverride(name = "zipcode", column = @Column(name = "billing_zip_code")),
            @AttributeOverride(name = "phoneNbr", column = @Column(name = "billing_phoneNbr")),
            @AttributeOverride(name = "recipientName", column = @Column(name = "billing_recipientName")),
    })
    private Address billingAddress;

    @Column(nullable = false)
    private String paymentMethod;

    private String paymentTransactionId;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column(length = 1000)
    private String orderNotes;

    private LocalDateTime estimatedDeliveryDate; // depends shipping method
    private LocalDateTime deliveredDate; // for now put +1 or estimated Delivery date

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(name = "updated_on")
    private LocalDateTime updatedOn;

    @PrePersist
    public void onPrePersist() {
        // This will run only when the entity will be first created
        this.createdOn = LocalDateTime.now();
    }

    @PreUpdate
    public void onPreUpdate() {
        this.updatedOn = LocalDateTime.now();
    }
}
