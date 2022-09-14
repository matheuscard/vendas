package com.math.domain.entity;

import com.math.domain.entity.enumerator.StatusPedido;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "pedido")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
    @Column(name = "data_pedido")
    private LocalDate dataPedido;
    //10000.00 --> O scale faz menção aos últimos 2 zeros. O Precision ao número de casas decimais que o campo terá.
    @Column(name = "total", scale = 2, precision = 20)
    private BigDecimal total;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusPedido statusPedido;
    @OneToMany(mappedBy = "pedido")
    private List<ItemPedido> itens;
    //Nunca retornará uma lista nula.
    //Apesar de vazia, porém nunca nula.
    //Boa prática.
    public List<ItemPedido> getItens(){
        if(this.itens == null){
            this.itens = new ArrayList<>();
        }
        return this.itens;
    }

}
