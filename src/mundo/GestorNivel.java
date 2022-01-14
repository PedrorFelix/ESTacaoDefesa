package mundo;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JOptionPane;

import arma.*;
import inimigo.FabricaVeiculos;
import inimigo.Inimigo;
import prof.jogos2D.image.ComponenteAnimado;
import prof.jogos2D.image.ComponenteMultiAnimado;
import prof.jogos2D.image.ComponenteSimples;
import prof.jogos2D.image.ComponenteVisual;

/**
 * Classe responsável pela gestão do nivel (ou round) como ler o ficheiro e
 * gerar os inimigos após o começo do round. A indicação da criação dos inimigos é dada
 * por ticks. A cada tick o gerador contém uma lista dos inimigos a criar
 * 
 */
public class GestorNivel {
	
	private int tick;			// tick do nível
	private int nextCriar;      // qual a próxima lista a ser usada para a criação

	private ArrayList<TickInfo> coisasCriar = new ArrayList<TickInfo>(); // lista com os inimigos a criar
	private ArrayList<Arma> armasDisponiveis = new ArrayList<Arma>();    // lista com as armas disponíveis
	private ArrayList<String> iconesArmas = new ArrayList<String>();     // lista com os ícones das armas
	
	/** Cria um gestor de nível
	 */
	public GestorNivel(){		
	}
	
	/**
	 * lê o ficheiro onde se encontra o nível a processar
	 * @param fileName ficheiro onde se encontra o nível a processar
	 */
	public void lerNivel( String fileName ){
		// limpar o que havia
		coisasCriar.clear();
		armasDisponiveis.clear();
		iconesArmas.clear();
		try {
			BufferedReader fin = new BufferedReader( new FileReader( fileName ));
			// ler o valor do round
			String linha = fin.readLine();

			// ler as armas e suas configs
			// ignorar tudo o que está antes de --ARMAS--
			while( linha != null && !linha.equals("--ARMAS--") ) {
				linha = fin.readLine();
				System.out.println("ignorar " + linha);
			}
			
			if( linha == null ) {
				fin.close();
				throw new IOException("--ARMAS-- não encontrado");
			}
				
			linha = fin.readLine();
			while( linha != null && !linha.equals("--INIMIGOS--") ) {
				switch( linha ) {
				case "canhao": lerCanhao( fin ); break;
				case "metralha": lerMetralha( fin ); break;
				case "drone": lerDrone( fin ); break;
				case "missil": lerMissil( fin ); break;
				case "laser": lerLaser( fin ); break;
				}
				linha = fin.readLine();
			}

			if( linha == null ) {
				fin.close();
				throw new IOException("--INIMIGOS-- não encontrado");
			}
			// ler os inimigos a criar
			linha = fin.readLine();
			while( linha != null ){
				TickInfo ticker = new TickInfo();
				String dados[] = linha.split("\t");
				// ler o tick de criação
				ticker.tickActivar = Integer.parseInt( dados[0] );
				for( int i = 1; i < dados.length; i++ ){
					// criar um par de criação
					ParCriacao par = new ParCriacao();
					par.caminhoId = Integer.parseInt( dados[i] );
					i++;
					if( dados[i].equals("blindado"))
						par.inimigoCriar = FabricaVeiculos.criaDiscoBlindado();
					else if( dados[i].equals("pequeno") )
						par.inimigoCriar = FabricaVeiculos.criaDiscoPequeno();
					else if( dados[i].equals("grande"))
						par.inimigoCriar = FabricaVeiculos.criaDiscoGrande();
					else
						throw new IOException("Tipo de inimigo desconhecido " + dados[i] );
					ticker.pares.add( par );
				}
				coisasCriar.add( ticker );
				linha = fin.readLine();
			}	
			fin.close();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog( null, "Ficheiro não encontrado " + fileName );
			e.printStackTrace();
		} catch (Exception e) {
			JOptionPane.showMessageDialog( null, "Erro na leitura do ficheiro " + fileName );
			e.printStackTrace();
		}
		startNivel();
	}
	
	private void lerCanhao(BufferedReader fin) throws IOException {
		iconesArmas.add( fin.readLine() );
		float dano = Float.parseFloat( fin.readLine() );
		int delay = Integer.parseInt( fin.readLine() );
		ComponenteVisual mira = lerComponenteVisual( fin.readLine() );
		armasDisponiveis.add( new Canhao( dano, delay, mira ) ); 
	}

	private void lerMetralha(BufferedReader fin) throws IOException {
		iconesArmas.add( fin.readLine() );
		float dano = Float.parseFloat( fin.readLine() );
		int delay = Integer.parseInt( fin.readLine() );
		int distMax = Integer.parseInt( fin.readLine() );
		armasDisponiveis.add( new Metralha( dano, delay, distMax ) ); 
	}

	private void lerDrone(BufferedReader fin) throws IOException {
		iconesArmas.add( fin.readLine() );
		String[] xy = fin.readLine().split("\t" );
		Point2D.Double posHangar = lerPosicao( xy[0], xy[1] );
		
		int delay = Integer.parseInt( fin.readLine() );
		int maxDrones = Integer.parseInt( fin.readLine() );
		ComponenteVisual mira = lerComponenteVisual( fin.readLine() );
		armasDisponiveis.add( new LancaDrones( mira, posHangar, delay, maxDrones) );
	}
	
	private void lerLaser(BufferedReader fin) throws IOException {
		iconesArmas.add( fin.readLine() );
		String fatores[] = fin.readLine().split("\t");
		float fatorAquec = Float.parseFloat( fatores[0] );
		float fatorArref = Float.parseFloat( fatores[1] );
		armasDisponiveis.add( new Laser( 0.1f, fatorAquec, fatorArref ) ); 
	}


	private void lerMissil(BufferedReader fin) throws IOException {
		iconesArmas.add( fin.readLine() );
		float dano = Float.parseFloat( fin.readLine() );
		int delay = Integer.parseInt( fin.readLine() );
		int veloc = Integer.parseInt( fin.readLine() );
		armasDisponiveis.add( new Missil( dano, delay, veloc ) ); 
	}

	/** retorna a lista de armas disponiveis neste nível
	 * @return a lista de armas disponiveis neste nível
	 */
	public List<Arma> getArmasDisponiveis() {
		return Collections.unmodifiableList( armasDisponiveis );
	}

	/** retorna a lista dos nomes dos ícones das armas disponíveis
	 * @return a lista dos nomes dos ícones das armas disponíveis
	 */
	public List<String> getIconesArmas() {
		return Collections.unmodifiableList( iconesArmas );
	}

	/**
	 * começar a geração de inimigos
	 */
	public void startNivel(){
		tick = -1;          // começar em -1 para que no método de criar se comece a somar logo os ticks
		nextCriar = 0;      // o próximo tick a criar vai ser o zero
	}
	
	/**
	 * devolve um array com os inimigos criados e adicionados ao mundo neste tick
	 * @return um array com os inimigos criados neste tick
	 */
	public List<Inimigo> createInimigos( Mundo m ){
		ArrayList<Inimigo> criados = new ArrayList<Inimigo>();

		// ver se ainda tem inimigos para criar
		if( !faltamInimigos() )
			return criados;
		
		// passou um tick
		tick++;
		
		// ver a informaçao neste tick sobre o que se cria
		// se esta linha for para ser criada numa altura diferente, nao faz nada
		TickInfo ticker = coisasCriar.get( nextCriar );
		if( ticker.tickActivar >= tick )
			return criados;
		
		for( ParCriacao p : ticker.pares ) {
			m.addInimigo( p.caminhoId, p.inimigoCriar );
			criados.add( p.inimigoCriar );
		}
		
		// esta linha foi processada passa para a próxima
		nextCriar++; 		

		// devolve os inimigos criados
		return criados;
	}
	
	
	/**
	 * indica se ainda faltam criar inimigos
	 * @return true se ainda faltam criar inimigos
	 */
	public boolean faltamInimigos( ){
		return  nextCriar < coisasCriar.size();
	}
	

	/**
	 * classe auxiliar que vai armazenar a informação de cada linha de criação
	 */
	private class TickInfo {
		int tickActivar;		  // a que tick devem ser criados estes inimigos
		ArrayList<ParCriacao> pares = new ArrayList<ParCriacao>(); // pares a criar neste tick
	}
	
	/**
	 * Classe Auxiliar para armazenar qual o inimigo a criar e o caminho onde deve ser colocado
	 * @author F. Sergio Barbosa
	 */
	public static class ParCriacao {
		int caminhoId;   	    // a que caminho deve ser associado o inimigo
		Inimigo inimigoCriar; 	// o inimigo a criar neste tick		
	}
	
	
	/** leitura de um componente visual
	 * @param dados informação da linha
	 * @return o componente visual criado
	 * @throws IOException
	 */
	private ComponenteVisual lerComponenteVisual( String dados ) throws IOException {
		String info[] = dados.split("\t");
		switch( info[0] ) {
		case "CS":  return criarComponenteSimples( info, 1 );
		case "CA":  return criarComponenteAnimado( info, 1 );
		case "CMA": return criarComponenteMultiAnimado( info, 1 );
		}
		return null;
	}

	/** lê a info e cria um componente simples
	 * Na linha a info é CS + nome da imagem
	 */
	private ComponenteSimples criarComponenteSimples(String[] info, int idx) throws IOException {
		return new ComponenteSimples( info[idx] );
	}
	
	/** lê a info e cria um componente animado
	 * Na linha a info é CA + nome da imagem + número de frames + delay na animação
	 */
	private ComponenteAnimado criarComponenteAnimado(String[] info, int idx ) throws IOException {
		String nomeImg =  info[idx];
		int nFrames = Integer.parseInt( info[idx+1] );
		int delay = Integer.parseInt( info[idx+2] );
		return new ComponenteAnimado( new Point(0,0), nomeImg, nFrames, delay );
	}

	/** lê a info e cria um componente multianimado
	 * Na linha a info é CMA + nome da imagem + número de animações + número de frames + delay
	 */
	private ComponenteMultiAnimado criarComponenteMultiAnimado(String[] info, int idx ) throws IOException {
		String nomeImg =  info[idx];
		int nAnims =  Integer.parseInt( info[idx+1] );
		int nFrames = Integer.parseInt( info[idx+2] );
		int delay = Integer.parseInt( info[idx+3] );
		return new ComponenteMultiAnimado( new Point(0,0), nomeImg, nAnims, nFrames, delay );
	}

	/**lê uma posição em point
	 * @param strx coordenada x
	 * @param stry coordenada y
	 * @return a posição lida
	 */
	private Point2D.Double lerPosicao(String strx, String stry) {
		int x = Integer.parseInt(strx);
		int y = Integer.parseInt(stry);
		return new Point2D.Double(x,y);
	}

}
