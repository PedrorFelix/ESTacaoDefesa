package inimigo;

import java.util.Comparator;

/** 
 * Compara dois inimigos pelo valor menor da resistência
 */
public class ComparatorMaisFraco implements Comparator<Inimigo> {

	@Override
	public int compare(Inimigo i1, Inimigo i2) {
		// x10 para que a diferença em décimas seja tida em conta
		return (int)(10*(i2.getResistencia() - i1.getResistencia()));
	}
}
