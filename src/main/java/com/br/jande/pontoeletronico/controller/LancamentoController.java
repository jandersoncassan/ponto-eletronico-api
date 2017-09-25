package com.br.jande.pontoeletronico.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.apache.commons.lang3.EnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.br.jande.pontoeletronico.dtos.LancamentoDto;
import com.br.jande.pontoeletronico.entities.Funcionario;
import com.br.jande.pontoeletronico.entities.Lancamento;
import com.br.jande.pontoeletronico.enums.TipoEnum;
import com.br.jande.pontoeletronico.response.Response;
import com.br.jande.pontoeletronico.service.FuncionarioService;
import com.br.jande.pontoeletronico.service.LancamentoService;

@RestController
@RequestMapping("/api/lancamentos")
@CrossOrigin("*")
public class LancamentoController {
	
	private static final Logger LOG = LoggerFactory.getLogger(LancamentoController.class);
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Autowired
	private LancamentoService lancamentoService;
	
	@Autowired
	private FuncionarioService funcionarioService;

	@Value("${paginacao.qtd_por_pagina}")
	private int qtdPorPagina;
	
	/**
	 * Buscar os lncamentos do funcionario por ID
	 * 
	 * @param funcionarioId
	 * @param pag
	 * @param ord
	 * @param dir
	 * @return
	 */
	@GetMapping("/funcionario/{funcionarioId}")
	public ResponseEntity<Response<Page<LancamentoDto>>> listarFuncionarioPorId(
			@PathVariable("funcionarioId") Long funcionarioId,
			@RequestParam(value="pag", defaultValue="0") int pag,
			@RequestParam(value="ord", defaultValue="id") String ord,
			@RequestParam(value="dir", defaultValue="DESC") String dir){
		LOG.info("Buscando lancamentos por ID do funcionario {}, paginas {}", funcionarioId, pag);
		Response<Page<LancamentoDto>> response = new Response<>();
		
		PageRequest pageRequest = new PageRequest(pag, qtdPorPagina, Direction.valueOf(dir),ord);
		Page<Lancamento> lancamentos= lancamentoService.buscarPorFuncionarioId(funcionarioId, pageRequest);
		Page<LancamentoDto> lancamentosDto = lancamentos.map(lancamento -> this.converterLancamentoDto(lancamento));
		
		response.setData(lancamentosDto);
		return ResponseEntity.ok(response);
	}

	/**
	 * Retorna um lancamento por Id
	 * @return
	 */
	@GetMapping(value="/{id}")
	public ResponseEntity<Response<LancamentoDto>> listarPorId(@PathVariable("id") Long id){
		LOG.info("Buscando lancamento por id {}", id);
		Response<LancamentoDto> response = new Response<>();
		Optional<Lancamento> lancamento =lancamentoService.buscaPorId(id);
		
		if(!lancamento.isPresent()) {
			LOG.info("Lancamento não encontrado para o ID {}", id);
			response.getErrors().add("Lancamento não encontrado para o Id {}"+ id);
			return ResponseEntity.badRequest().body(response);
		}
		
		response.setData(this.converterLancamentoDto(lancamento.get()));
		return ResponseEntity.ok(response);
	}
	
	/**
	 * Adiciona um novo lançamento
	 * 
	 * @param lancamentoDto
	 * @param result
	 * @return
	 * @throws ParseException
	 */
	@PostMapping
	public ResponseEntity<Response<LancamentoDto>> adicionar(@Valid @RequestBody LancamentoDto lancamentoDto, 
			BindingResult result)throws ParseException{
		
		LOG.info("Adicionando lancamento : {}", lancamentoDto.toString());
		Response<LancamentoDto> response = new Response<LancamentoDto>();
		validarFuncionario(lancamentoDto, result);
		Lancamento lancamento = converterDtoParaLancamento(lancamentoDto, result);
		
		if(result.hasErrors()) {
			LOG.error("Erro validando lancamento: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		lancamento = lancamentoService.persistir(lancamento);
		response.setData(converterLancamentoDto(lancamento));
		return ResponseEntity.ok(response);
	}
	
	/**
	 * Atualiza os dados de um lancamento
	 * 
	 * @param id
	 * @param lancamentoDto
	 * @param result
	 * @return
	 * @throws ParseException
	 */
	@PutMapping(value="/{id}")
	public ResponseEntity<Response<LancamentoDto>> atualizar(@PathVariable("id") Long id,
			@Valid @RequestBody LancamentoDto lancamentoDto, BindingResult result) throws ParseException{
		
		LOG.info("Atualizando lancamento : {}", lancamentoDto.toString());
		Response<LancamentoDto> response = new Response<LancamentoDto>();
		validarFuncionario(lancamentoDto, result);
		lancamentoDto.setId(Optional.of(id));
		Lancamento lancamento = converterDtoParaLancamento(lancamentoDto, result);
		
		if(result.hasErrors()) {
			LOG.error("Erro validando lancamento : {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		lancamento = lancamentoService.persistir(lancamento);
		response.setData(converterLancamentoDto(lancamento));
		return ResponseEntity.ok(response);
	}
	
	/**
	 * Remove o lançamento
	 * 
	 * @param id
	 * @return
	 */
	@DeleteMapping(value="/{id}")
	public ResponseEntity<Response<String>> remover(@PathVariable("id") Long id){
		LOG.info("Removendo lancamento : {}", id);
		Response<String> response = new Response<String>();
		Optional<Lancamento> lancamento = lancamentoService.buscaPorId(id);
		
		if(!lancamento.isPresent()) {
			LOG.error("Erro ao remover o lancamento : {}", id);
			response.getErrors().add("Erro ao remover lançamento. Registro não enontrado para o id "+ id);
			return ResponseEntity.badRequest().body(response);
		}
		lancamentoService.remover(id);
		return ResponseEntity.ok(response);
	}
	
	/**
	 * Valida os dados do Funcionario
	 * 
	 * @param lancamentoDto
	 * @param result
	 */
	private void validarFuncionario(LancamentoDto lancamentoDto, BindingResult result) {
		if(lancamentoDto.getFuncionarioId() == null) {
			result.addError(new ObjectError("funcionario", "Funcionario não informado!"));
			return;
		}
		
		LOG.info("Validando funcionario Id : {}", lancamentoDto.getFuncionarioId());
		Optional<Funcionario> funcionario = this.funcionarioService.buscarPorId(lancamentoDto.getFuncionarioId());
		if(!funcionario.isPresent()) {
			result.addError(new ObjectError("funcionario", "Funcionario não encontrado. Id não existente."));
		}
	}

	/**
	 * Converte o DTO para lançamento
	 * 
	 * @param lancamentoDto
	 * @param result
	 * @return
	 * @throws ParseException
	 */
	private Lancamento converterDtoParaLancamento(LancamentoDto lancamentoDto, BindingResult result) throws ParseException {
		Lancamento lancamento = new Lancamento();
		
		if(lancamentoDto.getId().isPresent()) {
			Optional<Lancamento> lanc = lancamentoService.buscaPorId(lancamentoDto.getId().get());
			if(lanc.isPresent()) {
				lancamento = lanc.get();
			}else {
				result.addError(new ObjectError("Lacamento","Lançamento não encontrado"));
			}
		}else {
			lancamento.setFuncionario(new Funcionario());
			lancamento.getFuncionario().setId(lancamentoDto.getFuncionarioId());
		}

		lancamento.setDescricao(lancamentoDto.getDescricao());
		lancamento.setLocalizacao(lancamentoDto.getLocalizacao());
		lancamento.setData(this.dateFormat.parse(lancamentoDto.getData()));
	
		if(EnumUtils.isValidEnum(TipoEnum.class, lancamentoDto.getTipo())) {
			lancamento.setTipo(TipoEnum.valueOf(lancamentoDto.getTipo()));
		}else {
			result.addError(new ObjectError("tipo", "Tipo inválido"));
		}
		
		return lancamento;
	}

	/**
	 * Converte um Lancamento para Lancamento DTO
	 * 
	 * @param lancamento
	 * @return
	 */
	private LancamentoDto converterLancamentoDto(Lancamento lancamento) {
		LancamentoDto lancamentoDto = new LancamentoDto();
		lancamentoDto.setId(Optional.of(lancamento.getId()));
		lancamentoDto.setData(this.dateFormat.format(lancamento.getData()));
		lancamentoDto.setTipo(lancamento.getTipo().toString());
		lancamentoDto.setDescricao(lancamento.getDescricao());
		lancamentoDto.setLocalizacao(lancamento.getLocalizacao());
		lancamentoDto.setFuncionarioId(lancamento.getFuncionario().getId());;
		
		return lancamentoDto;
	}
}
