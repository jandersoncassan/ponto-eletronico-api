package com.br.jande.pontoeletronico.repositories;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.br.jande.pontoeeletronico.utils.PasswordUtils;
import com.br.jande.pontoeletronico.entities.Empresa;
import com.br.jande.pontoeletronico.entities.Funcionario;
import com.br.jande.pontoeletronico.entities.Lancamento;
import com.br.jande.pontoeletronico.enums.PerfilEnum;
import com.br.jande.pontoeletronico.enums.TipoEnum;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class LancamentoRepositoryTest {

	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private FuncionarioRepository funcionarioRepository;
	
	@Autowired
	private EmpresaRepository empresaRepository;
	
	private Long funcionarioId;
	
	
	@Before
	public void setUp()throws Exception{
		Empresa empresa = this.empresaRepository.save(obterDadosEmpresa());
		
		Funcionario funcionario = this.funcionarioRepository.save(obterDadosFuncionarioEmpresa(empresa));
		this.funcionarioId = funcionario.getId();
		
		this.lancamentoRepository.save(obterDadosLancamentos(funcionario));
		this.lancamentoRepository.save(obterDadosLancamentos(funcionario));
	}
	
	@After
	public final void tearDown() {
		this.empresaRepository.deleteAll();
	}

	@Test
	public void testBuscarLancamentoPorFuncionario() {
		List<Lancamento> lancamentos = this.lancamentoRepository.findByFuncionarioId(funcionarioId);
		assertEquals(2, lancamentos.size());
	}
	
	@Test
	public void testBuscarLancamentosPorFuncionarioIdPaginacao() {
		PageRequest page = new PageRequest(0,  10);
		Page<Lancamento> lancamentos = this.lancamentoRepository.findByFuncionarioId(funcionarioId, page);
		assertEquals(2, lancamentos.getTotalElements());
		
	}
	
	private Empresa obterDadosEmpresa() {
		Empresa empresa = new Empresa();
		empresa.setRazaoSocial("Empresa de exemplo");
		empresa.setCnpj("51463645000100");
		return empresa;
	}
	
	private Funcionario obterDadosFuncionarioEmpresa(Empresa empresa) {
		Funcionario funcionario = new Funcionario();
		funcionario.setCpf("23465784766");
		funcionario.setEmail("teste@teste.com");
		funcionario.setEmpresa(empresa);
		funcionario.setNome("Fulano de Tal");
		funcionario.setPerfil(PerfilEnum.ROLE_ADIMIN);
		funcionario.setSenha(PasswordUtils.gerarBCrypt("123456"));
		funcionario.setQtdHorasAlmoco(1f);
		funcionario.setQtdHorasTrabalhoDia(8f);
		funcionario.setValorHora(new BigDecimal(45.50));
		return funcionario;
	}

	private Lancamento obterDadosLancamentos(Funcionario funcionario) {
		Lancamento lancamento = new Lancamento();
		lancamento.setData(new Date());
		lancamento.setDescricao("Almoco do dia");
		lancamento.setFuncionario(funcionario);
		lancamento.setLocalizacao("Osasco");
		lancamento.setTipo(TipoEnum.INICIO_ALMOCO);
		return lancamento;
	}

}
