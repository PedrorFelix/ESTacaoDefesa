package arma.drone;

import java.awt.geom.Point2D;
import prof.jogos2D.image.ComponenteVisual;

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
	protected void operacaoDois() {
		if( !estaAtivo() || !temMunicoes() ){  
			voarPara( getLancador().getHangar() );
			setVoltar(true);
		} else if( !chegouDestino() ){ 
			voarPara( getDestino() );
			if( getDestino().distanceSq( getPosicao() ) < 4 )
				setChegou(true);
		} else if( !temAlvoSelecionado() ){   // ainda não tem alvo?
			setAlvo( escolheAlvo( getPosicao(), 30 ) );
		} else {
			Point2D.Double pi = voarParaAlvo();
			if( dentroAlcance(pi) ){ 
				dispara();
				//se o matou, desliga dele
				if( getAlvo().estaMorto() ) {
					setAlvo( null );
				}
			}
		}
	}
}
