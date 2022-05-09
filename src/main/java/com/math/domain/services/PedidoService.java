package com.math.domain.services;

import com.math.domain.entity.Pedido;
import com.math.domain.entity.enumerator.StatusPedido;
import com.math.domain.rest.dto.PedidoDTO;
import org.springframework.data.domain.Example;

import java.util.List;
import java.util.Optional;

public interface PedidoService {
    Pedido salvar(PedidoDTO dto);
    Optional<Pedido> obterPedidoCompleto(Integer id);
    List<Pedido> findAll(Example example);
    void atualizaStatus(Integer id, StatusPedido statusPedido);
}
