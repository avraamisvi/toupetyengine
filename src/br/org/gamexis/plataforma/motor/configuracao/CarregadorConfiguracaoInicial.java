package br.org.gamexis.plataforma.motor.configuracao;

import java.io.IOException;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.newdawn.slick.SlickException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import br.org.gamexis.plataforma.Motor;
import br.org.gamexis.plataforma.motor.RecursosFactory;
import br.org.gamexis.plataforma.motor.filesystem.FileSystemFactory;

public class CarregadorConfiguracaoInicial {
	ConfiguracaoInicial configuracao;

	public ConfiguracaoInicial carregarConfiguracao() {

		configuracao = new ConfiguracaoInicial();

		SAXParser parser;
		try {
			parser = SAXParserFactory.newInstance().newSAXParser();
			String caminho = "motor.ini";

			CarregadorXMLHandler handler = new CarregadorXMLHandler();

			parser.parse(FileSystemFactory.getFileSystem().abrirInputStream(caminho), handler);

			return configuracao;

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

		@Override
		public void startElement(String uri, String localName, String tag,
				Attributes attributes) throws SAXException {

			if (tag.equalsIgnoreCase("resolucao")) {
				int comprimento = Integer.parseInt(attributes
						.getValue("comprimento"));
				int altura = Integer.parseInt(attributes.getValue("altura"));

				configuracao.comprimento = comprimento;
				configuracao.altura = altura;
			} else if (tag.equalsIgnoreCase("icone")) {
				String icone = attributes.getValue("valor");
				configuracao.icone = icone;
			} else if (tag.equalsIgnoreCase("titulo")) {
				String titulo = attributes.getValue("valor");
				configuracao.titulo = titulo;
			} else if (tag.equalsIgnoreCase("telacheia")) {
				boolean telacheia = paraBoolean(attributes.getValue("valor"));
				configuracao.telacheia = telacheia;
			} else if (tag.equalsIgnoreCase("debug")) {
				boolean debug = paraBoolean(attributes.getValue("valor"));
				configuracao.debug = debug;
			} else if (tag.equalsIgnoreCase("cenaInicial")) {
				String cenaInicial = attributes.getValue("nome");
				configuracao.cenaInicial = cenaInicial;
			} else if (tag.equalsIgnoreCase("cenaInicial")) {
				String cenaInicial = attributes.getValue("nome");
				configuracao.cenaInicial = cenaInicial;
			} else if (tag.equalsIgnoreCase("framerate")) {
				int framerate = Integer.parseInt(attributes.getValue("valor"));
				configuracao.framerate = framerate;
			} else if (tag.equalsIgnoreCase("atualizacao")) {
				int maximo = Integer.parseInt(attributes.getValue("maximo"));
				int minimo = Integer.parseInt(attributes.getValue("minimo"));
				configuracao.logicaMaxima = maximo;
				configuracao.logicaMinima = minimo;
			} else if (tag.equalsIgnoreCase("cursor")) {
				configuracao.cursor.referencia = attributes.getValue("referencia");
				configuracao.cursor.entidade = attributes.getValue("entidade");
			} else if (tag.equalsIgnoreCase("falas")) {
				Motor.obterInstancia().
				setMaximoIntervaloTexto(Integer.parseInt(attributes.getValue("velocidade")));
			} else if (tag.equalsIgnoreCase("CARREGAR")) {
				
				String referencia = attributes.getValue("referencia");
				String tipo = attributes.getValue("tipo");
				
				if(tipo.equalsIgnoreCase("script")) {
					try {
						RecursosFactory.getInstancia().getComportamento(referencia);
					} catch (SlickException e) {
						e.printStackTrace();
					}
				} else if(tipo.equalsIgnoreCase("imagem")) {
					try {
						RecursosFactory.getInstancia().getImage(referencia);
					} catch (SlickException e) {
						Motor.obterInstancia().tratarExcecao(e);
					}
				} else if(tipo.equalsIgnoreCase("multimagem")) {
					try {
						RecursosFactory.getInstancia().getMultiImagem(referencia);
					} catch (SlickException e) {
						Motor.obterInstancia().tratarExcecao(e);
					}
				} else if(tipo.equalsIgnoreCase("grandeimagem")) {
					try {
						RecursosFactory.getInstancia().getImage(referencia, true);
					} catch (SlickException e) {
						Motor.obterInstancia().tratarExcecao(e);
					}
				}
			}
			
		}

		private boolean paraBoolean(String val) {
			return val.equalsIgnoreCase("SIM") ? true : false;
		}

		@Override
		public void endElement(String uri, String localName, String tag)
				throws SAXException {
		}
	}
}