package com.math.domain.repository;

import com.math.domain.entity.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemsPedidos extends JpaRepository<ItemPedido,Integer> {
}
