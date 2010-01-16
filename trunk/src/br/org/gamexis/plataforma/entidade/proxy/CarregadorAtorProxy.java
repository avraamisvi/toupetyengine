package br.org.gamexis.plataforma.entidade.proxy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
import net.phys2d.raw.shapes.DynamicShape;
import net.phys2d.raw.shapes.Polygon;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import br.org.gamexis.plataforma.Motor;
import br.org.gamexis.plataforma.cena.Animacao;
import br.org.gamexis.plataforma.cena.Ator;
import br.org.gamexis.plataforma.cena.proxy.AtorProxy;
import br.org.gamexis.plataforma.motor.filesystem.FileSystemFactory;

public class CarregadorAtorProxy {

	AtorProxy ator;

	public Ator carregarAtor(String nome) {

		SAXParser parser;
		try {
			parser = SAXParserFactory.newInstance().newSAXParser();
			String caminho = "atores" + File.separator + nome + ".def";

			CarregadorXMLHandler handler = new CarregadorXMLHandler();

			parser.parse(FileSystemFactory.getFileSystem().abrirInputStream(caminho), handler);

			return ator;

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (FactoryConfigurationError e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	class CarregadorXMLHandler extends DefaultHandler {
		String nome;
		String animBase = null;
		DynamicShape box;
		Body corpo;
		boolean corpoEstatico = false;
		float atrito = 0;
		float peso = 0;
		boolean multi = false;
		
		String baseAnim;
		float comprimento;
		float altura;
		float deslocx;
		float deslocy;

		String animNome;
		boolean emissor = false;
		long tempoMaximo = 0;
		int quadros;
		boolean repetir;
		boolean efeitoGravidade = true;
		boolean mista = false;
		int velocidade_anim = Animacao.VELOCIDADE_BASICA;
		
		List<CaixaColisaoEntrada> caixasColisao = new ArrayList<CaixaColisaoEntrada>();
		
		//CORPO QNDO POLIGONO
		Vector2f[] vectors2f;
		int vectorIndex = 0;
		boolean construindoPoligono = false;
		float maxvy = 0, maxvx = 0;
		
		boolean converterValorBoolean(String v) {
			if (v != null && v.toUpperCase().equals("SIM")) {
				return true;
			}
			return false;
		}

		@Override
		public void startElement(String uri, String localName, String tag,
				Attributes attributes) throws SAXException {

			if (tag.toUpperCase().equals("ATOR")) {
				multi = false;
				nome = attributes.getValue("nome");
			} else if (tag.toUpperCase().equals("CORPO")) {
				corpoEstatico = converterValorBoolean(attributes.getValue("estatico"));
				
				peso = Float.parseFloat(attributes.getValue("peso"));
				atrito = Float.parseFloat(attributes.getValue("atrito"));
				efeitoGravidade = converterValorBoolean(attributes.getValue("gravidade"));
				
				try {
					maxvx = Float.parseFloat(attributes.getValue("vXMax"));
					maxvy = Float.parseFloat(attributes.getValue("vYMax"));
					
				} catch( Exception e) {
					//PARA ABAFAR ERROS DEVIDO A VERSÕES SEM ESSES PARAMETROS
				}
				
			} else if (tag.toUpperCase().equals("CAIXA")) {
				
				float comprimento = Float.parseFloat(attributes
						.getValue("comprimento"));
				float altura = Float.parseFloat(attributes.getValue("altura"));

				box = new Box(comprimento, altura);
//				box = new Circle(30);
			}  else if (tag.toUpperCase().equals("CAIXACOLISAO")) {
				
				float comprimento = Float.parseFloat(attributes.getValue("comprimento"));
				float altura = Float.parseFloat(attributes.getValue("altura"));
				float x = Float.parseFloat(attributes.getValue("x"));
				float y = Float.parseFloat(attributes.getValue("y"));
				String animacao = attributes.getValue("animacao");
				String nome = attributes.getValue("nome");
				int frame = Integer.parseInt(attributes.getValue("frame"));
				
				CaixaColisaoEntrada caixa = new CaixaColisaoEntrada();
				caixa.x = x;
				caixa.y = y;
				caixa.nome = nome;
				caixa.altura = altura;
				caixa.comprimento = comprimento;
				caixa.animacao = animacao;
				caixa.frame = frame;
				
				caixasColisao.add(caixa);
				
			} else if (tag.toUpperCase().equals("POLIGONO")) {
				
				int pontos = Integer.parseInt(attributes.getValue("pontos"));
				vectors2f = new Vector2f[pontos];
				vectorIndex = 0;

				construindoPoligono = true;
			} else if (tag.toUpperCase().equals("PONTO")) {
				float x = Float.parseFloat(attributes.getValue("x"));
				float y = Float.parseFloat(attributes.getValue("y"));

				vectors2f[vectorIndex] = new Vector2f(x, y);
				vectorIndex++;
			 } else if (tag.toUpperCase().equals("ANIMACOES")) {
				
				multi = converterValorBoolean(attributes.getValue("multi"));
				comprimento = Float.parseFloat(attributes
						.getValue("comprimento"));
				altura = Float.parseFloat(attributes.getValue("altura"));
				deslocx = Float.parseFloat(attributes.getValue("deslocx"));
				deslocy = Float.parseFloat(attributes.getValue("deslocy"));
				baseAnim = attributes.getValue("base");
				

				
			} else if (tag.toUpperCase().equals("ANIMACAO")) {
				animNome = attributes.getValue("nome");
				quadros = Integer.parseInt(attributes.getValue("quadros"));
				repetir = converterValorBoolean(attributes.getValue("repetir"));

//				if(nome.contains("goteira"))
//					System.out.println("animNome: " + animNome);
				
				try {
					velocidade_anim = Integer.parseInt(attributes.getValue("duracao"));
				} catch(Exception e) {
					//SUPORTAR VERSÕES ANTERIORES
					velocidade_anim = Animacao.VELOCIDADE_BASICA;
//					System.out.println("Ator:" + nome + " animNome: " + animNome);
				}
				
			} else if (tag.toUpperCase().equals("MISTA")) {
				animNome = attributes.getValue("nome");
				quadros = Integer.parseInt(attributes.getValue("quadros"));
				repetir = converterValorBoolean(attributes.getValue("repetir"));
				mista = true;
			} else if (tag.toUpperCase().equals("DIREITA")
					|| tag.toUpperCase().equals("ESQUERDA")
					|| tag.toUpperCase().equals("ACIMA")
					|| tag.toUpperCase().equals("ABAIXO")) {
				
				String animacaoNomeTag = animNome.toUpperCase() + "_" + tag.toUpperCase();
				//TODO ajustar para funcionar como o Carregador de Ator
				if(mista) {//MISTA
					
					int linha = Integer.parseInt(attributes.getValue("linha"));
					int coluna = Integer.parseInt(attributes.getValue("coluna"));
					
					float ex = Float.parseFloat(attributes.getValue("ex"));
					float ey = Float.parseFloat(attributes.getValue("ey"));
					
					String emissorNome = attributes.getValue("emissor");

					ator.adicionarPseudoAnimacao(baseAnim, quadros, (int)comprimento, (int)altura, linha, coluna,
							deslocx, deslocy, ex, ey, animacaoNomeTag, emissorNome, (int)tempoMaximo,
							repetir, mista, multi, velocidade_anim);
					
				} else if(emissor) {
						construirEmissor(attributes, animacaoNomeTag);
				} else { //SPRITES
					int linha = Integer.parseInt(attributes.getValue("linha"));
					int coluna = Integer
							.parseInt(attributes.getValue("coluna"));

					ator.adicionarPseudoAnimacao(baseAnim, quadros, (int)comprimento, (int)altura, linha, coluna,
							deslocx, deslocy, 0, 0, animacaoNomeTag, null, 0,
							repetir, false, multi, velocidade_anim);
				}
			} else if (tag.toUpperCase().equals("PARTICULAS")) {//EMISSORES
				animNome = attributes.getValue("nome");
				tempoMaximo = Long.parseLong(attributes.getValue("tempo"));
				repetir = converterValorBoolean(attributes.getValue("repetir"));
				emissor = true;
			}
		}
		
		public void construirEmissor(Attributes attributes, String animacaoNomeTag) {			
			float ex = Float.parseFloat(attributes.getValue("ex"));
			float ey = Float.parseFloat(attributes.getValue("ey"));
			
			String emissorNome = attributes.getValue("emissor");
			
			ator.adicionarPseudoAnimacao(baseAnim, 0, 0, 0, 0, 0,
					0, 0, ex, ey, animacaoNomeTag, emissorNome, (int)tempoMaximo,
					repetir, false, false, 0);
		}
		
		@Override
		public void endElement(String uri, String localName, String tag)
				throws SAXException {
			if (tag.toUpperCase().equals("CORPO")) {
				if(corpoEstatico) {
					corpo = new StaticBody("GXAtor" + Ator.contadorInterno, box);
					corpo.set(box, peso);
				} else {
					corpo = new Body("GXAtor" + Ator.contadorInterno, box, peso);
					corpo.setGravityEffected(efeitoGravidade);
				}
				
				corpo.setFriction(atrito);
				corpo.setMaxVelocX(maxvx);
				corpo.setMaxVelocY(maxvy);
				
				ator = new AtorProxy(corpo);
			} else if (tag.toUpperCase().equals("POLIGONO")) {
				construindoPoligono = false;
				box = new Polygon(vectors2f);
			} 
			else if (tag.toUpperCase().equals("ATOR")) {
				ator.setNome(nome);
				
				for (CaixaColisaoEntrada caixa : caixasColisao) {
					ator.adicionarCaixaColisao(caixa.comprimento, caixa.altura, caixa.x, caixa.y, caixa.animacao, caixa.frame, caixa.nome);
				}
			} else if (tag.toUpperCase().equals("MISTA")) {
				mista = false;
			} else if (tag.toUpperCase().equals("PARTICULAS")) {
				emissor = false;
			}
		}

	}
	
	public void tratarExcecao(Exception cause) {
		cause.printStackTrace();
		Logger.getLogger(Motor.ARQUIVO_LOG).log(Level.SEVERE, "erro cenario:", cause);		
	}
	
	class CaixaColisaoEntrada{
		String animacao;
		String nome;
		int frame;
		float comprimento;
		float altura;
		float x;
		float y;
	}
}
