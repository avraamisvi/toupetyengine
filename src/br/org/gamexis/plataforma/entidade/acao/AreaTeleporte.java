package br.org.gamexis.plataforma.entidade.acao;

import java.util.HashMap;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import br.org.gamexis.plataforma.cena.Ator;
import br.org.gamexis.plataforma.cena.Colisivel;
import br.org.gamexis.plataforma.entidade.AreaAcao;
import br.org.gamexis.plataforma.entidade.EntidadeJogavel;
import br.org.gamexis.plataforma.entidade.TipoEntidade;
import br.org.gamexis.plataforma.motor.VariaveisAmbiente;

/**
 * Area de ação para teleporte.
 * 
 * @author abraao
 * 
 */
public class AreaTeleporte extends AreaAcao {
	
	private Color efeitoCor = Color.white;
	private static final int INDO = 0;
	private static final int CHEGOU = 1;
	private static final int PARADO = -1;

	HashMap<String, Destino> destinos = new HashMap<String, Destino>();

	@Override
	public void executar(Colisivel colisivel) {
		if (colisivel.getEntidade().getTipo().equals(TipoEntidade.jogavel)) {
			EntidadeJogavel jogavel = (EntidadeJogavel) colisivel
					.getEntidade();
			jogavel.setTeleporteHabilitado(true);
			Ator ator = (Ator) jogavel.getAtor();

			Integer estado = (Integer) VariaveisAmbiente.getValorVariavel("estado");
			if (estado == null) {
				estado = new Integer(PARADO);
				VariaveisAmbiente.inserirVariavel("estado", estado);
			}

			if (ator.getNomeAnimacao().indexOf("TELEPORTE") >= 0) {
				if (ator.getAnimacaoAtual().ultimoQuadro()) {
					if (estado.intValue() == INDO) {
						jogavel.configureAnimacao("TELEPORTE");
						Destino destino = destinos.get(VariaveisAmbiente.getValorVariavel("destino"));
						
						ator.configurePosicao(destino.x, destino.y);
						VariaveisAmbiente.inserirVariavel("estado",
								new Integer(CHEGOU));
					} else {
						jogavel.configureAnimacao("PARADO");
						jogavel.setControlavel(true);
						jogavel.setTrancarAnimacao(false);
						VariaveisAmbiente.inserirVariavel("estado",
								new Integer(PARADO));
						jogavel.setArmaVisivel(true);
						ator.setAfetadoGravidade(true);
						jogavel.setEscudoVisivel(jogavel.isUltimoEstadoEscudoVisivel());
						jogavel.limparTeleportando();
					}
				} else {					
					
					if (estado.intValue() == PARADO) {
						Destino destino = destinos.get(VariaveisAmbiente.getValorVariavel("destino"));

						if (destino != null && destino.habilitado) {
							jogavel.setControlavel(false);
							jogavel.setTrancarAnimacao(true);
							VariaveisAmbiente.inserirVariavel("estado",
									new Integer(INDO));
							
							//CONFIGURA PARA O TELEPORTE
							ator.setAfetadoGravidade(false);
							ator.setVelocidadeX(-ator.getVelocidadeX());
							ator.setVelocidadeY(-ator.getVelocidadeY());
							jogavel.setArmaVisivel(false);
							jogavel.setTrancarAnimacao(true);
							jogavel.setControlavel(false);
							jogavel.setEscudoVisivel(false);
						} else {
							//RESTAURA A ENTIDADE
							jogavel.configureAnimacao("PARADO");
							jogavel.setControlavel(true);
							jogavel.setTrancarAnimacao(false);
							ator.setAfetadoGravidade(true);
							jogavel.setArmaVisivel(true);
							VariaveisAmbiente.inserirVariavel("estado", new Integer(PARADO));
							jogavel.setEscudoVisivel(jogavel.isUltimoEstadoEscudoVisivel());
							jogavel.limparTeleportando();
						}
					}

				}
			}
		}
	}

	public void adicionaDestino(String nome, float x, float y, boolean habilitado) {
		Destino destino = new Destino();
		
		destino.x = x;
		destino.y = y;
		destino.habilitado = habilitado;
		
		destinos.put(nome, destino);
	}
	
	public void setEfeitoCor(Color efeitoCor) {
		this.efeitoCor = efeitoCor;
	}
	
	public Color getEfeitoCor() {
		return efeitoCor;
	}
	
	class Destino {
		float x;
		float y;
		boolean habilitado;
	}

	@Override
	public String getNome() {
		// TODO criar teleporte baseado em nome?
		return "AreaTelepote";
	}
	
	@Override
	public void desenhar(Graphics g, float fatorX, float fatorY) {
		desenhar(g);
	}
}
