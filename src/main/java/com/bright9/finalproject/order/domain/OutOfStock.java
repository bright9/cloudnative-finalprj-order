package com.bright9.finalproject.order.domain;

import com.bright9.finalproject.order.infra.AbstractEvent;
import java.util.*;
import lombok.*;

@Data
@ToString
public class OutOfStock extends AbstractEvent {

    private Long id;
    private Long stock;
    private Long orderId;
}
