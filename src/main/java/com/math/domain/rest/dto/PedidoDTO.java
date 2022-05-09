package com.math.domain.rest.dto;
//Classe simples - Padrão:
// Data Transfer Object --Adapter

import com.math.validation.NotEmptyList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * {
 *     "cliente":1,
 *     "total": 100,
 *     "items":[ --> ItemPedidoDTO
 *         {
 *             "produto":1,
 *             "quantidade":10
 *         }
 *     ]
 * }
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoDTO {
    @NotNull(message = "{campo.codigo-cliente.obrigatorio}")
    private Integer cliente;
    @NotNull(message = "{campo.total-pedido.obrigatorio}")
    private BigDecimal total;
    @NotEmptyList(message = "{campo.items-pedido.obrigatorio}")
    private List<ItemPedidoDTO> items;
}
