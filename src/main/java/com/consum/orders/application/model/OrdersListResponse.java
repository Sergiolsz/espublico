package com.consum.orders.application.model;

import com.consum.orders.domain.dto.OrdersDTO;
import lombok.Builder;
import lombok.Value;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Value
@Builder
public class OrdersListResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    List<OrdersDTO> orders;
}
