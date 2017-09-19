package com.br.jande.pontoeletronico.controller;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.jande.pontoeeletronico.utils.PasswordUtils;
import com.br.jande.pontoeletronico.dtos.FuncionarioDto;
import com.br.jande.pontoeletronico.entities.Funcionario;
import com.br.jande.pontoeletronico.response.Response;
import com.br.jande.pontoeletronico.service.FuncionarioService;

@RestController
@RequestMapping("/api/funcionarios")
@CrossOrigin("*")
public class FuncionarioController {

	private static final Logger LOG = 	LoggerFactory.getLogger(FuncionarioController.class);
	
	@Autowired
	private FuncionarioService funcionarioService;
	
	@PutMapping(value="/{id}")
	public ResponseEntity<Response<FuncionarioDto>> atualizar(@PathVariable("id") Long id, 
			@Valid @RequestBody FuncionarioDto funcionarioDto, BindingResult result) throws NoSuchAlgorithmException{
		
		LOG.info("Atualizando funcionario {}", funcionarioDto.toString());
		Response<FuncionarioDto> response = new Response<FuncionarioDto>();
		Optional<Funcionario> funcionario = funcionarioService.buscarPorId(id);
		
		if(!funcionario.isPresent()) {
			result.addError(new ObjectError("funcionario", "Funcionario não encontrado"));
		
		}else{
		
			this.atualizarDadosFuncionario(funcionario.get(), funcionarioDto, result);
		}
		
		if(result.hasErrors()) {
			LOG.error("Erro  validando funcionrio : {}", result.getAllErrors());
			result.getAllErrors().forEach(erro -> response.getErrors().add(erro.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		this.funcionarioService.persisitir(funcionario.get());
		response.setData(this.converterFuncionarioDto(funcionario.get()));
		
		return ResponseEntity.ok(response);
	}

	private FuncionarioDto converterFuncionarioDto(Funcionario funcionario) {

		FuncionarioDto funcionarioDto = new FuncionarioDto();
		funcionarioDto.setId(funcionario.getId());
		funcionarioDto.setEmail(funcionario.getEmail());
		funcionarioDto.setNome(funcionario.getNome());
		funcionario.getQtdHorasAlmocoOpt().ifPresent(horasAlmoco -> funcionarioDto.setQtdHorasAlmoco(Optional.of(Float.toString(horasAlmoco))));
		funcionario.getQtdHorasTrabalhoDiaOpt().ifPresent(horasTrabalho -> funcionarioDto.setQtdHorasTrabalhadas(Optional.of(Float.toString(horasTrabalho))));
		funcionario.getValorHoraOpt().ifPresent(valorHora -> funcionarioDto.setValorHora(Optional.of(valorHora.toString())));

		return funcionarioDto;

	}

	/**
	 * Consistimos os dados para persistencia
	 * 
	 * @param funcionario
	 * @param funcionarioDto
	 * @param result
	 * @throws NoSuchAlgorithmException
	 */
	private void atualizarDadosFuncionario(Funcionario funcionario, FuncionarioDto funcionarioDto,
			BindingResult result) throws NoSuchAlgorithmException{
		funcionario.setNome(funcionarioDto.getNome());
		
		if(!funcionario.getEmail().equals(funcionarioDto.getEmail())) {
			this.funcionarioService.buscarPorEmail(funcionarioDto.getEmail())
				.ifPresent(func -> result.addError(new ObjectError("email","Email já existente")));
			funcionario.setEmail(funcionarioDto.getEmail());
		}
	
		funcionario.setQtdHorasAlmoco(null);
		funcionarioDto.getQtdHorasAlmoco().ifPresent(horasAlmoco -> funcionario.setQtdHorasAlmoco(Float.valueOf(horasAlmoco)));

		funcionario.setQtdHorasTrabalhoDia(null);
		funcionarioDto.getQtdHorasTrabalhadas().ifPresent(horasTrabalho -> funcionario.setQtdHorasTrabalhoDia(Float.valueOf(horasTrabalho)));

		funcionario.setValorHora(null);
		funcionarioDto.getValorHora().ifPresent(valorHora -> funcionario.setValorHora(new BigDecimal(valorHora)));
		
		funcionarioDto.getSenha().ifPresent(senha -> funcionario.setSenha(PasswordUtils.gerarBCrypt(senha)));

	}

}
