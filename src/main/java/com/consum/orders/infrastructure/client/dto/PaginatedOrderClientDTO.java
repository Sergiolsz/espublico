package com.consum.orders.infrastructure.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedOrderClientDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("page")
    int page;

    @JsonProperty("content")
    List<ContentClientDTO> content;

    @JsonProperty("links")
    LinksClientDTO links;

}
