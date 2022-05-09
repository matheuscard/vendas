package com.math.domain.services.impl;

import com.math.domain.entity.Cliente;
import com.math.domain.entity.ItemPedido;
import com.math.domain.entity.Pedido;
import com.math.domain.entity.Produto;
import com.math.domain.entity.enumerator.StatusPedido;
import com.math.domain.repository.Clientes;
import com.math.domain.repository.ItemsPedidos;
import com.math.domain.repository.Pedidos;
import com.math.domain.repository.Produtos;
import com.math.domain.rest.dto.ItemPedidoDTO;
import com.math.domain.rest.dto.PedidoDTO;
import com.math.domain.services.PedidoService;
import com.math.exception.PedidoNaoEncontradoException;
import com.math.exception.RegraNegocioException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final Pedidos pedidos;
    private final Clientes clientes;
    private final Produtos produtos;
    private final ItemsPedidos itemsPedidos;

    @Override
    @Transactional // Ou salva tudo corretamente ou dá rollback - Garante a Integridade.
    public Pedido salvar(PedidoDTO dto) {

        Integer idCliente = dto.getCliente();
        Cliente cliente =  clientes.findById(idCliente).orElseThrow(() -> new RegraNegocioException("Código de cliente inválido"));
        Pedido pedido = new Pedido();
        pedido.setTotal(dto.getTotal());
        pedido.setDataPedido(LocalDate.now());
        pedido.setCliente(cliente);
        pedido.setStatusPedido(StatusPedido.REALIZADO);
        List<ItemPedido> itemPedidos = converterItems(pedido, dto.getItems());
        pedidos.save(pedido);
        this.itemsPedidos.saveAll(itemPedidos);
        pedido.setItens(itemPedidos);
        return pedido;

    }

    @Override
    public Optional<Pedido> obterPedidoCompleto(Integer id) {
        return pedidos.findByIdFetchItens(id);
    }
    @Override
    public List<Pedido> findAll(Example example){
        return pedidos.findAll(example);
    }
    @Override
    @Transactional
    public void atualizaStatus(Integer id, StatusPedido statusPedido) {
        pedidos.findById(id)
                .map(pedido -> {
                    pedido.setStatusPedido(statusPedido);
                    return pedidos.save(pedido);
                }).orElseThrow(() -> new PedidoNaoEncontradoException("Pedido não encontrado."));
    }

    private List<ItemPedido> converterItems(Pedido pedido, List<ItemPedidoDTO> items){
        if(items.isEmpty()){
            throw new RegraNegocioException("Não é possível realizar um pedido sem items.");
        }
        return items.stream().map(dto -> {
            Integer idProduto = dto.getProduto();
            Produto produto = produtos.findById(idProduto).orElseThrow(() -> new RegraNegocioException("Código de produto inválido: " + idProduto));
            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setQuantidade(dto.getQuantidade());
            itemPedido.setPedido(pedido);
            itemPedido.setProduto(produto);
            return itemPedido;
        }).collect(Collectors.toList());
    }
}
