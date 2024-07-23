package com.consum.orders.infrastructure.service;

import com.consum.orders.domain.exception.ProcessingException;
import com.consum.orders.infrastructure.client.KatasClientFeign;
import com.consum.orders.infrastructure.client.dto.ContentClientDTO;
import com.consum.orders.infrastructure.client.dto.PaginatedOrderClientDTO;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrdersClientService {

    private final KatasClientFeign katasClientFeign;

    public OrdersClientService(KatasClientFeign katasClientFeign) {
        this.katasClientFeign = katasClientFeign;
    }

    /**
     * Obtiene pedidos paginados del servicio externo utilizando los parámetros de página y máximo por página especificados.
     *
     * @param page       Número de página a obtener.
     * @param maxPerPage Número máximo de elementos por página.
     * @return DTOs paginados de órdenes obtenidos del servicio externo.
     * @throws ProcessingException Si ocurre un error al obtener las órdenes paginadas.
     *                             El mensaje de la excepción indicará que falló en la llamada al servicio externo.
     */
    @Cacheable(value = "clientOrdersCache", key = "#page + '-' + #maxPerPage")
    public PaginatedOrderClientDTO getPagedOrdersClient(String page, String maxPerPage) {
        try {
            log.info("Llamando a getClientOrders con página={} y maxPerPage={}.", page, maxPerPage);
            PaginatedOrderClientDTO paginatedOrderClientDTO = katasClientFeign.getClientOrders(page, maxPerPage);

            log.info("Recibidos {} pedidos del servicio externo.", paginatedOrderClientDTO.getContent().size());
            return paginatedOrderClientDTO;
        } catch (FeignException feignException) {
            log.error("Error de comunicación con el servicio externo.", feignException);
            throw new ProcessingException("Error de comunicación con el servicio externo.", feignException);
        } catch (Exception exception) {
            log.error("Error al obtener pedidos del servicio externo.", exception);
            throw new ProcessingException("Fallo al obtener pedidos del servicio externo.", exception);
        }
    }


    /**
     * Obtiene un pedido por su UUID
     *
     * @param uuid Identificador único del pedido
     * @return ContentClientDTO Datos del pedido
     */
    @Cacheable(value = "clientOrderCache", key = "#uuid")
    public ContentClientDTO getOrderByUUIDClient(String uuid) {
        log.info("Llamando a getOrderClient con uuid={}.", uuid);
        ContentClientDTO contentClientDTO = katasClientFeign.getClientOrderByUUID(uuid);

        log.info("Recibido correctamente el Pedido por su uuid.");
        return contentClientDTO;
    }
}