package arma.drone;


import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Comparator;

import arma.LancaDrones;
import inimigo.Inimigo;
import mundo.FiltroRaio;
import mundo.Mundo;
import prof.jogos2D.image.ComponenteAnimado;
import prof.jogos2D.image.ComponenteVisual;
import prof.jogos2D.util.ImageLoader;
import prof.jogos2D.util.Vector2D;

/**
 * Classe que representa um drone, um veiculo de controlo remoto.
 * Os drones deslocam-se segundo o comportamento escolhido pelo jogador.
 * Quando ficam sem combustível (tempo) regressam ao hangar
 */
public abstract class DroneDefault implements Drone {
	private ComponenteVisual imagem;   	   // imagem do drone
	private int velocidade = 0;            // a velocidade a que se desloca
	private Mundo mundo;                   // o mundo onde se desloca
	private Point2D.Double posicao;        // a posição do centro do drone
	private Point2D.Double destino;        // posição para onde vai ser despoletado
	private int nProjecteis;			   // número de projéteis que transporta
	private float potencia;                // dano de cada projétil
	private int tempoAtivo;                // durante quantos ticks está ativo
	private int tempoDisparo = 20;		   // tempo entre disparos
	private int tickDisparo = 0;           // quantos ticks para disparar 
	private Comparator<Inimigo> tipoSeletor; // modo do drone selecionar os inimigos 

	private LancaDrones lancador;         // quem o lançou

	private boolean chegou = false;       // já chegou ao destino?
	private boolean voltar = false;       // esta a voltar?
	private Inimigo alvo;               // o inimigo a atacar

	/** construtor do drone
	 * @param des imagem associada ao drone
	 */
	public DroneDefault( ComponenteVisual des ){
		setComponente( des );
	}

	/** atualiza o tempo de disparo
	 */
	protected void reduzTempoDisparo() {
		tickDisparo--;
		if( tickDisparo < 0 ) tickDisparo = 0;
	}

	/** indica se um ponto est´+a dentro do alcance do drone
	 * @param pi ponto a verificar
	 * @return true, se estiver dentro do alcance
	 */
	protected boolean dentroAlcance(Point2D.Double pi) {
		// o alcance são de 3 pixeis (
		return pi.distance( getPosicao() ) <= 3;
	}
	
	/** acabou de disparar, recomeça novo ciclo
	 */
	protected void recomecaCicloDisparo() {
		tickDisparo = tempoDisparo;
	}

	/** indica se pode disparar
	 * @return true, se puder disparar
	 */
	protected boolean podeDisparar() {
		return tickDisparo == 0;
	}

	/** faz o drone voar para perseguir o alvo
	 * @return a posição onde está o inimigo que persegue 
	 */
	protected Point2D.Double voarParaAlvo() {
		Point2D.Double pi = new Point2D.Double(getAlvo().getPosicao().x, getAlvo().getPosicao().y);
		voarPara( pi );
		return pi;
	}

	/** voa o drone para uma dada posição
	 * @param p posição para onde o drone deve voar 
	 */
	protected void voarPara( Point2D.Double p ) {
		Vector2D direcao = new Vector2D( posicao, p );
		direcao.normalizar();
		posicao.x += velocidade*direcao.x;
		posicao.y += velocidade*direcao.y;
		imagem.setPosicaoCentro( new Point( (int)posicao.x, (int)posicao.y ) );
	}

	/** reduz o tempo de ativiade do drone porque este se está a mover
	 */
	protected void reduzTempoAtivo() {
		tempoAtivo--;
	}
	
	/** reduz o número de projéteis
	 */
	protected void reduzProjeteis() {
		nProjecteis--;
	}

	/** indica se está ativo (ainda tem bateria)
	 * @return true se está ativo
	 */
	protected boolean estaAtivo() {
		return tempoAtivo > 0;
	}
	
	/** indica se ainda tem munições
	 * @return true se tiver ainda pelo menos um projétil
	 */
	protected boolean temMunicoes() {
		return nProjecteis > 0;
	}
	
	/** Escolhe o alvo usando um filtro de círculo e o seletor escolhido
	 * @param posicao centro do círculo
	 * @param raio raio do círculo
	 * @return o iniimgo escolhjido, ou null caso não haja nenhum elegível
	 */
	protected Inimigo escolheAlvo( Point2D.Double posicao, int raio ){
		mundo.setFiltro(new FiltroRaio());
		return mundo.getInimigoMaisAdequado(posicao, raio, tipoSeletor );
	}

	@Override
	public void desenhar( Graphics g ){
		// desenhar o boneco
		imagem.desenhar(g);
	}

	@Override
	public ComponenteVisual getComponente() {
		return imagem;
	}

	@Override
	public void setComponente(ComponenteVisual v ) {
		imagem = v;
	}

	@Override
	public Mundo getMundo() {
		return mundo;
	}

	@Override
	public void setMundo(Mundo meuMundo) {
		this.mundo = meuMundo;
	}

	@Override
	public int getNProjecteis() {
		return nProjecteis;
	}

	@Override
	public void setNProjecteis(int projecteis) {
		nProjecteis = projecteis;
	}

	@Override
	public Point2D.Double getPosicao() {
		return posicao;
	}

	@Override
	public void setPosicao(Point2D.Double p) {
		imagem.setPosicaoCentro( new Point( (int)p.x, (int)p.y ) );
		posicao = p;
	}

	@Override
	public float getDano() {
		return potencia;
	}

	@Override
	public void setDano(float potencia) {
		this.potencia = potencia;
	}

	@Override
	public int getVelocidade() {
		return velocidade;
	}

	@Override
	public void setVelocidade(int speed) {
		this.velocidade = speed;
	}

	@Override
	public Point2D.Double getDestino() {
		return destino;
	}

	@Override
	public void setDestino(Point2D.Double destino) {
		this.destino = destino;
	}

	@Override
	public int getTempoAtivo() {
		return tempoAtivo;
	}

	@Override
	public void setTempoAtivo(int tempoActivo) {
		this.tempoAtivo = tempoActivo;
	}

	@Override
	public Comparator<Inimigo> getModoSelecao() {
		return tipoSeletor;
	}

	@Override
	public void setModoAtaque(Comparator<Inimigo> modoAtaque) {
		this.tipoSeletor = modoAtaque;
	}

	@Override
	public LancaDrones getLancador() {
		return lancador;
	}

	@Override
	public void setLancador(LancaDrones lancador) {
		this.lancador = lancador;
	}
	
	@Override
	public boolean estaHangar() {
		return estaVoltar() && lancador.getHangar().distanceSq( posicao ) < 4;
	}

	@Override
	public boolean estaVoltar() {
		return voltar;
	}

	@Override
	public void setVoltar(boolean voltar) {
		this.voltar = voltar;
	}

	@Override
	public boolean chegouDestino() {
		return chegou;
	}

	@Override
	public void setChegou(boolean chegou) {
		this.chegou = chegou;
	}

	@Override
	public boolean temAlvoSelecionado() {
		return alvo != null;
	}

	@Override
	public Inimigo getAlvo() {
		return alvo;
	}

	@Override
	public void setAlvo(Inimigo target) {
		this.alvo = target;
	}
	
	public void dispara() {
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
	
	// opera��es para o funcionamento dos drones
	protected void operacaoUm() {
		reduzTempoAtivo();
		reduzTempoDisparo();
		if( estaVoltar() ) {
			voarPara( getLancador().getHangar() );
			if( estaHangar() )
				getLancador().droneRegressou( this );
		}
	};
	protected void operacaoDois() {};
	
	
	@Override
	public void move() {	
		operacaoUm();
		operacaoDois();
	}
}
