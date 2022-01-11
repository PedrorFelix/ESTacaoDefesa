package aplicacao;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;

import arma.Arma;

/** Classe que apresenta os botões para escolha das armas.
 * 
 */
public class CheckBoxArma extends JCheckBox {

	/***/
	private static final long serialVersionUID = 1L;
	
	private Arma arma;      // a arma associada este botão 
	private ResumoArma resumo = new ResumoArma();
	
	/** cria uma checkbox para uma arma
	 */
	public CheckBoxArma( ) {
		setFocusable( false );
	}

	/** define os vários icons para este botão
	 * @param iconFile nome base dos ficheiros com os ícones
	 * a usar para este botão
	 */
	public void setIconFile( String iconFile ) {
		setSelectedIcon( new ImageIcon(iconFile + "_sel.png") );
		setIcon( new ImageIcon(iconFile + "_on.png") );
		setRolloverIcon( new ImageIcon(iconFile + "_over.png") );
		setOpaque( false );
	}

	/** retorna a arma associada
	 * @return a arma associada
	 */
	public Arma getArma() {
		return arma;
	}

	/** define a arma associada a este botão
	 * @param arma a arma a associar
	 */
	public void setArma(Arma arma) {
		this.arma = arma;		
	}

	/** desenha o botão e as informações base sobre a arma 
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);		
		if( arma.estaPronta() )
			g.setColor( Color.GREEN );
		else 
			g.setColor( Color.YELLOW );
		int xStatus = getWidth() - 14;
		g.fillOval( xStatus, 32, 4, 4);
		g.setColor( Color.BLACK );
		g.drawOval( xStatus, 32, 4, 4);
		resumo.paintResumo( arma, g );
	}
}
