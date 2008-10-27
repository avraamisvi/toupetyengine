package br.org.gamexis.plataforma.cena;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.newdawn.slick.SlickException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import br.org.gamexis.plataforma.cena.util.CarregadorMultImagem;
import br.org.gamexis.plataforma.motor.RecursosFactory;

public class CarregadorMosaicoAnimado {
	
	MosaicoAnimado mosaico;

	public MosaicoAnimado carregarMosaico(String nome) {

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
		
		private RecursosFactory recursos = RecursosFactory.getInstancia();
		
		@Override
		public void startElement(String uri, String localName, String tag,
				Attributes attributes) throws SAXException {
			
			if (tag.toUpperCase().equals("MOSAICOANIMADO")) {
				boolean multi = paraBoolean(attributes.getValue("multi"));
				
				String base=attributes.getValue("base");
				int comprimentoCelulas=Integer.parseInt(attributes.getValue("comprimentoCelulas")); 
				int alturaCelulas=Integer.parseInt(attributes.getValue("alturaCelulas")); 
				int linhas=Integer.parseInt(attributes.getValue("linhas"));
				int colunas=Integer.parseInt(attributes.getValue("colunas"));
				
				try {
					if(!multi) {
						mosaico = new MosaicoAnimado(base, comprimentoCelulas, 
							alturaCelulas, linhas, colunas);
					} else {
						mosaico = new MosaicoAnimado(recursos.getMultiImagem(base), 
								comprimentoCelulas, alturaCelulas, linhas, colunas);						
					}
				} catch(SlickException e) {
					e.printStackTrace();
				}
				
			} else if (tag.toUpperCase().equals("CELULA")) {
				
				//<celula linhaDestino="0" colunaDestino="0" animacao="0" visivel="SIM"/>				
				String nome=attributes.getValue("nome");
				int linha=Integer.parseInt(attributes.getValue("linhaDestino")); 
				int coluna=Integer.parseInt(attributes.getValue("colunaDestino")); 
				int animacao=Integer.parseInt(attributes.getValue("animacao"));
				boolean visivel = paraBoolean(attributes.getValue("visivel"));
				
				mosaico.adicionarTile(nome, linha, coluna, animacao, visivel);
				
			} else if (tag.equalsIgnoreCase("animacao")) {
				
				int linha=Integer.parseInt(attributes.getValue("linha")); 
				int coluna=Integer.parseInt(attributes.getValue("coluna"));
				int velocidade=Integer.parseInt(attributes.getValue("velocidade"));
				int quadros=Integer.parseInt(attributes.getValue("quadros"));
				boolean repetir=paraBoolean(attributes.getValue("repetir"));
				String nome = attributes.getValue("animacao");
				
				try {
					mosaico.adicionarAnimacao(nome, linha, coluna, quadros, repetir, velocidade);
				} catch (SlickException e) {
					e.printStackTrace();
				}
//				<animacao nome="topo" quadros="2" repetir="SIM" linha="0" coluna="0" velocidade="50" />
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
