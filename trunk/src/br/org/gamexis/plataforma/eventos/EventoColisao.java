package br.org.gamexis.plataforma.eventos;

import net.phys2d.math.ROVector2f;
import br.org.gamexis.plataforma.Motor;
import br.org.gamexis.plataforma.cena.Cena;
import br.org.gamexis.plataforma.entidade.Entidade;

/**
 * Evento de colisão.
 * 
 * @author abraao
 *
 */
public class EventoColisao implements Evento {
	private Entidade fonte;
	private float normalX;
	private float normalY;
	private ROVector2f pontoColisao;
	private Cena cena;
	
	public EventoColisao(Entidade fonte, Cena cena) {
		this.fonte = fonte;
		this.cena = cena;
	}
	
	public Entidade getFonte() {
		return fonte;
	}

	public float getNormalX() {
		return normalX;
	}

	public void setNormalX(float normalX) {
		this.normalX = normalX;
	}

	public float getNormalY() {
		return normalY;
	}

	public void setNormalY(float normalY) {
		this.normalY = normalY;
	}
	
	public void setPontoColisao(ROVector2f pontoColisao) {
		this.pontoColisao = pontoColisao;
	}
	
	public ROVector2f getPontoColisao() {
		return pontoColisao;
	}

	@Override
	public Cena getCena() {
		return cena;
	}
	
	@Override
	public Motor getMotor() {
		return Motor.obterInstancia();
	}
}
