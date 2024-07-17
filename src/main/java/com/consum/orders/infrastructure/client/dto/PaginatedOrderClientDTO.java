package com.consum.orders.infrastructure.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedOrderClientDTO {

    @JsonProperty("page")
    int page;

    @JsonProperty("content")
    List<ContentClientDTO> content;

    @JsonProperty("links")
    LinksClientDTO links;

}
