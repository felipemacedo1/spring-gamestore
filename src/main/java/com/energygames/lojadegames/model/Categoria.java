package com.energygames.lojadegames.model;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tb_categoria")
public class Categoria {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "Tipo da categoria é obrigatório")
	@Size(min = 3, max = 50, message = "O tipo deve conter entre 3 e 50 caracteres")
	private String tipo;

	@Size(max = 255, message = "A descrição não pode ultrapassar 255 caracteres")
	private String descricao;

	private String icone; // URL ou referência para ícone da categoria.

	@NotNull
	@Column(columnDefinition = "boolean default true")
	private Boolean ativo;

	@Column(name = "data_criacao", updatable = false)
	private LocalDateTime dataCriacao;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "categoria", cascade = CascadeType.REMOVE)
	@JsonIgnoreProperties("categoria")
	private List<Produto> produto;

	// Construtor vazio necessário para JPA
	public Categoria() {}

	// Construtor para inicialização
	public Categoria(String tipo, String descricao) {
		this.tipo = tipo;
		this.descricao = descricao;
		this.ativo = true; // Por padrão, categoria é ativa.
		this.dataCriacao = LocalDateTime.now(); // Define a data de criação automaticamente.
	}

	// Getters e Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getIcone() {
		return icone;
	}

	public void setIcone(String icone) {
		this.icone = icone;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public LocalDateTime getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(LocalDateTime dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public List<Produto> getProduto() {
		return produto;
	}

	public void setProduto(List<Produto> produto) {
		this.produto = produto;
	}
}
