package com.br.jande.pontoeletronico.service.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.jande.pontoeletronico.entities.Funcionario;
import com.br.jande.pontoeletronico.repositories.FuncionarioRepository;
import com.br.jande.pontoeletronico.service.FuncionarioService;

@Service
public class FuncionarioServiceImpl implements FuncionarioService{

	private static final Logger LOG = LoggerFactory.getLogger(FuncionarioServiceImpl.class);
	
	@Autowired
	private FuncionarioRepository funcionarioRepository;
	
	@Override
	public Funcionario persisitir(Funcionario funcionario) {
		LOG.info("Incluindo funcionario no banco : {}", funcionario);
		return this.funcionarioRepository.save(funcionario);
	}

	@Override
	public Optional<Funcionario> buscarPorId(Long id) {
		LOG.info("Buscando o funcionario por Id : {}", id);
		return Optional.ofNullable(this.funcionarioRepository.findOne(id));
	}

	@Override
	public Optional<Funcionario> buscarPorCpf(String cpf) {
		LOG.info("Buscando funcionario pelo CPF : {}", cpf);
		return Optional.ofNullable(this.funcionarioRepository.findByCpf(cpf));
	}

	@Override
	public Optional<Funcionario> buscarPorEmail(String email) {
		LOG.info("Buscando funcionario pelo Email : {}", email);
		return Optional.ofNullable(this.funcionarioRepository.findByEmail(email));
	}


}
