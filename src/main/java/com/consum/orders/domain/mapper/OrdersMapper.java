package com.consum.orders.domain.mapper;

import com.consum.orders.domain.dto.OrdersDTO;
import com.consum.orders.infrastructure.client.dto.ContentClientDTO;
import com.consum.orders.infrastructure.database.entity.Orders;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrdersMapper {

    /**
     * Convierte un ContentClientDTO en Orders.
     *
     * @param contentClientDTO DTO que contiene la información de la orden.
     * @return Orders mapeado desde el DTO.
     */
    @Mapping(source = "id", target = "orderId")
    @Mapping(source = "priority", target = "orderPriority")
    @Mapping(source = "date", target = "orderDate", dateFormat = "dd/MM/yyyy")
    @Mapping(source = "shipDate", target = "shipDate", dateFormat = "dd/MM/yyyy")
    Orders contentClientDTOToOrders(ContentClientDTO contentClientDTO);

    /**
     * Convierte un Orders en un OrderDTO.
     *
     * @param orders Orders.
     * @return OrderDTO mapeado desde el Orders.
     */
    OrdersDTO ordersToOrderDTO(Orders orders);

    /**
     * Convierte un ContentClientDTO en OrdersDTO.
     *
     * @param contentClientDTO DTO que contiene la información de la orden.
     * @return OrdersDTO mapeado desde el DTO.
     */
    @Mapping(source = "id", target = "orderId")
    @Mapping(source = "priority", target = "orderPriority")
    @Mapping(source = "date", target = "orderDate", dateFormat = "dd/MM/yyyy")
    @Mapping(source = "shipDate", target = "shipDate", dateFormat = "dd/MM/yyyy")
    OrdersDTO contentClientDTOToOrderDTO(ContentClientDTO contentClientDTO);

}
