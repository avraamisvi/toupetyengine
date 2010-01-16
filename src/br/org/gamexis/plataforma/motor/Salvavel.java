package br.org.gamexis.plataforma.motor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Representa um objeto que pode ser salvo
 * @author abraao
 *
 */
public class Salvavel {
	private Integer identificador;
	private Date data;
	private String descricao;
	private HashMap<String, Object> variaveis = new HashMap<String, Object>(); 
	private String cena;
	private Float jogavelX; 
	private Float jogavelY;
	
	public Integer getIdentificador() {
		return identificador;
	}
	public void setIdentificador(Integer identificador) {
		this.identificador = identificador;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public void setVariaveis(HashMap<String, Object> variaveis) {
		this.variaveis = variaveis;
	}
	
	public void setVariavel(String nome, Object valor) {
		variaveis.put(nome, valor);
	}
	
	public Object getValorVariavel(String nome) {
		return variaveis.get(nome);
	}
	
	public String getDataFormatada() {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");		
		return format.format(data);
	}
	
	public String getHorarioFormatado() {
		SimpleDateFormat format = new SimpleDateFormat("hh:mm");		
		return format.format(data);
	}
	
	public String getDataHoraFormatada() {
		SimpleDateFormat spd = (SimpleDateFormat)SimpleDateFormat.getInstance();		
		String dataFormatada = spd.format(data);
		
		return dataFormatada;
	}
	
	public String getCena() {
		return cena;
	}
	
	public void setCena(String cena) {
		this.cena = cena;
	}
	public Float getJogavelX() {
		return jogavelX;
	}
	public void setJogavelX(Float jogavelX) {
		this.jogavelX = jogavelX;
	}
	public Float getJogavelY() {
		return jogavelY;
	}
	public void setJogavelY(Float jogavelY) {
		this.jogavelY = jogavelY;
	}
	public HashMap<String, Object> getVariaveis() {
		return variaveis;
	}
	
	
}
