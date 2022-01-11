package arma;

import java.awt.Graphics2D;
import java.awt.Point;

import mundo.Mundo;

/** Representa uma Arma que é controlável pelo jogador
 */
public interface Arma {

	/** prepara a arma para ser usada no ponto indicado
	 * @param p ponto onde  arma está a ser preparada
	 */
	void preparar( Point p );
	
	/** aponta a arma para o local indicado
	 * @param p posição para onde a arma está a apontar
	 */
	void apontar( Point p );
	
	/** liberta a arma
	 * @param p local onde foi libertada
	 */
	void libertar( Point p );
	
	/** a arma deixou de estar ativa
	 */
	void trocar();

	/** atualiza a arma
	 */
	void atualizar( );
	
	/** desenha a arma
	 * @param g ambiente gráfico onde desenhar
	 * @param cx centro x do local onde desenhar
	 * @param cy centro y do local onde desenhar 
	 */
	void desenhar( Graphics2D g, int cx, int cy );
	
	/** faz o reset, isto é, coloca a arma nas condições iniciais
	 */
	void reset();
	
	/** indica se a arma está pronta a ser disparada
	 * @return true se está pronta a disparar
	 */
	boolean estaPronta( );

	/** retorna o tempo de recarga da arma
	 * @return o tempo de recarga da arma
	 */
	int getDelay();
	
	/** define o tempo de recarga da arma
	 * @param delay tempo de recarga da arma
	 */
	void setDelay(int delay);
	
	/** devolve o dano que a arma causa
	 * @return o dano que a arma causa
	 */
	float getDano();
	
	/** define o dano que a arma causa
	 * @param dano o dano que a arma causa
	 */
	void setDano(float dano);
	
	/** define o mundo em que a arma vai atuar
	 * @param m o mundo em que a arma vai atuar
	 */
	void setMundo( Mundo m );
	
	/** retorna o mundo em que a arma atua
	 * @return o mundo em que a arma atua
	 */
	Mundo getMundo( );
}
