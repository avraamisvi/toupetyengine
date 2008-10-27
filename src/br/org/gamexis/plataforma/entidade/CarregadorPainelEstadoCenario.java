package br.org.gamexis.plataforma.entidade;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import br.org.gamexis.plataforma.cena.Imagem;
import br.org.gamexis.plataforma.cena.componentes.ArmaMostrador;
import br.org.gamexis.plataforma.cena.componentes.BarraEstado;
import br.org.gamexis.plataforma.cena.componentes.PainelEstado;
import br.org.gamexis.plataforma.entidade.logestado.ArmaSelecionada;
import br.org.gamexis.plataforma.entidade.logestado.BarraLogEstado;
import br.org.gamexis.plataforma.entidade.logestado.Municao;
import br.org.gamexis.plataforma.motor.RecursosFactory;

public class CarregadorPainelEstadoCenario {
	private PainelEstado painel;
	public RecursosFactory recursos = RecursosFactory.getInstancia();
	
	public PainelEstado carregarPainelEstadoCenario(String nome) {

		SAXParser parser;
		try {
			parser = SAXParserFactory.newInstance().newSAXParser();
			String caminho = "recursos/entidades/" + nome + ".def";

			CarregadorXMLHandler handler = new CarregadorXMLHandler();

			parser.parse(new File(caminho), handler);

			return painel;

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
		float x;
		float y;

		boolean converterValorBoolean(String v) {
			if (v.toUpperCase().equals("SIM")) {
				return true;
			}
			return false;
		}

		private int convertValorMunicao(String valor) {
			int ret = 0;

			try {
				if (valor.equalsIgnoreCase("INFINITO")) {
					ret = Municao.INFINITO;
				} else {
					ret = Integer.parseInt(valor);
				}
			} catch (Exception e) {
				ret = Municao.INFINITO;
			}
			return ret;
		}

		@Override
		public void startElement(String uri, String localName, String tag,
				Attributes attributes) throws SAXException {

			if (tag.toUpperCase().equalsIgnoreCase("PainelEstado")) {
				x = Float.parseFloat(attributes.getValue("x"));
				y = Float.parseFloat(attributes.getValue("y"));
				
				painel = new PainelEstado();
				painel.setX(x);
				painel.setY(y);

			} else if (tag.toUpperCase().equalsIgnoreCase("molduraArma")) {
				String fundo = attributes.getValue("fundo");
				ArmaMostrador armaMostrador = new ArmaMostrador();
				try {
					armaMostrador.setFundo(recursos.getImage(fundo));
					painel.setArmaMostrador(armaMostrador);
				} catch (Exception e) {
					e.printStackTrace();
				}

			} else if (tag.toUpperCase().equalsIgnoreCase("barra")) {
				try {
					String nome = attributes.getValue("nome");
					String contorno = attributes.getValue("contorno");
					String unidade = attributes.getValue("unidade");
					float dx = Float.parseFloat(attributes.getValue("dx"));
					float dy = Float.parseFloat(attributes.getValue("dy"));
					int maximo = Integer
							.parseInt(attributes.getValue("maximo"));

					BarraEstado barraEstado = new BarraEstado();
					barraEstado.setContorno(recursos.getImage(contorno));
					barraEstado.setUnidade(recursos.getImage(unidade));

					barraEstado.setDeslocamentoX(dx);
					barraEstado.setDeslocamentoy(dy);

					BarraLogEstado barra = new BarraLogEstado();
					barra.setMaximo(maximo);
					barra.setNome(nome);

					barraEstado.setBarraEstadoPOJO(barra);
					painel.getLogEstado().adicionarBarraEstado(barra);
					painel.adicionarBarraEstado(barraEstado);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (tag.toUpperCase().equalsIgnoreCase("arma")) {
				String nome = attributes.getValue("nome");
				float dx = Float.parseFloat(attributes.getValue("dx"));
				float dy = Float.parseFloat(attributes.getValue("dy"));
				int maxMunicao = convertValorMunicao(attributes
						.getValue("municao"));
				boolean selecionada = converterValorBoolean(attributes
						.getValue("selecionada"));
				String icone = attributes.getValue("icone");

				try {
					ArmaSelecionada armaSelec = new ArmaSelecionada();

					armaSelec.setDeslocamentoX(dx);
					armaSelec.setDeslocamentoY(dy);
					armaSelec.setNome(nome);

					Municao munic = new Municao();
					munic.setMaximo(maxMunicao);
					armaSelec.setMunicao(munic);

					armaSelec.setIcone(new Imagem(icone));

					painel.getLogEstado().adicionarArma(armaSelec);

					if (selecionada)
						painel.setArma(armaSelec.getNome());

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}

		@Override
		public void endElement(String uri, String localName, String tag)
				throws SAXException {
			
		}

	}
}
