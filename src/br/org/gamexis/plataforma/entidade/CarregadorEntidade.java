package br.org.gamexis.plataforma.entidade;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import net.phys2d.raw.Body;
import net.phys2d.raw.shapes.Circle;

import org.newdawn.slick.SlickException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import br.org.gamexis.plataforma.Motor;
import br.org.gamexis.plataforma.cena.Ator;
import br.org.gamexis.plataforma.cena.AtorFaceOrientacao;
import br.org.gamexis.plataforma.cena.Desenho;
import br.org.gamexis.plataforma.cena.Efeito;
import br.org.gamexis.plataforma.cena.fisica.SuperCorpo;
import br.org.gamexis.plataforma.exception.CarregarEntidadeException;
import br.org.gamexis.plataforma.motor.RecursosFactory;
import br.org.gamexis.plataforma.motor.filesystem.FileSystemFactory;
import br.org.gamexis.plataforma.script.ScriptComportamento;

public class CarregadorEntidade {

	Entidade entidade;
	private static float FATOR_DIAMETRO_JOGAVEL = 2.5f;
	
	public Entidade carregarEntidade(String nome) {

		SAXParser parser;
		try {
			parser = SAXParserFactory.newInstance().newSAXParser();
			String caminho = "entidades/" + nome + ".def";

			CarregadorXMLHandler handler = new CarregadorXMLHandler();

			parser.parse(FileSystemFactory.getFileSystem().abrirInputStream(caminho), handler);

			return entidade;

		} catch (Exception e) {
			
			tratarExcecao(new CarregarEntidadeException(nome, e));
		}
		return null;
	}

	class CarregadorXMLHandler extends DefaultHandler {

//		private static final String RECURSOS_IMAGENS = "recursos/imagens/";

		private static final String RECURSOS_COMPORTAMENTOS = "recursos/comportamentos/";
		private static final String EFEITO_ANIMACAO = "animacao";
		private static final String EFEITO_EMISSOR = "emissor";
		
		AtorFaceOrientacao orientacao;

		float dbx, dby;
		float tirox, tiroy;
		float carregamentox = 0, carregamentoy = 0;
		boolean tagCarregamento = false;
		
		public Entidade novaPorTipo(String tipo) {
			if (tipo.equalsIgnoreCase("JOGAVEL")) {
				return new EntidadeJogavel();
			} else if (tipo.equalsIgnoreCase("PROJETIL")) {
				Projetil projetil = new Projetil();
				return projetil;
			} else if (tipo.equalsIgnoreCase("ARMA")) {
				return new EntidadeArma();
			} else if (tipo.equalsIgnoreCase("INIMIGO")) {
				return new EntidadeInimigo();
			} else if (tipo.equalsIgnoreCase("ESCUDO")) {
				return new EntidadeEscudo();
			} else if (tipo.equalsIgnoreCase("NEUTRA")) {
				return new EntidadeNeutra();
			}

			return null;
		}

		boolean converterValorBoolean(String v) {
			
			if ("SIM".equalsIgnoreCase(v)) {
				return true;
			}
			return false;
		}
		
		AtorFaceOrientacao converterFaceOrientacao(String direcao) {
			if(direcao.equalsIgnoreCase("direita")) {
				return AtorFaceOrientacao.direita;
			} else if(direcao.equalsIgnoreCase("direitaAcima")) {
				return AtorFaceOrientacao.direitaAcima;
			} else if(direcao.equalsIgnoreCase("direitaAbaixo")) {
				return AtorFaceOrientacao.direitaAbaixo;
			} else if(direcao.equalsIgnoreCase("esquerda")) {
				return AtorFaceOrientacao.esquerda;
			} else if(direcao.equalsIgnoreCase("esquerdaAcima")) {
				return AtorFaceOrientacao.esquerdaAcima;
			} else if(direcao.equalsIgnoreCase("esquerdaAbaixo")) {
				return AtorFaceOrientacao.esquerdaAbaixo;
			}
			
			return null;
		}
		
		@Override
		public void startElement(String uri, String localName, String tag,
				Attributes attributes) throws SAXException {

			if (tag.toUpperCase().equals("ENTIDADE")) {
				String tipo = attributes.getValue("tipo");
				String nome = attributes.getValue("nome");				
				String comportamento = attributes.getValue("comportamento");
				
				entidade = novaPorTipo(tipo);
				entidade.setNome(nome);
				
				if(comportamento != null) {
					ScriptComportamento script = null;
					try {
						script = RecursosFactory.getInstancia().getComportamento(comportamento);
					} catch (SlickException e) {
						e.printStackTrace();
					}
					entidade.setComportamento(script);
				}
				
				try {
					if(entidade instanceof Projetil) {
						String pstr = attributes.getValue("poder");						
						if(pstr != null) {
							int poder = Integer.parseInt(pstr);
							((Projetil)entidade).setPoder(poder);
						}
					} else if(entidade instanceof EntidadeInimigo) {
						String vstr = attributes.getValue("energiaVital");
						if(vstr != null) {
							Integer vital = Integer.parseInt(vstr);
							((EntidadeInimigo)entidade).setEnergiaVital(vital);
						}
					}
				}catch(Exception e) {
					tratarExcecao(e);
				}
								

			} if (tag.equalsIgnoreCase("ATOR")) {
				boolean proxy = converterValorBoolean(attributes.getValue("proxy"));
				String ator_nome = attributes.getValue("nome");
				AtorFaceOrientacao orientacao = converterFaceOrientacao(attributes.getValue("orientacao")); 
				Ator ator = null;
				String animacao = attributes.getValue("animacao");
				
				if(!proxy)
					ator = new CarregadorAtor().carregarAtor(ator_nome);
				else
					ator = RecursosFactory.getInstancia().getAtorProxy(ator_nome);
				
				if(animacao != null) {
					animacao = animacao.toUpperCase();
					ator.configureAnimacao(animacao);
				}
				
				
				ator.setParado(true);
				
				ator.setFace(orientacao);
				entidade.setAtor(ator);

				ator.setEntidade(entidade);
				
				if(entidade instanceof EntidadeOrientavel) {
					((EntidadeOrientavel)entidade).setFaceOrientacao(orientacao);
				}
				
				//TODO IMPLEMENTAR NO CARREGADORATOR SUPER CORPOS
				if(entidade.getTipo() == TipoEntidade.jogavel) {
					Body cpAtor = ator.getCorpo();
					
					float radius = ator.getCorpo().getShape().getBounds().getWidth() / FATOR_DIAMETRO_JOGAVEL;
					
					SuperCorpo corpo = new SuperCorpo(new Circle(radius-2), cpAtor.getMass());
					corpo.setPosition(cpAtor.getPosition().getX(),
							cpAtor.getPosition().getY());					
					corpo.setFriction(0);
					corpo.setRestitution(0);
					
					corpo.setMaxVelocX(ator.getCorpo().getMaxVelocX());
					corpo.setMaxVelocY(ator.getCorpo().getMaxVelocY());
					
					ator.setCorpo(corpo);
									
				}

			} else if (tag.toUpperCase().equals("ARMA")) {
				if (entidade.getTipo() == TipoEntidade.jogavel) {
					EntidadeJogavel jogavel = (EntidadeJogavel) entidade;
					String ator_nome = attributes.getValue("nome");

					Entidade entidadeArma = new CarregadorEntidade()
							.carregarEntidade(ator_nome);

					jogavel.adicionarArma((EntidadeArma) entidadeArma);
					
					if(jogavel.getArma() == null)
						jogavel.setArma((EntidadeArma) entidadeArma);

					Ator ator = (Ator) jogavel.getArma().getAtor();
					ator.configureAnimacao("PARADO_DIREITA");
					ator.setParado(true);

					ator.setFace(AtorFaceOrientacao.direita);
					// ator.setEntidade(entidade);

				}
			} else if (tag.toUpperCase().equals("PROJETIL")) {
				if (entidade.getTipo() == TipoEntidade.arma) {
					if (!tagCarregamento) {
						EntidadeArma arma = (EntidadeArma) entidade;
						String ent_nome = attributes.getValue("nome");						
						int poder = Integer.parseInt(attributes.getValue("poder"));
						
						Projetil entidadeProjetil = (Projetil)new CarregadorEntidade()
								.carregarEntidade(ent_nome);
						arma.setProjetil((Projetil) entidadeProjetil);						
						entidadeProjetil.setPoder(poder);
						
						Ator ator = (Ator) arma.getProjetil().getAtor();
						ator.configureAnimacao("DISPARANDO_DIREITA");
						ator.setParado(true);

						ator.setFace(AtorFaceOrientacao.direita);
						ator.setEntidade(entidade);
					} else {
						String projetil = attributes.getValue("nome");
						int poder = Integer.parseInt(attributes.getValue("poder"));
						
						Projetil entidadeProjetil = (Projetil)new CarregadorEntidade()
								.carregarEntidade(projetil);
						entidadeProjetil.setPoder(poder);
						
						EntidadeArma arma = (EntidadeArma) entidade;
						arma.adicionarProjeteisAcumulado(entidadeProjetil);
					}
				}
			} else if (tag.toUpperCase().equals("CARREGAMENTO")) {
				if (entidade.getTipo() == TipoEntidade.arma) {
					int maximo = Integer.parseInt(attributes.getValue("maximo"));
					int tempoAcumular = Integer.parseInt(attributes.getValue("tempoAcumular"));
					tagCarregamento = true;
					
					EntidadeArma arma = (EntidadeArma) entidade;
					arma.setMaximoNivelAcumulador(maximo);
					arma.setTempoAumentarNivel(tempoAcumular);

				}
			} else if (tag.toUpperCase().equals("VELOCIDADE")) {
				if (entidade.getTipo() == TipoEntidade.projetil) {
					Projetil projetil = (Projetil) entidade;
					projetil.setVelocidade(Float.parseFloat(attributes
							.getValue("valor")));
				}
			} else if (tag.toUpperCase().equals("DIREITA")) {
				orientacao = AtorFaceOrientacao.direita;
			} else if (tag.toUpperCase().equals("DIREITAACIMA")) {
				orientacao = AtorFaceOrientacao.direitaAcima;
			} else if (tag.toUpperCase().equals("DIREITAABAIXO")) {
				orientacao = AtorFaceOrientacao.direitaAbaixo;
			} else if (tag.toUpperCase().equals("ESQUERDA")) {
				orientacao = AtorFaceOrientacao.esquerda;
			} else if (tag.toUpperCase().equals("ESQUERDAABAIXO")) {
				orientacao = AtorFaceOrientacao.esquerdaAbaixo;
			} else if (tag.toUpperCase().equals("ESQUERDAACIMA")) {
				orientacao = AtorFaceOrientacao.esquerdaAcima;
			} else if (tag.toUpperCase().equals("BRILHO")) {
				dbx = Float.parseFloat(attributes.getValue("deslocX"));
				dby = Float.parseFloat(attributes.getValue("deslocY"));

			} else if (tag.equalsIgnoreCase("ANIMACAOBRILHO")) {
				String base = attributes.getValue("base");
				int duracao = Integer.parseInt(attributes.getValue("duracao"));
				int comprimento = Integer.parseInt(attributes.getValue("comprimento"));
				int altura = Integer.parseInt(attributes.getValue("altura"));
				int quadros = Integer.parseInt(attributes.getValue("quadros"));
				
				try {
					Efeito an = new Efeito((Desenho) entidade.getAtor(),
							base, comprimento, altura, 0, 0, quadros, false, duracao);
					((EntidadeArma)entidade).setAnimacaoBrilho(an);
				} catch (SlickException e) {
					tratarExcecao(e);
				}

			} else if (tag.toUpperCase().equals("TIRO")) {
				tirox = Float.parseFloat(attributes.getValue("deslocX"));
				tiroy = Float.parseFloat(attributes.getValue("deslocY"));
			}  else if (tag.equalsIgnoreCase("CARREGANDO")) {
				carregamentox = Float.parseFloat(attributes.getValue("deslocX"));
				carregamentoy = Float.parseFloat(attributes.getValue("deslocY"));
			} else if (tag.equalsIgnoreCase("ESCUDO")) {
				if (entidade.getTipo() == TipoEntidade.jogavel) {
					EntidadeJogavel jogavel = (EntidadeJogavel) entidade;
					String ator_nome = attributes.getValue("nome");

					EntidadeEscudo entidadeEscudo = (EntidadeEscudo) new CarregadorEntidade()
							.carregarEntidade(ator_nome);
					jogavel.setEscudo((EntidadeEscudo) entidadeEscudo);
					entidadeEscudo.setJogavel(jogavel);

					Ator ator = (Ator) jogavel.getEscudo().getAtor();
					ator.configureAnimacao("ATIVADO_DIREITA");
					ator.setParado(true);
					ator.setAfetadoGravidade(false);

					ator.setFace(AtorFaceOrientacao.direita);
					ator.setEntidade(entidadeEscudo);

				}
			}else
			if (tag.equalsIgnoreCase("EXPLOSAO")) {
				
				String tipo = attributes.getValue("tipo");
				String base = attributes.getValue("base");
				
				if(tipo == null || tipo.equalsIgnoreCase(EFEITO_ANIMACAO)) {
					int coluna = Integer.parseInt(attributes.getValue("coluna"));
					int linha = Integer.parseInt(attributes.getValue("linha"));
					int quadros = Integer.parseInt(attributes.getValue("quadros"));
					int comprimento = Integer.parseInt(attributes
							.getValue("comprimento"));
					int altura = Integer.parseInt(attributes.getValue("altura"));
					boolean repetir = converterValorBoolean(attributes
							.getValue("repetir"));
					int velocidade = Integer.parseInt(attributes
							.getValue("velocidade"));
	
					Projetil projetil = (Projetil) entidade;
					projetil.configureExplosao(base, Projetil.TipoExplosao.ANIMACAO, altura, coluna, comprimento, linha, quadros, repetir, velocidade, 0);
//					try {
//						Efeito efeito = new Efeito(null, base, comprimento, altura, linha, coluna,
//								quadros, repetir, velocidade);
//						projetil.setExplosao(efeito);
//					} catch (Exception e) {
//						tratarExcecao(e);
//					}
				} else if(tipo.equalsIgnoreCase(EFEITO_EMISSOR)) {
					Projetil projetil = (Projetil) entidade;
					int duracao = Integer.parseInt(attributes.getValue("duracao"));
					projetil.configureExplosao(base, Projetil.TipoExplosao.EMISSOR, 0, 0, 0, 0, 0, false, 0, duracao);
					
//					try {
//						projetil.setExplosao(RecursosFactory.
//								getInstancia().getEfeitoEmissor(base, null, duracao));
//					} catch (IOException e) {
//						tratarExcecao(e);
//					} catch (SlickException e) {
//						tratarExcecao(e);
//					}
				}
			} else if (tag.equalsIgnoreCase("EFEITO")) {				
				String base = attributes.getValue("base");
				int coluna = Integer.parseInt(attributes.getValue("coluna"));
				int linha = Integer.parseInt(attributes.getValue("linha"));
				int quadros = Integer.parseInt(attributes.getValue("quadros"));
				int comprimento = Integer.parseInt(attributes
						.getValue("comprimento"));
				int altura = Integer.parseInt(attributes.getValue("altura"));
				boolean repetir = converterValorBoolean(attributes
						.getValue("repetir"));
				int velocidade = Integer.parseInt(attributes
						.getValue("velocidade"));

				try {
					Efeito efeito = new Efeito(null, base, comprimento, altura, linha, coluna,
							quadros, repetir, velocidade);
					EntidadeArma arma = (EntidadeArma) entidade;
					arma.adicionarEfeitoCarregamento(efeito);					
				} catch (Exception e) {
					tratarExcecao(e);
				}
			} else if (tag.equalsIgnoreCase("DISTANCIA_MAXIMA")) {
				int valor = Integer.parseInt(attributes.getValue("valor"));
				if(entidade.getTipo().equals(TipoEntidade.projetil)) {
					((Projetil)entidade).setDistanciaMaxima(valor);
				}
			} else if (tag.equalsIgnoreCase("PULO")) {
				long maximo = Long.parseLong(attributes.getValue("maximo"));
				float impulso = Float.parseFloat(attributes.getValue("impulso"));
				
				if(entidade.getTipo().equals(TipoEntidade.jogavel)) {
					((EntidadeJogavel)entidade).setAlturaMaximoPuloIncrementar(maximo);
					((EntidadeJogavel)entidade).setImpulsoPulo(impulso);
				}
			}
		}

		@Override
		public void endElement(String uri, String localName, String tag)
				throws SAXException {

			if (tag.toUpperCase().equals("DIREITA")) {

				EntidadeArma arma = (EntidadeArma) entidade;
				arma.configureOrientacaoDireita(dbx, dby, tirox, tiroy,
						carregamentox, carregamentoy);
			} else if (tag.toUpperCase().equals("DIREITAACIMA")) {

				EntidadeArma arma = (EntidadeArma) entidade;
				arma.configureOrientacaoDireitaAcima(dbx, dby, tirox, tiroy,
						carregamentox, carregamentoy);
			} else if (tag.toUpperCase().equals("DIREITAABAIXO")) {

				EntidadeArma arma = (EntidadeArma) entidade;
				arma.configureOrientacaoDireitaAbaixo(dbx, dby, tirox, tiroy,
						carregamentox, carregamentoy);
			} else if (tag.equalsIgnoreCase("ESQUERDA")) {

				EntidadeArma arma = (EntidadeArma) entidade;
				arma.configureOrientacaoEsquerda(dbx, dby, tirox, tiroy,
						carregamentox, carregamentoy);
			} else if (tag.toUpperCase().equals("ESQUERDAABAIXO")) {

				EntidadeArma arma = (EntidadeArma) entidade;
				arma.configureOrientacaoEsquerdaAbaixo(dbx, dby, tirox, tiroy,
						carregamentox, carregamentoy);
			} else if (tag.toUpperCase().equals("ESQUERDAACIMA")) {

				EntidadeArma arma = (EntidadeArma) entidade;
				arma.configureOrientacaoEsquerdaAcima(dbx, dby, tirox, tiroy,
						carregamentox, carregamentoy);
			} else if (tag.toUpperCase().equals("CARREGAMENTO")) {
				tagCarregamento = false;
			}
		}

	}
	
	public void tratarExcecao(Throwable cause) {
		Motor.obterInstancia().tratarExcecao(cause);
	}
}
