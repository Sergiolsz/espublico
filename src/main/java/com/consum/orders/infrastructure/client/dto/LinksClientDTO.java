package com.consum.orders.infrastructure.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LinksClientDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("next")
    String next;

    @JsonProperty("self")
    String self;
}
