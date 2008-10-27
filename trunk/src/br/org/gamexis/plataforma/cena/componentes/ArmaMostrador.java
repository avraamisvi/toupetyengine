package br.org.gamexis.plataforma.cena.componentes;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import br.org.gamexis.plataforma.cena.Desenho;
import br.org.gamexis.plataforma.entidade.logestado.ArmaSelecionada;
import br.org.gamexis.plataforma.entidade.logestado.Municao;

public class ArmaMostrador implements Desenho {
	
	private Color efeitoCor = Color.white;
	private Image fundo;
	private boolean municaoInfinita;
	private boolean visivel = true;
	private float x, y;
	private ArmaSelecionada arma;
	private float textoX;
	private float textoY;
	
	public void setFundo(Image fundo) {
		this.fundo = fundo;
	}
	
	public void setArma(ArmaSelecionada arma) {
		this.arma = arma;
	}	
	
	public boolean isMunicaoInfinita() {
		return municaoInfinita;
	}

	public void setMunicaoInfinita(boolean municaoInfinita) {
		this.municaoInfinita = municaoInfinita;
	}

	public int getMunicaoMaxima() {
		return arma.getMunicao().getMaximo();
	}

	public void setMunicaoMaxima(int municaoMaxima) {
		arma.getMunicao().setMaximo(municaoMaxima);
	}

	public int getMunicaoAtual() {
		return arma.getMunicao().getMunicao();
	}

	public void decrementeMunicao(int dec) {
		arma.getMunicao().decrementeMunicao(dec);
	}

	public void incrementeMunicao(int inc) {
		arma.getMunicao().incrementeMunicao(inc);
	}
	
	public Image getFundo() {
		return fundo;
	}

	public ArmaSelecionada getArma() {
		return arma;
	}

	@Override
	public void desenhar(Graphics g) {
		fundo.draw(x, y);
		
		arma.getIcone().setX(x+arma.getDeslocamentoX());
		arma.getIcone().setY(y+arma.getDeslocamentoY());
		
		arma.getIcone().desenhar(g);
		if(arma.getMunicao().getMaximo() == Municao.INFINITO) {
			g.setColor(Color.green);
			
			int wTxt = g.getFont().getWidth("---");
			float deslocX = Math.abs(((wTxt-fundo.getWidth())/2));
			
			g.drawString("---", x+deslocX, y+arma.getIcone().getAltura());
		} else {
			g.setColor(Color.green);
			int wTxt = g.getFont().getWidth(""+getMunicaoAtual());
			float deslocX = Math.abs(((wTxt-fundo.getWidth())/2));
			
			g.drawString(""+getMunicaoAtual(), x+deslocX, y+arma.getIcone().getAltura() + 5);
		}
	}

	@Override
	public void excluir() {
	}

	@Override
	public boolean excluido() {
		return false;
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
	public void setX(float x) {
		this.x = x;
	}

	@Override
	public void setY(float y) {
		this.y = y;
	}
	
	public void atualizar() {
	}

	@Override
	public float getAltura() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getComprimento() {
		return 0;
	}

	@Override
	public boolean estaVisivel() {
		return visivel;
	}

	@Override
	public void setVisivel(boolean visivel) {
		this.visivel = visivel;
	}
	
	public void setEfeitoCor(Color efeitoCor) {
		this.efeitoCor = efeitoCor;
	}
	
	public Color getEfeitoCor() {
		return efeitoCor;
	}

	@Override
	public String getNome() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isDisposed() {
		// TODO Auto-generated method stub
		return false;
	}
}
