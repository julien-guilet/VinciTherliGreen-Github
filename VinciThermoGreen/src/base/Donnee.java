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

import com.twilio.*;
import com.twilio.rest.api.v2010.account.Message;

/**
 * <p>
 * Donn�e :
 * </p>
 *
 * <p>
 * Toutes les donn�es de l'application sont stock�es dans une BDD en
 * mySQL.<br />
 * Cette classe permet d'effectuer la connexion � cette BDD et a ex�cut� les
 * diff�rentes requ�tes vers cette BDD<br />
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
	 * Dans ce constructeur, nous �tablisons la connection entre la BDD et
	 * l'application
	 */
	public Donnee() {
		// Connection � la base de donn�e
		try {
			String url = "jdbc:mysql://localhost/vincithermogreen" + "?useUnicode=true"
					+ "&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&" + "serverTimezone=UTC";
			String utilisateur = "vinciThermo";
			String motDePasse = "vinciThermo";

			Class.forName("com.mysql.cj.jdbc.Driver");
			connect = DriverManager.getConnection(url, utilisateur, motDePasse);
			statement = connect.createStatement();
			setConnect(connect);
			System.out.println("Connection �tablie");

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null,"Probl�me avec la Base de donn�e");
		}
	}

	/**
	 * Cette fonction ex�cute une requ�te de selection afin de r�cup�rer les
	 * diff�rents nom des stades qui sont contenu dans un ArrayList
	 * 
	 * @return tous les noms de stades pr�sent dans la table stade dans base de
	 *         donn�e
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
	 * Cette fonction ex�cute une requ�te de selection permettant de r�cup�rer dans
	 * la base de donn�es tous les mesures de temp�ratures en fonction d'un nom de
	 * stade
	 * 
	 * @return un Arraylist contenant toutes les mesures du stade mis en param�tre
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
	 * Cette fonction retourne le nombre de zone pr�sent dans un stade
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
	 * Cette fonction r�cup�re les infos de la base de donn�es et les compare avec ceux mis en param�tre
	 * @param login 
	 * @param mdp
	 * @return un boolean en fonction des informations mis en param�tre et en base de donn�es
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
	 * Permet de r�cup�rer le nom d'utilisateur en fonction du login
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
	 * permet de r�cup�rer le r�le de l'utilisateur en fonction du login
	 * @param login
	 * @return le r�le de l'utilisateur
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
	 * ajoute l'utilisateur en base donn�es avec les informations mis en param�tres
	 * @param identifiant
	 * @param nom
	 * @param prenom
	 * @param mdp
	 * @param role
	 * @return true si l'insertion c'est bien execut� et false si une erreur survient
	 * @throws SQLException
	 * @since 3.1.0
	 */
	public boolean ajoutUser(String identifiant, String nom, String prenom, String mdp, String role, String tel)
			throws SQLException {
		
		try {

			boolean t = statement.execute("insert into  user values('"+identifiant+"','"+mdp+"','"+nom+"','"+prenom+"','"+role+"','"+tel+"');");
			return true;
			
		} catch (SQLException e) {
			System.out.println(e);
			return false;
		}

	}
	
	
	/**
	 * Permet de rcup�rer les valeur min et max de temp�rature en fonction du stade mis en param�tre
	 * @param nomstade correspond au nom du stade
	 * @return ArrayList<Float> poss�dant 2 Float, le premier correspond au min et le second au max du stade
	 * @throws SQLException
	 */
	public ArrayList<Float> getMinMaxTemp(String nomstade) throws SQLException{
		ArrayList<Float> minMax = new ArrayList<>();
		
		ResultSet resultset = null;
		resultset = statement.executeQuery("select temp_min, temp_max from stade where nomStade = '" + nomstade + "';");
		resultset.next();
		minMax.add(resultset.getFloat("temp_min"));
		minMax.add(resultset.getFloat("temp_max"));
		
		
		return minMax;
	}
	
	/**
	 * permet d'enregistrer les nouvelles valeur de min et max en fonction du stade, cela actualise �galement la date du changement
	 * @param nomstade correspond au stade auquel on souhaite modifier ces informations
	 * @param min correspond � la valeur min de temp�rature qui peut supporter le stade
	 * @param max correspond � la valeur max de temp�rature qui peut supporter le stade
	 * @return true, si la requ�te c'est bien executer. False dans le cas contraire
	 */
	public boolean engistreTemp(String nomstade,int min, int max) {
		try {
			statement.execute("  update stade set temp_min = '"+min+"' where nomStade ='"+nomstade+"';");
			statement.execute("  update stade set temp_max = '"+max+"' where nomStade ='"+nomstade+"';");
			statement.execute("  update stade set dateTemp = '"+java.time.LocalDate.now()+" "+java.time.LocalTime.now()+"' where nomStade ='"+nomstade+"';");
			return true;
		} catch(SQLException e) {
			System.out.println(e);
			return false;
		}
	}
	
	
	/**
	 * Cette fonction permet de v�rifier lors de la connexion d'un utilisateur, si dans la base de donn�e nous poss�dons une mesure qui d�passe les minimums ou maximums de temp�rature que doit avoir un stade.
	 * @return le nombre d'alerte collect�e
	 * @throws SQLException
	 */
	public int verifAlerte() throws SQLException {
		ResultSet resultset = null;
		int nbAlert =0;
		ArrayList<Integer> lesMesuresAlerte = new ArrayList<>();
		ArrayList<String> lesStadesAlerte = new ArrayList<>();
		ArrayList<Integer> lesTemperaturesAlerte = new ArrayList<>();
		ArrayList<Integer> lesTempMin = new ArrayList<>();
		ArrayList<Integer> lesTempMax = new ArrayList<>();
		ArrayList<String> lesGerants = new ArrayList<>();
		
		
		resultset = statement.executeQuery("select idMesure, mesure.nomStade, mesure.temperature, temp_min, temp_max , stade.gerant from mesure"
				 +" inner join stade on mesure.nomStade = stade.nomStade"
				 +" where mesure.horoDate >= stade.dateTemp and (temperature < stade.temp_min OR temperature >  stade.temp_max) AND mesure.idMesure not in (select IdAlert from alerte);");
		resultset.next();
		try {
			lesMesuresAlerte.add(resultset.getInt("idMesure"));
			lesStadesAlerte.add(resultset.getString("nomStade"));
			lesTemperaturesAlerte.add(resultset.getInt("temperature"));
			lesTempMin.add(resultset.getInt("temp_min"));
			lesTempMax.add(resultset.getInt("temp_max"));
			lesGerants.add(resultset.getString("gerant"));
			for (int i = 0; i < lesMesuresAlerte.size(); i++) {
				
				String message = "La mesure : "+lesMesuresAlerte.get(i)+" a �t� envoy� � : "+lesGerants.get(i)+" concernant le stade : "+lesStadesAlerte.get(i)+" car il possedait une temperature de : "+lesTemperaturesAlerte.get(i)+" alors que la temp�rature minimum est de : "+lesTempMin.get(i)+" et maximum de : "+lesTempMax.get(i)+".";
				System.out.println(message);
				statement.execute("  insert into alerte VALUES "
						+ "("+lesMesuresAlerte.get(i)+",'"+message+"');");
				nbAlert++;
				
				resultset = statement.executeQuery("select tel from user where login = '"+lesGerants.get(i)+"';");
				resultset.next();
				String numTel = resultset.getString("tel");
				
				
				//Envoie de message avec Twilio
				Twilio.init("ACf8bd48b1d88b4cd64938e365bd165ea7", "cabda4940afa91ef9dcae658bb0c31dd");
				Message messagee = Message.creator(
		        		new com.twilio.type.PhoneNumber(numTel),
		        		new com.twilio.type.PhoneNumber("+17073407596"),
		                message)
		            .create();
		        System.out.println(messagee.getSid());
			}
		        
		        return nbAlert;
		} catch(SQLException e) {
			return nbAlert;
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
