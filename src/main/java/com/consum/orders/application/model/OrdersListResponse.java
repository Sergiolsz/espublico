package com.consum.orders.application.model;

import com.consum.orders.domain.dto.OrdersDTO;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class OrdersListResponse {

    List<OrdersDTO> orders;
}
