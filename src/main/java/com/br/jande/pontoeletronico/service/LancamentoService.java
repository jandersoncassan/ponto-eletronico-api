package com.br.jande.pontoeletronico.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.br.jande.pontoeletronico.entities.Lancamento;

public interface LancamentoService {

	/**
	 * Buscar lancamento pelo id do funcionario
	 * 
	 * @param id
	 * @param pageRequest
	 * @return
	 */
	Page<Lancamento> buscarPorFuncionarioId(Long id, PageRequest pageRequest);
	
	/**
	 * Retorna o Lancamento de acordo com o Id
	 * 
	 * @param id
	 * @return
	 */
	Optional<Lancamento> buscaPorId(Long id);
	
	/**
	 * Inclui o lancamento na base de dados
	 * 
	 * @param lancamento
	 * @return
	 */
	Lancamento persistir(Lancamento lancamento);
	
	/**
	 * Remove o lancamento de acordo com o id
	 * 
	 * @param id
	 */
	void remover(Long id);
}
