package com.consum.orders.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

import java.util.Map;

@Value
@Builder
public class SummaryDTO {

    @Schema(description = "Resumen de pedidos por región", example = "{\"North America\": 10}")
    Map<String, Long> regionSummary;

    @Schema(description = "Resumen de pedidos por país", example = "{\"United States\": 7}")
    Map<String, Long> countrySummary;

    @Schema(description = "Resumen de pedidos por tipo de ítem", example = "{\"Electronics\": 8}")
    Map<String, Long> itemTypeSummary;

    @Schema(description = "Resumen de pedidos por canal de ventas", example = "{\"Online\": 9}")
    Map<String, Long> salesChannelSummary;

    @Schema(description = "Resumen de pedidos por prioridad de orden", example = "{\"H\": 6}")
    Map<String, Long> orderPrioritySummary;
}
