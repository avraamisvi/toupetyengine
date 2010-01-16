package br.org.gamexis.plataforma.motor.configuracao;

import java.io.File;

import com.thoughtworks.xstream.XStream;

import de.schlichtherle.io.FileInputStream;
import de.schlichtherle.io.FileOutputStream;

public class GerenciadorConfiguracaoGeral {

	private static final String ARQUIVO_CONFIGURACAO = "jogo.config";

	public void salvarConfiguracao(ConfiguracaoGeral config) throws Exception {		
		new XStream().toXML(config, new FileOutputStream(ARQUIVO_CONFIGURACAO));		
	}

	/**
	 * Carrega a configuração do jogo caso não exista o arquivo de configuraçao
	 * cria um novo padrão.
	 * 
	 * @return
	 * @throws Exception
	 */
	public ConfiguracaoGeral carregarConfiguracao() throws Exception {

		if (!validarExistenciaArquivo()) {
			gerarConfiguracaoPadrao();
		}

		ConfiguracaoGeral conf = null;
		conf = (ConfiguracaoGeral)new XStream().fromXML(new FileInputStream(ARQUIVO_CONFIGURACAO));

		return conf;
	}

	/**
	 * Usado apenas para gerar uma configuração padrão.
	 * 
	 * @throws Exception
	 */
	public void gerarConfiguracaoPadrao() throws Exception {
		ConfiguracaoGeral geral = new ConfiguracaoGeral();

		Video video = new Video();
		video.setAltura(600);
		video.setComprimento(800);
		video.setTelaCheia(false);

		Som som = new Som();
		som.setAlturaEfeitos(1);
		som.setAlturaMusica(1);

		geral.setResolucao(video);
		geral.setSom(som);

		geral.setControles(new Controles());

		salvarConfiguracao(geral);
	}

	private boolean validarExistenciaArquivo() {
		boolean ret = true;

		ret = new File(ARQUIVO_CONFIGURACAO).exists();

		return ret;
	}
}
