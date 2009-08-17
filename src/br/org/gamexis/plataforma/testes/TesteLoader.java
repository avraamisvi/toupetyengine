package br.org.gamexis.plataforma.testes;

import org.neodatis.odb.core.Objects;
import org.neodatis.odb.core.query.IQuery;
import org.neodatis.odb.core.query.criteria.CriteriaQuery;
import org.neodatis.odb.main.ODB;
import org.neodatis.odb.main.ODBFactory;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.renderer.Renderer;

import br.org.gamexis.plataforma.motor.CarregadorConfiguracaoInicial;
import br.org.gamexis.plataforma.motor.ConfiguracaoGeral;
import br.org.gamexis.plataforma.motor.ConfiguracaoInicial;
import br.org.gamexis.plataforma.motor.RecursosFactory;

public class TesteLoader extends BasicGame {

	Paralaxer p;
	
	public TesteLoader() {
		super("teste");
	}
//<mosaico alturaCelulas="70" base="tile_fundo_cavernas_acidas.png" colunas="15" comprimentoCelulas="132" linhas="15">
	@Override
	public void init(GameContainer container) throws SlickException {
		ConfiguracaoInicial inicial = new CarregadorConfiguracaoInicial().carregarConfiguracao();
		p = new Paralaxer();
		p.d = RecursosFactory.getInstancia().getMosaico("tile_fundo_caverna_sala_1", "tile_fundo_cavernas_acidas.png", 132, 70, 15, 15);
		p.iniciar();
	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
//		RecursosFactory.getInstancia().limparBuffer();
	}

	@Override
	public void render(GameContainer container, Graphics g)
			throws SlickException {
		p.desenhar(g);
	}

	public ConfiguracaoGeral carregarConfiguracao() {
		ConfiguracaoGeral conf = null;
		try {
			ODB odb = ODBFactory.open("jogo.config");
			IQuery query = new CriteriaQuery(ConfiguracaoGeral.class);
			Objects confs = odb.getObjects(query);
			conf = (ConfiguracaoGeral) confs.getFirst();
			odb.close();
		} catch (Exception e) {
		}

		return conf;
	}
	
	public static void main(String[] args) throws SlickException {
			
			Renderer.setRenderer(Renderer.VERTEX_ARRAY_RENDERER);
			Renderer.setLineStripRenderer(Renderer.QUAD_BASED_LINE_STRIP_RENDERER);			
			AppGameContainer app = new AppGameContainer(new TesteLoader());
			app.setShowFPS(false);
			app.start();

	}
}
