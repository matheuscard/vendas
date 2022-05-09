package com.math.domain.rest.controller;

import com.math.domain.entity.ItemPedido;
import com.math.domain.entity.Pedido;
import com.math.domain.entity.enumerator.StatusPedido;
import com.math.domain.rest.dto.AtualizacaoStatusPedidoDTO;
import com.math.domain.rest.dto.InformacaoItemPedidoDTO;
import com.math.domain.rest.dto.InformacoesPedidoDTO;
import com.math.domain.rest.dto.PedidoDTO;
import com.math.domain.services.PedidoService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public Integer save(@RequestBody PedidoDTO dto){
        Pedido pedido = service.salvar(dto);
        return pedido.getId();
    }
    @GetMapping("{id}")
    public InformacoesPedidoDTO getById(@PathVariable Integer id){
        return service.obterPedidoCompleto(id)
                .map(p -> converter(p))
                .orElseThrow(() ->
                        new ResponseStatusException(NOT_FOUND, "Pedido não encontrado."));
    }

    @GetMapping
    public List<InformacoesPedidoDTO> find(Pedido filtro){
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Example example = Example.of(filtro,matcher);
        return converterPedidos(service.findAll(example));
    }

    private InformacoesPedidoDTO converter(Pedido pedido){
        return InformacoesPedidoDTO
                .builder()
                .codigo(pedido.getId())
                .dataPedido(pedido.getDataPedido().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .cpf(pedido.getCliente().getCpf())
                .nomeCliente(pedido.getCliente().getNome())
                .total(pedido.getTotal())
                .status(pedido.getStatusPedido().name())
                .items(converter(pedido.getItens()))
                .build();

    }
    @PatchMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    public void updateStatus(@PathVariable Integer id,@RequestBody AtualizacaoStatusPedidoDTO dto){
        StatusPedido statusPedido = StatusPedido.valueOf(dto.getNovoStatus());
        service.atualizaStatus(id,statusPedido);
    }
    private List<InformacaoItemPedidoDTO> converter(List<ItemPedido> itens){
        if(CollectionUtils.isEmpty(itens)){
            return Collections.emptyList();
        }

        return itens.stream().map(item -> InformacaoItemPedidoDTO
                .builder()
                .precoUnitario(item.getProduto().getPreco())
                .descricaoProduto(item.getProduto().getDescricao())
                .quantidade(item.getQuantidade())
                .build()).collect(Collectors.toList());
    }

    private List<InformacoesPedidoDTO> converterPedidos(List<Pedido> pedidos){
        if(CollectionUtils.isEmpty(pedidos)){
            return Collections.emptyList();
        }

        return pedidos.stream().map(pedido -> InformacoesPedidoDTO
                .builder()
                .codigo(pedido.getId())
                .dataPedido(pedido.getDataPedido().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .cpf(pedido.getCliente().getCpf())
                .nomeCliente(pedido.getCliente().getNome())
                .total(pedido.getTotal())
                .status(pedido.getStatusPedido().name())
                .items(converter(pedido.getItens()))
                .build()).collect(Collectors.toList());
    }
}
