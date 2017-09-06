package com.br.jande.pontoeletronico.controller;

import java.security.NoSuchAlgorithmException;

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
import com.br.jande.pontoeletronico.dtos.CadastroPJDto;
import com.br.jande.pontoeletronico.entities.Empresa;
import com.br.jande.pontoeletronico.entities.Funcionario;
import com.br.jande.pontoeletronico.enums.PerfilEnum;
import com.br.jande.pontoeletronico.response.Response;
import com.br.jande.pontoeletronico.service.EmpresaService;
import com.br.jande.pontoeletronico.service.FuncionarioService;

@RestController
@RequestMapping("/api/cadastrar-pj")
@CrossOrigin("*")
public class CadastroPJController {

	private static final Logger LOG = LoggerFactory.getLogger(CadastroPJController.class);

	@Autowired
	private FuncionarioService funcionarioService;

	@Autowired
	private EmpresaService empresaService;

	@PostMapping
	public ResponseEntity<Response<CadastroPJDto>> cadastrar(@Valid @RequestBody CadastroPJDto cadastroPJDto,
			BindingResult result) throws NoSuchAlgorithmException {
		LOG.info(" Cadastrando PJ : {}", cadastroPJDto);
		Response<CadastroPJDto> response = new Response<CadastroPJDto>();

		validarDadosExistentes(cadastroPJDto, result);
		Empresa empresa = this.converterDtoParaEmpresa(cadastroPJDto);
		Funcionario funcionario = this.converterDtoParaFuncionario(cadastroPJDto, result);

		if (result.hasErrors()) {
			LOG.error("Erro validando dados de cadastro PJ : {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}

		this.empresaService.persistir(empresa);
		funcionario.setEmpresa(empresa);
		this.funcionarioService.persisitir(funcionario);

		response.setData(this.converterCadastroPJDto(funcionario));
		return ResponseEntity.ok(response);
	}

	/**
	 * Verifica se a empresa ou funcionario já existem na base de dados
	 * 
	 * @param cadastroPJDto
	 * @param result
	 */
	private void validarDadosExistentes(CadastroPJDto cadastroPJDto, BindingResult result) {
		this.empresaService.buscarPorCnpj(cadastroPJDto.getCnpj())
		.ifPresent(emp -> result.addError(new ObjectError("empresa", "Empresa já existe.")));
		this.funcionarioService.buscarPorCpf(cadastroPJDto.getCpf())
		.ifPresent(func -> result.addError(new ObjectError("funcionario", "CPF já existe.")));
		this.funcionarioService.buscarPorEmail(cadastroPJDto.getEmail())
		.ifPresent(func -> result.addError(new ObjectError("funcionario", "Email já existe.")));
	}

	
	/**
	 * Converter os dados do DTO para empresa
	 * 
	 * @param cadastroPJDto
	 * @return
	 */
	private Empresa converterDtoParaEmpresa(CadastroPJDto cadastroPJDto) {
		Empresa empresa = new Empresa();
		empresa.setCnpj(cadastroPJDto.getCnpj());
		empresa.setRazaoSocial(cadastroPJDto.getRazaoSocial());
		return empresa;
	}

	/**
	 * Converter dados DTO para funcionario
	 * @param cadastroPJDto
	 * @param result
	 * @return
	 */
	private Funcionario converterDtoParaFuncionario(CadastroPJDto cadastroPJDto, BindingResult result)throws NoSuchAlgorithmException {
		Funcionario funcionario = new Funcionario();
		funcionario.setNome(cadastroPJDto.getNome());
		funcionario.setEmail(cadastroPJDto.getEmail());
		funcionario.setCpf(cadastroPJDto.getCpf());
		funcionario.setPerfil(PerfilEnum.ROLE_ADMIN);
		funcionario.setSenha(PasswordUtils.gerarBCrypt(cadastroPJDto.getSenha()));
		
		return funcionario;
	}
	
	/**
	 * Convervete o objeto funcionario para CadastroPJDto
	 * 
	 * @param funcionario
	 * @return
	 */
	private CadastroPJDto converterCadastroPJDto(Funcionario funcionario) {
		CadastroPJDto cadastroPJDto = new CadastroPJDto();
		cadastroPJDto.setId(funcionario.getId());
		cadastroPJDto.setNome(funcionario.getNome());
		cadastroPJDto.setEmail(funcionario.getEmail());
		cadastroPJDto.setCpf(funcionario.getCpf());
		cadastroPJDto.setRazaoSocial(funcionario.getEmpresa().getRazaoSocial());
		cadastroPJDto.setCnpj(funcionario.getEmpresa().getCnpj());
		return cadastroPJDto;
	}
	


}
