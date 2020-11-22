package view;

import java.*;

import javax.swing.JFrame;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

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

/**
 * <p>
 * Le login :
 * </p>
 * <ol>
 * <li>Fenetre de connexion</li>
 * </li>
 * </ol>
 * 
 * @author Jérôme Valenti et Julien Guilet
 * @version 3.1.0
 *
 */
public class Login extends JFrame{
	private JTextField textField;
	private JTextField txtLogin;
	private Controller control;
	private JPasswordField txtMotDePasse;
	public Login(Controller control) {
		this.control = control;
		getContentPane().setMinimumSize(new Dimension(300, 300));
		setMinimumSize(new Dimension(500, 500));
		setLocation(new Point(1000, 300));
		setTitle("Page de connexion");
		getContentPane().setLayout(null);
		setIconImage(Toolkit.getDefaultToolkit().getImage("img\\vinci_ico.jpg"));
		JButton ButtonValider = new JButton("Valider");
		ButtonValider.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					demandeLogin();
				} catch (SQLException | ParseException e) {
					e.printStackTrace();
				}
			}
		});
		ButtonValider.setBounds(296, 251, 89, 23);
		getContentPane().add(ButtonValider);
		
		JButton btnNewButton = new JButton("Fermer");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		btnNewButton.setBounds(184, 251, 89, 23);
		getContentPane().add(btnNewButton);
		
		txtLogin = new JTextField();
		txtLogin.setBounds(184, 141, 201, 17);
		getContentPane().add(txtLogin);
		txtLogin.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Connexion");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 25));
		lblNewLabel.setBounds(184, 53, 178, 54);
		getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Identifiant");
		lblNewLabel_1.setBounds(80, 141, 107, 14);
		getContentPane().add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Mot de passe");
		lblNewLabel_2.setBounds(80, 195, 107, 14);
		getContentPane().add(lblNewLabel_2);
		
		txtMotDePasse = new JPasswordField();
		txtMotDePasse.setBounds(184, 192, 201, 20);
		getContentPane().add(txtMotDePasse);
		
		
	}
	
	/**
	 * Permet de récupérer les informations de la classe et utilise la fonction demandeConn du controleur
	 * @throws SQLException
	 * @throws ParseException
	 * @since 3.1.0
	 */
	public void demandeLogin() throws SQLException, ParseException {
		String login;
		String mdp;
		login = this.txtLogin.getText();
		mdp = this.txtMotDePasse.getText();
		
		if (control.demandeConn(login, mdp) == true) {
			JOptionPane.showMessageDialog(null,"Connexion réussi");
			control.connectionReussi(login);
		} else {
			JOptionPane.showMessageDialog(null,"Connexion échoué");
		}
		
	}
}
