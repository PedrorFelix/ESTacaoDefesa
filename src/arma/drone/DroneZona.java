package arma.drone;

import inimigo.Inimigo;
import prof.jogos2D.image.ComponenteVisual;

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
			super.operacaoDois();
		}
	}
	
	@Override
	protected void operacaoTres() {
		if( !chegouDestino() ){ 
			super.operacaoTres();
		}
	}
	
	@Override
	protected void operacaoQuatro() {
		Inimigo i = escolheAlvo( getPosicao(), 15 );
		if( i != null ) {
			setAlvo( i );
			// dispara sobre o alvo
			dispara();
		}
	}
}
