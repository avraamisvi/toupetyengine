package br.org.gamexis.plataforma.cena.componentes;

import java.util.HashMap;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.gui.GUIContext;

import br.org.gamexis.plataforma.cena.Desenho;

/**
 * Painel para composição de menus
 * 
 * @author abraao
 * 
 */
public class Painel extends ToupetyComponente {
	private HashMap<String, ToupetyComponente> componentes = new HashMap<String, ToupetyComponente>();
	private Painel painelPai = null;
	
	public Painel(GUIContext container) {
		super(container);
		setFocavel(false);
		try {
			fonte = new AngelCodeFont("recursos/fontes/toupetyFonte.fnt",
					"recursos/fontes/toupetyFonte.png");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	Desenho fundo;
	Font fonte;

//	float x = 0;
//	float y = 0;

	boolean excluido = false;
	boolean visivel = true;

	String titulo = " ";

	@Override
	public void desenhar(Graphics g) {
		if (visivel) {
			fundo.setX(getX());
			fundo.setY(getY());
			fundo.desenhar(g);

			g.setColor(Color.black);
			float alturaCaracter = fonte.getHeight("A");
			float comprimento = fonte.getWidth(titulo);

			fonte.drawString(((fundo.getComprimento() / 2) - (comprimento / 2))
					+ getX(), (alturaCaracter / 2) + getY(), titulo);

			for (ToupetyComponente comp : componentes.values()) {
				comp.desenhar(g);
			}
		}
	}

	public void adicionarComponente(ToupetyComponente componente) {
		componente.setPai(this);
		componentes.put(componente.getNome(), componente);
	}

	public ToupetyComponente obterComponente(String nome) {
		return componentes.get(nome);
	}

	@Override
	public boolean estaVisivel() {
		return visivel;
	}

	@Override
	public boolean excluido() {
		return excluido;
	}

	@Override
	public float getAltura() {
		return fundo.getAltura();
	}

	@Override
	public float getComprimento() {
		return fundo.getComprimento();
	}

	@Override
	public Color getEfeitoCor() {
		return Color.white;
	}

//	@Override
//	public float getX() {
//		return x;
//	}
//
//	@Override
//	public float getY() {
//		return y;
//	}

	@Override
	public void setEfeitoCor(Color cor) {
	}

	@Override
	public void setVisivel(boolean visivel) {
		this.visivel = visivel;
	}

//	@Override
//	public void setX(float x) {
//		this.x = x;
//	}
//
//	@Override
//	public void setY(float y) {
//		this.y = y;
//	}

	public void setFonte(Font fonte) {
		this.fonte = fonte;
	}

	public Font getFonte() {
		return fonte;
	}

	public Desenho getFundo() {
		return fundo;
	}

	public void setFundo(Desenho fundo) {
		this.fundo = fundo;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	@Override
	public void excluir() {
		super.excluir();
		excluido = true;
		for (ToupetyComponente comp : componentes.values()) {
			comp.excluir();
		}
	}

	/**
	 * Desativa o componente impedindo-o de receber eventos.
	 */
	public void desativar() {
		if (isAtivado()) {
			super.desativar();
			for (ToupetyComponente comp : componentes.values()) {
				comp.desativar();
			}			
		}
	}

	/**
	 * Ativa um componente habilitando-o a receber eventos
	 */
	public void ativar() {
		if (!isAtivado()) {
			super.ativar();
			for (ToupetyComponente comp : componentes.values()) {
				comp.ativar();
			}			
		}
	}
	
	public Painel getPainelPai() {
		return painelPai;
	}
	
	public void setPainelPai(Painel painelPai) {
		this.painelPai = painelPai;
	}
}
