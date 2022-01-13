package arma;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.List;

import inimigo.Inimigo;
import mundo.Mundo;
import prof.jogos2D.image.ComponenteAnimado;
import prof.jogos2D.util.ImageLoader;
import prof.jogos2D.util.Vector2D;

/** Representa a arma metralha
 */
public class Metralha extends ArmaDefault {
	
	private int distanciaMax;					// distãncia máxima da linha de metralha
	private Point inicioMetralha, fimMetralha;  // ponto inicial e final da linha
	private float raioMira = 3;                 // raio da mira de metralha (para animação da mesma)  
	
	/** Cria uma metralha
	 * @param dano dano causado
	 * @param delay tempo de recarregamento
	 * @param distanciaMax distância máxima da linha
	 */
	public Metralha( float dano, int delay, int distanciaMax) {
		super( dano, delay);
		this.distanciaMax = distanciaMax;
	}
	
	@Override
	public void reset() {
		super.reset();
		inicioMetralha = null;
		fimMetralha = null;
	}
	
	@Override
	public void preparar(Point p) {
		if( !estaPronta() ) return;
		
		// armazenar onde começa a metralhar
		inicioMetralha = p;
	}
	
	@Override
	public void apontar(Point p) {
		// se não pode disparar não faz nada
		if( !estaPronta() || inicioMetralha == null ) return; 
		
		limitarMetralha( p );
	}
	
	@Override
	public void libertar(Point p) {
		// se não pode disparar não faz nada
		if( !estaPronta() || inicioMetralha == null ) return; 
		
		limitarMetralha( p );
		// ver os inimigos afetados pela metralha e acertar-lhes 
		List<Inimigo> inis = getMundo().getInimigosFiltrados( Mundo.FILTRO_LINHA, inicioMetralha, fimMetralha );
		for( Inimigo i : inis )
			i.atingido( 1 );
		
		// configurar os efeitos visuais
		Vector2D dir = new Vector2D( inicioMetralha, fimMetralha );
		int nImg = (int)(dir.getComprimento() / 40) + 2;
		dir.normalizar();
		for( int i=0; i < nImg; i++ ) {
			Point pi = new Point( (int)(inicioMetralha.x + dir.x*i*40),
					              (int)(inicioMetralha.y + dir.y*i*40) );
			
			Image img = ImageLoader.getLoader().getImage("data/fx/impacto_multi.png" );
			ComponenteAnimado ca = new ComponenteAnimado( pi, (BufferedImage)img, 5, 3 );
			ca.setPosicaoCentro( pi );
			getMundo( ).addEfeito( ca );		
		}
		
		fimMetralha = null;
		inicioMetralha = null;
		resetContagem();
	}
	
	
	@Override
	public void desenhar(Graphics2D g, int cx, int cy) {
		g.setColor(Color.RED);
		// se não estiver a metralhar desenha a mira normal
		if( fimMetralha == null ){
			int rm = (int) raioMira;			
			g.drawOval(cx-20, cy-20, 40, 40);			
			g.drawOval(cx-20+rm, cy-20+rm, 40-rm-rm, 40-rm-rm);
			raioMira += 0.4;
			if( raioMira > 7 )
				raioMira = 3;
			
			// desenha a percentagem que falta para ativar se faltar tempo
			int perc = 100 - getTick()*100/getDelay();
			if( perc < 100 )
				g.drawString( perc + "%", cx-10, cy+5 );				
		}
		// está a metralhar, desenha a mira
		else {
			g.drawOval(inicioMetralha.x-20, inicioMetralha.y-20, 40, 40);			
			g.drawOval(fimMetralha.x-20, fimMetralha.y-20, 40, 40);
			g.drawLine(inicioMetralha.x, inicioMetralha.y, fimMetralha.x, fimMetralha.y);
		}
	}

	
	/** garantir que a metralha está dentro dos limites, ajustando o onto final
	 * @param p o ponto final da linha para ~ver se pode ser aceite
	 */
	private void limitarMetralha(Point p) {
		// ver se a metralha está dentro dos limites
		double dist = p.distance( inicioMetralha );
		if( dist > distanciaMax ){
			// ajustar o ponto final para estar dentro dos limites
			double angle = Math.acos( (p.x - inicioMetralha.x) / dist );
			if( p.y < inicioMetralha.y )
				angle = 2*Math.PI - angle;
			fimMetralha = new Point( (int)(inicioMetralha.x + Math.cos( angle ) * distanciaMax),(int)(inicioMetralha.y + Math.sin( angle ) * distanciaMax));
		}
		else
			fimMetralha = p;
	}					

	/** retorna a distância máxima
	 * @return a distância máxima
	 */
	public int getDistanciaMax() {
		return distanciaMax;
	}

	/** define a distância máxima
	 * @param distanciaMax a distância máxima
	 */
	public void setDistanciaMax(int distanciaMax) {
		this.distanciaMax = distanciaMax;
	}

	@Override
	public void aceita(VisitanteArmas v) {
		v.visitaMetralha(this);
		
	}
	
}
