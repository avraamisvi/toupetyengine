package br.org.gamexis.plataforma.cena.componentes;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.gui.GUIContext;

import br.org.gamexis.plataforma.Motor;
import br.org.gamexis.plataforma.cena.componentes.util.Caixa;
import br.org.gamexis.plataforma.cena.componentes.util.CaixaShape;
import br.org.gamexis.plataforma.motor.EntradaPerifericos;
import br.org.gamexis.plataforma.motor.RecursosFactory;
import br.org.gamexis.plataforma.motor.configuracao.Controles;

public class BalaoTexto extends ToupetyComponente {

	public BalaoTexto(GUIContext container) {
		super(container);

		try {
			fonte = RecursosFactory.getInstancia().getFonte("toupetyFonte");

			// face = new Image("recursos/imagens/faceToupety.png");

		} catch (Exception e) {
			e.printStackTrace();
		}
		caixa = new CaixaShape();
	}

	String nome;
	Caixa caixa;
	Font fonte;

	Image face;
	float faceDeslocamentoX = 20;
	float faceDeslocamentoY = -100;

	float x = 50;
	float y = 200;

	boolean excluido = false;
	boolean visivel = true;

	String texto = " ";

	long maximoIntervalo = 5;
	long intervalo = 0;
	long contador = 5;

	long bytesLidos = 0;
	int beginIndex = 0;
	int endIndex = 0;

	@Override
	public void desenhar(Graphics g) {
		if (visivel && !excluido) {
			caixa.setX(x);
			caixa.setY(y);
			caixa.desenhar(g);
			if (face != null)
				face.draw(x + faceDeslocamentoX, y + faceDeslocamentoY);

			contador++;
			if ((contador >= maximoIntervalo) && !(endIndex > texto.length())) {

				endIndex++;
				contador = 0;
			}

			if (endIndex >= texto.length()) {
				endIndex = texto.length() - 1;
			}

			g.setColor(Color.black);
			String linhas[] = texto.substring(0, endIndex).split("\\n");

			float alturaCaracter = fonte.getHeight("A") + 5;
			float yt = y + caixa.getMargemHorizontal();
			for (String str : linhas) {
				fonte.drawString(x + caixa.getMargemVertical(), yt, str,
						Color.white);
				yt = yt + alturaCaracter;
			}
		}
	}

	@Override
	public boolean estaVisivel() {
		return visivel;
	}

	@Override
	public void excluir() {
		excluido = true;
		super.excluir();
	}

	@Override
	public boolean excluido() {
		return excluido;
	}

	@Override
	public float getAltura() {
		return caixa.getAltura();
	}

	@Override
	public float getComprimento() {
		return caixa.getComprimento();
	}

	@Override
	public Color getEfeitoCor() {
		return Color.white;
	}

	@Override
	public String getNome() {
		return nome;
	}

	@Override
	public float getX() {
		return x;
	}

	@Override
	public float getY() {
		return y;
	}

	@Override
	public void setEfeitoCor(Color cor) {
	}

	@Override
	public void setVisivel(boolean visivel) {
		this.visivel = visivel;
	}

	@Override
	public void setX(float x) {
		this.x = x;
	}

	@Override
	public void setY(float y) {
		this.y = y;
	}

	public void setFonte(Font fonte) {
		this.fonte = fonte;
	}

	public Font getFonte() {
		return fonte;
	}

	private void botaoPressionado() {
		if (!excluido) {

			int indice = texto.substring(endIndex).indexOf('\n');

			if (indice <= 0)
				endIndex = texto.length() - 1;
			else
				endIndex += indice;

			if (texto.length() == 0 || endIndex >= texto.length() - 1) {
				endIndex = texto.length() - 1;
				if (getComportamento() != null) {

					try {
						getComportamento().execute("fimTexto",
								new Object[] { this, Motor.obterInstancia() });
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public void reiniciar() {
		endIndex = 0;
		beginIndex = 0;
		contador = 10;
		bytesLidos = 0;
	}

	public Caixa getCaixa() {
		return caixa;
	}

	public void setCaixa(Caixa caixa) {
		this.caixa = caixa;
	}

	public Image getFace() {
		return face;
	}

	public void setFace(Image face) {
		this.face = face;
	}

	public float getFaceDeslocamentoX() {
		return faceDeslocamentoX;
	}

	public void setFaceDeslocamentoX(float faceDeslocamentoX) {
		this.faceDeslocamentoX = faceDeslocamentoX;
	}

	public float getFaceDeslocamentoY() {
		return faceDeslocamentoY;
	}

	public void setFaceDeslocamentoY(float faceDeslocamentoY) {
		this.faceDeslocamentoY = faceDeslocamentoY;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public long getMaximoIntervalo() {
		return maximoIntervalo;
	}

	public void setMaximoIntervalo(long maximoIntervalo) {
		this.maximoIntervalo = maximoIntervalo;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public String getTexto() {
		return texto;
	}

	@Override
	public void desenhar(Graphics g, float fatorX, float fatorY) {
		// TODO ainda nao suportado
		desenhar(g);
	}

	//TODO REVER
	public int getAlvo() {
		return Controles.BAIXO;
	}

	@Override
	public void controllerButtonReleased(int controller, int button) {
		if(EntradaPerifericos.getInstancia().getControle().isTecladoSelecionado())
			return;
		
		int chave = EntradaPerifericos.getInstancia().getControle().traduzir(button);
		
		if(chave == getAlvo()) {
			botaoPressionado();
		}
	}

	@Override
	public void controllerDownReleased(int controller) {
		if(EntradaPerifericos.getInstancia().getControle().isTecladoSelecionado())
			return;
		
		if(getAlvo() == Controles.BAIXO) {
			botaoPressionado();
		}
	}

	@Override
	public void controllerLeftReleased(int controller) {
		if(EntradaPerifericos.getInstancia().getControle().isTecladoSelecionado())
			return;
		
		if(getAlvo() == Controles.ESQUERDA) {
			botaoPressionado();
		}
	}

	@Override
	public void controllerRightReleased(int controller) {
		if(EntradaPerifericos.getInstancia().getControle().isTecladoSelecionado())
			return;
		
		if(getAlvo() == Controles.DIREITA) {
			botaoPressionado();
		}
	}

	@Override
	public void controllerUpReleased(int controller) {
		if(EntradaPerifericos.getInstancia().getControle().isTecladoSelecionado())
			return;
		
		if(getAlvo() == Controles.CIMA) {
			botaoPressionado();
		}
	}

	@Override
	public boolean isAcceptingInput() {
		return true;
	}


	@Override
	public void keyReleased(int key, char c) {
		if(!EntradaPerifericos.getInstancia().getControle().isTecladoSelecionado())
			return;
		
		int chave = EntradaPerifericos.getInstancia().getControle().traduzir(key);
		
		if(getAlvo() == chave) {
			botaoPressionado();
		}
	}
}
