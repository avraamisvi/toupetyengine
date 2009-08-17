package br.org.gamexis.plataforma.cena;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import net.phys2d.math.Vector2f;
import net.phys2d.raw.Body;
import net.phys2d.raw.StaticBody;
import net.phys2d.raw.shapes.Box;
import net.phys2d.raw.shapes.Circle;
import net.phys2d.raw.shapes.Line;
import net.phys2d.raw.shapes.Polygon;
import net.phys2d.raw.shapes.Shape;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.particles.ParticleIO;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import br.org.gamexis.plataforma.EntidadePlataforma;
import br.org.gamexis.plataforma.Motor;
import br.org.gamexis.plataforma.cena.componentes.PainelEstado;
import br.org.gamexis.plataforma.cena.fisica.RetangularPlataformaCorpo;
import br.org.gamexis.plataforma.cena.fisica.SuperCorpo;
import br.org.gamexis.plataforma.cena.materiais.Paralaxer;
import br.org.gamexis.plataforma.entidade.CarregadorEntidade;
import br.org.gamexis.plataforma.entidade.CarregadorPainelEstadoCenario;
import br.org.gamexis.plataforma.entidade.Entidade;
import br.org.gamexis.plataforma.entidade.EntidadeJogavel;
import br.org.gamexis.plataforma.entidade.acao.AreaAcaoNegativa;
import br.org.gamexis.plataforma.entidade.acao.AreaAcaoPortal;
import br.org.gamexis.plataforma.entidade.acao.AreaAcaoScript;
import br.org.gamexis.plataforma.entidade.acao.AreaTeleporte;
import br.org.gamexis.plataforma.motor.RecursosFactory;
import br.org.gamexis.plataforma.motor.filesystem.FileSystemFactory;
import br.org.gamexis.plataforma.script.ScriptComportamento;
import br.org.gamexis.plataforma.testes.ParalaxeTeste;

/**
 * Carrega uma cena.
 * 
 * @author abraao
 * 
 */
public class CarregadorCenario {

	Cena cena;

	public Cena carregarCena(String nome) {

		cena = null;

		SAXParser parser;
		try {
			parser = SAXParserFactory.newInstance().newSAXParser();
			String caminho = "cenas/" + nome + ".def";

			CarregadorXMLHandler handler = new CarregadorXMLHandler();

			parser.parse(FileSystemFactory.getFileSystem().abrirInputStream(caminho), handler);
			
			return cena;

		} catch (Throwable e) {
			Motor.obterInstancia().tratarExcecao(e);
		}
		return null;
	}

	class CarregadorXMLHandler extends DefaultHandler {		
		boolean construindoPlataforma = false;
		String platIdentificador;
		float platX;
		float platY;
		float atrito = 0;

		boolean construindoPoligono = false;
		Shape shape;
		int vectorIndex;
		Vector2f v[];
		AreaTeleporte areaTeleporte;
		Entidade ultimaEntidade;
		
		public int parseExclusaoNiveis(String niveis) {
			int exclui = 0;
			if (niveis != null) {
				String[] lstNiveis = niveis.split(",");

				for (String string : lstNiveis) {
					exclui |= parseNivel(string);
				}
			}
			return exclui;
		}

		public int parseNivel(String string) {
			if ("fundo".equalsIgnoreCase(string)) {
				return NivelCena.fundo;
			} else if ("frente".equalsIgnoreCase(string)) {
				return NivelCena.frente;
			} else if ("meio".equalsIgnoreCase(string)) {
				return NivelCena.meio;
			} else if ("animados".equalsIgnoreCase(string)) {
				return NivelCena.animados;
			} else if ("plataforma".equalsIgnoreCase(string)) {
				return NivelCena.plataforma;
			} else if ("fixosFrente".equalsIgnoreCase(string)) {
				return NivelCena.fixosFrente;
			} else if ("fixosFundo".equalsIgnoreCase(string)) {
				return NivelCena.fixosFundo;
			}

			return NivelCena.fundo;
		}

		@Override
		public void startElement(String uri, String localName, String tag,
				Attributes attributes) throws SAXException {

			if (tag.equalsIgnoreCase("CENARIO")) {
				int comprimento = Integer.parseInt(attributes
						.getValue("comprimento"));
				String nome = attributes.getValue("nome");
				String comportamento = attributes.getValue("comportamento");

				int altura = Integer.parseInt(attributes.getValue("altura"));
				cena = new Cenario(comprimento, altura);
				PainelEstado painel = Motor.obterInstancia().getPainelEstado();
				
				if(painel == null) {
					painel = new CarregadorPainelEstadoCenario().
						carregarPainelEstadoCenario(attributes.getValue("painelEstado"));
					((Cenario) cena).setPainelEstado(painel);
					Motor.obterInstancia().setPainelEstado(painel);
				} else {
					((Cenario) cena).setPainelEstado(painel);					
				}

				if (comportamento != null)
					try {
						cena.setComportamento(RecursosFactory.getInstancia().getComportamento(comportamento));
					} catch (SlickException e) {
						e.printStackTrace();
					}

				cena.setNome(nome);

			}  else if (tag.equalsIgnoreCase("CORFUNDO")) {
				float r = Float.parseFloat(attributes.getValue("r"));
				float g = Float.parseFloat(attributes.getValue("g"));
				float b = Float.parseFloat(attributes.getValue("b"));
				
				((CenaBase) cena).setCorFundo(r, g, b);
			} else if (tag.equalsIgnoreCase("CAMERA")) {
				float faty = Float.parseFloat(attributes.getValue("fatorY"));
				float fatx = Float.parseFloat(attributes.getValue("fatorX"));
				float fatzx = Float.parseFloat(attributes.getValue("fatorZX"));
				float fatzy = Float.parseFloat(attributes.getValue("fatorZY"));
				
				
				((Cenario) cena).setCameraXFator(fatx);				
				((Cenario) cena).setCameraYFator(faty);
				((Cenario) cena).setFatorZX(fatzx);
				((Cenario) cena).setFatorZY(fatzy);				

				try {
					float minimox = Float.parseFloat(attributes.getValue("minimox"));
					float minimoy = Float.parseFloat(attributes.getValue("minimoy"));
					float maximox = Float.parseFloat(attributes.getValue("maximox"));
					float maximoy = Float.parseFloat(attributes.getValue("maximoy"));
					float x = Float.parseFloat(attributes.getValue("x"));
					float y = Float.parseFloat(attributes.getValue("y"));
					
					((Cenario) cena).setCameraY(y);
					((Cenario) cena).setCameraX(x);
					((Cenario) cena).setCameraXMinimo(minimox);
					((Cenario) cena).setCameraXMaximo(maximox);
					((Cenario) cena).setCameraYMinimo(minimoy);
					((Cenario) cena).setCameraYMaximo(maximoy);
				} catch(Exception e){
					//cause old versions
				}
				
			} else if (tag.equalsIgnoreCase("CENAESTATICA")) {// CENA ESTATICA
				String comportamento = attributes.getValue("comportamento");
				cena = new CenaEstatica();
				String nome = attributes.getValue("nome");
				if (comportamento != null)
					try {
						cena.setComportamento(RecursosFactory.getInstancia().getComportamento(comportamento));
					} catch (SlickException e) {
						e.printStackTrace();
					}

				cena.setNome(nome);

			} else if (tag.toUpperCase().equals("PLATAFORMA")) {
				platX = Float.parseFloat(attributes.getValue("x"));
				platY = Float.parseFloat(attributes.getValue("y"));
				atrito = Float.parseFloat(attributes.getValue("atrito"));
				platIdentificador = attributes.getValue("identificador");

				construindoPlataforma = true;
			} else if (tag.toUpperCase().equals("POLIGONO")) {
				int pontos = Integer.parseInt(attributes.getValue("pontos"));
				v = new Vector2f[pontos];
				vectorIndex = 0;

				construindoPoligono = true;
			} else if (tag.toUpperCase().equals("LINHA")) {
				float x1 = Float.parseFloat(attributes.getValue("x1"));
				float y1 = Float.parseFloat(attributes.getValue("y1"));
				float x2 = Float.parseFloat(attributes.getValue("x2"));
				float y2 = Float.parseFloat(attributes.getValue("y2"));

				shape = new Line(x1, y1, x2, y2);
				platX = platX - (x2 - x1) / 2;
				platY = platY - (y2 - y1) / 2;

			} else if (tag.toUpperCase().equals("CAIXA")) {
				float comp = Float.parseFloat(attributes
						.getValue("comprimento"));
				float alt = Float.parseFloat(attributes.getValue("altura"));

				shape = new Box(comp, alt);

			} else if (tag.toUpperCase().equals("CIRCULO")) {
				float raio = Float.parseFloat(attributes.getValue("raio"));

				shape = new Circle(raio);

			} else if (tag.toUpperCase().equals("PONTO")) {
				float x = Float.parseFloat(attributes.getValue("x"));
				float y = Float.parseFloat(attributes.getValue("y"));

				v[vectorIndex] = new Vector2f(x, y);
				vectorIndex++;
			} else if (tag.toUpperCase().equals("JOGAVEL")) {// TODO
				// FABRICA_ENTIDADES
				float x = Float.parseFloat(attributes.getValue("x"));
				float y = Float.parseFloat(attributes.getValue("y"));
				String nome = attributes.getValue("nome");
				
				Entidade ent = null;
				if (Motor.obterInstancia().getJogavelCarregado() == null) {
					ent = new CarregadorEntidade().carregarEntidade(nome);
					Motor.obterInstancia().setJogavelCarregado(
							(EntidadeJogavel) ent);
					
					// TODO COLOCAR NO ARQUIVO DE DEFINICAO
					((EntidadeJogavel) ent)
					.setFaceOrientacao(AtorFaceOrientacao.direita);
				} else {
					ent = Motor.obterInstancia().getJogavelCarregado();
					ent.setIdentificador("JOGAVEL");
				}

				Ator ator = (Ator) ent.getAtor();
				if (ator.getCorpo() instanceof SuperCorpo) {
					((SuperCorpo) (ator.getCorpo())).moveAll(x, y);
				} else {
					ator.getCorpo().move(x, y);
				}

				((Cenario) cena).setJogavel(ator);


			} else if (tag.toUpperCase().equals("ENTIDADE")) {
				float x = Float.parseFloat(attributes.getValue("x"));
				float y = Float.parseFloat(attributes.getValue("y"));
				String nome = attributes.getValue("definicao");
				String identificador = attributes.getValue("identificador");
				int nivel = parseNivel(attributes.getValue("nivel"));
				String comportamento = attributes.getValue("comportamento");
				String excluidos = attributes.getValue("excluidos");

				Entidade ent = new CarregadorEntidade().carregarEntidade(nome);
				Ator ator = (Ator) ent.getAtor();
				ator.getCorpo().setPosition(x, y);
				ent.setIdentificador(identificador);
				
				//ULTIMA ENTIDADE(PARA PARAMETROS)
				ultimaEntidade = ent;
				
				if (comportamento != null && comportamento.length() > 0) {
					try {
						ent.setComportamento(RecursosFactory.getInstancia().getComportamento(comportamento));
					} catch (SlickException e) {
						e.printStackTrace();
					}
				}

				if ("todos".equalsIgnoreCase(excluidos)) {// SEM CORPO
					((Cenario) cena).adicionarAtor(ator, nivel, 0, true);
				} else {
					((Cenario) cena).adicionarAtor(ator, nivel,
							parseExclusaoNiveis(excluidos));
				}

			}  else if (tag.toUpperCase().equals("PARAMETRO")) {
				if(ultimaEntidade != null) {
					String nome = attributes.getValue("nome");
					String tipo = attributes.getValue("tipo");
					String valorStr = attributes.getValue("valor");
					Object valor = null;
						
					if(tipo.equalsIgnoreCase("int")) {
						valor = Integer.parseInt(valorStr);
					} else if(tipo.equalsIgnoreCase("float")) {
						valor = Float.parseFloat(valorStr);
					} else if(tipo.equalsIgnoreCase("double")) {
						valor = Double.parseDouble(valorStr);
					} else if(tipo.equalsIgnoreCase("boolean")) {
						valor = Boolean.parseBoolean(valorStr);
					} else if(tipo.equalsIgnoreCase("long")) {
						valor = Long.parseLong(valorStr);
					} else if(tipo.equalsIgnoreCase("string")) {
						valor = valorStr;
					}
					
					ultimaEntidade.getComportamento().setParametro(nome, valor);
				}
				
			} else if (tag.equalsIgnoreCase("EMISSOR")) {//TODO COLOCAR IDENTIFICACAO EM TODOS OS ANIMADOS
				float x = Float.parseFloat(attributes.getValue("x"));
				float y = Float.parseFloat(attributes.getValue("y"));
				int nivel = parseNivel(attributes.getValue("nivel"));
				String ident = attributes.getValue("identificador");
				
				if(ident == null) {
					ident = "emissor_" + ((Cenario) cena).getTotalObjetos();
				}
					
				long comecarEm = 0;
				try {
					comecarEm = Long.parseLong(attributes.getValue("comecarEm"));
				}catch (Exception e) {
				}
				
				String referencia = attributes.getValue("referencia");

				try {
					EmissorParticulas emi = RecursosFactory.getInstancia().getEmissorParticulas(referencia);
//						new EmissorParticulas(ParticleIO
//							.loadConfiguredSystem(RECURSOS_EMISSORES
//									+ referencia + ".xml"));
					emi.setX(x);
					emi.setY(y);
					emi.setComecarEm(comecarEm);
					emi.setIdentificacao(ident);
					
					((Cenario) cena).adicionarEmissores(emi, nivel);
				} catch (Exception e) {
					e.printStackTrace();
				}

			} else if (tag.toUpperCase().equals("MOSAICO")) {
				String def = attributes.getValue("definicao");
				String ident = attributes.getValue("nome");
				int nivel = parseNivel(attributes.getValue("nivel"));
				float x = Float.parseFloat(attributes.getValue("x"));
				float y = Float.parseFloat(attributes.getValue("y"));

				try {
					Mosaico mosaico = new CarregadorMosaico()
							.carregarMosaico(def);
					mosaico.setNome(ident);
					mosaico.setX(x);
					mosaico.setY(y);
					cena.adicionarDesenho(mosaico, nivel);
				} catch (Exception e) {
					e.printStackTrace();
				}

			} else if (tag.toUpperCase().equals("MOSAICOANIMADO")) {

				String def = attributes.getValue("definicao");
				String ident = attributes.getValue("nome");
				int nivel = parseNivel(attributes.getValue("nivel"));
				float x = Float.parseFloat(attributes.getValue("x"));
				float y = Float.parseFloat(attributes.getValue("y"));

				try {
					MosaicoAnimado mosaico = new CarregadorMosaicoAnimado()
							.carregarMosaico(def);
					mosaico.setNome(ident);
					mosaico.setX(x);
					mosaico.setY(y);
					((Cenario) cena).adicionarAnimacao(mosaico, nivel);
				} catch (Exception e) {
					e.printStackTrace();
				}

			} else if (tag.toUpperCase().equals("IMAGEM")) {
				String ref = attributes.getValue("referencia");
				// String indent = attributes.getValue("indentificador");
				int nivel = parseNivel(attributes.getValue("nivel"));
				float x = Float.parseFloat(attributes.getValue("x"));
				float y = Float.parseFloat(attributes.getValue("y"));
				boolean grande = paraBoolean(attributes.getValue("grande"));

				try {
					Imagem imagem;
					imagem = RecursosFactory.getInstancia().getImagem(ref, grande);
					imagem.setX(x);
					imagem.setY(y);
					cena.adicionarDesenho(imagem, nivel);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (tag.equalsIgnoreCase("ANIMACAO")) {
				String base = attributes.getValue("base");
				String nome = attributes.getValue("nome");
				boolean multi = paraBoolean(attributes.getValue("multi"));
				int nivel = parseNivel(attributes.getValue("nivel"));
				int velocidade = Animacao.VELOCIDADE_BASICA;
				String velocidadeStr = attributes.getValue("velocidade");
				
				if(velocidadeStr != null)
					velocidade = Integer.parseInt(velocidadeStr);
				
				float x = Float.parseFloat(attributes.getValue("x"));
				float y = Float.parseFloat(attributes.getValue("y"));
				float altura = Float.parseFloat(attributes.getValue("altura"));
				float comprimento = Float.parseFloat(attributes
						.getValue("comprimento"));
				int quadros = Integer.parseInt(attributes.getValue("quadros"));

				
				try {
					if(!multi) {
						Animacao anim = RecursosFactory.getInstancia().getAnimacao(base, (int) comprimento, (int) altura, 0, 0, quadros, true, velocidade);
//							new Animacao(base, (int) comprimento, (int) altura, 0, 0, quadros, true, velocidade);
						anim.setDeslocX(0);
						anim.setDeslocY(0);
						anim.setX(x);
						anim.setY(y);
	
						((Cenario) cena).adicionarAnimacao(anim, nivel);
					} else {
						Animacao anim = RecursosFactory.getInstancia().getAnimacaoMultImagem(base, (int) comprimento, 
								(int) altura, 0, 0, quadros, true, velocidade);
//							new Animacao(base, RecursosFactory.getInstancia().
//								getMultiImagem(base),
//								(int) comprimento, (int) altura, 0, 0, quadros,
//								true, velocidade);
						anim.setDeslocX(0);
						anim.setDeslocY(0);
						anim.setX(x);
						anim.setY(y);
	
						((Cenario) cena).adicionarAnimacao(anim, nivel);						
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (tag.toUpperCase().equals("ACAO")) {
				String tipo = attributes.getValue("tipo");
				float x = Float.parseFloat(attributes.getValue("x"));
				float y = Float.parseFloat(attributes.getValue("y"));
				float comprimento = Float.parseFloat(attributes
						.getValue("comprimento"));
				float altura = Float.parseFloat(attributes.getValue("altura"));

				Rectangle ret = new Rectangle(x, y, comprimento, altura);

				try {
					if (tipo.equalsIgnoreCase("PERSONALIZADO")) {
						String comportamento = attributes.getValue("comportamento");
						ScriptComportamento scr = RecursosFactory.getInstancia().getComportamento(comportamento);

						AreaAcaoScript area = new AreaAcaoScript();

						area.setScript(scr);
						area.setRetangulo(ret);

						((Cenario) cena).adicionarAreaAcao(area);
					} else if (tipo.equalsIgnoreCase("NOCIVO")) {
						int dano = Integer
								.parseInt(attributes.getValue("dano"));
						AreaAcaoNegativa area = new AreaAcaoNegativa();
						area.setDano(dano);
						area.setRetangulo(ret);

						((Cenario) cena).adicionarAreaAcao(area);
					} else if (tipo.equalsIgnoreCase("TELEPORTE")) {
						areaTeleporte = new AreaTeleporte();
						areaTeleporte.setRetangulo(ret);

						((Cenario) cena).adicionarAreaAcao(areaTeleporte);
					} else if (tipo.equalsIgnoreCase("PORTA")) {
						float deslocY = Float.parseFloat(attributes.getValue("deslocY"));
						float deslocX = Float.parseFloat(attributes.getValue("deslocX"));						
						boolean desX = paraBoolean(attributes.getValue("desX"));
						boolean desY = paraBoolean(attributes.getValue("desY"));						
						float xFixo = Float.parseFloat(attributes.getValue("xFixo"));
						float yFixo = Float.parseFloat(attributes.getValue("yFixo"));
						float maxX = Float.parseFloat(attributes.getValue("maxX"));
						float minX = Float.parseFloat(attributes.getValue("minX"));
						float maxY = Float.parseFloat(attributes.getValue("maxY"));
						float minY = Float.parseFloat(attributes.getValue("minY"));
						String cenaDestino = attributes.getValue("cena");
						
						
						AreaAcaoPortal porta = new AreaAcaoPortal();
						porta.setRetangulo(ret);
						porta.setXFixo(xFixo);
						porta.setYFixo(yFixo);
						porta.setMaxX(maxX);
						porta.setMaxY(maxY);
						porta.setMinX(minX);
						porta.setMinY(minY);
						porta.setDeslocX(deslocX);
						porta.setDeslocY(deslocY);
						porta.setDesX(desX);
						porta.setDesY(desY);
						porta.setCena(cenaDestino);
						
						((Cenario) cena).adicionarAreaAcao(porta);
					} else {
						String script = attributes.getValue("script");
						// TODO CARREGAR O SCRIPT
						AreaAcaoScript area = new AreaAcaoScript();
						area.setScript(null);
						area.setRetangulo(ret);

						((Cenario) cena).adicionarAreaAcao(area);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (tag.equalsIgnoreCase("DIREITA")
					|| tag.equalsIgnoreCase("ESQUERDA")
					|| tag.equalsIgnoreCase("ABAIXO")
					|| tag.equalsIgnoreCase("ACIMA")) {
				if (areaTeleporte != null) {
					float destinoX = Integer.parseInt(attributes.getValue("x"));
					float destinoY = Integer.parseInt(attributes.getValue("y"));
					String habilitado = attributes.getValue("habilitado");

					areaTeleporte.adicionaDestino(tag.toUpperCase(), destinoX,
							destinoY, paraBoolean(habilitado));
				}
			} else if (tag.equalsIgnoreCase("CARREGAR")) {
				//maneira de reduzir tempo de loading entre niveis com recursos diferentes				
				String tipo = attributes.getValue("tipo");
				String ref = attributes.getValue("referencia");
				
				try {
					if(tipo.equalsIgnoreCase("multImagem")) {
						RecursosFactory.getInstancia().getMultiImagem(ref);
					} else if(tipo.equalsIgnoreCase("imagem")) {
						RecursosFactory.getInstancia().getImage(ref);
					}
				} catch (SlickException e) {
					tratarExcecao(e);
				}
			} else if (tag.equalsIgnoreCase("PARALAXERS")) {
				String referencia = attributes.getValue("referencia");
				List<Paralaxer> pas = RecursosFactory.getInstancia().getParalaxers(referencia);
				
				for(Paralaxer pa : pas) {
					((Cenario)cena).adicionarParalaxeFundo(pa);
				}
			}				
			
		}

		private boolean paraBoolean(String val) {			
			return ((val != null) && val.equalsIgnoreCase("SIM"));
		}

		@Override
		public void endElement(String uri, String localName, String tag)
				throws SAXException {
			if (tag.toUpperCase().equals("PLATAFORMA")) {
				construindoPlataforma = false;
				
				Body corpo = null;
				
				if(shape instanceof Box) {//TODO isso para cada tipo de plataforma
					corpo = new RetangularPlataformaCorpo(platIdentificador, shape);
				} else {
					corpo = new StaticBody(platIdentificador, shape);
				}
				
				corpo.setPosition(platX, platY);
				corpo.setFriction(atrito);

				Entidade ent = new EntidadePlataforma();
				ent.setNome(platIdentificador);
				Plataforma plat = new Plataforma(ent, corpo);
				((Cenario) cena).adicionarPlataforma(plat);

			} else if (tag.toUpperCase().equals("POLIGONO")) {
				construindoPoligono = false;
				shape = new Polygon(v);
			} else if (tag.toUpperCase().equals("ENTIDADE")) {
				ultimaEntidade = null;
			}
		}

	}
		
	public void tratarExcecao(Exception cause) {
		cause.printStackTrace();
		Logger.getLogger(Motor.ARQUIVO_LOG).log(Level.SEVERE, "erro cenario:", cause);		
	}	
}
