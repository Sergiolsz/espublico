package com.consum.orders.domain.utils;

import com.consum.orders.domain.exception.InvalidException;
import com.consum.orders.domain.exception.ProcessingException;
import com.consum.orders.domain.dto.OrdersDTO;
import com.consum.orders.domain.dto.SummaryDTO;
import com.consum.orders.domain.mapper.OrdersMapper;
import com.consum.orders.infrastructure.client.dto.ContentClientDTO;
import com.consum.orders.infrastructure.client.dto.PaginatedOrderClientDTO;
import com.consum.orders.infrastructure.database.entity.Orders;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Clase utilitaria que proporciona métodos relacionados con el procesamiento de pedidos.
 */
@Slf4j
@Component
public class OrdersMethods {

    private final OrdersMapper ordersMapper;
    private final OrdersValidations ordersValidations;

    public OrdersMethods(OrdersMapper ordersMapper,
                         OrdersValidations ordersValidations) {
        this.ordersMapper = ordersMapper;
        this.ordersValidations = ordersValidations;
    }

    /**
     * Procesa el listado recibido de la API Katas para mapearlo a objetos de Pedidos (Orders).
     *
     * @param paginatedOrderClientDTO Objeto respuesta de la API Katas.
     * @return Listado de Orders.
     */
    public List<Orders> convertToOrders(PaginatedOrderClientDTO paginatedOrderClientDTO) {
        log.info("Iniciando mapeo de PaginatedOrderClientDTO a Orders.");

        List<Orders> orders = paginatedOrderClientDTO.getContent()
                .stream()
                .map(this::convertToEntityWithExceptionHandling)
                .toList();

        log.info("Mapeo completo. Total de pedidos mapeados: {}", orders.size());
        return orders;
    }

    /**
     * Mapea objetos de Pedidos (Orders) a objetos de Respuesta DTO (OrderDTO).
     *
     * @param orders Pedidos - Datos de la tabla Orders.
     * @return Listado de OrderDTO
     */
    public List<OrdersDTO> convertToOrdersDTO(List<Orders> orders) {
        log.info("Iniciando mapeo de Orders a OrderResponse.");

        List<OrdersDTO> ordersDTO = orders.stream()
                .map(ordersMapper::ordersToOrderDTO)
                .toList();

        log.info("Mapeo completo. Total de OrderResponse mapeados: {}", ordersDTO.size());
        return ordersDTO;
    }

    /**
     * Resumen para el conteo por cada tipo de los campos: Region, Country, Item Type, Sales Channel, Order Priority.
     *
     * @param ordersDTOList Listado de pedidos.
     * @return Objeto OrderSummary que contiene los conteos por región, país, tipo de ítem, canal de ventas y prioridad de orden.
     */
    public SummaryDTO generateOrderSummary(List<OrdersDTO> ordersDTOList) {
        log.info("Iniciando generación de resumen de pedidos.");

        if (ordersDTOList == null || ordersDTOList.isEmpty()) {
            String message = "La lista de pedidos no puede ser vacía.";
            log.error(message);
            throw new ProcessingException(message);
        }

        Map<String, Long> countByRegion = generateSummary(ordersDTOList, OrdersDTO::getRegion, "region");
        Map<String, Long> countByCountry = generateSummary(ordersDTOList, OrdersDTO::getCountry, "country");
        Map<String, Long> countByItemType = generateSummary(ordersDTOList, OrdersDTO::getItemType, "item_type");
        Map<String, Long> countBySalesChannel = generateSummary(ordersDTOList, OrdersDTO::getSalesChannel, "sales_channel");
        Map<String, Long> countByOrderPriority = generateSummary(ordersDTOList, OrdersDTO::getOrderPriority, "order_priority");

        log.info("Resumen de pedidos generado con éxito.");
        return SummaryDTO.builder()
                .regionSummary(countByRegion)
                .countrySummary(countByCountry)
                .itemTypeSummary(countByItemType)
                .salesChannelSummary(countBySalesChannel)
                .orderPrioritySummary(countByOrderPriority)
                .build();
    }

    public OrdersDTO convertContentClientDTOToOrderDTO(ContentClientDTO contentClientDTO) {
        log.info("Iniciando mapeo de ContentClientDTO a OrdersDTO.");
        return ordersMapper.contentClientDTOToOrderDTO(contentClientDTO);
    }

    /**
     * Mapeo de un objeto ContentClientDTO a una entidad Order.
     *
     * @param contentClientDTO DTO que contiene la información de la orden.
     * @return Entidad Order mapeada desde el DTO.
     */
    private Orders convertToEntityWithExceptionHandling(ContentClientDTO contentClientDTO) {
        log.info("Iniciando conversión de ContentClientDTO a Order para ID: {}", contentClientDTO.getId());

        try {
            // Validar el DTO antes de mapearlo a la entidad Order
            ordersValidations.validateContentClientDTO(contentClientDTO);

            Orders orders = ordersMapper.contentClientDTOToOrders(contentClientDTO);
            log.info("Conversión procesada correctamente para ID: {}", contentClientDTO.getId());

            return orders;
        } catch (InvalidException e) {
            log.error("Error en la validación de datos para order ID: {}. Mensaje: {}", contentClientDTO.getId(), e.getMessage(), e);
            throw new ProcessingException("Error en la validación de datos para el order id: " + contentClientDTO.getId(), e);
        } catch (Exception e) {
            log.error("Error inesperado al procesar order ID: {}. Mensaje: {}", contentClientDTO.getId(), e.getMessage(), e);
            throw new ProcessingException("Error inesperado al procesar el order id: " + contentClientDTO.getId(), e);
        }
    }

    /**
     * Genera un resumen agrupado de los pedidos.
     *
     * @param ordersDTOList Lista de pedidos.
     * @param classifier   Función que especifica el campo de agrupación.
     * @param logField     Campo para propósitos de log.
     * @return Mapa con el conteo por cada valor de los datos requeridos.
     */
    private Map<String, Long> generateSummary(List<OrdersDTO> ordersDTOList,
                                              Function<OrdersDTO, String> classifier,
                                              String logField) {

        Map<String, Long> summary = ordersDTOList.stream()
                .collect(Collectors.groupingBy(classifier, Collectors.counting()));
        log.debug("Conteo por {}: {}", logField, summary);

        return summary;
    }

}
