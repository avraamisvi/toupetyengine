function aoSalvar(motor, salvo)
--aki devo colocar todos os valores que devem ser salvos no jogo
	
	if(motor:getCenaAtual():getJogavel():getEntidade():getArma():modoAcumulador()) then
		flag = luajava.newInstance("java.lang.Integer", 1)
		salvo:setVariavel("carregavel", flag)	
	else
		flag = luajava.newInstance("java.lang.Integer", 0)
		salvo:setVariavel("carregavel", flag)
	end
	 
end

function aoCarregar(motor, salvo)
-- aki devo setar a fase atual, condições do personagem, vairaveis de ambiente etc
	flag = salvo:getValorVariavel("carregavel")
	
	if flag == 1 then
		motor:getCenaAtual():getJogavel():getEntidade():getArma():configureModoAcumulador()		
	end
end
