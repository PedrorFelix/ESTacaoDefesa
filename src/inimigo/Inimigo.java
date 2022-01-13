package inimigo;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

import mundo.Caminho;
import mundo.Mundo;
import prof.jogos2D.image.ComponenteVisual;

/**
 * interface que define os métodos que todos os Inimigos devem implementar 
 */
public interface Inimigo {
	
	// TODO COMUNICAÇÃO Este método não deve estar nos Inimigos
	/** Altera o jogo ao qual o inimigo está associado
	 * <br><b>TODO COMUNICAÇÃO substitutir esta solução por uma melhor</b>
	 * @param estacao o jogo
	 */
	
	// TODO COMUNICAÇÃO Este método não deve estar nos Inimigos
	/** devolve o jogo em que está a ser jogado.
	 * <br><b>TODO COMUNICAÇÃO substitutir esta solução por uma melhor</b>
	 * @return  o jogo em que está a ser jogado
	 */

	
	/**
	 * desenha o inimigo
	 * @param g sistema gráfico onde desenhar o inimigo
	 */
	public void desenhar( Graphics2D g );
	
	/**
	 * move o inimigo
	 */
	public void move();
	
	/**
	 * devolve o componente visual que representa este inimigo
	 * @return o componente visual que representa este inimigo
	 */
	public ComponenteVisual getComponente();
	
	/**
	 * define qual o caminho que o inimigo percorre
	 * @param caminho o caminho a percorrer
	 */
	public void setCaminho(Caminho caminho);
	
	/**
	 * devolve o caminho em que o inimigo se move
	 * @return o caminho em que o inimigo se move
	 */
	public Caminho getCaminho();	
	
	/**
	 * indica em que posição do caminho está 
	 * @return a posição do caminho em que está
	 */
	public int getPosicaoCaminho();
	
	/**
	 * indica em que posição do ecran está. 
	 * @return a posição do ecran em que está
	 */
	public Point2D.Double getPosicao();
	
	/**
	 * coloca o inimigo numa nova posição do caminho. 
	 * @param posCaminho a nova posição
	 */
	public void setPosCaminho(int posCaminho);
	
	/**
	 * define em que mundo se move 
	 * @param m o novo mundo
	 */
	public void setMundo( Mundo m );
	
	/**
	 * Devolve o mundo onde se move
	 * @return o mundo onde se move
	 */
	public Mundo getMundo();
	
	/**
	 * define a posição do écran onde colocar o inimigo
	 * @param p a posição do écran onde colocar o inimigo
	 */
	public void setPosicao(Point2D.Double p);
	
	/**
	 * define a velocidade do inimigo
	 * @param v velocidade em pixeis por frame
	 */
	public void setVelocidade( float v );
	
	/**
	 * retorna a velocidade do inimigo
	 * @return velocidade em pixeis por frame
	 */
	public float getVelocidade();
	
	/**
	 * devolve o rectângulo ocupado pelo inimigo no écran
	 * @return o rectângulo ocupado pelo inimigo no écran
	 */
	public Rectangle getBounds();
	
	/**
	 * método para atingir o inimigo. Cada inimigo suporta
	 * até um dado estrago, valor que é dado pela sua resistência.
	 * Se o inimigo não aguentar o estrago, explode e devolve o
	 * estrago que não suportou. 
	 * @param estrago valor de estrago que atingiu o inimigo
	 * @return o valor de estrago que o inimigo não aguentou
	 */
	public float atingido( float estrago );
	
	/**
	 * devolve a resistência do inimigo.
	 * @return a resistência do inimigo.
	 */
	public float getResistencia();
	
	/** indica se o inimigo está morto.
	 * @return true, se estiver morto
	 */
	public boolean estaMorto();
	
	public void addObserverInimigo(ObserverInimigo o);
	
	public void removeObserverInimigo(ObserverInimigo o);
	
}	
