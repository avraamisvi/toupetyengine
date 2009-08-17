package br.org.gamexis.plataforma.cena;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.phys2d.math.Vector2f;
import net.phys2d.raw.Body;
import net.phys2d.raw.CollisionEvent;
import net.phys2d.raw.World;
import net.phys2d.raw.shapes.AABox;
import net.phys2d.raw.shapes.Box;
import net.phys2d.raw.shapes.Circle;

import org.lwjgl.Sys;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import br.org.gamexis.plataforma.Motor;
import br.org.gamexis.plataforma.cena.colisao.CorpoColisao;
import br.org.gamexis.plataforma.cena.colisao.RetanguloColisao;
import br.org.gamexis.plataforma.cena.fisica.SuperCorpo;
import br.org.gamexis.plataforma.entidade.Entidade;
import br.org.gamexis.plataforma.eventos.EventoAtualizar;

public class Ator implements Desenho, Colisivel, Atualizavel {

	private World mundoFisico;

	public static int contadorInterno = 0;

	public boolean disposed = false;

	private Color efeitoCor = Color.white;
	private boolean visivel = true;
	private Entidade entidade;
	private float x;
	private float y;

	private float fatorXPadrao = 1;
	private float fatorYPadrao = 1;

	private boolean controlavel = true;
	private boolean parado = false;
	private boolean andando = false;
	private boolean pulando = false;
	private boolean noar = false;
	private boolean voando = false;
	private boolean caindo = false;
	private boolean quicando = false;
	private boolean escorregando = false;

	private Animado atual;// ANIMAÇÃO ATUAL
	private HashMap<String, Animado> animacoes = new HashMap<String, Animado>();

	// Relação de efeitos por nome
	private HashMap<String, Animado> efeitosPorNome = new HashMap<String, Animado>();
	private List<Animado> efeitos = new ArrayList<Animado>();
	// são desenhados por cima da animação
	private List<Animado> efeitosPrioritarios = new ArrayList<Animado>();

	private String animNome = "";
	private Body corpo;
	private AtorFaceOrientacao face;
	private long tempoEstado;
	private String nome;

	// Embora exista a caixa de colisão não é avaliada pelo motor, ficando a
	// cargo do
	// designer codificar o seu comportamento no script
	private HashMap<String, CorpoColisao> caixasColisao = new HashMap<String, CorpoColisao>();

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Ator(Body corpo) {
		this.corpo = corpo;
		corpo.setPrivateData(this);
		corpo.setRotatable(false);
		contadorInterno++;
	}

	public void setCorpo(Body corpo) {
		this.corpo = corpo;
		corpo.setPrivateData(this);
		corpo.setRotatable(false);
	}

	public Ator(Ator copia) {
		try {
			contadorInterno++;
			efeitoCor = copia.efeitoCor;
			visivel = copia.visivel;
			entidade = copia.entidade;
			x = copia.y;
			y = copia.x;

			controlavel = copia.controlavel;
			parado = copia.parado;
			andando = copia.andando;
			pulando = copia.pulando;
			noar = copia.noar;
			voando = copia.voando;
			caindo = copia.caindo;
			quicando = copia.quicando;
			escorregando = copia.escorregando;

			// atual= (Animado)copia.atual;// ANIMAÇÃO ATUAL
			// animacoes = copia.animacoes;

			atual = (Animado) copia.atual.clone();// ANIMAÇÃO ATUAL
			for (String chave : copia.animacoes.keySet()) {
				adicioneAnimacao(chave, (Animado) copia.animacoes.get(chave)
						.clone());
			}

			// Relação de efeitos por nome
			// efeitosPorNome = new HashMap<String, Animado>();
			// efeitos = new ArrayList<Animado>();
			// efeitosPrioritarios = copia.efeitosPrioritarios;

			// clonar o corpo n podem ter o mesmo corpo
			float comp = copia.corpo.getShape().getBounds().getWidth();
			float alt = copia.corpo.getShape().getBounds().getHeight();
			float peso = copia.corpo.getMass();
			boolean gravidade = copia.corpo.getGravityEffected();
			boolean rotat = copia.corpo.isRotatable();

			Box box = new Box(comp, alt);
			corpo = new Body("GXAtor" + Ator.contadorInterno, box, peso);
			corpo.setGravityEffected(gravidade);
			corpo.setRotatable(rotat);
			corpo.setPrivateData(this);

			face = copia.face;
			tempoEstado = copia.tempoEstado;
			nome = copia.nome;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return Retorna o tempo em milisegundos
	 */
	private long getTempo() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	public void setEntidade(Entidade entidade) {
		this.entidade = entidade;
	}

	public void setFace(AtorFaceOrientacao face) {
		this.face = face;
	}

	public AtorFaceOrientacao getFace() {
		return face;
	}

	public Entidade getEntidade() {
		return entidade;
	}

	public String getNomeAnimacao() {
		return animNome;
	}

	public boolean isAnimacao(String nome) {
		return animNome.toUpperCase().contains(nome);
	}

	public void adcionarEfeito(Animado anim) {
		efeitos.add(anim);
		efeitosPorNome.put(anim.getNome(), anim);
	}

	public void adcionarEfeitoPrioritario(Animado anim) {
		efeitosPrioritarios.add(anim);
		efeitosPorNome.put(anim.getNome(), anim);
	}

	public boolean configureAnimacao(String animacao) {
		return configureAnimacao(animacao, false);
	}

	public boolean configureAnimacao(String animacao, boolean verificar) {
		if (verificar && animacao.equals(animNome))
			return true;

		if (animacoes.containsKey(animacao)) {
			animNome = animacao;

			atual = animacoes.get(animacao);
			atual.setX(x);
			atual.setY(y);
			atual.reiniciar();
			return true;
		}

		return false;
	}

	public void adicioneAnimacao(String nome, Animado animacao) {
		animacoes.put(nome, animacao);
	}

	public void atualizar(int delta) {

		x = corpo.getPosition().getX();
		y = corpo.getPosition().getY();

		atual.setX(x);
		atual.setY(y);
		atual.atualizar(delta);
		tempoEstado = tempoEstado + delta;

		for (Animado an : efeitos) {// EFEITOS
			an.atualizar(delta);
		}

		for (Animado an : efeitosPrioritarios) {// EFEITOS
			an.atualizar(delta);
		}
		// RODA O SCRIPT
		try {
			EventoAtualizar atualizar = new EventoAtualizar(delta);
			entidade.atualizar(atualizar);
		} catch (Throwable e) {
			tratarExcecao(e);
		}

		limparEfeitosExcluidos();
	}

	public void desenhar(Graphics g) {

		if (visivel) {
			for (Animado an : efeitosPrioritarios) {// EFEITOS
				an.desenhar(g, fatorXPadrao, fatorYPadrao);
			}

			x = corpo.getPosition().getX();
			y = corpo.getPosition().getY();
			atual.setX((int) x);
			atual.setY((int) y);

			atual.setEfeitoCor(getEfeitoCor());
			atual.desenhar(g, fatorXPadrao, fatorYPadrao);

			for (Animado an : efeitos) {
				try {
					an.desenhar(g, fatorXPadrao, fatorYPadrao);
				} catch (NullPointerException e) {
					e.printStackTrace();
					throw e;
				}
			}
		}

		if (Motor.obterInstancia().isModoDebug()) {
			Color tc = g.getColor();
			g.setColor(Color.darkGray);
			g.setColor(tc);

			g.drawString("Ator:" + getNome() + " X:" + (x) + " Y:" + (y), x, y);
			g.drawString("Tipo entidade:" + getEntidade().getTipo().toString(),
					x, y - 30);

			desenharCaixasColisao(g);
		}
	}

	public Animado getAnimacaoAtual() {
		return atual;
	}

	@Override
	public float getX() {
		return corpo.getPosition().getX();
	}

	@Override
	public float getY() {
		return corpo.getPosition().getY();
	}

	public void configurePosicao(float x, float y) {
		if (corpo instanceof SuperCorpo) {
			((SuperCorpo) corpo).moveAll(x, y);
		} else {
			corpo.move(x, y);
		}
	}

	@Override
	public void setX(float x) {
		this.x = x;
		corpo.setPosition(x, corpo.getPosition().getY());
	}

	@Override
	public void setY(float y) {
		this.y = y;
		corpo.setPosition(corpo.getPosition().getX(), y);
	}

	public boolean isControlavel() {
		return controlavel;
	}

	public void setControlavel(boolean controlavel) {
		this.controlavel = controlavel;
	}

	public boolean isPulando() {
		return pulando;
	}

	/**
	 * Configura todos os estados como falsos.
	 */
	private void setEstadosFalso() {
		parado = false;
		pulando = false;
		caindo = false;
		quicando = false;
		escorregando = false;
		andando = false;
		noar = false;
		voando = false;

		tempoEstado = 0;// ZERA O CONTADOR
	}

	public void setAndando(boolean andando) {
		setEstadosFalso();
		this.andando = andando;
	}

	public boolean isAndando() {
		return andando;
	}

	public boolean isNoar() {
		return noar;
	}

	public void setNoar(boolean noar) {
		setEstadosFalso();
		this.noar = noar;
	}

	public void setParado(boolean parado) {
		setEstadosFalso();
		this.parado = parado;
	}

	public boolean isParado() {
		return parado;
	}

	public void setPulando(boolean pulando) {
		setEstadosFalso();
		this.pulando = pulando;
	}

	public boolean isCaindo() {
		return caindo;
	}

	public void setCaindo(boolean caindo) {
		setEstadosFalso();
		this.caindo = caindo;
	}

	public boolean isVoando() {
		return voando;
	}

	public void setVoando(boolean voando) {
		setEstadosFalso();
		this.voando = voando;
	}

	public boolean isEscorregando() {
		return escorregando;
	}

	public void setEscorregando(boolean escorregando) {
		setEstadosFalso();
		this.escorregando = escorregando;
	}

	public boolean isQuicando() {
		return quicando;
	}

	public void setQuicando(boolean quicando) {
		setEstadosFalso();
		this.quicando = quicando;
	}

	public void zerarVelocidadeX() {
		Vector2f v = new Vector2f(corpo.getVelocity().getX(), 0);
		v.scale(-1);
		corpo.adjustVelocity(v);
	}

	public void zerarVelocidadeY() {
		Vector2f v = new Vector2f(0, corpo.getVelocity().getY());
		v.scale(-1);
		corpo.adjustVelocity(v);
	}

	public float getVelocidadeX() {
		return corpo.getVelocity().getX();
	}

	public void setVelocidadeX(float velocidadeX) {
		// float vy = corpo.getVelocity().getY();
		corpo.adjustVelocity(new Vector2f(velocidadeX, 0));
	}

	public float getVelocidadeY() {
		return corpo.getVelocity().getY();
	}

	public void setVelocidadeY(float velocidadeY) {
		// float vx = corpo.getVelocity().getX();
		corpo.adjustVelocity(new Vector2f(0, velocidadeY));
	}

	public Body getCorpo() {
		return corpo;
	}

	public float getComprimento() {
		return getCorpo().getShape().getBounds().getWidth();
	}

	public float getAltura() {
		return getCorpo().getShape().getBounds().getHeight();
	}

	/**
	 * Tempo em que permanece naquele estado.
	 * 
	 * @return
	 */
	public long getTempoEstado() {
		return tempoEstado;
	}

	public void tratarExcecao(Throwable cause) {
	}

	private boolean excluido = false;

	@Override
	public void excluir() {
		excluido = true;
		contadorInterno--;
	}

	@Override
	public boolean excluido() {
		return excluido;
	}

	public void removaExclusao() {// SUBSTITUIR POR CLONE?
		excluido = false;
	}

	@Override
	public boolean estaVisivel() {
		return visivel;
	}

	@Override
	public void setVisivel(boolean visivel) {
		this.visivel = visivel;
	}

	public void setAfetadoGravidade(boolean gravidade) {
		corpo.setGravityEffected(gravidade);
	}

	public Efeito obterEfeitoPorNome(String nomeEfeito) {
		return (Efeito) efeitosPorNome.get(nomeEfeito);
	}

	public void excluirEfeitoPorNome(String nomeEfeito) {
		try {
			efeitosPorNome.get(nomeEfeito).excluir();
			efeitosPrioritarios.remove(efeitosPorNome.get(nomeEfeito));
			efeitos.remove(efeitosPorNome.get(nomeEfeito));
			efeitosPorNome.remove(nomeEfeito);// JA REMOVE
		} catch (Exception e) {
		}
	}

	/**
	 * Limpa os efeitos excluidos.
	 */
	public void limparEfeitosExcluidos() {
		ArrayList<Animado> efeitosTemp = new ArrayList<Animado>();

		for (Animado an : efeitosPrioritarios) {
			if (!an.excluido()) {
				efeitosTemp.add(an);
			} else {
				efeitosPorNome.remove(an.getNome());
			}
		}
		efeitosPrioritarios = efeitosTemp;

		efeitosTemp = new ArrayList<Animado>();
		for (Animado an : efeitos) {
			if (!an.excluido()) {
				efeitosTemp.add(an);
			} else {
				efeitosPorNome.remove(an.getNome());
			}
		}
		efeitos = efeitosTemp;
	}

	public void limparEfeitos() {
		efeitos = new ArrayList<Animado>();
		efeitosPrioritarios = new ArrayList<Animado>();
		efeitosPorNome = new HashMap<String, Animado>();
	}

	@Override
	public Color getEfeitoCor() {
		return efeitoCor;
	}

	@Override
	public void setEfeitoCor(Color cor) {
		this.efeitoCor = cor;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return new Ator(this);
	}

	public void excluirColisao(Colisivel colisivel) {
		getCorpo().addExcludedBody(colisivel.getCorpo());
//		colisivel.getCorpo().addExcludedBody(getCorpo());
	}

	public void habilitarColisao(Colisivel colisivel) {
		getCorpo().removeExcludedBody(colisivel.getCorpo());
	}
	
	public void desabilitarColisao() {
		getCorpo().setCanColide(false);
	}

	public void habilitarColisao() {
		getCorpo().setCanColide(true);
	}

	private Vector2f getSize(Body corpo) {
		Vector2f ret = null;

		if (corpo.getShape() instanceof Box) {
			Box b = (Box) corpo.getShape();

			ret = new Vector2f(b.getSize().getX(), b.getSize().getY());
		} else if (corpo.getShape() instanceof Circle) {
			Circle c = (Circle) corpo.getShape();
			ret = new Vector2f(c.getRadius() * 2, c.getRadius() * 2);
		}

		return ret;
	}

	public boolean estaTocando(Ator ator) {

		float x1 = corpo.getPosition().getX();
		float y1 = corpo.getPosition().getY();

		float x2 = ator.getCorpo().getPosition().getX();
		float y2 = ator.getCorpo().getPosition().getY();

		Vector2f v1 = getSize(corpo);
		AABox ab1 = new AABox(v1.x, v1.y);

		v1 = getSize(ator.getCorpo());
		AABox ab2 = new AABox(v1.x, v1.y);

		return ab1.touches(x1, y1, ab2, x2, y2);
	}

	/**
	 * Simula caixas de colisão, para permitir analisar se ator esta tocando
	 * caixa de colisão virtual.
	 * 
	 * @param comprimento
	 * @param altura
	 * @param x
	 * @param y
	 * @return true se esta colidindo, false caso contrario.
	 */
	public boolean estaTocando(float comprimento, float altura, float x, float y) {

		float x1 = corpo.getPosition().getX();
		float y1 = corpo.getPosition().getY();

		float x2 = x;
		float y2 = y;

		Vector2f v1 = getSize(corpo);
		AABox ab1 = new AABox(v1.x, v1.y);

		AABox ab2 = new AABox(comprimento, altura);

		return ab1.touches(x1, y1, ab2, x2, y2);
	}

	public void ativarGravidade() {
		corpo.setGravityEffected(true);
	}

	public void desativarGravidade() {
		corpo.setGravityEffected(false);
	}

	public void setRotavel(boolean rotavel) {
		corpo.setRotatable(rotavel);
	}

	public void adicionarChaveExclusao(Object chave) {
		corpo.addExclusionKey(chave);
	}

	// TALVEZ INUTIL
	public void dispose() {
		corpo = null;
		atual = null;

		for (Animado ani : animacoes.values()) {
			ani.dispose();
		}

		animacoes = null;
		efeitosPorNome = null;
		efeitos = null;
		efeitosPrioritarios = null;
		disposed = true;
	}

	@Override
	public boolean isDisposed() {
		return disposed;
	}

	public void setVelocidadeYMaxima(float vy) {
		corpo.setMaxVelocY(vy);
	}

	public void setVelocidadeXMaxima(float vx) {
		corpo.setMaxVelocX(vx);
	}

	public float getVelocidadeYMaxima() {
		return corpo.getMaxVelocY();
	}

	public float getVelocidadeXMaxima() {
		return corpo.getMaxVelocX();
	}

	/**
	 * 
	 * @param comprimento
	 * @param altura
	 * @param x
	 *            deslocamento positivo em x(ou seja posicao x do jogavel + x de
	 *            deslocamento)
	 * @param y
	 *            deslocamento positivo em y
	 */
	public void adicionarCaixaColisao(float comprimento, float altura, float x,
			float y, String animacao, int frame, String nome) {

		CorpoColisao corpoColisao = caixasColisao.get((animacao + "_" + frame).toUpperCase());
		if (corpoColisao == null) {
			corpoColisao = new CorpoColisao();
			caixasColisao.put((animacao + "_" + frame).toUpperCase(), corpoColisao);
			corpoColisao.setDono(this);
		}

		corpoColisao.adicionarRectangulo(nome.toUpperCase(), new Rectangle(x, y, comprimento,
				altura));

	}

	/**
	 * Verifica se ator esta colidindo com alguma a caixa de colisão passada.
	 * 
	 * @param ator
	 *            @ * @return retorna o false caso não haja colisão true caso
	 *            contrario.
	 */
	public String colideCaixaColisao(Ator ator) {

		CorpoColisao corpoColisao = getCorpoColisao();
		CorpoColisao corpoColisao2;
		String saida = null;

		if (corpoColisao != null) {

			saida = corpoColisao.colide(ator.getComprimento(),
					ator.getAltura(), ator.getX(), ator.getY());

			if (saida == null) {
				corpoColisao2 = ator.getCorpoColisao();
				if (corpoColisao2 != null) {
					// COLIDE COM O CORPO

					for (RetanguloColisao retCol : corpoColisao2
							.getCaixasColisao()) {

						saida = corpoColisao.colide(retCol.getRectangle()
								.getWidth(), retCol.getRectangle().getHeight(),
								retCol.getRectangle().getX() + ator.getX(),
								retCol.getRectangle().getY() + ator.getY());

						if (saida != null)
							break;
					}
				}
			}
		}

		return saida;
	}

	public CorpoColisao getCorpoColisao() {
		int frame = 0;
		CorpoColisao ret = null;

		if (getAnimacaoAtual() instanceof Animacao) {
			frame = ((Animacao) getAnimacaoAtual()).getQuadroAtual();
			ret = caixasColisao.get((getNomeAnimacao() + "_" + frame).toUpperCase());
		} else if (getAnimacaoAtual() instanceof AnimacaoMista) {
			Animacao anim = ((AnimacaoMista)getAnimacaoAtual()).getAnimacao();
			frame = anim.getQuadroAtual();
			ret = caixasColisao.get((getNomeAnimacao() + "_" + frame).toUpperCase());
		}

		return ret;
	}

	/**
	 * Desenha as caixas de colisão.
	 * 
	 * @param g
	 */
	private void desenharCaixasColisao(Graphics g) {

		Color tc = g.getColor();
		int cor = 0;

		g.setColor(new Color(1, 0, 0, 0.5f));

		CorpoColisao corpoColisao = getCorpoColisao();
		if (corpoColisao != null) {
			for (RetanguloColisao ret : corpoColisao.getCaixasColisao()) {
				Rectangle c = ret.getRectangle();
				g.fillRect(getX() + c.getX(), getY() + c.getY(), c.getWidth(),	c.getHeight());
				if (cor == 0) {
					cor = 1;
					g.setColor(new Color(1, 0.3f, 0, 0.5f));
				}

				if (cor == 1) {
					cor = 2;
					g.setColor(new Color(1, 0.3f, 0.3f, 0.5f));
				}

				if (cor == 2) {
					cor = 0;
					g.setColor(new Color(1, 0, 0, 0.5f));
				}
			}
		}
		g.setColor(tc);
	}

	public void setMundoFisico(World mundoFisico) {
		this.mundoFisico = mundoFisico;
	}

	public boolean estaNochao() {
		Body body = this.getCorpo();

		if (mundoFisico == null) {
			return false;
		}

		// loop through the collision events that have occured in the
		// world
		CollisionEvent[] events = mundoFisico.getContacts(body);

		for (int i = 0; i < events.length; i++) {
			// if the point of collision was below the centre of the actor
			// i.e. near the feet
			if (events[i].getPoint().getY() > getY() + (getAltura() / 4)) {
				// check the normal to work out which body we care about
				// if the right body is involved and a collision has happened
				// below it then we're on the ground
				if (events[i].getNormal().getY() < -0) {
					if (events[i].getBodyB() == body) {
						return true;
					}
				}
				if (events[i].getNormal().getY() > 0) {
					if (events[i].getBodyA() == body) {
						return true;
					}
				}
			}
		}

		return false;
	}

	@Override
	public void desenhar(Graphics g, float fatorX, float fatorY) {
		if (visivel) {
			for (Animado an : efeitosPrioritarios) {// EFEITOS
				an.desenhar(g, fatorX, fatorY);
			}

			x = corpo.getPosition().getX();
			y = corpo.getPosition().getY();
			atual.setX((int) x);
			atual.setY((int) y);

			atual.setEfeitoCor(getEfeitoCor());
			atual.desenhar(g, fatorX, fatorY);

			for (Animado an : efeitos) {// EFEITOS
				an.desenhar(g, fatorX, fatorY);
			}
		}

		if (Motor.obterInstancia().isModoDebug()) {
			Color tc = g.getColor();
			g.setColor(Color.darkGray);
			g.setColor(tc);

			g.drawString("Ator:" + getNome() + " X:" + (x) + " Y:" + (y), x, y);
			g.drawString("Tipo entidade:" + getEntidade().getTipo().toString(),
					x, y - 30);

			desenharCaixasColisao(g);
		}
	}

	public float getFatorX() {
		return fatorXPadrao;
	}

	public void setFatorX(float fatorX) {
		this.fatorXPadrao = fatorX;
	}

	public float getFatorY() {
		return fatorYPadrao;
	}

	public void setFatorY(float fatorY) {
		this.fatorYPadrao = fatorY;
	}

}
