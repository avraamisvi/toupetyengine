class balaoTexto
	def cont = 0;

	def fimTexto(dono, motor) {		
			if(cont == 0) {
				dono.setTexto("Mais um texto \n testando hehehe \n")
				dono.reiniciar()
			}
		
			if(cont == 1 ) {
				dono.setTexto("segundo texto \n testando hehehe \n")
				dono.reiniciar()
			} 
		
			if (cont == 2) {
				dono.setTexto("texto 3 \n testando hehehe \n")
				dono.reiniciar()
			} else {
				if( cont > 3 ){
					dono.excluir()
					dono.setFocus(false)
				}
			}
			cont = cont + 1
	}
}
