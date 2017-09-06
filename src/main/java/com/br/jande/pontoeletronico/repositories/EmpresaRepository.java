package com.br.jande.pontoeletronico.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.br.jande.pontoeletronico.entities.Empresa;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

	@Transactional(readOnly=true)
	Empresa findByCnpj(String cnpj);
}
