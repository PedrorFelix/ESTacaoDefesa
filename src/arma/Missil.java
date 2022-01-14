package arma;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Comparator;

import inimigo.ComparatorMaisForte;
import inimigo.Inimigo;
import mundo.FiltroTriangulo;
import prof.jogos2D.image.ComponenteAnimado;
import prof.jogos2D.util.ImageLoader;

/** Representa a arma Missil
 */
public class Missil extends ArmaDefault {
	// constantes para esta arma
	private static final int largArea = 80;  // largura da área  
	private static final int largDetec = 15; // largura da área de deteção
	// constante para determinar a altura do triângulo de deteção
	private static final double alturaTiang = Math.sqrt( 3 ) / 2;  
	
	private Rectangle areaCobrir; // a área a ser coberta pelo missíl
	private Polygon areaDetecao;  // a área onde vai detetar inimigos
	private Point pontoEscolhido; // o centro da área de deteção 
	private int velocidade;       // velocidade de movimento da área de deteção
	
	// tipo de seletor dos inimigos
	private Comparator<Inimigo> seletorInimigo = new ComparatorMaisForte();
		
	/** Cria um missil
	 * @param dano dano que o míssil inflige
	 * @param delay tempo entre disparos
	 * @param veloc velocidade da zona de deteção
	 */
	public Missil( float dano, int delay, int veloc ) {
		super( dano, delay );
		this.velocidade = veloc;
	}

	@Override
	public void preparar(Point p) {
		pontoEscolhido = p;
	}

	@Override
	public void apontar(Point p) {
		pontoEscolhido = p;
	}

	@Override
	public void libertar(Point p) {
		pontoEscolhido = null;
		// vai definir a zona a cobrir
		Point topoArea = new Point( p.x-largArea/2, p.y - largArea/2 );
		areaCobrir = new Rectangle( topoArea, new Dimension(largArea, largArea) );
		int []xs = { topoArea.x, topoArea.x + largDetec, topoArea.x + largDetec/2  };
		int []ys = { topoArea.y, topoArea.y, (int)(topoArea.y + largDetec*alturaTiang)  };
		areaDetecao = new Polygon( xs, ys, 3 ); 		
	}
	
	@Override
	public void trocar() {
	}
	
	@Override
	public void atualizar() {
		super.atualizar();
		// a zona de deteção pode ainda não ter sido definida
		if( areaDetecao != null ) {
			// vai mudando a área de deteção de local
			areaDetecao.translate( velocidade, 0);
			if( areaDetecao.xpoints[1] > areaCobrir.x + largArea )
				areaDetecao.translate(-largArea, velocidade);
			if( areaDetecao.ypoints[0] > areaCobrir.y + largArea )
				areaDetecao.translate(0, -largArea);
			
			// se está pronta vai ver se há inimigos e acertar-lhes
			if( estaPronta() ) {
				Point pTiro = new Point( areaDetecao.xpoints[0], areaDetecao.ypoints[0] );
				getMundo().setFiltro(new FiltroTriangulo());
				Inimigo ini = getMundo().getInimigoMaisAdequado(areaDetecao, null,
                                                                 seletorInimigo );
				// há inimigo! Atira-lhe com o míssil para cima!
				if( ini != null ) {
					ini.atingido( getDano() );
					Image img = ImageLoader.getLoader().getImage("data/fx/impacto_grande.png" );
					ComponenteAnimado ca = new ComponenteAnimado( pTiro, (BufferedImage)img, 5, 3 );
					ca.setPosicaoCentro( pTiro );
					getMundo( ).addEfeito( ca );
					resetContagem();
				}
			}
		}
	}
	
	@Override
	public void desenhar(Graphics2D g, int cx, int cy) {
		// desenha a mira
		g.setColor( Color.RED );
		g.drawOval(cx-20, cy-20, 40, 40);

		// desenha a área de deteção e área a cobrir
		if( areaDetecao != null ) {
			g.setColor( Color.red );
			g.draw( areaCobrir );
			g.draw( areaDetecao );
			// desenha a percentagem que falta para ativar se faltar tempo
			int perc = 100 - getTick()*100/getDelay();
			if( perc < 100 )
				g.drawString( perc + "%", areaCobrir.x, areaCobrir.y - 10 );				
		}
		
		// se está a escolher uma nova área, desenha-a a azul
		if( pontoEscolhido != null ) {
			g.setColor( Color.BLUE );
			Point topoArea = new Point( pontoEscolhido.x-largArea/2, pontoEscolhido.y - largArea/2 );
			Rectangle areaNova = new Rectangle( topoArea, new Dimension(largArea, largArea) );
			g.draw( areaNova );
		}
	}

	/** define o tipo de seletor a usar no míssil
	 * @param tipoSel o tipo de seletor a usar no míssil
	 */
	public void setSeletorInimigo(Comparator<Inimigo> tipoSel) {
		seletorInimigo = tipoSel ;
	}
	
	/** retorna a velocidade de varrimento
	 * @return a velocidade de varrimento
	 */
	public int getVelocidade() {
		return velocidade;
	}

	@Override
	public void aceita(VisitanteArmas v) {
		v.visitaMissil(this);
		
	}
	
}
