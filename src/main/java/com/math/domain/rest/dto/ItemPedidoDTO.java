package com.math.domain.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  "items":[ --> ItemPedidoDTO
 *     {
 *         "produto":1,
 *         "quantidade":10
 *     }
 *  ]
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemPedidoDTO {
    private Integer produto;
    private Integer quantidade;
}
