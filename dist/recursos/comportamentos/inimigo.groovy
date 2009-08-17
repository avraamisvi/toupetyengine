class inimigo {
	def morto = false

	def atualizar(dono, motor) {
		if(dono.getEnergiaVital() <= 0 && !morto) {
			if (dono.getAtor().configureAnimacao("MORRENDO_DIREITA")) {
				morto = true
				dono.setTrancarAnimacao(true)
			} else if(dono.getAtor().getAnimacaoAtual().ultimoQuadro() && morto) {
				dono.getAtor().excluir()
			}
		}
	}

	def colisao(dono, evento) {

	}

}
