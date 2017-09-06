package com.br.jande.pontoeletronico.service.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.br.jande.pontoeletronico.entities.Lancamento;
import com.br.jande.pontoeletronico.repositories.LancamentoRepository;
import com.br.jande.pontoeletronico.service.LancamentoService;

@Service
public class LancamentoServiceImpl implements LancamentoService{

	private static final Logger LOG = LoggerFactory.getLogger(LancamentoServiceImpl.class);
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Override
	public Page<Lancamento> buscarPorFuncionarioId(Long funcionarioId, PageRequest pageRequest) {
		LOG.info("Buscando lancamento por id fo funcionario : {}", funcionarioId);
		return this.lancamentoRepository.findByFuncionarioId(funcionarioId, pageRequest);
	}

	@Override
	public Optional<Lancamento> buscaPorId(Long id) {
		LOG.info("Buscando lancamento por id : {}", id);
		return Optional.ofNullable(lancamentoRepository.findOne(id));
	}

	@Override
	public Lancamento persistir(Lancamento lancamento) {
		LOG.info("Incluindo o lancamento : {}", lancamento);
		return this.lancamentoRepository.save(lancamento);
	}

	@Override
	public void remover(Long id) {
		LOG.info("Removendo o lancamento id : {}", id);
		this.lancamentoRepository.delete(id);
	}

}
