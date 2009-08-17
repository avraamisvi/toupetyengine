package br.org.gamexis.plataforma.entidade.acao;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import br.org.gamexis.plataforma.cena.Colisivel;
import br.org.gamexis.plataforma.entidade.AreaAcao;
import br.org.gamexis.plataforma.script.ScriptComportamento;
import br.org.gamexis.plataforma.script.ScriptComportamentoGroovy;

public class AreaAcaoScript extends AreaAcao {
	private ScriptComportamento script;
	private Color efeitoCor = Color.white;
	
	@Override
	public void executar(Colisivel colisivel) {
		try {
			script.execute("colisao", new Object[]{colisivel.getEntidade()});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setScript(ScriptComportamento script) {
		this.script = script;
	}
	
	public void setEfeitoCor(Color efeitoCor) {
		this.efeitoCor = efeitoCor;
	}
	
	public Color getEfeitoCor() {
		return efeitoCor;
	}

	@Override
	public String getNome() {
		return null;
	}
	
	@Override
	public void desenhar(Graphics g, float fatorX, float fatorY) {
		desenhar(g);
	}
}
