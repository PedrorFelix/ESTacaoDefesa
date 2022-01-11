package inimigo;

import java.util.Comparator;

/** 
 * Compara dois inimigos pela sua velocidade
 */
public class ComparatorMaisVeloz implements Comparator<Inimigo> {

	@Override
	public int compare(Inimigo i1, Inimigo i2) {
		// x10 para que a diferença em décimas seja tida em conta
		return (int)(10*(i1.getVelocidade() - i2.getVelocidade()));
	}
}
