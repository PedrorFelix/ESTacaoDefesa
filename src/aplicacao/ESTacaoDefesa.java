package aplicacao;


import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.swing.*;

import arma.*;
import inimigo.Inimigo;
import mundo.Caminho;
import mundo.GestorNivel;
import mundo.Mundo;
import prof.jogos2D.image.*;
import prof.jogos2D.util.ImageLoader;
import prof.jogos2D.util.SKeyboard;

/** Janela para o jogo ESTação Defesa
 */
public class ESTacaoDefesa extends JFrame {
	private static final long serialVersionUID = 1L;
	
	// dimensões do jogo
	private static final int COMPRIMENTO = 648;
	private static final int ALTURA = 478;
	private static final int CENTRO_H = COMPRIMENTO / 2;
	private static final int CENTRO_V = ALTURA / 2;
	
	// variáveis para os vários elementos visuais do jogo
	private JPanel zonaJogo = null;   // zona onde se desenha o jogo
	private JLabel vidasLBL = null;   // label onde aparecem as vidas
	private JLabel nivelLBL = null;   // label onde aparecem o número do nível
	private JLabel mortesLBL = null;  // label onde aparecem as mortes
	private JButton playBt = null;    // botãode play
	private CheckBoxArma []controlosArmas = new CheckBoxArma[10]; // controlos para 10 armas

	// teclado para saber que teclas estão premidas
	private SKeyboard teclado;  
	
	// variáveis para o mundo
	private Mundo mundo = null; 
	private GestorNivel gereNivel = new GestorNivel(); 

	// variáveis patra lidar com as armas
	private java.util.List<Arma> armasDisponiveis;  // lista das armas disponíveis numa ronda
	private Arma armaAtual;                         // arma atualmente selecionada
	private ConfiguradorArmas configurador = new ConfiguradorArmas();
	
	// variáveis sobre o estado do jogo em si
	private int round = 0;               // nº do round atual
	private boolean nivelAcabado = true; // nível acabou? 
	private int nVidas;                  // vidas disponíveis 
	private int nMortes;				 // mortes conseguidas
		
	// variáveis auxiliares para o jogo
	// cor a ser usada nos vários paineis
	private static final Color verdeTropa = new Color(90, 110, 70); 
	// temporizador para atualizar o jogo
	private Timer temporizador;                                     

	/**
	 * Construtor do jogo
	 */
	public ESTacaoDefesa() {
		super();
		initialize();
	}
	
	/**
	 * inicializa o jogo e arranca com o 1º nível
	 */
	private void iniciarJogo() {
		// preparar o teclado 	
		zonaJogo.requestFocus();
		teclado = new SKeyboard( ); 
		
		// aqui deveria-se controlar a escolha da pista, mas só se vai suportar 1 pista por isso...
		mundo = new Mundo();
		loadPista( 1 );
	
		// carregar o primeiro nível
		setNivel( 900 );
		// definir vidas e mortes
		setVidas( 5 );
		setMortes( 0 );
		
		// no fim está tudo pronto para mandar desenhar o mundo
		repaint();
	}

	/**
	 * define qual o nível que se vai jogar
	 * @param n nº do nível a jogar
	 */
	private void setNivel( int n ){
		// se está a repetir o nível não precisa de voltar a ler
		if( n == round )
			return;
		round = n;
		
		// o gerador de nível vai ler as informações do nível do ficheiro
		gereNivel.lerNivel( "data/niveis/nivel" + round + ".txt" );				
		nivelLBL.setText( ""+round );
		
		// preparar as armas disponíveis neste nível
		armasDisponiveis = gereNivel.getArmasDisponiveis();
		java.util.List<String> icones =  gereNivel.getIconesArmas();
		
		// configurar os elementos da interface e iniciar as armas
		for( int i = 0; i < armasDisponiveis.size(); i++ ) {
			Arma a = armasDisponiveis.get( i );
			a.setMundo( mundo );
			controlosArmas[i].setIconFile(icones.get(i) );
			controlosArmas[i].setArma( a );
			controlosArmas[i].setVisible( true );
			controlosArmas[i].setEnabled( true );
			controlosArmas[i].setSelected( false );
		}
		// os botões não usados são tornados invisíveis
		for( int i = armasDisponiveis.size(); i<10; i++ ) {
			controlosArmas[i].setVisible( false );
			controlosArmas[i].setSelected( false );
		}
		
		// seleccionar a arma associada ao primeiro controlo
		selecionarArma( controlosArmas[0] );
	}

	/**
	 * começa um nível
	 */
	private void startNivel(){		
		// fazer o reset às variáveis de controlo do nível
		nivelAcabado = false;
		gereNivel.startNivel();

		// por os delays e outras vars a zero
		for( Arma a : armasDisponiveis )
			a.reset();
		
		// selecionar a primeira arma
		controlosArmas[0].setSelected( true );
		
		// ativar o cursor novo				 
		Image image = prof.jogos2D.util.ImageLoader.getLoader().getImage("data/miras/cursor.gif");  
		Point hotSpot = new Point(15,15);
		Cursor cursor = Toolkit.getDefaultToolkit().createCustomCursor(image, hotSpot, "cursor");
		zonaJogo.setCursor(cursor);
		
		// ativar a actualizadora do jogo
		temporizador.restart();

		// por o botão de play visível
		playBt.setVisible( false );
	}

	/** método que atualiza o jogo 30 vezes por segundo (chamado pelo temporizador)
	 */
	private void atualizarJogo() {
		// ver as teclas de mudança de arma
		for( int tecla = KeyEvent.VK_1; tecla <= KeyEvent.VK_9; tecla++ ) {
			int idxArma = tecla - KeyEvent.VK_1;  
			if( teclado.estaPremida( tecla ) && controlosArmas[idxArma].isVisible() ){
				controlosArmas[idxArma].setSelected( true );
				break;
			}
		}

		// Ao carregar em space é preciso mostrar
		// a janela de configuração da arma
		if( teclado.estaPremida( KeyEvent.VK_SPACE ) ){
			armaAtual.aceita(configurador);
		}
		
		// vai atualizar todos os elementos do jogo
		mundo.atualizar();  
		
		// criar os novos elementos do jogo, são adicionados automaticamente ao mundo
		java.util.List<Inimigo> criados = gereNivel.createInimigos( mundo );
		// TODO COMUNICAÇÃO configurar os inimigos para esta estação 
		for( Inimigo i : criados )
			i.setEstacao( this );
		
		// verificar se o round está ganho (o perdido é testado no setVidas)
		if( !nivelAcabado )
			testaVitoria();
		
		// verificar se o round acabou completamente
		if( nivelAcabado && !mundo.temEfeitos() ){
			endNivel();				
		}
		
		// atualizar as armas todas
		for( Arma a : armasDisponiveis )
			a.atualizar();
		
		// mandar atualizar o desenho das coisas
		repaint();
		
	}

	/**
	 * termina um nível
	 */
	private void endNivel(){
		// para de atualizar o écran
		temporizador.stop();
		
		// limpa inimigos e outros elementos móveis (drones por exemplo)
		mundo.clearMoveis();
		
		// retomar o cursor antigo
		zonaJogo.setCursor( Cursor.getDefaultCursor() );
				
		// se tem nVidas = 0 é porque perdeu
		if( nVidas <= 0 ){
			iniciarJogo();
		}
		//	senão passa ao nível seguinte, se ainda houver
		// ESTE CÓDIGO NÃO É FLÉXIVEL, MAS NÃO É PRECISO CORRIGIR
		// ESTE CÓDIGO NÃO É FLÉXIVEL, MAS NÃO É PRECISO CORRIGIR
		// ESTE CÓDIGO NÃO É FLÉXIVEL, MAS NÃO É PRECISO CORRIGIR	
		else if( round < 6 ){
			zonaJogo.repaint();
			setNivel( round + 1 );
		}
		else {
			iniciarJogo();
		}
		playBt.setVisible( true );		
	}

	/**
	 * desenha todos os componentes do jogo
	 */
	private void desenharJogo(Graphics2D g) {
		if( mundo != null )
			mundo.desenha(g);
		
		// ver a posição do rato relativamente à zona de jogo
		Point pos = MouseInfo.getPointerInfo().getLocation();
		Point r = zonaJogo.getLocationOnScreen();
		int cx = pos.x - r.x;
		int cy = pos.y - r.y;
		
		if( armaAtual != null )
			armaAtual.desenhar( (Graphics2D)g, cx, cy );
	}

	/** método chamado pela interface quando o utilizador escolhe uma arma no painel
	 * @param sel o botão selecionado
	 */
	private void selecionarArma(CheckBoxArma sel) {
		// desativar a arma anterior
		if( armaAtual != null )
			armaAtual.trocar();
		
		// ativar a nova arma
		armaAtual = sel.getArma();
	}
	
	/**
	 * Método que deve ser chamado sempre que um inimigo morre
	 * @param b inimigo que rebenta
	 */
	public void inimigoMorreu( Inimigo ini ){
		setMortes( nMortes + 1 );
		testaVitoria();
	}

	/**
	 * Método que deve ser chamado sempre que um inimigo sai do mundo
	 * @param b inimigo que escapa
	 */
	public void inimigoPassou( Inimigo ini ){
		setVidas( nVidas - 1 );
	}
	
	/** vai testar se o nível foi ultrapassado com sucesso
	 */
	private void testaVitoria() {
		// ver se já matou tudo e já não faltam mais inimigos por criar
		if( mundo.getInimigos().size() == 0 && !gereNivel.faltamInimigos() && nVidas > 0  ) {
			nivelAcabado = true;
			BufferedImage img = (BufferedImage)ImageLoader.getLoader().getImage("data/fx/nivelcompleto.png");
			Point posCentro = new Point(CENTRO_H,CENTRO_V);
			ComponenteAnimado passou = new ComponenteAnimado( posCentro, img, 2, 4 );
			ComponenteTemporario ct = new ComponenteTemporario( passou, 60 );
			ct.setPosicaoCentro( posCentro );
			mundo.addEfeito( ct );
		}
	}
	
	/**
	 * define e atualiza as vidas do jogo
	 * @param vidas novo número de vidas
	 */
	private void setVidas( int vidas ){
		nVidas = vidas;
		vidasLBL.setText( ""+nVidas ); // atualiza o label
		if( nVidas <= 0 ){
			nVidas = 0;
			nivelAcabado = true;	
			
			// adicionar o efeito game over :-(
			BufferedImage img = (BufferedImage)ImageLoader.getLoader().getImage("data/fx/gameover.png");
			Point posCentro = new Point(CENTRO_H,CENTRO_V);
			ComponenteAnimado gameOver = new ComponenteAnimado( posCentro, img, 2, 4 );
			ComponenteTemporario go = new ComponenteTemporario( gameOver, 60 );
			go.setPosicaoCentro( posCentro );
			mundo.addEfeito( go );
		}
	}
	
	/**
	 * define e atualiza as mortes efetuadas
	 * @param mortes novo número de mortes
	 */
	private void setMortes( int mortes ){
		nMortes = mortes;
		mortesLBL.setText( ""+nMortes );  // atualiza o label
	}

	/** 
	 * método para ler a pista do jogo
	 * @param numPista
	 */
	private void loadPista( int numPista){
		ImageLoader loader = ImageLoader.getLoader();
		
		// ler imagem da pista
		ComponenteVisual fundo = new ComponenteSimples( loader.getImage("data/terrenos/pista"+numPista+".gif") );
		mundo.setFundo( fundo );
		
		// ler caminho da pista
		Caminho caminho = null;
		try( BufferedReader fin = new BufferedReader( new FileReader( "data/terrenos/caminho" + numPista + ".txt")) ) {
			String linha = fin.readLine();
			do {
				// Na primeira linha está a string -INICIO-
				if( !linha.equals("-INICIO-") )
					throw new IOException();
				// criar um novo caminho
				caminho = new Caminho();
				// o 1º ponto é especial pois é o ponto de partida
				linha = fin.readLine();
				String []data = linha.split("\t");
				int x = Integer.parseInt( data[0] );
				int y = Integer.parseInt( data[1] );
				Point p = new Point( x, y );
				caminho.addPonto(p);
				while( !linha.equals("-FIM-") ){
					data = linha.split("\t");
					x = Integer.parseInt( data[0] );
					y = Integer.parseInt( data[1] );
					p = new Point( x, y );
					caminho.addSegmento(p);
					linha = fin.readLine();
				}
				mundo.addCaminho( caminho );
				linha = fin.readLine();
			} while( linha != null );
			fin.close();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	/*
	 * Métodos de inicialização da interface gráfica, 
	 * não deve ser necessário alterar nada aqui
	 */
	/**
	 * Inicializar a interface gráfica
	 */
	private void initialize() {
		this.setTitle("ESTação Defesa");	
		getContentPane().add(getZonaBotoes(), BorderLayout.WEST);
		getContentPane().add(getZonaJogo(), BorderLayout.CENTER);
		getContentPane().add(getZonaStatus(), BorderLayout.SOUTH );
		this.pack();
		this.setResizable(false);
		setLocationRelativeTo( null ); // para ficar no centro do écran
		// inicializar o temporizador
		temporizador = new Timer( 33, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				atualizarJogo();
			}
		});
	}

	/**
	 * Inicializa a zona onde vai decorrer o jogo  	
	 */
	private JPanel getZonaJogo() {
		if (zonaJogo == null) {
			zonaJogo = new JPanel(){
				private static final long serialVersionUID = 1L;

				public void paint(Graphics g) {
					desenharJogo((Graphics2D) g );
				}
			};
			Dimension d = new Dimension( COMPRIMENTO, ALTURA);
			zonaJogo.setPreferredSize( d );
			zonaJogo.setSize( d );
			zonaJogo.setMinimumSize( d );
			zonaJogo.setBackground(Color.pink);
			MouseAdapter ouve = new MouseAdapter() {
				// se o jogador carregou com o rato está a preparar a arma
				public void mousePressed(MouseEvent e) {
					armaAtual.preparar( e.getPoint() );
				}
				// se o jogador arrastou o rato está a apontar a arma
				public void mouseDragged(MouseEvent e) {
					armaAtual.apontar( e.getPoint() );
				}
				// se o jogador libertou o rato está a disparar a arma
				public void mouseReleased(MouseEvent e) {
					armaAtual.libertar( e.getPoint() );
				}
			};
			zonaJogo.addMouseListener( ouve );
			zonaJogo.addMouseMotionListener( ouve );						
		}
		return zonaJogo;
	}
			
	
	/** inicializa a barra de estado
	 */
	private JPanel getZonaStatus() {
		Font arial16 = new Font("Arial", Font.BOLD, 14);
		Color verde = new Color(177, 194, 154);
		JLabel estGamesLbl = new JLabel( new ImageIcon("data/icons/estgames.gif"), SwingConstants.LEFT );
		estGamesLbl.setPreferredSize( new Dimension(250, 20) );
		JLabel jLabel1 = new JLabel( "Vidas: ");
		jLabel1.setBounds(new Rectangle(5, 36, 71, 19));
		jLabel1.setForeground( verde );
		//jLabel1.setText("Vidas:");
		jLabel1.setFont(new Font("Arial", Font.BOLD, 14));
		vidasLBL = new JLabel( "20", SwingConstants.LEFT);
		vidasLBL.setPreferredSize( new Dimension(100, 20) );
		vidasLBL.setForeground( verde );
		vidasLBL.setFont( arial16 );
		JLabel jLabel = new JLabel();
		jLabel.setText("Nivel:");
		jLabel.setFont( arial16 );
		jLabel.setForeground( verde );
		nivelLBL = new JLabel("1", SwingConstants.LEFT);
		nivelLBL.setSize( 100, 20 );
		nivelLBL.setForeground( verde );
		nivelLBL.setFont( arial16 );
		JLabel jLabel2 = new JLabel();
		jLabel2.setFont( arial16 );
		jLabel2.setForeground( verde );
		jLabel2.setText("Mortes:");
		mortesLBL = new JLabel( "20", SwingConstants.LEFT);
		mortesLBL.setPreferredSize( new Dimension(100, 20) );
		mortesLBL.setForeground( verde );
		mortesLBL.setFont( arial16 );
		JPanel zonaStatus = new JPanel( new FlowLayout(FlowLayout.LEFT, 5, 3) );
		zonaStatus.setPreferredSize( new Dimension(100, 25) );		
		zonaStatus.setBackground( verdeTropa );
		zonaStatus.add(estGamesLbl, null);
		zonaStatus.add(jLabel1, null);
		zonaStatus.add(vidasLBL, null);
		zonaStatus.add(jLabel, null);
		zonaStatus.add(nivelLBL, null);
		zonaStatus.add(jLabel2, null);
		zonaStatus.add(mortesLBL, null);
		return zonaStatus;
	}

	/** inicializa a zona dos botões das armas
	 */
	private JPanel getZonaBotoes() {
		JPanel zonaBotoes = new JPanel( new BorderLayout() );
		//zonaBotoes.setSize(new Dimension(150, 478));
		zonaBotoes.setPreferredSize(new Dimension(140, 120));
		zonaBotoes.setBackground( verdeTropa );

		JPanel botoesArmas = new JPanel( ); 
		botoesArmas.setBackground( verdeTropa );

		// criar os vários botões, que são na verdade checkboxes
		ButtonGroup grp = new ButtonGroup( );  // o grupo dos botões
		// o listener dos botões, sempre que um botão é selecionado,
		// a arma correspondente é escolhida
		ItemListener ouveArmas = new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					CheckBoxArma sel = (CheckBoxArma)e.getItemSelectable();
					selecionarArma(sel);			
				}
		};      
		for( int i=0; i < 10; i++) {
			CheckBoxArma cb = criaBotaoArma( botoesArmas, grp, "data/icons/missil", ouveArmas, false );
			controlosArmas[i] = cb;			
		}

		zonaBotoes.add( botoesArmas, BorderLayout.CENTER );

		// adicionar o botão de play e respetivo listener
		Icon icon = new ImageIcon("data/icons/play.gif");
		playBt = new JButton( icon );
		playBt.setDisabledIcon( icon );
		playBt.setBorderPainted( false );   			
		playBt.setFocusPainted(false);
		playBt.setContentAreaFilled(false);
		playBt.setFocusable( false );       
		playBt.addActionListener( new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				startNivel();
			}
		});
		zonaBotoes.add( playBt, BorderLayout.SOUTH );
		return zonaBotoes;
	}

	/**
	 * método auxiliar para criar um botão de arma
	 */
	private CheckBoxArma criaBotaoArma( JPanel panel, ButtonGroup grp, String iconFile, ItemListener il, boolean enabled ){
		CheckBoxArma bot = new CheckBoxArma(  );
		bot.setEnabled( enabled );
		bot.setHorizontalAlignment( SwingConstants.LEFT );
		if( grp != null )
			grp.add( bot );
		panel.add( bot );
		bot.addItemListener( il );
		return bot;
	}	
		
	/**
	 * programa principal
	 */
	public static void main(String[] args) {
		ESTacaoDefesa game = new ESTacaoDefesa();
		game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.iniciarJogo();
		game.setVisible( true );
	}		
}
