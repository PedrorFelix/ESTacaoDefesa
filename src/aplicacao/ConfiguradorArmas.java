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

/** Classe respons�vel por apresentar a configura��o para cada uma das armas.
 */
public class ConfiguradorArmas implements VisitanteArmas{

	/** apresenta a janela de configura��o para a arma
	 * @param a arma para a qual é preciso apresentar a informa��o
	 */

	
	public void visitaCanhao(Canhao c) {
		String resumo = "<html><h3>Canh�o</h3>Tempo de disparo: " + c.getDelay()
        				+ "<br>Dano: " + c.getDano() + "<br>N�o tem configura��es!</html>";
		JOptionPane.showMessageDialog(null, resumo, "Config. Canh�o", JOptionPane.PLAIN_MESSAGE );
	}
	
	public void visitaMetralha(Metralha m) {
		String resumo = "<html><h3>Metralha</h3>Tempo de disparo: " + m.getDelay()
        			     + "<br>Dano: " + m.getDano()
        			     + "<br>Cobertura: " + m.getDistanciaMax() + "<br>N�o tem configura��es!</html>";
		JOptionPane.showMessageDialog(null, resumo, "Config. Metralha", JOptionPane.PLAIN_MESSAGE );
	}
	
	public void visitaLancaDrones(LancaDrones l) {
		// prepara o resumo
		String resumo = "<html><h3>Drone</h3>Tempo lan�amento: " + l.getDelay()
        		        + "<br>Dano: " + l.getDano()
                        + "<br>Max. Drones: " + l.getMaxDronesAtivos() + "</html>";

		// prepara a janela onde apresentar os dados e as op��es
		JPanel painelGlobal = new JPanel( new GridLayout(0,1) );
		painelGlobal.add( new JLabel( resumo, SwingConstants.CENTER ) );

		// op��es de drones e de ataques
		String []tiposDrone = { "Sentinela", "Zona", "Perseguidor" };
		String []tiposAtaque = { "Mais Forte", "Mais Fraco", "Mais r�pido" };
		JPanel painelOpcoes = new JPanel( new GridLayout(0, 1) );
		painelOpcoes.add( new JLabel( "Drone" ) );
		JComboBox<String> tipoDrone = new JComboBox<String>( tiposDrone );
		painelOpcoes.add( tipoDrone );
		painelOpcoes.add( new JLabel( "Sele��o Alvo" ) );
		JComboBox<String> tipoAtaque = new JComboBox<String>( tiposAtaque );
		painelOpcoes.add( tipoAtaque );
		painelGlobal.add( painelOpcoes );
		
		// apresenta a janela de configura��o
		JOptionPane.showMessageDialog(null, painelGlobal, "Config. Drone", JOptionPane.PLAIN_MESSAGE );

		// ver qual o tipo de sele��o a usar
		Comparator<Inimigo> tipoSel; 
		switch( tipoAtaque.getSelectedIndex() ) {
		default:
		case 0: tipoSel = new ComparatorMaisForte(); break;
		case 1: tipoSel = new ComparatorMaisFraco(); break;
		case 2: tipoSel = new ComparatorMaisVeloz(); break;
		}
		
		// ver qual o tipo de drone a lan�ar
		
		switch( tipoDrone.getSelectedIndex() ) {
		default:
		case 0: l.mudaTipoSentinela(tipoSel); break;
		case 1: l.mudaTipoZona(tipoSel); break;
		case 2: l.mudaTipoPerseguidor(tipoSel); break;
		}
	}
	
	public void visitaLaser(Laser l) {
		// prepara o resumo
		String resumo = "<html><h3>Laser</h3>Fator Aquec: " + l.getFatorAquecimento() + "<br>Fator Arref: " + l.getFatorArrefecimento() + "</html>";

		// prepara a janela onde apresentar os dados e o seletor de pot�ncia
		JPanel painelGlobal = new JPanel( new GridLayout(0,1) );
		painelGlobal.add( new JLabel( resumo, SwingConstants.CENTER ) );
		
		// cria o slider para escoher a pot�ncia
		JSlider slider = new JSlider( JSlider.HORIZONTAL, 1, 10, (int)(l.getDano()*10) );
		JPanel painelOpcoes = new JPanel( new GridLayout(0, 1) );
		painelOpcoes.add( new JLabel( "Pot�ncia" ) );
		painelOpcoes.add( slider );
		painelGlobal.add( painelOpcoes );
		
		// apresenta a janela de configura��o
		JOptionPane.showMessageDialog(null, painelGlobal, "Config. Laser", JOptionPane.PLAIN_MESSAGE );

		// processa a escolha do jogador
		l.setDano( slider.getValue()/10.0f );
	}
	
	public void visitaMissil(Missil m) {
		// prepara o resumo
		String resumo = "<html><h3>M�ssil</h3>Tempo de disparo: " + m.getDelay()
		                + "<br>Dano: " + m.getDano() + "<br>V. Varrimento: " + m.getVelocidade()  
		                + "<br>N�o tem configura��es!</html>";
                        
		// prepara a janela onde apresentar os dados e as op��es
		JPanel painelGlobal = new JPanel( new GridLayout(0,1) );
		painelGlobal.add( new JLabel( resumo, SwingConstants.CENTER ) );

		// op��es de ataques
		String []tiposAtaque = { "Mais Forte", "Mais Fraco", "Mais r�pido" };
		JPanel painelOpcoes = new JPanel( new GridLayout(0, 1) );
		painelOpcoes.add( new JLabel( "Sele��o Alvo" ) );
		JComboBox<String> tipoSelector = new JComboBox<String>( tiposAtaque );
		painelOpcoes.add( tipoSelector );
		painelGlobal.add( painelOpcoes );
		
		
		// apresenta a janela de configura��o
		JOptionPane.showMessageDialog(null, painelGlobal, "Config. M�ssil", JOptionPane.PLAIN_MESSAGE );

		// ver qual o tipo de sele��o a usar
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
