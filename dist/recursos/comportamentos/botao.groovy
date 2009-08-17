function ratoSoltado(botao, motor)
	comp = botao:getPai():obterComponente("caixa1")	
	
	if comp:isSelecionado() then
		motor:fechar()
	else
		botao:getPai():excluir()
--		motor:getCenaAtual():getJogavel():getEntidade():setControlavel(true)
	end
end