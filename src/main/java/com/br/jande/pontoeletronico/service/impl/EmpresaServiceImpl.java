package com.br.jande.pontoeletronico.service.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.jande.pontoeletronico.entities.Empresa;
import com.br.jande.pontoeletronico.repositories.EmpresaRepository;
import com.br.jande.pontoeletronico.service.EmpresaService;

@Service
public class EmpresaServiceImpl implements EmpresaService{

	private static final Logger LOG = LoggerFactory.getLogger(EmpresaServiceImpl.class);
	
	@Autowired
	private EmpresaRepository empresaRepository;
	
	@Override
	public Optional<Empresa> buscarPorCnpj(String cnpj) {
		LOG.info("Busncando a empresa CNPJ {}", cnpj);
		return Optional.ofNullable(empresaRepository.findByCnpj(cnpj));
	}
	
	@Override
	public Empresa persistir(Empresa empresa) {
		LOG.info("Incluindo a empresa : {}", empresa);
		return empresaRepository.save(empresa);
	}
}
