package arma;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import inimigo.ComparatorMaisForte;
import inimigo.Inimigo;
import mundo.FiltroRaio;
import prof.jogos2D.image.ComponenteAnimado;
import prof.jogos2D.util.ImageLoader;

/** Representa a arma laser
 */
public class Laser extends ArmaDefault {
	// constantes para o laser
	private static final float tempMax = 500;  // temperatura máxima
	private static final float tempDisp = 200; // temperatura em que está disponível
	private static final float tempMin = 20;   // temperatura mímima
	// barra que, na mira, indica a temperaturra a que fica disponível
	private static final int altBarraDisp = (int)( tempDisp / 10 );  

	private boolean disparando = false;  // indica se está a disparar
	private Point localDisparo;          // local onde está a disparar
	private float temperatura = tempMin; // temperatura atual
	private float fatorAquecimento;     
	private float fatorArrefecimento;
	
	// cores para a barra da temperatura (em função da temperatura)
	private static final Color coresTemp[] = { Color.CYAN, Color.BLUE, Color.YELLOW, Color.ORANGE, Color.RED, Color.RED}; 
	
	/** cria um Laser
	 * @param dano dano inicial
	 * @param fatorAque fator de aquecimento
	 * @param fatorArref fator de arrefecimento
	 */
	public Laser( float dano, float fatorAque, float fatorArref) {
		super( dano, 0 );
		fatorAquecimento = fatorAque;
		fatorArrefecimento = fatorArref;		
	}
	
	@Override
	public boolean estaPronta() {
		return temperatura < tempDisp;
	}

	@Override
	public void preparar(Point p) {
		localDisparo = p;
		if( temperatura < tempDisp )
			disparando = true;
	}

	@Override
	public void apontar(Point p) {
		localDisparo = p;
		
		if( !disparando && temperatura < tempDisp )
			disparando = true;
	}

	@Override
	public void libertar(Point p) {
		disparando = false;
	}
	
	@Override
	public void trocar() {
		disparando = false;
	}
	
	@Override
	public void atualizar() {
		super.atualizar();
		// se está a disparar vai aquecendo
		if( disparando ) {
			temperatura += fatorAquecimento*getDano();
			getMundo().setFiltro(new FiltroRaio());
			Inimigo ini = getMundo().getInimigoMaisAdequado( new Point2D.Double(localDisparo.x, localDisparo.y), 10, 
                                                             new ComparatorMaisForte() );
			// yes! Tem inimigo, vai queimá-lo :-)
			if( ini != null ) {
				ini.atingido( getDano() );
				Image img = ImageLoader.getLoader().getImage("data/fx/impacto_pequeno.png" );
				ComponenteAnimado ca = new ComponenteAnimado( localDisparo, (BufferedImage)img, 5, 3 );
				ca.setPosicaoCentro( localDisparo );
				getMundo( ).addEfeito( ca );		
			}
		}
		// senão vai arrefecendo
		else
			temperatura -= fatorArrefecimento;
			
		// verificar os limites da temperatura
		if( temperatura < tempMin )
			temperatura = tempMin;
		if( temperatura > tempMax ) {
			temperatura = tempMax;
			disparando = false;    // se ultrapassou o máximo, deixa de disparar
		}
			
	}
	
	@Override
	public void desenhar(Graphics2D g, int cx, int cy) {
		// se está a disparar apresenta as faixas verdes
		if( disparando ) {
			g.setColor( Color.GREEN );
			g.drawLine( cx-25, cy-25, cx+25, cy+25);
			g.drawLine( cx-25, cy+25, cx+25, cy-25);
		}

		// calcular a altura das barras de temperatura e potência
		int altBarraTemp = (int)(temperatura/10);
		int altBarraPot = (int)(getDano()*50);

		// isto está cheio de números mágicos, mas não é preciso corrigir
		// desenhar a barra de potência
		g.setColor( Color.ORANGE );
		g.fillRect(cx+25, cy+25-altBarraPot, 6, altBarraPot);
		g.setColor( Color.red );
		g.drawLine( cx+25, cy-25, cx+25, cy+25);
		g.drawLine( cx+25, cy-25, cx+31, cy-25);
		g.drawLine( cx+25, cy+25, cx+31, cy+25);
		
		// desenhar a barra de temperatura
		g.setColor( coresTemp[altBarraTemp / 10] );
		g.fillRect(cx-25, cy+25-altBarraTemp, 6, altBarraTemp);
		g.setColor( Color.red );
		g.drawLine( cx-25, cy-25, cx-25, cy+25);
		g.drawLine( cx-25, cy-25, cx-31, cy-25);
		g.drawLine( cx-25, cy+25-altBarraDisp, cx-31, cy+25-altBarraDisp);
		g.drawLine( cx-25, cy+25, cx-31, cy+25);
	}
	
	/** retona o fator de aquecimento
	 * @return o fator de aquecimento
	 */
	public float getFatorAquecimento() {
		return fatorAquecimento;
	}
	
	/** retona o fator de arrefecimento
	 * @return o fator de arrefecimento
	 */
	public float getFatorArrefecimento() {
		return fatorArrefecimento;
	}

	@Override
	public void aceita(VisitanteArmas v) {
		v.visitaLaser(this);
		
	}
	
}
