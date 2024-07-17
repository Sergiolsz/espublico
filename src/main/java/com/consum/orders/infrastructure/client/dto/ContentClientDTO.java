package com.consum.orders.infrastructure.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ContentClientDTO {

    @JsonProperty("uuid")
    String uuid;

    @JsonProperty("id")
    String id;

    @JsonProperty("region")
    String region;

    @JsonProperty("country")
    String country;

    @JsonProperty("item_type")
    String itemType;

    @JsonProperty("sales_channel")
    String salesChannel;

    @JsonProperty("priority")
    String priority;

    @JsonProperty("date")
    String date;

    @JsonProperty("ship_date")
    String shipDate;

    @JsonProperty("units_sold")
    int unitsSold;

    @JsonProperty("unit_price")
    double unitPrice;

    @JsonProperty("unit_cost")
    double unitCost;

    @JsonProperty("total_revenue")
    double totalRevenue;

    @JsonProperty("total_cost")
    double totalCost;

    @JsonProperty("total_profit")
    double totalProfit;

    @JsonProperty("links")
    ContentLinksDTO links;
}
