package base;

import java.sql.Connection;
import model.*;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JOptionPane;

import Bcrypt.BCrypt;

/**
 * <p>
 * Donnée :
 * </p>
 *
 * <p>
 * Toutes les données de l'application sont stockées dans une BDD en
 * mySQL.<br />
 * Cette classe permet d'effectuer la connexion à cette BDD et a exécuté les
 * différentes requêtes vers cette BDD<br />
 * 
 * </p>
 * 
 * @author jguilet
 * @version 3.0.0
 * @since 3.0.0
 *
 */

public class Donnee {

	Connection connect;
	Statement statement;

	/**
	 * Dans ce constructeur, nous établisons la connection entre la BDD et
	 * l'application
	 */
	public Donnee() {
		// Connection à la base de donnée
		try {
			String url = "jdbc:mysql://localhost/vincithermogreen" + "?useUnicode=true"
					+ "&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&" + "serverTimezone=UTC";
			String utilisateur = "vinciThermo";
			String motDePasse = "vinciThermo";

			Class.forName("com.mysql.cj.jdbc.Driver");
			connect = DriverManager.getConnection(url, utilisateur, motDePasse);
			statement = connect.createStatement();
			setConnect(connect);
			System.out.println("Connection établie");

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null,"Problème avec la Base de donnée");
		}
	}

	/**
	 * Cette fonction exécute une requête de selection afin de récupérer les
	 * différents nom des stades qui sont contenu dans un ArrayList
	 * 
	 * @return tous les noms de stades présent dans la table stade dans base de
	 *         donnée
	 * @throws SQLException
	 * @since 3.0.0
	 */
	public ArrayList<String> SelectAllStadiums() throws SQLException {

		ArrayList<String> stades = new ArrayList<>();

		ResultSet resultset = null;
		resultset = statement.executeQuery("select * from stade;");

		while (resultset.next()) {
			stades.add(resultset.getString("nomStade"));

		}
		return stades;
	}

	/**
	 * Cette fonction exécute une requête de selection permettant de récupérer dans
	 * la base de données tous les mesures de températures en fonction d'un nom de
	 * stade
	 * 
	 * @return un Arraylist contenant toutes les mesures du stade mis en paramètre
	 * @param le nom du stade
	 * @throws SQLException
	 * @since 3.0.0
	 */
	public ArrayList<Mesure> SelectMesuresStadium(String nomStade) throws SQLException {

		ArrayList<Mesure> mesure = new ArrayList<>();

		ResultSet resultset = null;
		resultset = statement.executeQuery("select * from mesure where nomStade = '" + nomStade + "';");

		while (resultset.next()) {

			String zone = resultset.getString("zone");
			float temperature = resultset.getFloat("temperature");

			Date horoDate = new Date();

			horoDate = resultset.getDate("horoDate");

			// System.out.println(" Zone : "+zone+" temp : "+temperature+" horodate :
			// "+horoDate);

			Mesure m = new Mesure(zone, horoDate, temperature);

			mesure.add(m);

		}

		return mesure;

	}

	/**
	 * Cette fonction retourne le nombre de zone présent dans un stade
	 * 
	 * @param nom du stade
	 * @return integer
	 * @throws SQLException
	 * @since 3.0.0
	 */
	public int NbZoneStade(String stade) throws SQLException {
		ResultSet resultset = null;
		resultset = statement.executeQuery("select max(zone) from mesure where nomStade = '" + stade + "';");
		resultset.next();
		return resultset.getInt("max(zone)");

	}

	/**
	 * Cette fonction récupère les infos de la base de données et les compare avec ceux mis en paramètre
	 * @param login 
	 * @param mdp
	 * @return un boolean en fonction des informations mis en paramètre et en base de données
	 * @throws SQLException
	 * @since 3.1.0
	 */
	public boolean demandeConnextion(String login, String mdp) throws SQLException {
		ResultSet resultset = null;
		resultset = statement.executeQuery(
				"select login, password from user where login = '" + login + "';");
		resultset.next();
		try {
			String mdp2 = resultset.getString("password");
			if (BCrypt.checkpw(mdp, mdp2) == true) {
				return true;
			} else {
				return false;
			}
			
		} catch (java.sql.SQLException e) {
			return false;
		}

	}
	
	/**
	 * Permet de récupèrer le nom d'utilisateur en fonction du login
	 * @param login
	 * @return le nom d'utilisateur
	 * @throws SQLException
	 * @since 3.1.0
	 */
	public String getNomUtilisateur(String login) throws SQLException {

		ResultSet resultset = null;
		resultset = statement.executeQuery("select nom, prenom from user where login = '" + login + "';");
		resultset.next();
		return resultset.getString("nom") + ' ' + resultset.getString("prenom");
	}
	
	
	/**
	 * permet de récupèrer le rôle de l'utilisateur en fonction du login
	 * @param login
	 * @return le rôle de l'utilisateur
	 * @throws SQLException
	 * @since 3.1.0
	 */
	public String getRoleUtilisateur(String login) throws SQLException {

		ResultSet resultset = null;
		resultset = statement.executeQuery("select type from user where login = '" + login + "';");
		resultset.next();
		return resultset.getString("type");
	}
	
	
	/**
	 * ajoute l'utilisateur en base données avec les informations mis en paramètres
	 * @param identifiant
	 * @param nom
	 * @param prenom
	 * @param mdp
	 * @param role
	 * @return true si l'insertion c'est bien executé et false si une erreur survient
	 * @throws SQLException
	 * @since 3.1.0
	 */
	public boolean ajoutUser(String identifiant, String nom, String prenom, String mdp, String role)
			throws SQLException {
		
		try {

			boolean t = statement.execute("insert into  user values('"+identifiant+"','"+mdp+"','"+nom+"','"+prenom+"','"+role+"');");
			return true;
			
		} catch (SQLException e) {
			System.out.println(e);
			return false;
		}

	}

	// Les seteurs et geteurs
	public Connection getConnect() {
		return connect;
	}

	public void setConnect(Connection connect) {
		this.connect = connect;
	}

	public Statement getStatement() {
		return statement;
	}

	public void setStatement(Statement statement) {
		this.statement = statement;
	}

}
