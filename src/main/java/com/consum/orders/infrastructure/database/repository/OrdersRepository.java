package com.consum.orders.infrastructure.database.repository;

import com.consum.orders.infrastructure.database.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {
}
