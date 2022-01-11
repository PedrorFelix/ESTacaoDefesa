package arma;

import java.awt.Point;

import mundo.Mundo;

/** Implementa o código base de uma Arma.
 */
public abstract class ArmaDefault implements Arma {

	private Mundo mundo;   // o mundo onde a arma está instalada
	private float dano;    // dano que inflinge aos inimigos
	
	private int delay;     // tempo que a arma demora a carregar	
	private int tick = 0;  // contador descrescente até a arma estar pronta

	/** construtor da arma
	 * @param dano dano que inglige aos inimigos
	 * @param delay tempo que a arma demora a carregar
	 */
	public ArmaDefault(float dano, int delay) {
		super();
		this.setDelay(delay);
		this.setDano( dano );
	}

	@Override
	public void reset() {
		tick = 0;
	}
	
	@Override
	public void atualizar() {
		tick--;
		if( tick < 0 ) tick = 0;
	}
	
	@Override
	public void preparar(Point p) {
	}
	
	@Override
	public void apontar(Point p) {
	}
	
	@Override
	public void libertar(Point p) {
	}
	
	@Override
	public void trocar() {
	}
	
	@Override
	public Mundo getMundo() {
		return mundo;
	}
	
	@Override
	public void setMundo(Mundo m) {
		mundo = m;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}
	
	public boolean estaPronta( ) {
		return tick == 0;
	}
	
	protected void resetContagem( ) {
		tick = delay;
	}
	
	protected int getTick() {
		return tick;
	}

	public float getDano() {
		return dano;
	}

	public void setDano(float dano) {
		this.dano = dano;
	}
}
