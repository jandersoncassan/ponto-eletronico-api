package com.br.jande.pontoeletronico.repositories;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.br.jande.pontoeeletronico.utils.PasswordUtils;
import com.br.jande.pontoeletronico.entities.Empresa;
import com.br.jande.pontoeletronico.entities.Funcionario;
import com.br.jande.pontoeletronico.enums.PerfilEnum;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class FuncionarioRepositoryTest {

	@Autowired
	private FuncionarioRepository funcionarioRepository;
	
	@Autowired
	private EmpresaRepository empresaRepository;
	
	private static final String EMAIL = "email@email";
	private static final String CPF = "24291173474";
	
	@Before
	public void setUp() throws Exception{
		Empresa empresa = this.empresaRepository.save(obterDadosEmpresa());
		this.funcionarioRepository.save(obterDadosFuncionarioEmpresa(empresa));
	}

	@After
	public final void tearDown() {
		this.empresaRepository.deleteAll();
	}

	@Test
	public void testFuncionarioBuscarPorEmail() {
		Funcionario funcionario = funcionarioRepository.findByEmail(EMAIL);
		assertEquals(EMAIL, funcionario.getEmail());
	}
	
	@Test
	public void testFuncionarioBuscarCpf() {
		Funcionario funcionario = funcionarioRepository.findByCpf(CPF);
		assertEquals(CPF, funcionario.getCpf());
	}
	
	@Test
	public void testFuncionarioBucarCpfOrEmail() {
		Funcionario funcionario = funcionarioRepository.findByCpfOrEmail(CPF, EMAIL);
		assertNotNull(funcionario);
	}
	
	@Test
	public void testBuscarFuncionarioPorCpfOrEmailCpfInvalido() {
		Funcionario funcionario = funcionarioRepository.findByCpfOrEmail("22138384877", EMAIL);
		assertNotNull(funcionario);
	}
	
	@Test
	public void testBuscarFuncionarioPorEmailOrCpfEmailInvalido() {
		Funcionario funcionario = funcionarioRepository.findByCpfOrEmail(CPF, "teste@teste.com");
		assertNotNull(funcionario);
	}

	private Empresa obterDadosEmpresa() {
		Empresa empresa = new Empresa();
		empresa.setRazaoSocial("Empresa de exemplo");
		empresa.setCnpj("51463645000100");
		return empresa;
	}
	
	private Funcionario obterDadosFuncionarioEmpresa(Empresa empresa) {
		Funcionario funcionario = new Funcionario();
		funcionario.setCpf(CPF);
		funcionario.setEmail(EMAIL);
		funcionario.setEmpresa(empresa);
		funcionario.setNome("Fulano de Tal");
		funcionario.setPerfil(PerfilEnum.ROLE_ADIMIN);
		funcionario.setSenha(PasswordUtils.gerarBCrypt("123456"));
		funcionario.setQtdHorasAlmoco(1f);
		funcionario.setQtdHorasTrabalhoDia(8f);
		funcionario.setValorHora(new BigDecimal(45.50));
		return funcionario;
	}
}
