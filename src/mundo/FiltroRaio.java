package mundo;

import java.awt.geom.Point2D;

import inimigo.Inimigo;

public class FiltroRaio implements Filtro {

	@Override
	public boolean filtragem(Object param1, Object param2, Inimigo i) {
		Point2D.Double centro = (Point2D.Double)param1;
		int raio = (Integer)param2;
		return centro.distance( i.getPosicao() ) < raio;
	}

}
