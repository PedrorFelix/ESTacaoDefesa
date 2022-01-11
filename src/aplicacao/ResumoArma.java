package aplicacao;

import java.awt.Color;
import java.awt.Graphics;

import arma.*;

/** Esta classe apresenta o resumo da arma nos botões da interface
 */
public class ResumoArma {
	
	/** desenha o resumo no botão
	 * @param a arma da qual apresentar o resumo
	 * @param g onde desenhar o resumo
	 */
	void paintResumo( Arma a, Graphics g ) {
		// TODO INSTANCEOF estes instanceof têm de desaparecer
		if( a instanceof Canhao ) {
			Canhao c = (Canhao) a;
			g.setColor( Color.GRAY );
			g.drawString( "" + c.getDano(), 26, 30 );
			g.drawString( "" + c.getDelay(), 66, 30 );
		} else if( a instanceof Metralha ) {
			Metralha m = (Metralha)a;
			g.setColor( Color.GRAY );
			g.drawString( "" + m.getDano(), 8, 37 );
			g.drawString( "" + m.getDelay(), 36, 37 );
			g.drawString( "" + m.getDistanciaMax(), 56, 37 );
		} else if( a instanceof LancaDrones ) {
			LancaDrones l = (LancaDrones)a;
			g.setColor( Color.GRAY );
			g.drawString( "" + l.getDelay(), 36, 20 );
			g.drawString( l.getnDronesLancados() + "/" + l.getMaxDronesAtivos(), 36, 37 );
		} else if( a instanceof Laser ) {
			Laser l = (Laser)a;
			g.setColor( Color.GRAY );
			g.drawString( "" + l.getDano(), 10, 37 );
			g.drawString( "" + l.getFatorAquecimento(), 40, 37 );
			g.drawString( "" + l.getFatorArrefecimento(), 70, 37 );
		} else if( a instanceof Missil ) {
			Missil m = (Missil)a;
			g.setColor( Color.GRAY );
			g.drawString( "" + m.getDano(), 8, 37 );
			g.drawString( "" + m.getDelay(), 36, 37 );
			g.drawString( "" + m.getVelocidade(), 63, 37 );
		}
	}
}
