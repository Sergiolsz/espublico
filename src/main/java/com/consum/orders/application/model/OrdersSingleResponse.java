package com.consum.orders.application.model;

import com.consum.orders.domain.dto.OrdersDTO;
import lombok.Builder;
import lombok.Value;

import java.io.Serial;
import java.io.Serializable;

@Value
@Builder
public class OrdersSingleResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    OrdersDTO order;
}
