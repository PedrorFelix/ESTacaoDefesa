package arma.drone;

import java.awt.geom.Point2D;
import inimigo.Inimigo;
import prof.jogos2D.image.ComponenteVisual;

/** Drone que persegue os inimigos que passam pelo seu local de operação.
 * Não precisa de chegar ao local para escolher o alvo.
 * Após destruir o inimigo, desloca-se para o seu local de operação, ou ataca 
 * outro inimigo que entretanto passe por lá
 */
public class DronePerseguidor extends DroneDefault {

	/** cria um drone perseguidor
	 * @param des imagem do drone
	 */
	public DronePerseguidor(ComponenteVisual des) {
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
			// se entretanto passar por lá um alvo escolhe-o
			Inimigo ini = escolheAlvo( getDestino(), 20 );
			if( ini != null ){
				setAlvo(ini);
				// para este ter alvo é como se tivesse
				// chegado ao destino
				setChegou( true );
			}
		} else if( !temAlvoSelecionado() ){      // ainda não tem alvo?
			// este procura sempre na área de destino
			Inimigo ini = escolheAlvo( getDestino(), 20 );
			if( ini != null ){
				setAlvo(ini);
				// para este ter alvo é como se tivesse
				// chegado ao destino
				setChegou( true ); 
			}
		} else {
			Point2D.Double pi = voarParaAlvo();
			if( dentroAlcance(pi) ){ 
				// dispara sobre o alvo
				dispara();
				//se o matou, desliga dele
				if( getAlvo().estaMorto() ) {
					setAlvo( null );
					// neste caso o drone volta a deslocar-se para o destino
					setChegou( false );
				}
			}
		}
	}
}
