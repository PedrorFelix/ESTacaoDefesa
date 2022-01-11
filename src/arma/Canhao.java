package arma;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import inimigo.ComparatorMaisForte;
import inimigo.Inimigo;
import mundo.Mundo;
import prof.jogos2D.image.ComponenteAnimado;
import prof.jogos2D.image.ComponenteVisual;
import prof.jogos2D.util.ImageLoader;


/** Representa a arma canhão
  */
public class Canhao extends ArmaDefault {
	private ComponenteVisual mira;      // imagem da mira
	private boolean disparando = false; // indica se está a disparar
	private Point localDisparo;         // local do disparo
	
	/** cria um canhão
	 * @param dano dano que o canhão causa
	 * @param delay tempo que demora a carregar (em ticks - 30 ticks por segundo) 
	 * @param mira desenho da mira
	 */
	public Canhao( float dano, int delay, ComponenteVisual mira ) {
		super( dano, delay );
		this.mira = mira;
	}

	@Override
	public void preparar(Point p) {
		localDisparo = p;
		disparando = true;
	}

	@Override
	public void apontar(Point p) {
		localDisparo = p;
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
		// se está pontra a o jogador tem "o dedo no gatilho"
		if( estaPronta() && disparando ) {
			resetContagem();
			// ver se acerta em algum inimigo
			Inimigo ini = getMundo().getInimigoMaisAdequado( Mundo.FILTRO_RAIO,
					                                         new Point2D.Double(localDisparo.x, localDisparo.y), 10, 
					                                         new ComparatorMaisForte() );
			// se não tem inimigo, falhou :-(
			if( ini == null ) {
				Image img = ImageLoader.getLoader().getImage("data/fx/impacto_pequeno.png" );
				ComponenteAnimado ca = new ComponenteAnimado( localDisparo, (BufferedImage)img, 5, 3 );
				ca.setPosicaoCentro( localDisparo );
				getMundo( ).addEfeito( ca );		
			}
			// se tem inimigo, acertou :-)
			else {
				ini.atingido( getDano() );
				Image img = ImageLoader.getLoader().getImage("data/fx/impacto_grande.png" );
				ComponenteAnimado ca = new ComponenteAnimado( localDisparo, (BufferedImage)img, 5, 3 );
				ca.setPosicaoCentro( localDisparo );
				getMundo( ).addEfeito( ca );		
			}
		}
	}
	
	@Override
	public void desenhar(Graphics2D g, int cx, int cy) {
		mira.setPosicaoCentro( new Point(cx, cy) );
		mira.desenhar(g);
	}
	
}
