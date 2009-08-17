package br.org.gamexis.plataforma.cena.componentes;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.gui.GUIContext;

public class CampoTexto extends ToupetyComponente {

	private static final String CARET = "|";

	/** The width of the field */
	private float width;

	/** The height of the field */
	private float height;

	/** The maximum number of characters allowed to be input */
	private int maxCharacter = 10000;

	/** The value stored in the text field */
	private String value = "";

	/** The font used to render text in the field */
	private Font font;

	/** The border color - null if no border */
	private Color border = Color.white;

	/** The text color */
	private Color text = Color.white;

	/** The background color - null if no background */
	private Color background = new Color(0, 0, 0, 0.5f);

	/** The current cursor position */
	private int cursorPos;

	/** True if the cursor should be visible */
	private boolean visibleCursor = true;
	
	/**
	 * Create a new text field
	 * 
	 * @param container
	 *            The container rendering this field
	 * @param font
	 *            The font to use in the text field
	 * @param x
	 *            The x coordinate of the top left corner of the text field
	 * @param y
	 *            The y coordinate of the top left corner of the text field
	 * @param width
	 *            The width of the text field
	 * @param height
	 *            The height of the text field
	 */
	public CampoTexto(GUIContext container, Font font, int x, int y, int width, int height) {
		super(container);

		this.font = font;
		setX(x);
		setY(y);
		this.width = width;
		this.height = height;
				
	}

	/**
	 * Set the background color. Set to null to disable the background
	 * 
	 * @param color
	 *            The color to use for the background
	 */
	public void setBackgroundColor(Color color) {
		background = color;
	}

	/**
	 * Set the border color. Set to null to disable the border
	 * 
	 * @param color
	 *            The color to use for the border
	 */
	public void setBorderColor(Color color) {
		border = color;
	}

	/**
	 * Set the text color.
	 * 
	 * @param color
	 *            The color to use for the text
	 */
	public void setTextColor(Color color) {
		text = color;
	}

	/**
	 * Get the value in the text field
	 * 
	 * @return The value in the text field
	 */
	public String getText() {
		return value;
	}

	/**
	 * Set the value to be displayed in the text field
	 * 
	 * @param value
	 *            The value to be displayed in the text field
	 */
	public void setText(String value) {
		this.value = value;
		if (cursorPos > value.length()) {
			cursorPos = value.length();
		}
	}

	/**
	 * Set the position of the cursor
	 * 
	 * @param pos
	 *            The new position of the cursor
	 */
	public void setCursorPos(int pos) {
		cursorPos = pos;
		if (cursorPos > value.length()) {
			cursorPos = value.length();
		}
	}

	/**
	 * Indicate whether the mouse cursor should be visible or not
	 * 
	 * @param visibleCursor
	 *            True if the mouse cursor should be visible
	 */
	public void setCursorVisible(boolean visibleCursor) {
		this.visibleCursor = visibleCursor;
	}

	/**
	 * Set the length of the allowed input
	 * 
	 * @param length
	 *            The length of the allowed input
	 */
	public void setMaxLength(int length) {
		maxCharacter = length;
		if (value.length() > maxCharacter) {
			value = value.substring(0, maxCharacter);
		}
	}

	/**
	 * @see org.newdawn.slick.gui.AbstractComponent#keyPressed(int, char)
	 */
	public void keyPressed(int key, char c) {
		if (hasFocus()) {			
			if(key == Input.KEY_RSHIFT || key == Input.KEY_LSHIFT) {
				container.getInput().consumeEvent();
				return;
			} else if(key == Input.KEY_F1) {
				container.getInput().consumeEvent();
				return;				
			}
			
			if (key == Input.KEY_LEFT) {
				if (cursorPos > 0) {
					cursorPos--;
				}
			} else if (key == Input.KEY_RIGHT) {
				if (cursorPos < value.length()) {
					cursorPos++;
				}
			} else if (key == Input.KEY_BACK) {
				if ((cursorPos > 0) && (value.length() > 0)) {
					if (cursorPos < value.length()) {
						value = value.substring(0, cursorPos - 1)
								+ value.substring(cursorPos);
					} else {
						value = value.substring(0, cursorPos - 1);
					}
					cursorPos--;
				}
			} else if ((value.length() < maxCharacter)) {//(c < 127) && (c > 31) && 
				int comp = font.getWidth(value+CARET);
				int compC = font.getWidth(""+c);
				
				if((comp+compC) <= getComprimento()) {
					if (cursorPos < value.length()) {
						value = value.substring(0, cursorPos) + c
								+ value.substring(cursorPos);
					} else {
						value = value.substring(0, cursorPos) + c;
					}
					cursorPos++;
				}
			} else if (key == Input.KEY_RETURN) {
				notifyListeners();
			}

			// Nobody more will be notified
			container.getInput().consumeEvent();
		}
	}
		
	@Override
	public void desenhar(Graphics g) {
		// Someone could have set a color for me to blend...
		Color clr = g.getColor();
		g.resetLineWidth();
		
		// g.setClip(x, y - 1, width + 1, height + 1);
		if (background != null) {
			g.setColor(background.multiply(clr));
			g.fillRect(getX(), getY(), width, height);
		}
		if (border != null) {
			g.setColor(border.multiply(clr));
			g.drawRect(getX(), getY(), width, height);
		}
		g.setColor(text);//text.multiply(clr)
		Font temp = g.getFont();

		int cpos = font.getWidth(value.substring(0, cursorPos));
		int tx = 0;
		if (cpos > width) {
			tx = (int)width - cpos - font.getWidth("_");
		}

		g.translate(tx + 2, 0);
		g.setFont(font);
		g.drawString(value, getX() + 1, getY() + 1);

		if (hasFocus() && visibleCursor) {
			g.drawString(CARET, getX() + 1 + cpos + 2, getY() + 1);
		}

		g.translate(-tx - 2, 0);

		// g.clearClip();

		g.setColor(clr);
		g.setFont(temp);
	}

	@Override
	public float getAltura() {
		return height;
	}

	@Override
	public float getComprimento() {
		return width;
	}

	@Override
	public Color getEfeitoCor() {
		return null;
	}

	@Override
	public void setEfeitoCor(Color cor) {
	}

	@Override
	public void desenhar(Graphics g, float fatorX, float fatorY) {
		// TODO ainda nao suportado
		desenhar(g);
	}
}
