package arma.drone;


import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.Comparator;

import arma.LancaDrones;
import inimigo.Inimigo;
import mundo.Mundo;
import prof.jogos2D.image.ComponenteVisual;


/**
 * Interface que representa um drone, um veículo de controlo remoto.
 * Os drones saiem do hangar e adotam o comportamento escolhido pelo jogador.
 * Quando ficam sem bateria regressam ao hangar
 */
public interface Drone {

	/**
	 * Método que move o drone.   
	 */
	void move();

	/** desenha o drone
	 * @param g ambiente gráfico onde desenhar o drone
	 */
	void desenhar(Graphics g);

	/** define qual o lançador que enviou este drone
	 * @param lancaDrones o lançador que enviou este drone
	 */
	void setLancador(LancaDrones lancaDrones);
	
	/** retorna o lançador que enviou este drone
	 * @return o lançador que enviou este drone
	 */
	LancaDrones getLancador();

	/** retorna a imagem associada ao drone
	 * @return a imagem associada ao drone
	 */
	ComponenteVisual getComponente();

	/** define a imagem associada ao drone
	 * @param v a imagem associada ao drone
	 */
	void setComponente(ComponenteVisual v);

	/** devolve o mundo onde o drone se movimenta
	 * @return o mundo onde o drone se movimenta
	 */
	Mundo getMundo();

	/** define o mundo onde o drone se movimenta
	 * @param m o mundo onde o drone se movimenta
	 */
	void setMundo(Mundo m);

	/** retorna o número de projeteis que ainda tem
	 * @return o número de projeteis que ainda tem
	 */
	int getNProjecteis();

	/** define o número de projeteis 
	 * @param projecteis o número de projeteis que ainda tem
	 */
	void setNProjecteis(int projecteis);

	/** retorna a posição do drone
	 * @return a posição do drone
	 */
	Point2D.Double getPosicao();

	/** define a posição do drone
	 * @param p a posição do drone
	 */
	void setPosicao(Point2D.Double p);

	/** retorna o dano que o drone provoca
	 * @return o dano que o drone provoca
	 */
	float getDano();

	/** define o dano que o drone provoca
	 * @param dano o dano que o drone provoca
	 */
	void setDano(float dano);

	/** retorna a velocidade
	 * @return a velocidade de movimento do drone
	 */
	int getVelocidade();

	/** define a velocidade
	 * @param veloc a velicodade de movimento
	 */
	void setVelocidade(int veloc);

	/** retorna o destino, local onde o drone atua
	 * @return o local onde o drone atua
	 */
	Point2D.Double getDestino();

	/** define o destino, local onde o drone vai atuar
	 * @param destino local onde o drone vai atuar
	 */
	void setDestino(Point2D.Double destino);

	/** indica o tempo que o drone está ativo, em ticks
	 * @return o tempo que o drone está ativo
	 */
	int getTempoAtivo();

	/** define o tempo em que o drone está ativo
	 * @param tempoAtivo tempo em que o drone está ativo
	 */
	void setTempoAtivo(int tempoAtivo);

	/** devolve o modo de seleção de inimigos
	 * @return  o modo de seleção de inimigos
	 */
	Comparator<Inimigo> getModoSelecao();

	/** define o modo de seleção de inimigos
	 * @param tipoSel o modo de seleção de inimigos
	 */
	void setModoAtaque( Comparator<Inimigo> tipoSel );

	/** indica se o drone chegou ao hangar
	 * @return true, se o drone chegou ao hangar
	 */
	boolean estaHangar();

	/** indica se o drone está a voltar para o hangar
	 * @return true, se o drone está a voltar para o hangar
	 */
	boolean estaVoltar();

	/** indica ao drone para voltar, ou não, para o hangar
	 * @param voltar true, para voltar
	 */
	void setVoltar(boolean voltar);

	/** indica se o drone chegou ao destino
	 * @return true, se o drone chegou ao destino
	 */
	boolean chegouDestino();

	/** define que o drone chegou ao destino
	 * @param chegou true, para indicar que chegou
	 */
	void setChegou(boolean chegou);

	/** indica se o drone tem alvo selecionado
	 * @return true, se o drone tem alvo selecionado 
	 */
	boolean temAlvoSelecionado();

	/** retorna o alvo selecionado
	 * @return o alvo selecionado
	 */
	Inimigo getAlvo();

	/** define o alvo a atacar
	 * @param alvo o alvo a atacar
	 */
	void setAlvo(Inimigo alvo);
	
	// opera��es para o funcionamento dos drones
	void operacaoUm();
	void operacaoDois();
	void operacaoTres();	
	void operacaoQuatro();
}