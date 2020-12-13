/**
 * @author Jérôme Valenti, GUILET Julien
 */
package view;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import control.Controller;
import javafx.scene.control.Slider;
import jdk.nashorn.internal.runtime.NumberToString;
import model.Mesure;
import base.Donnee;
import java.awt.List;
import java.awt.Choice;
import java.awt.Rectangle;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Point;
import javax.swing.border.EtchedBorder;
import javax.swing.SwingConstants;

/**
 * <p>ConsoleGUI : IHM de l'application de consultation des températures en fonction du stade selectionné</p>
 * <p>Projet Vinci Thermo Green</p>
 * @author Jérôme Valenti et Julien Guilet
 * @version 3.0.0
 * @see control.Controller
 * @see model.Mesure
 */
public class AppGUI extends JFrame {

	private Controller control;
	/**
	 * <p>Container intermédiaire JPanel</p>
	 * <p>Contient les critères de filtrage des données de la table</p>
	 * @see JPanel 
	 */
	JPanel pnlCriteria = new JPanel();

	/**
	 * <p>Bouton radio pour le choix de conversion</p>
	 */
	private static JRadioButton rdbtnCelsius = new JRadioButton("Celsius");
	JRadioButton rdbtnFahrenheit = new JRadioButton("Fahrenheit");

	/**
	 * <p>Liste de choix d'une zone</>
	 * @see JComboBox
	 */
	JComboBox<String> choixZone = new JComboBox<String>();
	
	/**
	 * <p>Saisie de la date de début de période</p>
	 * @see JTextField
	 */	
	private JTextField dateDebut;

	/**
	 * <p>Saisie de la date de fin de période</p>
	 * @see JTextField
	 */	
	private JTextField dateFin;
	
	private JButton btnFiltrer = new JButton("Filtrer");
	
	/**
	 * <p>Container intermédiaire JPanel</p>
	 * <p>Contient l'affichage graphique des données de la Table</p>
	 * @see JPanel 
	 */
	JPanel pnlParam = new JPanel();
	JPanel pnlGraph = new JPanel();

	/**
	 * <p>Affiche la température minimale sur la période</p>
	 * @see JTextField
	 */	
	private static JTextField tempMin;
	
	/**
	 * <p>Affiche la température moyenne sur la période</p>
	 * @see JTextField
	 */
	private static JTextField tempMoy;
	
	/**
	 * <p>Affiche la température maximale sur la période</p>
	 * @see JTextField
	 */
	private static JTextField tempMax;
	
	/**
	 * <p>Pour recevoir les données collectées</p>
	 * @see JTable 
	 */
	private static JTable laTable;
	
	/**
	 * <p>Un objet de la classe Mesure</p>
	 * @see model.Mesure
	 */
	private static Mesure uneMesure;
	
	/**
	 * <p>Une liste pour recevoir tous les stades présent dans la BDD</p>
	 */
	private static ArrayList<String> lesStades;
	
	/**
	 * <p>Pour recevoir les données collectées</p>
	 * @see ArrayList
	 * @see model.Mesure
	 */
	private static ArrayList<Mesure> lesMesures;
	
	private String nom;
	
	private String role;
	
	private ArrayList<Float> minMaxtemp;

	private static String nomStade = "";
	private JSlider slidermin;
	private JSlider slidermax;
	private JLabel labelslidemin;
	private JLabel labelslidemax;
	
	/**
	 * <p>Pour recevoir le JTable qui contient les mesures selectionnées</p>
	 */
	private static JScrollPane scrollPane = new JScrollPane();
	
	/**
	 * <p>Tableau d'objet pour alimenter la JTable</p>
	 */
	private static Object[][] data;

	/**
	 * <p>Container intermédiaire JPanel</p>
	 * <p>Contient les bornes des valeurs nominales</p>
	 * @see JPanel 
	 */
	JPanel pnlBounds = new JPanel();

	public AppGUI(Controller con, String nomm, String rolee) throws ParseException, SQLException {
		//Appelle le constructeur de la classe mère
		super();
		
		
		this.nom = nomm;
		this.role = rolee;
		this.control = con;
		this.lesStades = this.control.getStades();
		
		int nbalerte = control.verifAlerte();
		System.out.println("nb:"+nbalerte);
		if ( nbalerte!= 0) {
			JOptionPane.showMessageDialog(null,"Nombre d'alertes signalées : "+nbalerte);
		}
		
		//control = new Controller();
		setIconImage(Toolkit.getDefaultToolkit().getImage("img\\vinci_ico.jpg"));
		setTitle("Vinci Thermo Green");
		setSize(712, 658);
		setResizable(false);
		setFont(new Font("Consolas", Font.PLAIN, 12));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocation(new Point(1000, 300));
		//Pane pointe sur le container racine
		Container pane = getContentPane();
		//Fixe le Layout de la racine à Absolute
		getContentPane().setLayout(null);
		
		//Définit le JPanel des critères
		pnlCriteria.setBounds(10, 140, 325, 145);
		pnlCriteria.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Filtrage", TitledBorder.LEADING, TitledBorder.TOP, null, Color.GRAY));
		pnlCriteria.setBackground(UIManager.getColor("Label.background"));
		pnlCriteria.setLayout(null);
		pane.add(pnlCriteria);
		
		//Ajoute deux boutons radio au JPanel pnlCriteria
		rdbtnCelsius.setFont(new Font("Consolas", Font.PLAIN, 12));
		rdbtnCelsius.setBounds(15, 20, 100, 23);
		pnlCriteria.add(rdbtnCelsius);
		
		//Sélectionne la convertion celsius par défaut
		rdbtnCelsius.setSelected(true);
		
		rdbtnFahrenheit.setFont(new Font("Consolas", Font.PLAIN, 12));
		rdbtnFahrenheit.setBounds(115, 20, 100, 23);
		pnlCriteria.add(rdbtnFahrenheit);
		
	    //Groupe les boutons radio.
	    ButtonGroup group = new ButtonGroup();
	    group.add(rdbtnCelsius);
	    group.add(rdbtnFahrenheit);
		
		choixZone.setBounds(115, 50, 100, 20);
		pnlCriteria.add(choixZone);
		
		//un bouchon "Quick & Dirty" pour peupler la liste déroulante
		//TODO peupler la liste avec un équivalent à SELECT DISTINCT
		//TODO implémenter la classe métier Zone pour peupler une JComboBox<Zone>
		
		
		JLabel lblZone = new JLabel("Zone");
		lblZone.setFont(new Font("Consolas", Font.PLAIN, 12));
		lblZone.setBounds(15, 54, 99, 14);
		pnlCriteria.add(lblZone);
		
		JLabel lblDebut = new JLabel("D\u00E9but");
		lblDebut.setFont(new Font("Consolas", Font.PLAIN, 12));
		lblDebut.setBounds(15, 83, 46, 14);
		pnlCriteria.add(lblDebut);
		
		dateDebut = new JTextField();
		dateDebut.setBounds(115, 79, 100, 20);
		pnlCriteria.add(dateDebut);
		dateDebut.setColumns(10);
		
		JLabel lblFin = new JLabel("Fin");
		lblFin.setFont(new Font("Consolas", Font.PLAIN, 12));
		lblFin.setBounds(15, 114, 46, 14);
		pnlCriteria.add(lblFin);
		
		dateFin = new JTextField();
		dateFin.setColumns(10);
		dateFin.setBounds(115, 110, 100, 20);
		pnlCriteria.add(dateFin);
		
		btnFiltrer.setBounds(225, 109, 89, 23);
		pnlCriteria.add(btnFiltrer);
		btnFiltrer.addActionListener(new filtrerData());
		
		JLabel lblLogoVinci = new JLabel();
		lblLogoVinci.setIcon(new ImageIcon("img\\s_vinci.png"));
		lblLogoVinci.setBounds(221, 11, 95, 35);
		pnlCriteria.add(lblLogoVinci);

		//Définit le JScrollPane qui reçoit la JTable
		scrollPane.setBounds(10, 296, 325, 325);
		pane.add(scrollPane);
		
		//Définit le JPanel des paramètres du graphique
		pnlParam.setBounds(345, 140, 355, 350);
		pnlParam.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Graphique des temp\u00E9ratures", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(128, 128, 128)));
		pnlParam.setBackground(UIManager.getColor("Label.background"));
		pnlParam.setLayout(null);
		pane.add(pnlParam);
		
		JCheckBox chckbxDistinctZone = new JCheckBox("Distinguer les zones");
		chckbxDistinctZone.setFont(new Font("Consolas", Font.PLAIN, 12));
		chckbxDistinctZone.setBounds(15, 20, 165, 23);
		pnlParam.add(chckbxDistinctZone);
		
		JLabel lblTypeDeGraphique = new JLabel("Type de graphique");
		lblTypeDeGraphique.setFont(new Font("Consolas", Font.PLAIN, 12));
		lblTypeDeGraphique.setBounds(15, 50, 120, 14);
		pnlParam.add(lblTypeDeGraphique);
		
		JComboBox choixGraphique = new JComboBox();
		choixGraphique.setBounds(152, 47, 190, 20);
		pnlParam.add(choixGraphique);
		
		JButton btnActualiser = new JButton("Actualiser");
		btnActualiser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnActualiser.setBounds(222, 19, 120, 23);
		pnlParam.add(btnActualiser);
		
		JLabel lblMin = new JLabel("Min");
		lblMin.setFont(new Font("Consolas", Font.PLAIN, 12));
		lblMin.setBounds(15, 306, 30, 14);
		pnlParam.add(lblMin);
		
		tempMin = new JTextField();
		tempMin.setEditable(false);
		tempMin.setBounds(55, 302, 50, 20);
		pnlParam.add(tempMin);
		tempMin.setColumns(10);
		
		JLabel lblMoy = new JLabel("Moy");
		lblMoy.setFont(new Font("Consolas", Font.PLAIN, 12));
		lblMoy.setBounds(137, 304, 30, 14);
		pnlParam.add(lblMoy);
		
		tempMoy = new JTextField();
		tempMoy.setEditable(false);
		tempMoy.setColumns(10);
		tempMoy.setBounds(177, 300, 50, 20);
		pnlParam.add(tempMoy);
		
		JLabel lblMax = new JLabel("Max");
		lblMax.setFont(new Font("Consolas", Font.PLAIN, 12));
		lblMax.setBounds(252, 304, 30, 14);
		pnlParam.add(lblMax);
		
		tempMax = new JTextField();
		tempMax.setEditable(false);
		tempMax.setColumns(10);
		tempMax.setBounds(292, 300, 50, 20);
		pnlParam.add(tempMax);
		
		//Définit le JPanel qui recoit le graphique
		this.pnlGraph.setBorder(new TitledBorder(null, "Graphique", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		this.pnlGraph.setBackground(UIManager.getColor("Label.background"));
		this.pnlGraph.setBounds(15, 75, 330, 215);
		
		//pose le pnlGraph dans le pnlParam
		pnlParam.add(this.pnlGraph);
		this.pnlGraph.setLayout(null);
		
		//Définit le JPanel des bornes nominales
		pnlBounds.setBounds(345, 501, 355, 124);
		pnlBounds.setBorder(new TitledBorder(null, "D\u00E9bord des valeurs nominales", TitledBorder.LEADING, TitledBorder.TOP, null, Color.GRAY));
		pnlBounds.setBackground(UIManager.getColor("Label.background"));
		pnlBounds.setLayout(null);
		pane.add(pnlBounds);
		
		String a ="admin";
		JButton btnDebord = new JButton("Changer");
		
		btnDebord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				enregistrementTemp();
			}
		});
		btnDebord.setBounds(258, 15, 87, 23);
		pnlBounds.add(btnDebord);
		
		JSlider sliderMin = new JSlider();
		
		
		slidermin = sliderMin;
		slidermin.setBounds(16, 40, 240, 25);
		slidermin.isFocusable();
		pnlBounds.add(slidermin);
		
		slidermin.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				changeLabelSliderMin();
				
			}
		});
		
		JSlider sliderMax = new JSlider();
		slidermax = sliderMax;
		slidermax.setBounds(15, 88, 240, 25);
		if (a.equals(role)) {
			sliderMin.setEnabled(true);
			sliderMax.setEnabled(true);
			btnDebord.setEnabled(true);
		}else {
			sliderMin.setEnabled(false);
			sliderMax.setEnabled(false);
			btnDebord.setEnabled(false);
		}
		pnlBounds.add(slidermax);
		slidermax.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				changeLabelSliderMax();
				
			}
		});
		
		JLabel lblDebordMin = new JLabel("Minimum");
		lblDebordMin.setBounds(15, 20, 60, 14);
		pnlBounds.add(lblDebordMin);
		
		JLabel lblDebordMaximum = new JLabel("Maximum");
		lblDebordMaximum.setBounds(15, 70, 60, 14);
		pnlBounds.add(lblDebordMaximum);
		
		JLabel lbAlerte = new JLabel();
		lbAlerte.setIcon(new ImageIcon("img\\s_green_button.png"));
		lbAlerte.setBounds(270, 42, 75, 75);
		pnlBounds.add(lbAlerte);
		
		JLabel labelSlideMin = new JLabel("-");
		labelslidemin = labelSlideMin;
		labelslidemin.setBounds(106, 24, 47, 14);
		pnlBounds.add(labelslidemin);
		
		JLabel labelSlideMax = new JLabel("-");
		labelslidemax = labelSlideMax;
		labelslidemax.setBounds(106, 76, 47, 14);
		pnlBounds.add(labelslidemax);
		
		//Defini le JPanel du choix du stade
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Choix du stade", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 66, 686, 63);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		//Rentrons comme valeur la liste de tous les stades
		Choice choiceStade = new Choice();
		choiceStade.setBounds(10, 23, 536, 30);
		panel.add(choiceStade);
		choiceStade.setPreferredSize(new Dimension(200, 50));
		for (int i = 0; i < lesStades.size(); i++) {
			choiceStade.add(lesStades.get(i));
		}
		
		//JButton permettant de selectionner les températures en fonction du nom de stade selectionné
		JButton ValiderStade = new JButton("Valider");
		ValiderStade.addActionListener(new ActionListener() {
			

			public void actionPerformed(ActionEvent arg0) {
				String nomStade = choiceStade.getSelectedItem();
				
				try {
					AppGUI.setnomStade(nomStade);
					minMaxtemp = control.getMinMaxTemp(nomStade);
					String min = Float.toString(minMaxtemp.get(0));
					labelSlideMin.setText(min);
					String max = Float.toString(minMaxtemp.get(1));
					labelSlideMax.setText(max);
					//Recupère les mesures du stade sélectionné
					lesMesures = control.getMesuresStade(nomStade);
					
					//Construit le tableau d'objet
					laTable = setTable(lesMesures);
					
					//Definit le JScrollPane qui va recevoir la JTable
					scrollPane.setViewportView(laTable);
					
					//Mettre le graphique
					setChart();
					
					//Insère dans le champ "zone" de l'application le nombre de zone présente dans le stade selectionné
					int NbZone = control.getNbZone(nomStade);
					choixZone.removeAllItems();
					choixZone.addItem("*");
					for (int i = 0; i < NbZone; i++) {
						choixZone.addItem(""+(i+1));
					}
					
				} catch (SQLException e) {
					e.printStackTrace();
					
				}
			}
		});
		ValiderStade.setBounds(552, 23, 124, 23);
		panel.add(ValiderStade);
		
		JPanel panelCompte = new JPanel();
		panelCompte.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Compte " + role, TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelCompte.setBounds(10, 11, 675, 54);
		getContentPane().add(panelCompte);
		panelCompte.setLayout(null);
		
		
		JLabel labelCompte = new JLabel("Vous \u00EAtes connect\u00E9 en tant que : " + this.nom);
		labelCompte.setBounds(10, 22, 375, 22);
		panelCompte.add(labelCompte);
		
		
		JButton btnNewButton = new JButton("D\u00E9connexion");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deconnection();
			}
		});
		btnNewButton.setBounds(549, 22, 116, 23);
		panelCompte.add(btnNewButton);
		
		
		
		if (a.equals(role)) {
			JButton btnNewButton_1 = new JButton("Cr\u00E9er un compte");
			btnNewButton_1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					control.afficheCreateUser();
				}
			});
			btnNewButton_1.setBounds(395, 22, 144, 23);
			panelCompte.add(btnNewButton_1);
		}
	}
	
	
	/**
	 * Permet d'enregistrer les nouvelles valeur min et max du stade selectionné
	 */
	public void enregistrementTemp() {
		int min = slidermin.getValue();
		int max = slidermax.getValue();
		if (control.enregistreTemp(nomStade, min, max) == true) {
			JOptionPane.showMessageDialog(null,"Changement réussi !");
		} else {
			JOptionPane.showMessageDialog(null,"Changement échoué !");
		}
	}
	
	/**
	 * Permet d'actualiser le label au dessus du slider min 
	 */
	public void changeLabelSliderMin() {
		String slidemine = Integer.toString(slidermin.getValue());
		labelslidemin.setText(slidemine);
		
	}
	
	/**
	 * Permet d'actualiser le label au dessus du slider max 
	 */
	public void changeLabelSliderMax() {
		String slidemax = Integer.toString(slidermax.getValue());
		labelslidemax.setText(slidemax);
	}
	
	public void test(Controller control) throws SQLException {
		
		
		//Recupère tous les noms des stades
		lesStades = control.getStades();
				
		//Construit et affiche l'IHM
				
		this.setLocation(100,100);
				
		//Demande l'acquisition des data
		lesMesures = control.getLesMesures();
		laTable = setTable(lesMesures);
		
		scrollPane.setViewportView(laTable);
		System.out.println("Before set chart in main()");
		//affiche le graphique
		this.setChart();
		System.out.println("After set chart in main()");
		this.setVisible(true);
		
	}
	
	/**
	 * <p>Transfert les données de la collection vers un tableau d'objets</p>
	 * <p>La température est en degré Fahrenheit</p>
	 * 
	 * @param ArrayList<Mesure>
	 * @return Object[][]
	 */
	private static JTable setTable(ArrayList<Mesure> mesures) {
		
		float min = 0;
		float max = 0;
		float moy = 0;
		DecimalFormat round = new DecimalFormat("0.##");
		Object[][] dataTable = new Object[mesures.size()][3];

		if (rdbtnCelsius.isSelected()) {

			System.out.println("Celsius : " + rdbtnCelsius.isSelected() + " | " + mesures.size());

			// Initialisation de min et max
			min = mesures.get(0).getCelsius();
			max = mesures.get(0).getCelsius();

			for (int i = 0; i < mesures.size(); i++) {

				uneMesure = lesMesures.get(i);
				dataTable[i][0] = uneMesure.getNumZone();
				dataTable[i][1] = uneMesure.getHoroDate();
				dataTable[i][2] = round.format(uneMesure.getCelsius());

				// Min, max et moy
				moy = moy + uneMesure.getCelsius();

				if (uneMesure.getCelsius() < min) {
					min = uneMesure.getCelsius();
				}
				if (uneMesure.getCelsius() > max) {
					max = uneMesure.getCelsius();
				}
			}
		} else {

			System.out.println("Celsius : " + rdbtnCelsius.isSelected() + " | " + mesures.size());

			// Initialisation de min et max
			min = mesures.get(0).getFahrenheit();
			max = mesures.get(0).getFahrenheit();

			for (int i = 0; i < mesures.size(); i++) {
				uneMesure = lesMesures.get(i);
				dataTable[i][0] = uneMesure.getNumZone();
				dataTable[i][1] = uneMesure.getHoroDate();
				dataTable[i][2] = round.format(uneMesure.getFahrenheit());

				// Min, max et moy
				moy = moy + uneMesure.getFahrenheit();

				if (uneMesure.getFahrenheit() < min) {
					min = uneMesure.getFahrenheit();
				}
				if (uneMesure.getCelsius() > max) {
					max = uneMesure.getFahrenheit();
				}
			}
		}

		String[] titreColonnes = { "Zone", "Date-heure", "T°" };
		JTable uneTable = new JTable(dataTable, titreColonnes);
		// Les données de la JTable ne sont pas modifiables
		uneTable.setEnabled(false);

		// Arroundi et affecte les zones texte min, max et moy
		tempMin.setText(round.format(min));
		tempMax.setText(round.format(max));
		moy = moy / mesures.size();
		tempMoy.setText(round.format(moy));

		return uneTable;
	}

	/**
	 * demande une deconnection au controleur
	 * @since 3.1.0
	 */
	public void deconnection() {
		control.demandeDeconnexion();
	}
	
	//TODO factoriser le code avec setTable
	//TODO gérer le choix du graphique
	/**
	 * <p>Impl&eacute;mente la biblioth&egrave;que JFreeChart :</p>
	 * <ol>
	 * <li>d&eacute;finit le type de container de donn&eacute;es -&gt; DefaultCategoryDataset</li>
	 * <li>alimente le container des donn&eacute;es</li>
	 * <li>Fabrique un graphique lin&eacute;aire -&gt; ChartFactory.createLineChart</li>
	 * <li>Englobe le graphique dans un panel sp&eacute;cifique -&gt; new ChartPanel(chart)</li>
	 * <li>Englobe ce panel dans un JPanle de l'IHM -&gt; pnlGraph.add(chartPanel)<br /></li>
	 * </ol>
	 * @author Jérôme Valenti
	 * @see JFreeChart
	 */
	public void setChart (){
		
		int i1 = 0,i2 = 0,i3 = 0,i4 = 0;
		DefaultCategoryDataset dataChart = new DefaultCategoryDataset();
		
		// Set data ((Number)temp,zone,dateHeure)
		for (int i = 0; i < lesMesures.size(); i++) {

			uneMesure = lesMesures.get(i);

			switch (uneMesure.getNumZone()) {
			case "1":
				dataChart.addValue((Number)uneMesure.getCelsius(),uneMesure.getNumZone(),i1);
				i1++;
				break;
			case "2":
				dataChart.addValue((Number)uneMesure.getCelsius(),uneMesure.getNumZone(),i2);
				i2++;
				break;
			case "3":
				dataChart.addValue((Number)uneMesure.getCelsius(),uneMesure.getNumZone(),i3);
				i3++;
				break;
			case "4":
				dataChart.addValue((Number)uneMesure.getCelsius(),uneMesure.getNumZone(),i4);
				i4++;
				break;
			default:
				break;
			}
		}

		//un bouchon pour tester
		// Set data ((Number)temp,zone,dateHeure)
//        dataChart.addValue((Number)1.0, "1", 1);
//        dataChart.addValue((Number)5.0, "2", 1);
//        dataChart.addValue((Number)4.0, "1", 2);
//        dataChart.addValue((Number)7.0, "2", 2);
//        dataChart.addValue((Number)3.0, "1", 3);
//        dataChart.addValue((Number)6.0, "2", 3);
//        dataChart.addValue((Number)5.0, "1", 4);
//        dataChart.addValue((Number)8.0, "2", 4);
//        dataChart.addValue((Number)5.0, "1", 5);
//        dataChart.addValue((Number)4.0, "2", 5);
//        dataChart.addValue((Number)7.0, "1", 6);
//        dataChart.addValue((Number)4.0, "2", 6);
//        dataChart.addValue((Number)7.0, "1", 7);
//        dataChart.addValue((Number)2.0, "2", 7);
//        dataChart.addValue((Number)8.0, "1", 8);
//        dataChart.addValue((Number)1.0, "2", 8);
//		System.out.println(dataChart.getRowCount() + " lignes " + dataChart.getColumnCount() + " colonnes");
		
		JFreeChart chart = ChartFactory.createLineChart(
                null,       				// chart title
                "Heure",					// domain axis label
                "Températures",				// range axis label
                dataChart,					// data
                PlotOrientation.VERTICAL,	// orientation
                true,						// include legend
                true,						// tooltips
                false						// urls
            );
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBounds(5, 20, 320, 190);
        chartPanel.setVisible(true);
        this.pnlGraph.add(chartPanel);
		System.out.println("chartPanel added to pnlGraph");
	}
	
	
	
	
	/**
	 * <p>Classe interne qui gère le clique sur le bouton filtrer
	 * @author Jérôme Valenti
	 */
	class filtrerData implements ActionListener {
        public void actionPerformed(ActionEvent e){

        	lesMesures = control.filtrerLesMesure(choixZone.getSelectedItem().toString());
        	System.out.println("Filtrer Celsius : " + rdbtnCelsius.isSelected() + 
        				" Fahrenheit : " + rdbtnFahrenheit.isSelected() + 
        				" choix : " + choixZone.getSelectedItem() + 
        				" début : " + dateDebut.getText() );
        	displayLesMesures(lesMesures);
        	
        	//Construit le tableau d'objet
    		laTable = setTable(lesMesures);

    		//Definit le JScrollPane qui va recevoir la JTable
    		scrollPane.setViewportView(laTable);
    		
    		System.out.println("Before setChart in filtrerData()");
    		//affiche le graphique
    		setChart();
    		System.out.println("After setChart in filtrerData()");
        }
	}
	
	private void displayLesMesures(ArrayList<Mesure> uneCollection){

		for (int i = 0; i < uneCollection.size(); i++) {
        	System.out.println(i + " " + uneCollection.get(i).getNumZone() + " | " 
        					+ uneCollection.get(i).getHoroDate() + " | " 
        					+ uneCollection.get(i).getCelsius() );
		} 
	}
	
	public static void setnomStade(String nomStadee) {
		nomStade = nomStadee;
	}
}