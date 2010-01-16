package br.org.gamexis.plataforma.debug;

import groovy.lang.GroovyShell;
import groovy.lang.Script;

import org.codehaus.groovy.control.CompilationFailedException;

import br.org.gamexis.plataforma.Motor;
import br.org.gamexis.plataforma.motor.RecursosFactory;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.console.ConsoleCommandHandler;
import de.lessvoid.nifty.controls.console.ConsoleControl;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.mapping.Default;
import de.lessvoid.nifty.screen.KeyInputHandler;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * MainMenu.
 * @author void
 *
 */
public class ConsoleControler implements ScreenController, KeyInputHandler {

  /**
   * the nifty.
   */
  private Nifty nifty;

  /**
   * screen.
   */
  private Screen screen;

  private GroovyShell shell = new GroovyShell();
  private Script script;
	
  /**
   * bind this ScreenController to a screen.
   * @param newNifty nifty
   * @param newScreen screen
   */
  public final void bind(
      final Nifty newNifty,
      final Screen newScreen) {
    nifty = newNifty;

    screen = newScreen;
    screen.addKeyboardInputHandler(new Default(), this);
    
    final Element element = nifty.getCurrentScreen().findElementByName("console");
    //element.hide();
    element.enable();
    element.setFocus();
    
    final ConsoleControl control = (ConsoleControl) element.getAttachedInputControl().getController();
    control.output("Toupety Console");
    control.addCommandHandler(new ConsoleCommandHandler() {
      public void execute(final String line) {
        if ("exit".equals(line.toLowerCase())) {
          back();
        } else if(line != null && !line.isEmpty()) {
        	String[] set = line.split("=");
        	String cmd = set[0].trim();
        	
        	if("mudar_cena".equalsIgnoreCase(cmd)) {
        		String param = set[1].trim();
        		mudarCena(param);
        	} else if("script".equalsIgnoreCase(cmd)) {
        		String script = line.substring("script=".length(), line.length());
        		System.out.println("script:" + script);
        		executar(script);
        	} else if(line.equalsIgnoreCase("desativar_som")) {//TODO construir isso
        		if(line.contains("true")) {
        			Motor.obterInstancia().getConfigGeral().getSom().setAlturaEfeitos(0);
        			Motor.obterInstancia().aplicarConfiguracaoSom();
        		}
        	} else if(line.equalsIgnoreCase("ativar_som")) {
    			Motor.obterInstancia().getConfigGeral().getSom().setAlturaEfeitos(100);
    			Motor.obterInstancia().aplicarConfiguracaoSom();
        	} else if(line.equalsIgnoreCase("ativar_foguete")) {
    			Motor.obterInstancia().getJogavelCarregado().habilitaFoguetePropulsor(true);
        	} else if(line.equalsIgnoreCase("ativar_teleporte")) {
        		Motor.obterInstancia().getJogavelCarregado().setTeleporteDestravado(true);
        	} else if(line.equalsIgnoreCase("reiniciar_jogavel")) {//TODO bugado
        		Motor.obterInstancia().getJogavelCarregado().reiniciar();
        	} else if(line.equalsIgnoreCase("ativar_bazuca")) {
    			Motor.obterInstancia().getJogavelCarregado().setBazucahabilitada(true);
        	} else if(line.equalsIgnoreCase("ativar_fogo")) {
    			Motor.obterInstancia().getJogavelCarregado().setArmaFogoHabilitada(true);
        	} else if(line.equalsIgnoreCase("ativar_gelo")) {
    			Motor.obterInstancia().getJogavelCarregado().setArmaGeloHabilitada(true);
        	} else if(line.equalsIgnoreCase("ativar_escudo")) {
    			Motor.obterInstancia().getJogavelCarregado().getEscudo().destravar();
        	} else if(line.equalsIgnoreCase("fechar")) {
    			Motor.obterInstancia().fechar();
        	}
        }
      }
    });
  }

  /**
   * just goto the next screen.
   */
  public final void onStartScreen() {
	  definirFuncoes();
  }

  /**
   * on end screen.
   */
  public final void onEndScreen() {
  }

  /**
   * back.
   */
  public final void back() {
	Motor.obterInstancia().esconderConsole();
  }
  
  public final void mudarCena(String param) {
	  Motor.obterInstancia().mudarCena(param);
  }
  
  public void ativarSom() {
  }
  
  public void DesativarSom() {
  }
  
  /**
   * process a keyEvent for the whole screen.
   * @param inputEvent the input event
   * @return true when consume and false when not
   */
  public boolean keyEvent(final NiftyInputEvent inputEvent) {
	  return false;
  }
	
	public void definirFuncoes() {
		try {
			shell.setProperty("MOTOR", Motor.obterInstancia());
			
			if(Motor.obterInstancia().getJogavelCarregado() != null) {
				shell.setProperty("JOGAVEL", Motor.obterInstancia().getJogavelCarregado());
				shell.setProperty("ESTADO", Motor.obterInstancia().getJogavelCarregado().getPainelEstado());
			}
			
			shell.setProperty("FABRICA", RecursosFactory.getInstancia());
			shell.setProperty("DEBUG", new DebugMestre());
		} catch (CompilationFailedException e) {
			e.printStackTrace();
		}
	}

	public void executar(String texto) {
		try {
			shell.evaluate(texto);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
