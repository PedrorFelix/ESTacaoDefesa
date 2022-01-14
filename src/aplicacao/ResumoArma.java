package aplicacao;

import java.awt.Color;
import java.awt.Graphics;

import arma.*;

/** Esta classe apresenta o resumo da arma nos botões da interface
 */
public class ResumoArma implements VisitanteArmas {
	
	private Graphics g;

	void paintResumo( Graphics g ) {
		this.g =g;
		
	}

	@Override
	public void visitaCanhao(Canhao c) {
		// TODO Auto-generated method stub
		g.setColor( Color.GRAY );
		g.drawString( "" + c.getDano(), 26, 30 );
		g.drawString( "" + c.getDelay(), 66, 30 );
	}

	@Override
	public void visitaLancaDrones(LancaDrones l) {
		// TODO Auto-generated method stub
		g.setColor( Color.GRAY );
		g.drawString( "" + l.getDelay(), 36, 20 );
		g.drawString( l.getnDronesLancados() + "/" + l.getMaxDronesAtivos(), 36, 37 );
	}

	@Override
	public void visitaLaser(Laser l) {
		// TODO Auto-generated method stub
		g.setColor( Color.GRAY );
		g.drawString( "" + l.getDano(), 10, 37 );
		g.drawString( "" + l.getFatorAquecimento(), 40, 37 );
		g.drawString( "" + l.getFatorArrefecimento(), 70, 37 );
	}

	@Override
	public void visitaMetralha(Metralha m) {
		// TODO Auto-generated method stub
		g.setColor( Color.GRAY );
		g.drawString( "" + m.getDano(), 8, 37 );
		g.drawString( "" + m.getDelay(), 36, 37 );
		g.drawString( "" + m.getDistanciaMax(), 56, 37 );
	}

	@Override
	public void visitaMissil(Missil m) {
		// TODO Auto-generated method stub
		g.setColor( Color.GRAY );
		g.drawString( "" + m.getDano(), 8, 37 );
		g.drawString( "" + m.getDelay(), 36, 37 );
		g.drawString( "" + m.getVelocidade(), 63, 37 );
	}
}
