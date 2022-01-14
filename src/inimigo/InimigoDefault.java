package inimigo;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import mundo.Caminho;
import mundo.Mundo;
import prof.jogos2D.image.ComponenteAnimado;
import prof.jogos2D.image.ComponenteVisual;
import prof.jogos2D.util.ImageLoader;


/**
 * Classe que representa um inimigo genérico. Define vários métodos e variáveis comuns a todos
 * os inimigos. 
 */
public class InimigoDefault implements Inimigo {

	/** variáveis comuns aos vários inimigos
	 */
	private ComponenteVisual imagem;  // o desenho do inimigo
	private float velocidade = 0;     // a velocidade do inimigo
	private Caminho meuCaminho;       // o caminho onde o inimigo se move
	private float posCaminho = 0;     // a posição no caminho
	private Mundo meuMundo;           // o mundo em que se move o inimigo
	private Point2D.Double posicao;   // a posição no ecrán do centro do inimigo
	private float resistencia;        // o valor de resistência do inimigo
	private float resistInicial;      // resistencia inicial
	
	public ArrayList<ObserverInimigo> observadores = new ArrayList<ObserverInimigo>();
	
	/** Cria um inimigo usando os valors por defeito
	 */
	public InimigoDefault(  ){
		// uma posição bem fora do écran
		posicao = new Point2D.Double(-10000,-10000);
	}
	
	/** Cria um inimigo
	 * @param cv imagem que o representa
	 */
	public InimigoDefault( ComponenteVisual cv ){
		posicao = new Point2D.Double(-10000,-10000);
		setComponente( cv );		
	}
	
	@Override
	public void desenhar( Graphics2D g ){
		// desenhar o boneco
		imagem.desenhar(g);
		// desenhar a linha de vida
		g.setColor( Color.BLACK );
		int py = (int)(posicao.y - imagem.getAltura()/2 - 2);
		g.drawLine((int)posicao.x, py, (int)posicao.x + 10, py );
		g.setColor( Color.green );
		g.drawLine( (int)posicao.x, py, (int)posicao.x + (int)(resistencia*10/resistInicial), py );
	}

	/**
	 * permite a um inimigo definir qual o seu aspeto
	 * @param v o novo aspeto do inimigo
	 */
	protected void setComponente( ComponenteVisual v ){
		imagem = v;
		imagem.setPosicaoCentro( new Point( (int)posicao.x, (int)posicao.y) );
	}
	
	@Override
	public ComponenteVisual getComponente(){
		return imagem;
	}
	
	@Override
	public int getPosicaoCaminho(){
		return (int)posCaminho;
	}
	
	@Override
	public void setVelocidade( float v ){
		this.velocidade = v;
	}		

	@Override
	public Point2D.Double getPosicao(){
		return posicao;
	}
	
	@Override
	public void setPosCaminho(int posCaminho) {
		this.posCaminho = posCaminho;
		Point p = meuCaminho.getPoint( posCaminho );
		setPosicao( new Point2D.Double(p.x, p.y) );
	}

	@Override
	public void setMundo( Mundo m ){
		meuMundo = m;
	}
	
	@Override
	public Mundo getMundo(){
		return meuMundo;
	}
	
	@Override
	public float getResistencia() {
		return resistencia;
	}

	public void setResistencia(float resistencia) {
		this.resistencia = resistencia;
	}
	
	public float getResistenciaInicial() {
		return resistInicial;
	}

	public void setResistenciaInicial(float resistencia) {
		this.resistInicial = resistencia;
		this.resistencia = resistencia;
	}

	@Override
	public float getVelocidade() {
		return velocidade;
	}

	@Override
	public void setPosicao(Point2D.Double p){
		posicao = p;
		imagem.setPosicaoCentro( new Point((int)p.x, (int)p.y) );
	}
	
	@Override
	public void move(){
		posCaminho += velocidade;
		Point p = meuCaminho.getPoint( (int)posCaminho );
		if( p == null ){
			for(ObserverInimigo o : observadores) {
				o.inimigoPassou();
			}
			
			// colocar a resistência a 0 para indicar que deve ser apagado
			resistencia = 0;
			return;
		}
		setPosicao( new Point2D.Double( p.x, p.y) );
	}
	
	@Override
	public void setCaminho(Caminho caminho) {
		meuCaminho = caminho;
	}
	
	@Override
	public Caminho getCaminho(){
		return meuCaminho;
	}
	
	@Override
	public Rectangle getBounds(){
		return imagem.getBounds();		
	}
		
	@Override
	public float atingido( float estrago ){
		resistencia -= estrago;
		// se não aguentou o estrago todo devolve o estrago sobrante
		if( resistencia <= 0){
			float sobra = -resistencia;
			resistencia = 0;
			
			// criar o efeito de explosão
			ImageLoader loader = ImageLoader.getLoader();		
			BufferedImage img = (BufferedImage)loader.getImage("data/inimigos/explosao.gif");
			Point pExplo = new Point((int)posicao.x, (int)posicao.y);
			ComponenteAnimado explosao = new ComponenteAnimado( pExplo, img, 17, 2 );
			explosao.setPosicaoCentro( pExplo );
			getMundo().addEfeito( explosao );
			for(ObserverInimigo o: observadores) {
				o.inimigoMorreu();
			}
				
			return sobra;
		}
		return 0;
	}
	
	@Override
	public boolean estaMorto() {
		return resistencia <= 0;
	}

	@Override
	public void addObserverInimigo(ObserverInimigo o) {
		observadores.add(o);
		
	}

	@Override
	public void removeObserverInimigo(ObserverInimigo o) {
		observadores.remove(o);
	}
	
}
