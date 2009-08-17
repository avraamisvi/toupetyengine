
class fase {

def vai = 0
def acc = 0
def criou = false

def iniciar(dono, motor) {
	dono.setCorFundo(0.843137255f,0.933333333f,0.956862745f)
	motor.tocarMusica("recursos/musica/ice.ogg")
	dono.getJogavel().getEntidade().getArma().configureModoAcumulador()
}

def atualizar(dono, motor) {
		
	def input = motor.getAppGameContainer().getInput();

	if (input.isKeyPressed(input.KEY_F6)) {
		motor.pararMusica()
	}
	
	if (input.isKeyPressed(input.KEY_F5)) {
		motor.tocarMusica("recursos/musica/ice.ogg")
		if (!criou) {
			criou = true
		}
	}		
}


}
