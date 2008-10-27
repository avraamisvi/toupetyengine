package br.org.gamexis.plataforma.cena.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.newdawn.slick.SlickException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import br.org.gamexis.plataforma.cena.Tile;
import br.org.gamexis.plataforma.motor.RecursosFactory;

/**
 * O intuito dessa classe � de otimizar a performance do jogo por permitir
 * montar uma imagem de base para as anima��es a partir de muitas imagens com
 * propor��es menores
 * 
 * a estrutura seria o seguinte:
 * recursos/imagens/nome.pngs/nome.png.def + *.png
 * @author abraao.isvi
 * 
 */
public class CarregadorMultImagem {

	HashMap<String, Tile> imagens;
	String referencia;

	public HashMap<String, Tile> carregarImagem(String nome) {

		imagens = null;

		SAXParser parser;
		try {
			referencia = nome + "s";
			parser = SAXParserFactory.newInstance().newSAXParser();
			String caminho = "recursos/imagens/" + referencia + "/" + nome + ".def";

			CarregadorXMLHandler handler = new CarregadorXMLHandler();

			parser.parse(new File(caminho), handler);

			return imagens;

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

	public String getReferencia() {
		return referencia;
	}

	class CarregadorXMLHandler extends DefaultHandler {

		private static final String RECURSOS_IMAGENS = "recursos/imagens/";
		boolean grande;
		
		@Override
		public void startElement(String uri, String localName, String tag,
				Attributes attributes) throws SAXException {

			if (tag.toUpperCase().equals("IMAGEM")) {
				grande = paraBoolean(attributes.getValue("grande"));
				imagens = new HashMap<String, Tile>();
				
			} else if (tag.toUpperCase().equals("CELS")) {
				String celnome = attributes.getValue("nome");
				String ext = attributes.getValue("ext");
				int linha  = Integer.parseInt(attributes.getValue("linha"));
				int quadros  = Integer.parseInt(attributes.getValue("quadros"));
				int coluna = 0;
				
				try {
						
					for(; coluna < quadros; coluna++) {
						Tile tile = RecursosFactory.getInstancia().
						getTileMultImagem(referencia, celnome, grande, ext, linha, quadros, coluna);
						imagens.put("l_"+tile.getLinha()+"c_"+tile.getColuna(), tile);
						System.gc();
					}
									
										
				} catch (SlickException e) {
					e.printStackTrace();
				}
			}
		}

		 private boolean paraBoolean(String val) {
			 return "SIM".equalsIgnoreCase(val)? true : false;
		 }

		@Override
		public void endElement(String uri, String localName, String tag)
				throws SAXException {
		}

	}
}
