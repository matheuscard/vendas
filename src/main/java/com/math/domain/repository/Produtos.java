package com.math.domain.repository;

import com.math.domain.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Produtos extends JpaRepository<Produto,Integer> {


}
