package com.br.jande.pontoeletronico.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.br.jande.pontoeletronico.enums.TipoEnum;

@Entity
@Table(name="lancamento")
public class Lancamento implements Serializable{

	private static final long serialVersionUID = -2186019256220625193L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="data", nullable=false)
	private Date data;

	@Column(name="descricao", nullable=false)
	private String descricao;
	
	@Column(name="localizacao", nullable=false)
	private String localizacao;
	
	@Column(name="data_criacao", nullable=false)
	private Date dataCriacao;

	@Column(name="data_atualizacao", nullable=false)
	private Date dataAtualizacao;
	
	@Enumerated(EnumType.STRING)
	@Column(name="tipo", nullable=false)
	private TipoEnum tipo;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private Funcionario funcionario;

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
	 * @return the data
	 */
	public Date getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(Date data) {
		this.data = data;
	}

	/**
	 * @return the descricao
	 */
	public String getDescricao() {
		return descricao;
	}

	/**
	 * @param descricao the descricao to set
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	/**
	 * @return the localizacao
	 */
	public String getLocalizacao() {
		return localizacao;
	}

	/**
	 * @param localizacao the localizacao to set
	 */
	public void setLocalizacao(String localizacao) {
		this.localizacao = localizacao;
	}

	/**
	 * @return the dataCriacao
	 */
	public Date getDataCriacao() {
		return dataCriacao;
	}

	/**
	 * @param dataCriacao the dataCriacao to set
	 */
	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	/**
	 * @return the dataAtualizacao
	 */
	public Date getDataAtualizacao() {
		return dataAtualizacao;
	}

	/**
	 * @param dataAtualizacao the dataAtualizacao to set
	 */
	public void setDataAtualizacao(Date dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}

	/**
	 * @return the tipo
	 */
	public TipoEnum getTipo() {
		return tipo;
	}

	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(TipoEnum tipo) {
		this.tipo = tipo;
	}

	/**
	 * @return the funcionario
	 */
	public Funcionario getFuncionario() {
		return funcionario;
	}

	/**
	 * @param funcionario the funcionario to set
	 */
	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}
	
	@PreUpdate
	public void preUpdate(){
		dataAtualizacao = new Date();
	}
	
	@PrePersist
	public void prePersistence() {
		final Date dataAtual = new Date();
		dataCriacao = dataAtual;
		dataAtualizacao = dataAtual;
	}

	@Override
	public String toString() {
		return "Lancamento [id=" + id + ", data=" + data + ", descricao=" + descricao + ", localizacao=" + localizacao
				+ ", dataCriacao=" + dataCriacao + ", dataAtualizacao=" + dataAtualizacao + ", tipo=" + tipo
				+ ", funcionario=" + funcionario + "]";
	}

}
