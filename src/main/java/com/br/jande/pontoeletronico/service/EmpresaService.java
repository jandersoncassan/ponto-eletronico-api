package com.br.jande.pontoeletronico.service;

import java.util.Optional;

import com.br.jande.pontoeletronico.entities.Empresa;

public interface EmpresaService {

	/**
	 * Retorna a empresa, dado um cnpj
	 * 
	 * @param cnpj
	 * @return
	 */
	Optional<Empresa> buscarPorCnpj(String cnpj);
	
	/**
	 * Cria uma nova empresa no banco de dados
	 * 
	 * @param empresa
	 * @return
	 */
	Empresa persistir(Empresa empresa);
}
