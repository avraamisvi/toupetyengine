package br.org.gamexis.plataforma.cena.materiais;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.newdawn.slick.SlickException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import br.org.gamexis.plataforma.Motor;
import br.org.gamexis.plataforma.motor.RecursosFactory;
import br.org.gamexis.plataforma.motor.filesystem.FileSystemFactory;

public class CarregadorParalaxer {
	List<Paralaxer> ret = new ArrayList<Paralaxer>();

	public List<Paralaxer> carregarParalaxer(String nome) {

		SAXParser parser;
		try {
			parser = SAXParserFactory.newInstance().newSAXParser();
			String caminho = "cenas/" + nome + ".def";

			CarregadorXMLHandler handler = new CarregadorXMLHandler();

			parser.parse(FileSystemFactory.getFileSystem().abrirInputStream(caminho), handler);

			return ret;

		} catch (Throwable e) {
			Motor.obterInstancia().tratarExcecao(e);
		}
		return null;
	}

	class CarregadorXMLHandler extends DefaultHandler {
		Paralaxer pa;
		@Override
		public void startElement(String uri, String localName, String tag,
				Attributes attributes) throws SAXException {

			if (tag.equalsIgnoreCase("PARALAXER")) {
				float x = Float.parseFloat(attributes.getValue("x"));
				float y = Float.parseFloat(attributes.getValue("y"));
				String nome = attributes.getValue("nome");
				float fatorDimensional = Float.parseFloat(attributes
						.getValue("fatorDimensional"));
				float velocidadeX = Float.parseFloat(attributes
						.getValue("velocidadeX"));
				float velocidadeDeslocX = Float.parseFloat(attributes
						.getValue("velocidadeDeslocX"));
				float velocidadeDeslocY = Float.parseFloat(attributes
						.getValue("velocidadeDeslocY"));
				float maxY = Float.parseFloat(attributes.getValue("maxY"));
				float minY = Float.parseFloat(attributes.getValue("minY"));

				pa = new Paralaxer(nome);
				pa.setX(x);
				pa.setY(y);
				pa.setVelocidadeDeslocX(velocidadeDeslocX);
				pa.setVelocidadeDeslocY(velocidadeDeslocY);
				pa.setVelocidadeX(velocidadeX);
				pa.setFatorTamanho(fatorDimensional);
				pa.setMaxY(maxY);
				pa.setMinY(minY);
				
				ret.add(pa);
				
			} else if (tag.equalsIgnoreCase("IMAGEM")) {
				String ref = attributes.getValue("referencia");
				try {
					pa.adicionarImagen(RecursosFactory.getInstancia().getImage(ref));
				} catch (Exception e) {
					tratarExcecao(e);
				}
			}

		}

//		private boolean paraBoolean(String val) {
//			return ((val != null) && val.equalsIgnoreCase("SIM"));
//		}

		@Override
		public void endElement(String uri, String localName, String tag)
				throws SAXException {
		}

	}

	public void tratarExcecao(Exception cause) {
		Motor.obterInstancia().tratarExcecao(cause);
	}
}
