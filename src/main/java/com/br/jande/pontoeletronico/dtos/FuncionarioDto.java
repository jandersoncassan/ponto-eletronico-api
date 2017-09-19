package com.br.jande.pontoeletronico.dtos;

import java.util.Optional;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class FuncionarioDto {

	private Long id;
	
	@NotEmpty(message="Nome não pode ser vazio")
	@Length(min=3, max=200, message="Nome deve conter entre {min} e {max} caracteres")
	private String nome;

	@NotEmpty(message="Email não pode ser vazio")
	@Length(min=5, max=200, message="Email deve conter entre {min} e {max} caracteres")
	@Email(message="Email inválido")
	private String email;

	private Optional<String> senha = Optional.empty();
	
	private Optional<String> valorHora = Optional.empty();
	
	private Optional<String> qtdHorasTrabalhadas = Optional.empty();
	
	private Optional<String> qtdHorasAlmoco = Optional.empty();

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @param nome the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the senha
	 */
	public Optional<String> getSenha() {
		return senha;
	}

	/**
	 * @param senha the senha to set
	 */
	public void setSenha(Optional<String> senha) {
		this.senha = senha;
	}

	/**
	 * @return the valorHora
	 */
	public Optional<String> getValorHora() {
		return valorHora;
	}

	/**
	 * @param valorHora the valorHora to set
	 */
	public void setValorHora(Optional<String> valorHora) {
		this.valorHora = valorHora;
	}

	/**
	 * @return the qtdHorasTrabalhadas
	 */
	public Optional<String> getQtdHorasTrabalhadas() {
		return qtdHorasTrabalhadas;
	}

	/**
	 * @param qtdHorasTrabalhadas the qtdHorasTrabalhadas to set
	 */
	public void setQtdHorasTrabalhadas(Optional<String> qtdHorasTrabalhadas) {
		this.qtdHorasTrabalhadas = qtdHorasTrabalhadas;
	}

	/**
	 * @return the qtdHorasAlmoco
	 */
	public Optional<String> getQtdHorasAlmoco() {
		return qtdHorasAlmoco;
	}

	/**
	 * @param qtdHorasAlmoco the qtdHorasAlmoco to set
	 */
	public void setQtdHorasAlmoco(Optional<String> qtdHorasAlmoco) {
		this.qtdHorasAlmoco = qtdHorasAlmoco;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "FuncionarioDto [id=" + id + ", nome=" + nome + ", email=" + email + ", senha=" + senha + ", valorHora="
				+ valorHora + ", qtdHorasTrabalhadas=" + qtdHorasTrabalhadas + ", qtdHorasAlmoco=" + qtdHorasAlmoco
				+ "]";
	}

	
}
