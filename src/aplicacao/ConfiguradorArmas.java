package aplicacao;

import java.awt.GridLayout;
import java.util.Comparator;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

import arma.*;
import inimigo.*;

/** Classe responsável por apresentar a configuração para cada uma das armas.
 */
public class ConfiguradorArmas {

	/** apresenta a janela de configuração para a arma
	 * @param a arma para a qual é preciso apresentar a informação
	 */
	public void configuraArma( Arma a ) {
		// TODO INSTANCEOF estes instanceof têm de desaparecer
		if( a instanceof Canhao )
			configuraCanhao( (Canhao) a );
		else if( a instanceof Metralha )
			configuraMetralha( (Metralha)a );
		else if( a instanceof LancaDrones )
			configuraLancaDrone((LancaDrones)a );
		else if( a instanceof Laser )
			configuraLaser( (Laser)a );
		else if( a instanceof Missil )
			configuraMissil( (Missil)a );
	}
	
	public void configuraCanhao(Canhao c) {
		String resumo = "<html><h3>Canhão</h3>Tempo de disparo: " + c.getDelay()
        				+ "<br>Dano: " + c.getDano() + "<br>Não tem configurações!</html>";
		JOptionPane.showMessageDialog(null, resumo, "Config. Canhão", JOptionPane.PLAIN_MESSAGE );
	}
	
	public void configuraMetralha(Metralha m) {
		String resumo = "<html><h3>Metralha</h3>Tempo de disparo: " + m.getDelay()
        			     + "<br>Dano: " + m.getDano()
        			     + "<br>Cobertura: " + m.getDistanciaMax() + "<br>Não tem configurações!</html>";
		JOptionPane.showMessageDialog(null, resumo, "Config. Metralha", JOptionPane.PLAIN_MESSAGE );
	}
	
	public void configuraLancaDrone(LancaDrones l) {
		// prepara o resumo
		String resumo = "<html><h3>Drone</h3>Tempo lançamento: " + l.getDelay()
        		        + "<br>Dano: " + l.getDano()
                        + "<br>Máx. Drones: " + l.getMaxDronesAtivos() + "</html>";

		// prepara a janela onde apresentar os dados e as opções
		JPanel painelGlobal = new JPanel( new GridLayout(0,1) );
		painelGlobal.add( new JLabel( resumo, SwingConstants.CENTER ) );

		// opções de drones e de ataques
		String []tiposDrone = { "Sentinela", "Zona", "Perseguidor" };
		String []tiposAtaque = { "Mais Forte", "Mais Fraco", "Mais rápido" };
		JPanel painelOpcoes = new JPanel( new GridLayout(0, 1) );
		painelOpcoes.add( new JLabel( "Drone" ) );
		JComboBox<String> tipoDrone = new JComboBox<String>( tiposDrone );
		painelOpcoes.add( tipoDrone );
		painelOpcoes.add( new JLabel( "Seleção Alvo" ) );
		JComboBox<String> tipoAtaque = new JComboBox<String>( tiposAtaque );
		painelOpcoes.add( tipoAtaque );
		painelGlobal.add( painelOpcoes );
		
		// apresenta a janela de configuração
		JOptionPane.showMessageDialog(null, painelGlobal, "Config. Drone", JOptionPane.PLAIN_MESSAGE );

		// ver qual o tipo de seleção a usar
		Comparator<Inimigo> tipoSel; 
		switch( tipoAtaque.getSelectedIndex() ) {
		default:
		case 0: tipoSel = new ComparatorMaisForte(); break;
		case 1: tipoSel = new ComparatorMaisFraco(); break;
		case 2: tipoSel = new ComparatorMaisVeloz(); break;
		}
		
		// ver qual o tipo de drone a lançar
		int tipo;
		switch( tipoDrone.getSelectedIndex() ) {
		default:
		case 0: tipo = FabricaVeiculos.DRONE_SENTINELA; break;
		case 1: tipo = FabricaVeiculos.DRONE_ZONA; break;
		case 2: tipo = FabricaVeiculos.DRONE_PERSEGUIDOR; break;
		}
		l.setTipoDroneLancar( tipo, tipoSel );
	}
	
	public void configuraLaser(Laser l) {
		// prepara o resumo
		String resumo = "<html><h3>Laser</h3>Fator Aquec: " + l.getFatorAquecimento() + "<br>Fator Arref: " + l.getFatorArrefecimento() + "</html>";

		// prepara a janela onde apresentar os dados e o seletor de potência
		JPanel painelGlobal = new JPanel( new GridLayout(0,1) );
		painelGlobal.add( new JLabel( resumo, SwingConstants.CENTER ) );
		
		// cria o slider para escoher a potência
		JSlider slider = new JSlider( JSlider.HORIZONTAL, 1, 10, (int)(l.getDano()*10) );
		JPanel painelOpcoes = new JPanel( new GridLayout(0, 1) );
		painelOpcoes.add( new JLabel( "Potência" ) );
		painelOpcoes.add( slider );
		painelGlobal.add( painelOpcoes );
		
		// apresenta a janela de configuração
		JOptionPane.showMessageDialog(null, painelGlobal, "Config. Laser", JOptionPane.PLAIN_MESSAGE );

		// processa a escolha do jogador
		l.setDano( slider.getValue()/10.0f );
	}
	
	public void configuraMissil(Missil m) {
		// prepara o resumo
		String resumo = "<html><h3>Míssil</h3>Tempo de disparo: " + m.getDelay()
		                + "<br>Dano: " + m.getDano() + "<br>V. Varrimento: " + m.getVelocidade()  
		                + "<br>Não tem configurações!</html>";
                        
		// prepara a janela onde apresentar os dados e as opções
		JPanel painelGlobal = new JPanel( new GridLayout(0,1) );
		painelGlobal.add( new JLabel( resumo, SwingConstants.CENTER ) );

		// opções de ataques
		String []tiposAtaque = { "Mais Forte", "Mais Fraco", "Mais rápido" };
		JPanel painelOpcoes = new JPanel( new GridLayout(0, 1) );
		painelOpcoes.add( new JLabel( "Seleção Alvo" ) );
		JComboBox<String> tipoSelector = new JComboBox<String>( tiposAtaque );
		painelOpcoes.add( tipoSelector );
		painelGlobal.add( painelOpcoes );
		
		
		// apresenta a janela de configuração
		JOptionPane.showMessageDialog(null, painelGlobal, "Config. Míssil", JOptionPane.PLAIN_MESSAGE );

		// ver qual o tipo de seleção a usar
		Comparator<Inimigo> tipoSel; 
		switch( tipoSelector.getSelectedIndex() ) {
		default:
		case 0: tipoSel = new ComparatorMaisForte(); break;
		case 1: tipoSel = new ComparatorMaisFraco(); break;
		case 2: tipoSel = new ComparatorMaisVeloz(); break;
		}
		m.setSeletorInimigo( tipoSel );
	}
}
