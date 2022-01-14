package mundo;

import java.awt.Point;
import java.awt.geom.Line2D;

import inimigo.Inimigo;

public class FiltroLinha implements Filtro {

	@Override
	public boolean filtragem(Object param1, Object param2, Inimigo i) {
		Line2D.Double line = new Line2D.Double( (Point)param1, (Point)param2 );
		return line.intersects( i.getBounds() );
	}

}
