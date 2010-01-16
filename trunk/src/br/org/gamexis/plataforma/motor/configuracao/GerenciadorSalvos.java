package br.org.gamexis.plataforma.motor.configuracao;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import br.org.gamexis.plataforma.motor.Salvavel;

import com.thoughtworks.xstream.XStream;

import de.schlichtherle.io.FileInputStream;
import de.schlichtherle.io.FileOutputStream;


//TODO construir gerenciador de salvos
public class GerenciadorSalvos {

	private static final String ARQUIVO_SALVAR = "jogo.salvo";

	@SuppressWarnings("unchecked")
	public void salvar(Salvavel salvo) throws Exception {
		List salvos;
		
		try {
			salvos = (List)new XStream().fromXML(new FileInputStream(ARQUIVO_SALVAR));
		} catch(Exception e) {
			salvos = new ArrayList();
		}
		
		if(salvos.isEmpty() || salvo.getIdentificador() >= salvos.size()) {
			salvos.add(salvo);
		} else {
			salvos.set(salvo.getIdentificador().intValue(), salvo);
		}
		
		
		new XStream().toXML(salvos, new FileOutputStream(ARQUIVO_SALVAR));		
	}

	/**
	 * Carrega a configuração do jogo caso não exista o arquivo de configuraçao
	 * cria um novo padrão.
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Salvavel carregar(int posicao) throws Exception {
		List salvos;
		salvos = (List)new XStream().fromXML(new FileInputStream(ARQUIVO_SALVAR));
		Salvavel ret = null;
		
		if(posicao < salvos.size()) {
			ret = (Salvavel) salvos.get(posicao);
		}
		
		return ret;
	}
}
