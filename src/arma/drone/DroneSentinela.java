package arma.drone;


import java.awt.Image;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import prof.jogos2D.image.ComponenteAnimado;
import prof.jogos2D.image.ComponenteVisual;
import prof.jogos2D.util.ImageLoader;

/** Drone que persegue os inimigos que passam pelo seu local de operação.
 * Primeiro desloca-se para o local de operação e depois ataca os inimigos que passam por ele.
 * Persegue o inimigo até o destruir. Após destruir o inimigo fica parado e
 * ataca o inimigo que passe por onde está. 
 */
public class DroneSentinela extends DroneDefault {

	public DroneSentinela(ComponenteVisual des) {
		super(des);
	}
	
	@Override
	public void operacaoUm() {
		if( !estaAtivo() || !temMunicoes() ){  
			voarPara( getLancador().getHangar() );
			setVoltar(true);
		}
	}
	
	@Override
	public void operacaoDois() {
		if( !chegouDestino() ){ 
			voarPara( getDestino() );
			if( getDestino().distanceSq( getPosicao() ) < 4 )
				setChegou(true);
		}	
	}
	
	@Override
	public void operacaoTres() {
		if( !temAlvoSelecionado() ){   // ainda não tem alvo?
			setAlvo( escolheAlvo( getPosicao(), 30 ) );
		}
	}
	
	@Override
	public void operacaoQuatro() {
		Point2D.Double pi = voarParaAlvo();
		if( dentroAlcance(pi) ){ 
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
			//se o matou, desliga dele
			if( getAlvo().estaMorto() ) {
				setAlvo( null );
			}
		}
	}
}
