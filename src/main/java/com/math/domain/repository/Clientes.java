package com.math.domain.repository;

import com.math.domain.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface  Clientes extends JpaRepository<Cliente, Integer> {
    @Query(value = "select c from Cliente c where c.nome like %:nome%")
    List<Cliente> encontrarPorNome(@Param("nome") String nome);

    List<Cliente> findByNomeLikeOrIdOrderById(String nome, Integer id);

    Cliente findOneByNome(String nome);

    boolean existsByNomeLike(String nome);
    @Query(" select c from Cliente c left join fetch c.pedidos where  c.id = :id")
    Cliente findClienteByIdFetchPedidos(@Param("id") Integer id);
}
