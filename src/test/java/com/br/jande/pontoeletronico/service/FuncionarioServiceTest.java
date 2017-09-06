package com.br.jande.pontoeletronico.service;

import static org.junit.Assert.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.br.jande.pontoeletronico.entities.Funcionario;
import com.br.jande.pontoeletronico.repositories.FuncionarioRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class FuncionarioServiceTest {

	@MockBean
	private FuncionarioRepository funcionarioRepository;
	
	@Autowired
	private FuncionarioService funcionarioService;
	
	@Before
	public void setUp()throws Exception{
		BDDMockito.given(this.funcionarioRepository.save(Mockito.any(Funcionario.class))).willReturn(new Funcionario());
		BDDMockito.given(this.funcionarioRepository.findOne(Mockito.anyLong())).willReturn(new Funcionario());
		BDDMockito.given(this.funcionarioRepository.findByEmail(Mockito.anyString())).willReturn(new Funcionario());
		BDDMockito.given(this.funcionarioRepository.findByCpf(Mockito.anyString())).willReturn(new Funcionario());
	}
	
	@Test
	public void testPersistirFuncionario()throws Exception{
		Funcionario funcionario = this.funcionarioService.persisitir(new Funcionario());
		assertNotNull(funcionario);
	}
	
	@Test
	public void testBuscarFuncionarioPorId()throws Exception{
		Funcionario funcionario  = this.funcionarioService.buscarPorId(1l);
		assertNotNull(funcionario);
	}
	
	@Test
	public void testBuscarFuncionarioPorCpf()throws Exception{
		Optional<Funcionario> funcionario  = this.funcionarioService.buscarPorCpf("12345678908");
		assertTrue(funcionario.isPresent());
	}
	
	@Test
	public void testBuscarFuncionarioPorEmail()throws Exception{
		Optional<Funcionario> funcionario  = this.funcionarioService.buscarPorEmail("teste@teste.com");
		assertTrue(funcionario.isPresent());
	}

}
