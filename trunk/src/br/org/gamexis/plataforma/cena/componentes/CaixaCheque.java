package br.org.gamexis.plataforma.cena.componentes;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.gui.GUIContext;

import br.org.gamexis.plataforma.Motor;

/**
 * Baseado no codigo fonte de mouse over area do slick
 * @author abraao
 *
 */
public class CaixaCheque extends ToupetyComponente {

	private boolean selecionado;
	
	/** The default state */
	private static final int NORMAL = 1;

	/** The mouse down state */
	private static final int MOUSE_DOWN = 2;

	/** The mouse over state */
	private static final int MOUSE_OVER = 3;

	/** The normalImage being displayed in normal state */
	private Image normalImage;
	private Image normalSelecionadoImage;

	/** The normalImage being displayed in mouseOver state */
	private Image mouseOverImage;
	private Image mouseOverSelecionadoImage;

	/** The normalImage being displayed in mouseDown state */
	private Image mouseDownImage;
	private Image mouseDownSelecionadoImage;
	
	/** The colour used in normal state */
	private Color normalColor = Color.white;

	/** The colour used in mouseOver state */
	private Color mouseOverColor = Color.white;

	/** The colour used in mouseDown state */
	private Color mouseDownColor = Color.white;

	/** The sound for mouse over */
	private Sound mouseOverSound;

	/** The sound for mouse down */
	private Sound mouseDownSound;

	/** The shape defining the area */
	private Shape area;

	/** The current normalImage being displayed */
	private Image currentImage;

	/** The current color being used */
	private Color currentColor;

	/** True if the mouse is over the area */
	private boolean over;

	/** True if the mouse button is pressed */
	private boolean mouseDown;

	/** The state of the area */
	private int state = NORMAL;

	/** True if the mouse has been up since last press */
	private boolean mouseUp;

	private GrupoCaixaCheque grupo;

	public CaixaCheque(GUIContext container, Image image, Image selecionadoImage, Shape shape) {
		super(container);

		area = shape;
		area.setX(0);
		area.setY(0);

		normalImage = image;
		currentImage = image;
		mouseOverImage = image;
		mouseDownImage = image;

		normalSelecionadoImage= selecionadoImage;
		mouseOverSelecionadoImage= selecionadoImage;
		mouseDownSelecionadoImage= selecionadoImage;
		
		currentColor = normalColor;

		state = NORMAL;
		Input input = container.getInput();
		over = area.contains(input.getMouseX(), input.getMouseY());
		mouseDown = input.isMouseButtonDown(0);
		updateImage();
		setFocus(false);
	}

	@Override
	public void desenhar(Graphics g) {
		if (currentImage != null) {

			int xp = (int) (getX() + ((getWidth() - currentImage.getWidth()) / 2));
			int yp = (int) (getY() + ((getHeight() - currentImage.getHeight()) / 2));

			currentImage.draw(xp, yp, currentColor);
		} else {
			g.setColor(currentColor);
			g.fill(area);
		}
		updateImage();
	}

	@Override
	public float getAltura() {
		return 0;
	}

	@Override
	public float getComprimento() {
		return 0;
	}

	@Override
	public Color getEfeitoCor() {
		return null;
	}

	@Override
	public void setEfeitoCor(Color cor) {
	}

	/**
	 * Moves the component.
	 * 
	 * @param x
	 *            X coordinate
	 * @param y
	 *            Y coordinate
	 */
	public void setLocation(int x, int y) {
		setX(x);
		setY(y);
	}

	@Override
	public void setX(float x) {
		super.setX(x);

		// if(area != null)
		// area.setX(x);
	}

	@Override
	public void setY(float y) {
		super.setY(y);

		// if(area != null)
		// area.setY(y);
	}

	/**
	 * Set the normal color used on the image in the default state
	 * 
	 * @param color
	 *            The color to be used
	 */
	public void setNormalColor(Color color) {
		normalColor = color;
	}

	/**
	 * Set the color to be used when the mouse is over the area
	 * 
	 * @param color
	 *            The color to be used when the mouse is over the area
	 */
	public void setMouseOverColor(Color color) {
		mouseOverColor = color;
	}

	/**
	 * Set the color to be used when the mouse is down the area
	 * 
	 * @param color
	 *            The color to be used when the mouse is down the area
	 */
	public void setMouseDownColor(Color color) {
		mouseDownColor = color;
	}

	/**
	 * Set the normal image used on the image in the default state
	 * 
	 * @param image
	 *            The image to be used
	 */
	public void setNormalImage(Image image) {
		normalImage = image;
	}

	/**
	 * Set the image to be used when the mouse is over the area
	 * 
	 * @param image
	 *            The image to be used when the mouse is over the area
	 */
	public void setMouseOverImage(Image image) {
		mouseOverImage = image;
	}

	/**
	 * Set the image to be used when the mouse is down the area
	 * 
	 * @param image
	 *            The image to be used when the mouse is down the area
	 */
	public void setMouseDownImage(Image image) {
		mouseDownImage = image;
	}

	/**
	 * Update the current normalImage based on the mouse state
	 */
	private void updateImage() {
		if(selecionado) {
			atualizeSelecionado();
		} else {
			atualizeDesselecionado();
		}
		mouseDown = false;
		state = NORMAL;
	}

	private void atualizeSelecionado() {
		if (!over) {
			currentImage = normalSelecionadoImage;
			currentColor = normalColor;
			state = NORMAL;
			mouseUp = false;
		} else {
			if (mouseDown) {
				if ((state != MOUSE_DOWN) && (mouseUp)) {
					if (mouseDownSound != null) {
						mouseDownSound.play();
					}
					currentImage = mouseDownSelecionadoImage;
					currentColor = mouseDownColor;
					state = MOUSE_DOWN;

					notifyListeners();
					mouseUp = false;
				}
			} else {
				mouseUp = true;
				if (state != MOUSE_OVER) {
					if (mouseOverSound != null) {
						mouseOverSound.play();
					}
					currentImage = mouseOverSelecionadoImage;
					currentColor = mouseOverColor;
					state = MOUSE_OVER;
				}
			}
		}		
	}
	
	private void atualizeDesselecionado() {
		if (!over) {
			currentImage = normalImage;
			currentColor = normalColor;
			state = NORMAL;
			mouseUp = false;
		} else {
			if (mouseDown) {
				if ((state != MOUSE_DOWN) && (mouseUp)) {
					if (mouseDownSound != null) {
						mouseDownSound.play();
					}
					currentImage = mouseDownImage;
					currentColor = mouseDownColor;
					state = MOUSE_DOWN;

					notifyListeners();
					mouseUp = false;
				}
			} else {
				mouseUp = true;
				if (state != MOUSE_OVER) {
					if (mouseOverSound != null) {
						mouseOverSound.play();
					}
					currentImage = mouseOverImage;
					currentColor = mouseOverColor;
					state = MOUSE_OVER;
				}
			}
		}		
	}
	
	/**
	 * Set the mouse over sound effect
	 * 
	 * @param sound
	 *            The mouse over sound effect
	 */
	public void setMouseOverSound(Sound sound) {
		mouseOverSound = sound;
	}

	/**
	 * Set the mouse down sound effect
	 * 
	 * @param sound
	 *            The mouse down sound effect
	 */
	public void setMouseDownSound(Sound sound) {
		//FIXME NAO ESTA ADEQUADA A IMPLEMENTACAO PADRAO, VER BOTAO
		mouseDownSound = sound;
	}

	/**
	 * @see org.newdawn.slick.util.InputAdapter#mouseMoved(int, int, int, int)
	 */
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		over = area.contains(newx - getX(), newy - getY());
	}

	/**
	 * @see org.newdawn.slick.util.InputAdapter#mousePressed(int, int, int)
	 */
	public void mousePressed(int button, int mx, int my) {
		over = area.contains(mx - getX(), my - getY());
		if (button == 0) {
			mouseDown = true;
		}
	}

	/**
	 * Chamada quando o botão do mouse for solto e com o ponteiro sobre o componente.
	 */
	public void mouseReleased(int button, int mx, int my) {

		over = area.contains(mx - getX(), my - getY());
		if (button == 0) {
			mouseDown = false;
		}

		if (over) {
			
			if(grupo != null && selecionado)
				return;
			
			selecionado = !selecionado;
			
			if(selecionado) {
				if(grupo != null)
					grupo.notificarSelecao(this);
			}
			
			if (getComportamento() != null) {

				try {
					getComportamento().execute("ratoSoltado", new Object[] { this, Motor.obterInstancia() });
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
						
		}
	}

	/**
	 * @see org.newdawn.slick.gui.AbstractComponent#getHeight()
	 */
	public int getHeight() {
		return (int) (area.getMaxY() - area.getY());
	}

	/**
	 * @see org.newdawn.slick.gui.AbstractComponent#getWidth()
	 */
	public int getWidth() {
		return (int) (area.getMaxX() - area.getX());
	}

	public boolean isSelecionado() {
		return selecionado;
	}

	public void setSelecionado(boolean selecionado) {
		this.selecionado = selecionado;
	}

	public void setNormalSelecionadoImage(Image normalSelecionadoImage) {
		this.normalSelecionadoImage = normalSelecionadoImage;
	}

	public void setMouseOverSelecionadoImage(Image mouseOverSelecionadoImage) {
		this.mouseOverSelecionadoImage = mouseOverSelecionadoImage;
	}

	public void setMouseDownSelecionadoImage(Image mouseDownSelecionadoImage) {
		this.mouseDownSelecionadoImage = mouseDownSelecionadoImage;
	}	
	
	public void setGrupo(GrupoCaixaCheque grupo) {
		this.grupo = grupo;
		grupo.adicionar(this);
	}
	
	@Override
	public void desenhar(Graphics g, float fatorX, float fatorY) {
		// TODO ainda nao suportado
		desenhar(g);
	}
}
