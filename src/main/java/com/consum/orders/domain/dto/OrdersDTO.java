package com.consum.orders.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Value
@Builder
public class OrdersDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "ID único del pedido", example = "443368995")
    String orderId;

    @Schema(description = "Prioridad del pedido", example = "M")
    String orderPriority;

    @Schema(description = "Fecha en la que se realizó el pedido", example = "7/10/2024")
    Date orderDate;

    @Schema(description = "Región del pedido", example = "Sub-Saharan Africa")
    String region;

    @Schema(description = "País del pedido", example = "South Africa")
    String country;

    @Schema(description = "Tipo de ítem del pedido", example = "Electronics")
    String itemType;

    @Schema(description = "Canal de ventas del pedido", example = "Online")
    String salesChannel;

    @Schema(description = "Fecha de envío del pedido", example = "7/12/2024")
    Date shipDate;

    @Schema(description = "Unidades vendidas", example = "100")
    Integer unitsSold;

    @Schema(description = "Precio por unidad", example = "50.00")
    BigDecimal unitPrice;

    @Schema(description = "Costo por unidad", example = "10.00")
    BigDecimal unitCost;

    @Schema(description = "Ingresos totales del pedido", example = "500.00")
    BigDecimal totalRevenue;

    @Schema(description = "Costo total del pedido", example = "100.00")
    BigDecimal totalCost;

    @Schema(description = "Beneficio total del pedido", example = "400.00")
    BigDecimal totalProfit;
}
