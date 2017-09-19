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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.jande.pontoeeletronico.utils.PasswordUtils;
import com.br.jande.pontoeletronico.dtos.CadastroPFDto;
import com.br.jande.pontoeletronico.entities.Empresa;
import com.br.jande.pontoeletronico.entities.Funcionario;
import com.br.jande.pontoeletronico.enums.PerfilEnum;
import com.br.jande.pontoeletronico.response.Response;
import com.br.jande.pontoeletronico.service.EmpresaService;
import com.br.jande.pontoeletronico.service.FuncionarioService;

@RestController
@RequestMapping("/api/cadastrar-pf")
@CrossOrigin(origins="*")
public class CadastroPFController {

	private static final Logger LOG = 	LoggerFactory.getLogger(CadastroPFController.class);
	
	@Autowired
	private EmpresaService empresaService;
	
	@Autowired
	private FuncionarioService funcionarioService;
	
	@PostMapping
	public ResponseEntity<Response<CadastroPFDto>> cadastrar(@Valid @RequestBody CadastroPFDto cadastroPFDto, BindingResult result)throws NoSuchAlgorithmException{
		LOG.info("Cadastro PF : {}", cadastroPFDto.toString());
		Response<CadastroPFDto> response = new Response<>();
		
		validarDadosExistentes(cadastroPFDto, result);
		Funcionario funcionario = this.converterDtoParaFuncionario(cadastroPFDto, result);
		
		if(result.hasErrors()) {
			LOG.error("Erro validando dados de cadastro PF : {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		Optional<Empresa> empresa =this.empresaService.buscarPorCnpj(cadastroPFDto.getCnpj());
		empresa.ifPresent(emp -> funcionario.setEmpresa(emp));
		this.funcionarioService.persisitir(funcionario);
		
		response.setData(this.converterCadastroPFDto(funcionario));		
		return ResponseEntity.ok(response);
	}

	/**
	 * Verifica se a empresa está cadastrada e se o funcionário já existe na base de dados
	 * @param cadastroPFDto
	 * @param result
	 */
	private void validarDadosExistentes(CadastroPFDto cadastroPFDto, BindingResult result) {
		Optional<Empresa> empresa = this.empresaService.buscarPorCnpj(cadastroPFDto.getCnpj());
		if(!empresa.isPresent()) {
			result.addError(new ObjectError("empresa", "Empresa não cadastrada"));
		}
		
		this.funcionarioService.buscarPorCpf(cadastroPFDto.getCpf())
		.ifPresent(func -> result.addError(new ObjectError("funcionario","CPF já existente")));
		
		this.funcionarioService.buscarPorEmail(cadastroPFDto.getEmail())
		.ifPresent(func -> result.addError(new ObjectError("funcionario","Email já existente")));
		
	}

	/**
	 * Transforma o objeto DTO em Funcionario
	 * 
	 * @param cadastroPFDto
	 * @param result
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	private Funcionario converterDtoParaFuncionario(CadastroPFDto cadastroPFDto, BindingResult result)
			throws NoSuchAlgorithmException {
		
		Funcionario funcionario = new Funcionario();
		funcionario.setNome(cadastroPFDto.getNome());
		funcionario.setEmail(cadastroPFDto.getEmail());
		funcionario.setCpf(cadastroPFDto.getCpf());
		funcionario.setPerfil(PerfilEnum.ROLE_USUARIO);
		funcionario.setSenha(PasswordUtils.gerarBCrypt(cadastroPFDto.getSenha()));
		cadastroPFDto.getQtdHorasAlmoco()
		.ifPresent(qtdHorasAlmoco -> funcionario.setQtdHorasAlmoco(Float.valueOf(qtdHorasAlmoco)));
		cadastroPFDto.getQtdHorasTrabalhadas()
		.ifPresent(qtdHorasTrabalhadas -> funcionario.setQtdHorasTrabalhoDia(Float.valueOf(qtdHorasTrabalhadas)));
		cadastroPFDto.getValorHora()
		.ifPresent(valorHora -> funcionario.setValorHora(new BigDecimal(valorHora)));
		
		return funcionario;
		
	}

	/**
	 * Converter o Funcionario para DTO
	 * 
	 * @param funcionario
	 * @return
	 */
	private CadastroPFDto converterCadastroPFDto(Funcionario funcionario) {
		CadastroPFDto cadastro = new CadastroPFDto();
		cadastro.setId(funcionario.getId());
		cadastro.setNome(funcionario.getNome());
		cadastro.setEmail(funcionario.getEmail());
		cadastro.setCpf(funcionario.getCpf());
		cadastro.setCnpj(funcionario.getEmpresa().getCnpj());
		funcionario.getQtdHorasAlmocoOpt().ifPresent(qtdHorasAlmoco -> cadastro.setQtdHorasAlmoco(Optional.of(Float.toString(qtdHorasAlmoco))));
		funcionario.getQtdHorasTrabalhoDiaOpt().ifPresent(qtdHorasTrabalho -> cadastro.setQtdHorasTrabalhadas(Optional.of(Float.toString(qtdHorasTrabalho))));
		funcionario.getValorHoraOpt().ifPresent(valorHora -> cadastro.setValorHora(Optional.of(valorHora.toString())));
		
		return cadastro;
	}


}
