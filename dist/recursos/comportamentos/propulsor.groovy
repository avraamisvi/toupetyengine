import br.org.gamexis.plataforma.entidade.TipoEntidade;

class propulsor {
	def som = null
	def usado = false
	def colidiu = false

	def colisao(dono, evento) {
		def motor = evento.getMotor()
		def obj = evento.getFonte()
		
		if( obj.getTipo().equals(TipoEntidade.jogavel)) {
			colidiu = true	
			evento.getCena().setPausado(true, false)
			motor.getCenaAtual().setControlesTravados(true)
			motor.pararMusica()
			som = motor.tocarEfeitoSom("recursos/sons/novoItem.ogg")		
		 } else {
			def at = dono.getAtor()
			def v = at.getVelocidadeX()
			at.setVelocidadeX(-v)
			v = at.getVelocidadeY()
			at.setVelocidadeY(-v)
		}
	}

	def atualizar(dono, motor) {
		if((!usado) && colidiu) {
			if (som == null || !som.estaTocando()) {
				usado = true
				def cena = motor.getCenaAtual()
				def jogavel = cena.getJogavel()
				jogavel.getEntidade().habilitaFoguetePropulsor(true)
				motor.getCenaAtual().getPainelEstado().getBarraEstado("combustivel").setVisivel(true)			
				motor.getCenaAtual().setPausado(false)
				motor.getCenaAtual().setControlesTravados(false)
				dono.getAtor().excluir()
			}
		}
	}
}
