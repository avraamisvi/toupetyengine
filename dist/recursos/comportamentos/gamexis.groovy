class gamexis {
	def som = null
	def mudar = false

	def iniciar(dono, motor) {
		dono.setCorFundo(0,0,0)
		som = motor.tocarEfeitoSom("recursos/sons/thunder_1.wav")
	}

	def atualizar(dono, motor) {
		if (!mudar) {		
			if (!som.estaTocando()) {
				mudar = true
				motor.mudarCena("demo")
			}
		}
	}
}
