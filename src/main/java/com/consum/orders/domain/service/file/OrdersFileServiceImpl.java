package com.consum.orders.domain.service.file;

import com.consum.orders.application.model.OrdersFileResponse;
import com.consum.orders.domain.exception.ProcessingException;
import com.consum.orders.domain.exception.FileException;
import com.consum.orders.domain.dto.OrdersDTO;
import com.consum.orders.domain.mapper.OrdersMapper;
import com.consum.orders.domain.utils.OrdersFileMethods;
import com.consum.orders.infrastructure.service.OrdersRepositoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class OrdersFileServiceImpl implements OrdersFileService {

    private static final String FILENAME = "orders_file";

    private final OrdersRepositoryService ordersRepositoryService;
    private final OrdersFileMethods ordersFileMethods;
    private final OrdersMapper ordersMapper;

    public OrdersFileServiceImpl(OrdersRepositoryService ordersRepositoryService,
                                 OrdersFileMethods ordersFileMethods,
                                 OrdersMapper ordersMapper) {
        this.ordersRepositoryService = ordersRepositoryService;
        this.ordersFileMethods = ordersFileMethods;
        this.ordersMapper = ordersMapper;
    }

    @Override
    @Cacheable("orders")
    public OrdersFileResponse generateOrderFileCSV() throws ProcessingException {
        try {
            List<OrdersDTO> ordersDtoList = ordersRepositoryService.findAll()
                    .stream()
                    .map(ordersMapper::ordersToOrderDTO)
                    .toList();

            log.info("Obtenidos {} registros de pedidos de la base de datos", ordersDtoList.size());
            return ordersFileMethods.generateOrderFileCSV(ordersDtoList, FILENAME);
        } catch (Exception e) {
            String errorMessage = "Error inesperado al generar archivo CSV desde la base de datos";
            log.error("{}: {}", errorMessage, e.getMessage());
            throw new FileException(errorMessage, e);
        }
    }
}
