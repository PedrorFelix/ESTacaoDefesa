package arma;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.Comparator;

import arma.drone.Drone;
import inimigo.ComparatorMaisForte;
import inimigo.FabricaVeiculos;
import inimigo.Inimigo;
import mundo.Mundo;
import prof.jogos2D.image.ComponenteVisual;

/** Representa a arma que lança drones
 */
public class LancaDrones extends ArmaDefault {

	private Point2D.Double hangar; // posição de onde aparecem e para onde retornam os drones
	private ComponenteVisual mira; // imagem da mira

	private int maxDronesAtivos;     // nº máximo de drones ativos
	private int nDronesLancados = 0; // quantos drones estão já lançados 

	private int tipoDrone;  // o tipo de drone a lançar (a váriavel tipo indica que há problemas!!!) 
	private Comparator<Inimigo> tipoSelector = new ComparatorMaisForte(); // tipo de seletor a usar

	/** Cria um lançador de drones
	 * @param mira imagem da mira
	 * @param hangar posição de partida dos drones
	 * @param delay tempo entre cada lançamento de drone
	 * @param tipoDrone tipo de drone a lançar 
	 * @param maxDronesAtivos número máximo de drones simultâneos
	 */
	public LancaDrones( ComponenteVisual mira, Point2D.Double hangar, int delay, int tipoDrone, int maxDronesAtivos) {
		super( 0, delay );
		this.maxDronesAtivos = maxDronesAtivos;
		this.hangar = hangar;

		this.mira = mira;
		
		this.tipoDrone = tipoDrone;
	}

	@Override
	public void preparar(Point p) {
	}
	
	@Override
	public void apontar(Point p) {
	}
	
	@Override
	public void libertar(Point p) {
		Mundo mundo = getMundo();
		
		// se já tem o máximo de drones não pode fazer nada
		if( nDronesLancados >= maxDronesAtivos ) return;
		
		// se já lançou há pouco tempo não pode fazer nada
		if( !estaPronta() ) return;
		
		// vai lançar um drone
		
		resetContagem();  // começar a descontar o tempo
		
		// passar as coord para double por causa dos drones
		Point2D.Double dest = new Point2D.Double( p.x, p.y );

		// cria o drone
		Drone d;
		
		// TODO SWITCH um switch tipo!?!?!?!?!
		switch( tipoDrone ) {
		default:
		case FabricaVeiculos.DRONE_PERSEGUIDOR:
			d = FabricaVeiculos.criaDronePerseguidor( tipoSelector );
			break;
		case FabricaVeiculos.DRONE_ZONA:
			d = FabricaVeiculos.criaDroneZona( tipoSelector );
			break;
		case FabricaVeiculos.DRONE_SENTINELA:
			d = FabricaVeiculos.criaDroneSentinela( tipoSelector );
			break;
		}
		d.setDestino( dest );
		d.setLancador( this );
		d.setPosicao( (Point2D.Double)hangar.clone() );
		
		nDronesLancados++;
		mundo.addDrone( d );
	}
	
	/** define qual o drone a criar e respetivo seletor
	 * @param tipo  tipo de drone (ainda usam inteiros para isto?)
	 * @param tipoSel tipo de seletor (aqui já se usam objetos, muito bem)
	 */
	public void setTipoDroneLancar(int tipo, Comparator<Inimigo> tipoSel) {
		tipoDrone = tipo;
		tipoSelector = tipoSel;
	}
	
	@Override
	public void desenhar(Graphics2D g, int cx, int cy) {
		mira.setPosicaoCentro( new Point( cx, cy ) );
		mira.desenhar(g);
		
		// desenhar o número de drones lançados / máximo
		g.setColor( Color.BLUE );
		g.drawString("" + nDronesLancados + "/" + maxDronesAtivos, cx-20, cy+30);

		// desenhar a marca de pronto
		g.setColor( estaPronta()? Color.GREEN: Color.YELLOW );
		g.fillOval(cx+15, cy+20, 6, 6);
	}
	
	/** indica quantos drones estão lançados neste momento
	 * @return quantos drones estão lançados neste momento
	 */
	public int getnDronesLancados() {
		return nDronesLancados;
	}
	
	/** retorna o máximo de drones que podem ser lançados
	 * @return o máximo de drones que podem ser lançados
	 */
	public int getMaxDronesAtivos() {
		return maxDronesAtivos;
	}

	/** método chamado quando um drone regressa à base
	 * @param droneDefault o drone que regressou
	 */
	public void droneRegressou(Drone drone) {
		nDronesLancados--;
	}
	
	/** retorna a posição de partida/chegada dos drones
	 * @return a posição de partida/chegada dos drones
	 */
	public Point2D.Double getHangar() {
		return hangar;
	}

}
