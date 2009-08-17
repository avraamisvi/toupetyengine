class borboleta {

def colisao(dono, evento) {
}

def atualizar(dono, motor) {
	def cena = motor.getCenaAtual()
	def jogavel = cena.getJogavel()
	def borb = dono.getAtor()
	
	if (borb.getVelocidadeX() == 0 && borb.getX() > jogavel.getX()) {
		dono.getAtor().setVelocidadeX(-20)
	} else {
		if (borb.getVelocidadeX() == 0 && borb.getX() < jogavel.getX()) {
			dono.getAtor().setVelocidadeX(20)
		} else {
			if (borb.getVelocidadeX() != 0) {
				def v = borb.getVelocidadeX()
				borb.setVelocidadeX(-v)
			}
		}	
	}
}
}
