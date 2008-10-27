package br.org.gamexis.plataforma.cena.componentes;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.gui.GUIContext;

import br.org.gamexis.plataforma.Motor;
import br.org.gamexis.plataforma.motor.RecursosFactory;
import br.org.gamexis.plataforma.script.ScriptException;

public class Botao extends ToupetyComponente {

	/** The default state */
	private static final int NORMAL = 1;

	/** The mouse down state */
	private static final int MOUSE_DOWN = 2;

	/** The mouse over state */
	private static final int MOUSE_OVER = 3;

	/** The normalImage being displayed in normal state */
	private Image normalImage;

	/** The normalImage being displayed in mouseOver state */
	private Image mouseOverImage;

	/** The normalImage being displayed in mouseDown state */
	private Image mouseDownImage;

	/** The colour used in normal state */
	private Color normalColor = Color.white;

	/** The colour used in mouseOver state */
	private Color mouseOverColor = Color.white;

	/** The colour used in mouseDown state */
	private Color mouseDownColor = Color.white;

	/** The sound for mouse over */
	private String mouseOverSound;

	/** The sound for mouse down */
	private String mouseDownSound;

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
	
	/**
	 * Indica se um som ja foi tocado para o estado mouse sobre num certo instante.
	 */
	private boolean tocouSobreSom = false;
	
	/**
	 * Indica se um som ja foi tocado para o estado mouse clicado num certo instante.
	 */
	private boolean tocouClicadoSom = false;
	
	/**
	 * Create a new mouse over area
	 * 
	 * @param container
	 *            The container displaying the mouse over area
	 * @param image
	 *            The normalImage to display
	 * @param shape
	 *            The shape defining the area of the mouse sensitive zone
	 */
	public Botao(GUIContext container, Image image, Shape shape) {
		super(container);

		area = shape;
		area.setX(0);
		area.setY(0);

		normalImage = image;
		currentImage = image;
		mouseOverImage = image;
		mouseDownImage = image;

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

			int xp = (int) (getX() + ((getComprimento() - currentImage.getWidth()) / 2));
			int yp = (int) (getY() + ((getAltura() - currentImage.getHeight()) / 2));

			currentImage.draw(xp, yp, currentColor);
		} else {
			g.setColor(currentColor);
			g.fill(area);
		}
		updateImage();
	}

	@Override
	public float getAltura() {
		return (area.getMaxY() - area.getY());
	}

	@Override
	public float getComprimento() {
		return (area.getMaxX() - area.getX());
	}

//	public int getHeight() {
//		return (int) (area.getMaxY() - area.getY());
//	}
//
//	public int getWidth() {
//		return (int) (area.getMaxX() - area.getX());
//	}
	
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
		if (!over) {
			currentImage = normalImage;
			currentColor = normalColor;
			state = NORMAL;
			mouseUp = false;
			tocouSobreSom = false;
		} else {
			if (mouseDown) {
				if ((state != MOUSE_DOWN) && (mouseUp)) {
					if (mouseDownSound != null) {
						if(!tocouClicadoSom) {
							Motor.obterInstancia().tocarEfeitoSom(mouseDownSound);
							tocouClicadoSom = true;
						}
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
						if(!tocouSobreSom) {
							Motor.obterInstancia().tocarEfeitoSom(mouseOverSound);
							tocouSobreSom = true;
						}
					}
					currentImage = mouseOverImage;
					currentColor = mouseOverColor;
					state = MOUSE_OVER;
				}
			}
		}

		mouseDown = false;
		state = NORMAL;
	}

	/**
	 * Set the mouse over sound effect
	 * 
	 * @param sound
	 *            The mouse over sound effect
	 */
	public void setMouseOverSound(String sound) {
		mouseOverSound = RecursosFactory.diretorioSom + sound;
	}

	/**
	 * Set the mouse down sound effect
	 * 
	 * @param sound
	 *            The mouse down sound effect
	 */
	public void setMouseDownSound(String sound) {
		mouseDownSound = RecursosFactory.diretorioSom + sound;
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
			tocouClicadoSom = false;
		} else {
			over = false;
		}
		
		if (over) {
			if (getComportamento() != null) {

				try {
					getComportamento().execute("ratoSoltado", new Object[] { this, Motor.obterInstancia() });					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
