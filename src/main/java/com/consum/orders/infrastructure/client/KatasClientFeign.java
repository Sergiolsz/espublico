package com.consum.orders.infrastructure.client;

import com.consum.orders.infrastructure.client.dto.ContentClientDTO;
import com.consum.orders.infrastructure.client.dto.PaginatedOrderClientDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "katas-client", url = "https://kata-espublicotech.g3stiona.com/v1/")
public interface KatasClientFeign {

    @GetMapping("orders")
    PaginatedOrderClientDTO getClientOrders(
            @RequestParam(value = "page", defaultValue = "1") String page,
            @RequestParam(value = "maxPerPage", defaultValue = "100") String maxPerPage
    );

    @GetMapping("orders/{uuid}")
    ContentClientDTO getClientOrderByUUID(@PathVariable(value = "uuid") String uuid);
}
