package mundo;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Vector;

import arma.drone.Drone;
import inimigo.Inimigo;
import prof.jogos2D.image.ComponenteVisual;


/**
 * classe que agrupa todos os elementos que se movem no jogo
 */
public class Mundo {

	/** 
	 * arrays com os vários elementos do jogo
	 */
	private ArrayList<Inimigo> inimigos = new ArrayList<Inimigo>();
	private ArrayList<Caminho> caminhos = new ArrayList<Caminho>();
	private Vector<Drone> drones = new Vector<Drone>();
	private ComponenteVisual fundo; // imagem de fundo do mundo
	
	private Filtro filtro;	

	private ArrayList<ComponenteVisual> efeitos = new ArrayList<ComponenteVisual>();
	
	/**
	 * define a imagem de fundo do mundo
	 * @param fundo a imagem de fundo do mundo
	 */
	public void setFundo( ComponenteVisual fundo ){
		this.fundo= fundo;
	}
	
	/**
	 * adiciona um inimigo ao mundo, pondo-o num dado caminho
	 * @param caminho número do caminho onde colocar o inimigo
	 * @param i inimigo a colocar no mundo
	 */
	public void addInimigo( int caminho, Inimigo i ){
		// ver qual o caminho em que se vai meter o inimigo
		Caminho c = caminhos.get(caminho);
		addInimigo( c, i);
	}

	
	/**
	 * adiciona um inimigo ao mundo, pondo-o num dado caminho
	 * @param c caminho onde colocar o inimigo
	 * @param b inimigo a colocar no mundo
	 */
	public void addInimigo( Caminho c, Inimigo i ){
		i.setMundo( this );             // associar o inimigo ao mundo
		i.setCaminho( c );              // associar o inimigo ao caminho
		i.setPosCaminho( 0 );           // colocá-lo no início do caminho
		inimigos.add( i );                
	}

	/**
	 * remove um inimigo do mundo
	 * @param b
	 */
	public void removeInimigo( Inimigo i ){
		i.setCaminho( null );  // retira a informação do caminho
		i.setMundo( null );    // retira a informação do mundo 
		inimigos.remove( i );				
	}
	
	/**
	 * devolve todos os inimigos presentes no mundo
	 * @return todos os inimigos presentes no mundo
	 */
	public List<Inimigo> getInimigos() {
		return Collections.unmodifiableList( inimigos );
	}
	
	/**
	 * adiciona um caminho ao mundo
	 * @param c o caminho a adicionar
	 */
	public void addCaminho( Caminho c ){
		caminhos.add( c );
	}
	
	/**
	 * remove um caminho do mundo
	 * @param c caminho a remover
	 */
	public void removeCaminho( Caminho c ){
		caminhos.remove( c );
	}
	
	/** Devolve o caminho que está mais próximo de um dado ponto 
	 * @param p ponto a verificar
	 * @return  o caminho mais próximo de p
	 */
	public Caminho getCaminhoMaisProximo( Point p ) {
		Caminho sel = caminhos.get(0);
		double dist = sel.distancia(p);
		for( int i=1; i < caminhos.size(); i++ ) {
			Caminho c = caminhos.get(i);
			double d = c.distancia(p);
			if( d < dist ) {
				dist = d;
				sel = c;
			}
		}			
		return sel;
	}
	
	/** devolve todos os caminhos existentes
	 * @return uma lista com os caminhos existentes
	 */
	public List<Caminho> getCaminhos() {
		return Collections.unmodifiableList(caminhos);
	}
	
	/**
	 * adiciona um drone ao mundo
	 * @param p o drone a adicionar
	 */
	public void addDrone( Drone p ){
		drones.add( p );
		p.setMundo( this );
	}
	
	/**
	 * remove um drone do mundo
	 * @param p o drone a remover
	 */
	public void removeDrone( Drone p ){
		drones.remove( p );
	}
		
	/**
	 * indica quantos drones estão no mundo
	 * @return nº de drones no mundo
	 */
	public int getNumDrones() {		
		return drones.size();
	}
	
	/**
	 *  limpa o mundo todo
	 */
	public void clearTodos(){
		inimigos.clear();
		drones.clear();
		caminhos.clear();
	}

	/**
	 * limpa os elementos móveis do mundo, nomeadamente
	 * os inimigos e os drones
	 */
	public void clearMoveis(){
		inimigos.clear();
		drones.clear();		
	}

	/**
	 * atualiza o mundo, isto é, processa uma jogada	 
	 */
	public void atualizar(){
		// move os inimigos todos
		for( Inimigo b : inimigos ) {
			b.move();
		}

		// move os drones todos 
		for( Drone d : drones )
			d.move();
						
		// depois de tudo atualizado vamos verificar se há inimigos mortos
		for( int i = inimigos.size()-1; i >= 0; i-- ){
			Inimigo ini = inimigos.get( i ); 
			if( ini.estaMorto() )
				inimigos.remove( i );
		}
		
		// e verificar se há drones no hangar
		for( int i = drones.size()-1; i >= 0; i-- ){
			Drone d = drones.get( i ); 
			if( d.estaHangar() )
				drones.remove( i );
		}
		
		// ver se os efeitos já foram reproduzidos
		for( int i = efeitos.size()-1; i >= 0; i-- ){
			ComponenteVisual fx = efeitos.get( i ); 
			if( fx.numCiclosFeitos() > 0 )
				efeitos.remove( i );
		}
	}
	
	/**
	 * desenha o mundo e os seus constituintes
	 * @param g o ambiente gráfico onde se vai desenhar
	 */
	public void desenha( Graphics2D g ){
		if( fundo != null )
			fundo.desenhar(g);
		for( Inimigo b : inimigos )
			b.desenhar(g);
		for( Drone p : drones )
			p.desenhar(g);
		for( ComponenteVisual f : efeitos )
			f.desenhar(g);
	}
	
	
	/** Método que filtra os inimigos de acordo com um critério escolhido pelo cliente
	 * Os critérios suportados até ao momento são:
	 *   FILTRO_RAIO, escolhe os inimigos que estão dentro de um circulo
	 *   FILTRO_LINHA, escolhe os inimigos que intersetam uma linha
	 *   FILTRO_TRIANGULO, escolhe os inimigos que estão dentro de um triângulo
	 * 
	 * @param param1 primeiro param do filtro (pode ser o centro, o ponto inicial da linha ou o triangulo)
	 * @param param2 segundo param do filtro (pode ser o raio ou o ponto final da linha)
	 * @return a lista de inimigos que obedecem ao critério indicado
	 */
	public List<Inimigo> getInimigosFiltrados(Object param1, Object param2 ){
		ArrayList<Inimigo> sel = new ArrayList<Inimigo>();
		for( Inimigo i : inimigos ) {
			if( filtro.filtragem(param1, param2, i))
				sel.add( i );
		}
		return sel;
	}

	/** Escolhe qual o inimigo mais adequado dos que obedecem a um dado critério de filtragem.
	 * Usa o método de filtrar, pelo que padece do mesmo problema dos parâmetros de entrada
	 * 
	 *  
	 * @param tipoFiltro especifica o filtro a usar
	 * @param param1 primeiro param do filtro (pode ser o centro, o ponto inicial da linha ou o triangulo)
	 * @param param2 segundo param do filtro (pode ser o raio ou o ponto final da linha)
	 * @param seletor qual o modo de seleção do inimigo
	 * @return o inimigo que mais se aproxima do modo de seleção,
	 *         dentro dos que foram filtrados, ou null se não existir nenhum
	 */
	public Inimigo getInimigoMaisAdequado( Object param1, Object param2, Comparator<Inimigo> seletor ){
		List<Inimigo> alvos = getInimigosFiltrados( param1, param2 );
		try {
			return Collections.max( alvos, seletor );
		} catch (NoSuchElementException e) {
			return null;
		}
 	}

	/** adiciona um efeito visual ao mundo
	 * @param fx o efeito a adicionar
	 */
	public void addEfeito( ComponenteVisual fx ) {
		efeitos.add( fx );
	}

	/** indica se tem efeitos a serem reproduzidos
	 * @return true, se tem efeitos a serem reproduzidos
	 */
	public boolean temEfeitos() {
		return efeitos.size() > 0;
	}
	
	public Filtro getFiltro() {
		return filtro;
	}

	public void setFiltro(Filtro filtro) {
		this.filtro = filtro;
	}
}
