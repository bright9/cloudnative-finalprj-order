package com.bright9.finalproject.order.domain;

import com.bright9.finalproject.order.OrderApplication;
import com.bright9.finalproject.order.domain.OrderCancelled;
import com.bright9.finalproject.order.domain.OrderPlaced;

import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Order_table")
@Data
//<<< DDD / Aggregate Root
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long productId;

    private Integer qty;

    private String customerId;

    private Double amount;

    private String status;

    private String address;

    @PostPersist
    public void onPostPersist() {
        repository().findById(id).ifPresent(order ->{

            order.setStatus("OrderPlaced");
            repository().save(order);
        });

        OrderPlaced orderPlaced = new OrderPlaced(this);
        orderPlaced.publishAfterCommit();

//        OrderCancelled orderCancelled = new OrderCancelled(this);
//        orderCancelled.publishAfterCommit();
    }

    @PreRemove
    public void onPreRemove() {}

    public static OrderRepository repository() {
        OrderRepository orderRepository = OrderApplication.applicationContext.getBean(
            OrderRepository.class
        );
        return orderRepository;
    }

    //<<< Clean Arch / Port Method
    public static void updateStatus(OutOfStock outOfStock) {
        repository().findById(outOfStock.getOrderId()).ifPresent(order ->{

            order.setStatus("OrderCancelled");
            repository().save(order);

            OrderCancelled orderCancelled = new OrderCancelled(order);
            orderCancelled.publishAfterCommit();
        });

    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
