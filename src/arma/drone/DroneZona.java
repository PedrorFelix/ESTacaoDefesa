package arma.drone;

import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;

import inimigo.Inimigo;
import prof.jogos2D.image.ComponenteAnimado;
import prof.jogos2D.image.ComponenteVisual;
import prof.jogos2D.util.ImageLoader;

/** Drone que se desloca para o local de operação e fica parado, atacando os inimigos
 * que passam por lá. Não persegue nenhum inimigo. Só regressa quando fica sem munições.
 */
public class DroneZona extends DroneDefault {

	public DroneZona(ComponenteVisual des) {
		super(des);
	}
	
	@Override
	protected void operacaoDois() {
		if( !temMunicoes() ){  
			voarPara( getLancador().getHangar() );
			setVoltar(true);
		}
	}
	
	@Override
	protected void operacaoTres() {
		if( !chegouDestino() ){ 
			voarPara( getDestino() );
			if( getDestino().distanceSq( getPosicao() ) < 4 )
				setChegou(true);
		}	
	}
	
	@Override
	protected void operacaoQuatro() {
		Inimigo i = escolheAlvo( getPosicao(), 15 );
		if( i != null ) {
			setAlvo( i );
			
			// dispara sobre o alvo
			if( !podeDisparar() )
				return;
			recomecaCicloDisparo();
			
			Image img = ImageLoader.getLoader().getImage("data/fx/impacto_pequeno.png" );
			Point pa = new Point( (int)getAlvo().getPosicao().x, (int)getAlvo().getPosicao().y );
			ComponenteAnimado ca = new ComponenteAnimado( pa, (BufferedImage)img, 5, 3 );
			ca.setPosicaoCentro( pa );
			getMundo( ).addEfeito( ca );
			
			getAlvo().atingido( getDano() );
			reduzProjeteis();
		}	
	}
}
