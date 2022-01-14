package mundo;

import java.awt.Polygon;

import inimigo.Inimigo;

public class FiltroTriangulo implements Filtro {

	@Override
	public boolean filtragem(Object param1, Object param2, Inimigo i) {
		Polygon triangulo = (Polygon)param1;
		return triangulo.intersects( i.getBounds());
	}

}
