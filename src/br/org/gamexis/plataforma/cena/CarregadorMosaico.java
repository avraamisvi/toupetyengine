package br.org.gamexis.plataforma.cena;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import br.org.gamexis.plataforma.motor.RecursosFactory;

public class CarregadorMosaico {

	Mosaico mosaico;

	public Mosaico carregarMosaico(String nome) {

		mosaico = null;

		SAXParser parser;
		try {
			parser = SAXParserFactory.newInstance().newSAXParser();
			String caminho = "recursos/mosaicos/" + nome + ".def";

			CarregadorXMLHandler handler = new CarregadorXMLHandler();

			parser.parse(new File(caminho), handler);

			return mosaico;

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

			if (tag.toUpperCase().equals("MOSAICO")) {
				//solucionar problemas de texturas muito grandes
				boolean multi = paraBoolean(attributes.getValue("multi"));
				
				Color corEfeito = paraCor(attributes.getValue("corEfeito"));
				
				String base = attributes.getValue("base");
				int comprimentoCelulas = Integer.parseInt(attributes
						.getValue("comprimentoCelulas"));
				int alturaCelulas = Integer.parseInt(attributes
						.getValue("alturaCelulas"));
				int linhas = Integer.parseInt(attributes.getValue("linhas"));
				int colunas = Integer.parseInt(attributes.getValue("colunas"));
				
				try {
					if (!multi) {
						mosaico = new Mosaico(base, comprimentoCelulas, 
								alturaCelulas, linhas, colunas);
					} else {
						mosaico = new Mosaico(RecursosFactory.getInstancia().getMultiImagem(base), 
								comprimentoCelulas,alturaCelulas, linhas, colunas);
					}
					
					if(corEfeito != null) {
						mosaico.setEfeitoCor(corEfeito);
					}					
				} catch (SlickException e) {
					e.printStackTrace();
				}

			} else if (tag.toUpperCase().equals("CELULA")) {
				String nome = attributes.getValue("nome");
				int linha = Integer.parseInt(attributes
						.getValue("linhaDestino"));
				int coluna = Integer.parseInt(attributes
						.getValue("colunaDestino"));
				int parteLinha = Integer.parseInt(attributes
						.getValue("parteLinha"));
				int parteColuna = Integer.parseInt(attributes
						.getValue("parteColuna"));
				boolean visivel = paraBoolean(attributes.getValue("visivel"));

				mosaico.adicionarTile(nome, linha, coluna, parteLinha,
						parteColuna, visivel);
			}
		}

		Color paraCor(String c) {
			
			if(c == null)
				return null;
			
			String cs[] = c.split(",");
			
			float r = Float.parseFloat(cs[0]);
			float g = Float.parseFloat(cs[1]);
			float b = Float.parseFloat(cs[2]);
			float a = Float.parseFloat(cs[3]);
			
			return new Color(r, g, b, a);
		}
		
		private boolean paraBoolean(String val) {
			if (val == null)
				return false;

			return val.equalsIgnoreCase("SIM") ? true : false;
		}

		@Override
		public void endElement(String uri, String localName, String tag)
				throws SAXException {
		}

	}
}
