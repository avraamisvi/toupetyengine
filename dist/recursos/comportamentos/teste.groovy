import br.org.gamexis.plataforma.entidade.TipoEntidade;

class teste {

	def colisao(obj) {
		if( obj.getTipo().equals(TipoEntidade.jogavel)) {
			obj.getArma().configureModoAcumulador()
		}
	}

}
