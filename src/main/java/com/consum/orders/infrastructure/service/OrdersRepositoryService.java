package com.consum.orders.infrastructure.service;

import com.consum.orders.domain.exception.ProcessingException;
import com.consum.orders.infrastructure.database.entity.Orders;
import com.consum.orders.infrastructure.database.repository.OrdersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para procesar operaciones de los datos de la tabla Orders
 */
@Slf4j
@Service
public class OrdersRepositoryService {

    private final OrdersRepository ordersRepository;

    public OrdersRepositoryService(OrdersRepository ordersRepository) {
        this.ordersRepository = ordersRepository;
    }

    /**
     * Guarda una lista de pedidos en la tabla Orders.
     *
     * @param ordersList lista de pedidos.
     * @throws ProcessingException si ocurre un error durante el proceso de guardado.
     */
    public void saveAllOrders(List<Orders> ordersList) {
        try {
            log.info("Intentando guardar {} pedidos", ordersList.size());
            var a = ordersRepository.saveAll(ordersList);
            log.info("Pedidos guardados correctamente: {}", ordersList.size());
        } catch (Exception exception) {
            log.error("Error en el proceso de guardar los pedidos en la base de datos", exception);
            throw new ProcessingException("Error al guardar los pedidos", exception);
        }
    }

    /**
     * Recupera un pedido por su ID de la base de datos.
     *
     * @param id ID del pedido a buscar.
     * @return El pedido encontrado, o un Optional vacío si no se encuentra.
     */
    public Optional<Orders> findById(Long id) {
        return ordersRepository.findById(id);
    }

    /**
     * Recupera todos los pedidos de la base de datos.
     *
     * @return Listado de todos los pedidos.
     */
    public List<Orders> findAll() {
        return ordersRepository.findAll();
    }
}