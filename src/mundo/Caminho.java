package mundo;

import java.awt.Point;
import java.util.ArrayList;

/**
 * Classe que representa um caminho. Um caminho é uma sequência de pontos. O primeiro ponto
 * na sequência é o ponto de partida e último é o ponto de chegada. Para andar no caminho
 * basta passar para o ponto seguinte na sequência ou seja, passar do ponto n para o ponto n+1.
 * A posição no caminho é assim dada por um valor inteiro em que 0 representa o início do caminho.
 * Isto permite especificar um caminho sem indicar qual a posição no écran de um elemento
 */
public class Caminho {

	/**
	 * Lista que vai conter todos os pixeis do caminho
	 * isto é um bocado força bruta... mas funciona...
	 */ 
	private ArrayList<Point> caminho = new ArrayList<Point>();
	
	public Caminho(){
	}
	
	/**
	 * Adiciona um novo segmento de recta ao caminho. Os pontos intermédios 
	 * são adicionados automaticamente ao caminho. Assume que o segmento de recta começaa 
	 * no último ponto do caminho
	 * @param p o ponto onde termina o segmento de reta.
	 */
	public void addSegmento( Point p ){
		// utilizar os algoritmos de computação gráfica para determinar os pontos interm�dios
		// ver qual o ponto anterior
		Point last = getFim();
		
		int dx = p.x - last.x;
		int dy = p.y - last.y;
		
		if( dx == 0 ){ // é vertical
			int step = dy > 0? 1: -1;
			for( int y = last.y; y != p.y; y += step){
				Point novo = new Point( p.x, y);
				caminho.add( novo );
			}				
		}
		else if( dy == 0 ){ // é horizontal
			int step = dx > 0? 1: -1;
			for( int x = last.x; x != p.x; x += step){
				Point novo = new Point( x, p.y);
				caminho.add( novo );
			}				
		}
		else if( Math.abs(dx) > Math.abs( dy ) ){
			// controla o x
			int step = dx > 0? 1: -1;
			float m = (float)dy/dx*step;
			float x = last.x;
			float y = last.y;
			int nX = Math.abs( dx );
			for( int i = 0; i < nX; i++){
				y += m;
				x += step;
				Point novo = new Point( (int)x, (int)y);
				caminho.add( novo );
			}							
		}
		else {
			// controla o y
			int step = dy > 0? 1: -1;
			float m = (float)dx/dy*step;
			float x = last.x;
			float y = last.y;
			int nY = Math.abs( dy );
			for( int i = 0; i < nY; i++){
				y += step;
				x += m;
				Point novo = new Point( (int)Math.round(x), (int)Math.round(y));
				caminho.add( novo );
			}							
		}		
	}
	
	/**
	 * adiciona um ponto (em coordenadas do écran ao caminho). Deve ser usado para definir qual o ponto inicial 
	 * @param p o ponto a adicionar ao caminho
	 */
	public void addPonto( Point p ){
		caminho.add( p );
	}
	
	/**
	 * devolve o ponto (coordenada do écran) de início do caminho
	 * @return o ponto de início do caminho
	 */
	public Point getInicio(){
		return caminho.get( 0 );
	}
	
	/**
	 * devolve o ponto (coordenada do écran) de término do caminho
	 * @return o ponto de t�rmino do caminho
	 */
	public Point getFim(){
		return caminho.get( caminho.size()-1);
	}
	
	/**
	 * devolve o ponto i do caminho, ou seja, onde se situa o ponto i no écran
	 * @param i o índice do ponto no caminho
	 * @return o ponto i do caminho, null se o ponto não fizer parte do caminho
	 */
	public Point getPoint( int i ){
		try {
			return caminho.get( i );
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}
	
	/**
	 * devolve qual o próximo ponto no caminho a seguir a outro ponto
	 * @param p o ponto onde se está
	 * @param veloc a velocidade de movimento
	 * @return o ponto onde se vai estar, ou null caso não exista esse ponto
	 */
	public Point getNext( Point p, int veloc ){
		int idx = caminho.indexOf( p );
		// verificar se está ou não no último ponto do caminho
		int proxPonto = idx + veloc;
		if( idx < 0 || proxPonto >= caminho.size() )
			return null;
		return caminho.get( proxPonto );
	}
	
	/** 
	 * Calcula a distância entre o ponto dado e o caminho.
	 * Esta distância é a menor distância do ponto a qualquer ponto do caminho. 
	 * @param p ponto a verificar
	 * @return a menor distância entre o ponto dado e o caminho.
	 */
	public double distancia( Point p ) {
		// se não tiver pontos devolve o maior valor possivel (em inteiro)
		if( caminho.isEmpty() )
			return Integer.MAX_VALUE;
		
		// ver qual dos pontos está a menor distância
		double dist = caminho.get(0).distanceSq( p );
		for( Point pc : caminho ) {
			double distAtual = pc.distanceSq( p );
			if( distAtual < dist )
				dist = distAtual;
		}			
		return Math.sqrt(dist);
	}
}
