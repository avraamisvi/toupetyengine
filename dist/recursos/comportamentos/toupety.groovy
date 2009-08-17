class toupety {
	def usou = false

	def colisao(dono, evento) {
	
	}

	def atualizar(dono, motor) {

			if( dono.getAtor().getNomeAnimacao().indexOf("SUBINDO") >= 0 ) {		
				if(!usou) {
					motor.tocarEfeitoSom("recursos/sons/jump.wav")
					usou = true
				}
			} else {
				usou = false
			}
	}

}
