package inimigo;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Comparator;

import arma.drone.*;
import prof.jogos2D.image.ComponenteAnimado;
import prof.jogos2D.util.ImageLoader;

/**
 * Classe que vai servir para criar todos os veículos
 * NÃO CONFUNDIR COM A PATTERN ABSTRACT FACTORY
 */
public class FabricaVeiculos {
	
	// Constantes para os tipos de drones
	// tipos? A sério? Ainda há quem use constantes para tipos?? 
	public static final int DRONE_ZONA = 0;
	public static final int DRONE_PERSEGUIDOR = 1;
	public static final int DRONE_SENTINELA = 2;
	
	
	/** Cria e configura um drone zona
	 * @param inimigoSel tipo de seletor a usar
	 * @return o drone criado
	 */
	public static Drone criaDroneZona( Comparator<Inimigo> inimigoSel ){
		ImageLoader loader = ImageLoader.getLoader();		
		BufferedImage img = (BufferedImage)loader.getImage("data/inimigos/drone_zona.png");
		ComponenteAnimado sprite = new ComponenteAnimado( new Point(), img, 2, 4 );
		
		Drone drone = new DroneZona( sprite );
		drone.setVelocidade( 3 );
		drone.setNProjecteis( 20 );
		drone.setDano( 1.5f );
		drone.setModoAtaque( inimigoSel );
		return drone;
	}

	/** Cria e configura um drone perseguidor
	 * @param inimigoSel tipo de seletor a usar
	 */ 
	public static Drone criaDronePerseguidor( Comparator<Inimigo> inimigoSel ){
		ImageLoader loader = ImageLoader.getLoader();		
		BufferedImage img = (BufferedImage)loader.getImage("data/inimigos/drone_perseguidor.png");
		ComponenteAnimado sprite = new ComponenteAnimado( new Point(), img, 2, 4 );
		
		Drone drone = new DronePerseguidor( sprite );
		drone.setVelocidade( 4 );
		drone.setNProjecteis( 8 );
		drone.setDano( 1 );
		drone.setTempoAtivo( 300 );
		drone.setModoAtaque( inimigoSel );
		return drone;
	}
	
	/** Cria e configura um drone sentinela
	 * @param inimigoSel tipo de seletor a usar
	 */
	public static Drone criaDroneSentinela( Comparator<Inimigo> inimigoSel ){
		ImageLoader loader = ImageLoader.getLoader();		
		BufferedImage img = (BufferedImage)loader.getImage("data/inimigos/drone_sentinela.png");
		ComponenteAnimado sprite = new ComponenteAnimado( new Point(), img, 2, 4 );
 
		Drone drone = new DroneSentinela( sprite );
		drone.setVelocidade( 3 );
		drone.setNProjecteis( 12 );
		drone.setDano( 1.2f );
		drone.setTempoAtivo( 300 );
		drone.setModoAtaque( inimigoSel );
		return drone;
	}
	
	/** Cria e configura um disco pequeno
	 * @return o inimigo criado
	 */
	public static Inimigo criaDiscoPequeno(){
		InimigoDefault ini = new InimigoDefault();
		ImageLoader loader = ImageLoader.getLoader();		
		BufferedImage img = (BufferedImage)loader.getImage("data/inimigos/discoPequeno.png");
		
		// definição das características do disco pequeno
		ComponenteAnimado sprite = new ComponenteAnimado( new Point(), img, 4, 3 ); 
		ini.setComponente( sprite );
		ini.setVelocidade( 2.5f );
		ini.setResistenciaInicial( 1 );
		return ini;
	}
	
	/** Cria e configura um disco grande
	 * @return o inimigo criado
	 */
	public static Inimigo criaDiscoGrande(){
		InimigoDefault ini = new InimigoDefault();
		ImageLoader loader = ImageLoader.getLoader();		
		BufferedImage img = (BufferedImage)loader.getImage("data/inimigos/discoGrande.png");
		
		// definição das características do disco grande
		ComponenteAnimado sprite = new ComponenteAnimado( new Point(), img, 4, 3 ); 
		ini.setComponente( sprite );
		ini.setVelocidade( 2 );
		ini.setResistenciaInicial( 4 );
		return ini;
	}
	
	/** Cria e configura um disco blindado
	 * @return o inimigo criado
	 */
	public static Inimigo criaDiscoBlindado(){
		InimigoDefault ini = new InimigoDefault();
		ImageLoader loader = ImageLoader.getLoader();		
		BufferedImage img = (BufferedImage)loader.getImage("data/inimigos/discoBlindado.png");
		
		// definição das características do disco blindado
		ComponenteAnimado sprite = new ComponenteAnimado( new Point(), img, 4, 3 ); 
		ini.setComponente( sprite );
		ini.setVelocidade( 2.1f );
		ini.setResistenciaInicial( 6 );
		return ini;
	}
}
