package com.br.jande.pontoeletronico.service;

import java.util.Optional;

import com.br.jande.pontoeletronico.entities.Funcionario;

public interface FuncionarioService {

	/**
	 * Persisti um funcionário na base de dados
	 * 
	 * @param funcionario
	 * @return
	 */
	Funcionario persisitir(Funcionario funcionario);
	
	/**
	 * Retorna o Funcionario de acordo com o id
	 * 
	 * @param id
	 * @return
	 */
	Funcionario buscarPorId(Long id);
	
	/**
	 * Retorna um funcionario de acordo com o CPF
	 * 
	 * @param cpf
	 * @return
	 */
	Optional<Funcionario> buscarPorCpf(String cpf);
	
	/**
	 * Retorna um funcionario dado um email
	 * 
	 * @param email
	 * @return
	 */
	Optional<Funcionario> buscarPorEmail(String email);
	
}
