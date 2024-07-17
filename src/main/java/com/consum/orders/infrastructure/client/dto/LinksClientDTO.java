package com.consum.orders.infrastructure.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LinksClientDTO {

    @JsonProperty("next")
    String next;

    @JsonProperty("self")
    String self;
}
