package view;

import java.*;

import javax.swing.JFrame;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jfree.ui.about.SystemPropertiesPanel;

import Bcrypt.BCrypt;

import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.ParseException;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.Point;
import javax.swing.JOptionPane;

import control.*;
import java.awt.Font;
import javax.swing.JPasswordField;
import javax.swing.JList;



/**
 * <p>
 * Création d'utilisateur
 * </p>
 * <ol>
 * <li>Fenetre de creation d'utilisateur</li>
 * </li>
 * </ol>
 * 
 * @author Jérôme Valenti et Julien Guilet
 * @version 3.1.0
 *
 */
public class CreateUser extends JFrame{
	private JTextField textNom;
	private JTextField textPrenom;
	private JPasswordField passwordField;
	private JComboBox comboBoxRole;
	private Controller control;
	
	public CreateUser(Controller conn) {
		setTitle("Cr\u00E9ation d'un compte");
		
		this.control = conn;
		getContentPane().setMinimumSize(new Dimension(300, 300));
		setMinimumSize(new Dimension(600, 400));
		setLocation(new Point(1000, 300));
		getContentPane().setLayout(null);
		setIconImage(Toolkit.getDefaultToolkit().getImage("img\\vinci_ico.jpg"));
		JLabel lblNewLabel = new JLabel("Cr\u00E9er un compte");
		lblNewLabel.setBounds(208, 41, 115, 14);
		getContentPane().add(lblNewLabel);
		
		textNom = new JTextField();
		textNom.setBounds(92, 111, 96, 20);
		getContentPane().add(textNom);
		textNom.setColumns(10);
		
		textPrenom = new JTextField();
		textPrenom.setBounds(92, 159, 96, 20);
		getContentPane().add(textPrenom);
		textPrenom.setColumns(10);
		
		JComboBox comboBoxRole = new JComboBox();
		this.comboBoxRole = comboBoxRole;
		this.comboBoxRole.setBounds(381, 158, 96, 22);
		getContentPane().add(this.comboBoxRole);
		
		JLabel lblNewLabel_1 = new JLabel("Nom");
		lblNewLabel_1.setBounds(10, 114, 72, 14);
		getContentPane().add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Prenom");
		lblNewLabel_2.setBounds(10, 162, 72, 14);
		getContentPane().add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("Mot de passe");
		lblNewLabel_3.setBounds(292, 114, 79, 14);
		getContentPane().add(lblNewLabel_3);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(381, 111, 96, 20);
		getContentPane().add(passwordField);
		
		JLabel lblNewLabel_4 = new JLabel("R\u00F4le");
		lblNewLabel_4.setBounds(292, 162, 47, 14);
		getContentPane().add(lblNewLabel_4);
		
		
		JButton buttonAjouter = new JButton("Ajouter");
		buttonAjouter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					ajoutUser();
				} catch (SQLException e1) {
					
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null,"Problème avec la Base de donnée");
				}
			}
		});
		buttonAjouter.setBounds(388, 285, 89, 23);
		getContentPane().add(buttonAjouter);
		
		JButton buttonFermer = new JButton("Fermer");
		buttonFermer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				control.fermeCreationUser();
			}
		});
		buttonFermer.setBounds(92, 285, 89, 23);
		getContentPane().add(buttonFermer);
		
		JLabel lblNewLabel_5 = new JLabel("A noter que le login d'authentification sera g\u00E9n\u00E9rer avec la premi\u00E8re lettre du prenom et le nom.");
		lblNewLabel_5.setBounds(38, 235, 629, 14);
		getContentPane().add(lblNewLabel_5);
		
		JLabel lblNewLabel_6 = new JLabel("Exemple : nom : Pierre, prenom : DUPONT alors le le login d'authentification sera pdupont.");
		lblNewLabel_6.setBounds(38, 260, 629, 14);
		getContentPane().add(lblNewLabel_6);
		
		this.comboBoxRole.addItem("membre");
		this.comboBoxRole.addItem("admin");
	}
	
	/**
	 * Cette fonction récupère toutes les informations du formulaire, hash le mot de passe envoie les informations au controlleur
	 * @throws SQLException
	 */
	public void ajoutUser() throws SQLException {
		try {
			String l= this.textPrenom.getText().substring(0,1);
			String identifiant = l + this.textNom.getText();
			String mdp = this.passwordField.getText();
			String nom = this.textNom.getText();
			String prenom = this.textPrenom.getText();
			int role = this.comboBoxRole.getSelectedIndex(); 
			String Role;
			if (role == 1) {
				Role ="admin";
			} else {
				Role ="membre";
			}
			String mdphash = BCrypt.hashpw(mdp, BCrypt.gensalt(10));                
			//System.out.println("identifiant: "+identifiant+" mdp: "+mdphash+" nom: "+nom+" prenom: "+prenom+" role: "+Role);
			this.control.creationUser(identifiant, nom,  prenom,  mdphash,  Role);
		} catch(Exception ex) {
			JOptionPane.showMessageDialog(null, "Ajout raté !");
		}
		
		
	}
	
	/**
	 * Lors de l'ajout d'un utilisateur affiche un message de réussite ou d'échec
	 * @param b correspond au résultat de l'ajout d'un utilisateur
	 */
	public void ajout(boolean b) {
		if (b == true) {
			JOptionPane.showMessageDialog(null, "Ajout réussi !");
		} else {
			JOptionPane.showMessageDialog(null, "Ajout raté !");
		}
	}
}