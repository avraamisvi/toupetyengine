package br.org.gamexis.plataforma.cena.componentes;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.util.InputAdapter;

import br.org.gamexis.plataforma.cena.Desenho;
import br.org.gamexis.plataforma.script.ScriptComportamento;

//TODO SUBSTITUIR TODOS OS COMPONENTES POR NIFTY COMPONENTS?
public abstract class ToupetyComponente extends InputAdapter implements Desenho {
	
	private ScriptComportamento comportamento;
	
	private boolean visivel;
	
	private boolean excluido;
	
	/** The component that currently has focus */
	private static ToupetyComponente currentFocus = null;
	
	/** The game container */
	protected GUIContext container;

	/** Listeners for the component to notify */
	protected Set listeners;

	/** True if this component currently has focus */
	private boolean focus = false;
	
	private boolean focavel = true;

	/** The input we're responding to */
	private Input input;
	
	private ToupetyComponente pai;
	
	private String nome;
	
	private float x;
	private float y;
	
	/**
	 * Create a new component
	 * 
	 * @param container
	 *            The container displaying this component
	 */
	public ToupetyComponente(GUIContext container) {
		this.container = container;

		listeners = new HashSet();

		input = container.getInput();
		input.addPrimaryListener(this);

		setX(0);
		setY(0);
		setFocus(false);
	}
	
	public void adicionarControler(ComponenteControler controler) {
		listeners.add(controler);
	}

	/**
	 * Remove a component listener.
	 * 
	 * It will ignore if the listener wasn't added.
	 * 
	 * @param listener
	 *            listener
	 */
	public void removeListener(ComponenteControler controler) {
		listeners.remove(controler);
	}

	/**
	 * Notify all the listeners.
	 */
	protected void notifyListeners() {
		Iterator it = listeners.iterator();
		while (it.hasNext()) {
			((ComponenteControler) it.next()).componenteAtivado(this);
		}
	}

	/**
	 * Indicate whether this component should be focused or not
	 * 
	 * @param focus
	 *            if the component should be focused
	 */
	public void setFocus(boolean focus) {
		if(!isFocavel())
			return;
			
		if (focus) {
			if (currentFocus != null && currentFocus != this) {				
				currentFocus.setFocus(false);				
			}
			currentFocus = this;
		} else {
			if (currentFocus == this) {
				currentFocus = null;				
			}
		}
		this.focus = focus;
	}

	/**
	 * Check if this component currently has focus
	 * 
	 * @return if this field currently has focus
	 */
	public boolean hasFocus() {
		return focus;
	}

	/**
	 * Consume the event currently being processed
	 */
	protected void consumeEvent() {
		input.consumeEvent();
	}
	
	/**
	 * Gives the focus to this component with a click of the mouse.
	 * 
	 * @see org.newdawn.slick.gui.AbstractComponent#mouseReleased(int, int, int)
	 */
	public void mouseReleased(int button, int x, int y) {
		setFocus(Rectangle.contains(x, y, getX(), getY(), getComprimento(),
				getAltura()));
	}

	public ScriptComportamento getComportamento() {
		return comportamento;
	}

	public void setComportamento(ScriptComportamento comportamento) {
		this.comportamento = comportamento;
	}
	
	public ToupetyComponente getPai() {
		return pai;
	}
	
	public void setPai(ToupetyComponente pai) {
		this.pai = pai;
	}
	
	@Override
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}

	public float getX() {
		if(pai != null)
			return pai.getX()+x;
		
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		if(pai != null)
			return pai.getY()+y;
		
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
	
	@Override
	public boolean estaVisivel() {
		return visivel;
	}
	
	@Override
	public boolean excluido() {
		return excluido;
	}
	
	@Override
	public void excluir() {
		excluido = true;
		input.removeListener(this);
	}
	
	@Override
	public void setVisivel(boolean visivel) {
		this.visivel = visivel;
	}
	
	public void setFocavel(boolean focavel) {
		this.focavel = focavel;
	}
	
	public boolean isFocavel() {
		return focavel;
	}
	
	private boolean ativado = true;
	
	/**
	 * Desativa o componente impedindo-o de receber eventos.
	 */
	public void desativar(){
		if(ativado) {
			input.removeListener(this);
			ativado = false;
		}
	}

	/**
	 * Ativa um componente habilitando-o a receber eventos
	 */
	public void ativar() {
		if(!ativado) {
			input.addListener(this);
			ativado = true;
		}
	}
	
	public boolean isAtivado() {
		return ativado;
	}
	
	public GUIContext getContainer() {
		return container;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isDisposed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	
}
