/**
 * @author Jérôme Valenti 
 */
package control;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import base.Donnee;

import java.sql.*;

import model.Mesure;
import view.ConsoleGUI;

/**
 * <p>
 * Le controlleur :
 * </p>
 * <ol>
 * <li>lit les mesures de température dans un fichier texte</li>
 * <li>retourne la collection des mesures<br />
 * </li>
 * </ol>
 * 
 * @author Jérôme Valenti et Julien Guilet
 * @version 2.0.0
 *
 */
public class Controller {

	/**
	 * <p>
	 * Les mesures lues dans le fichier des relevés de températures
	 * </p>
	 */
	private ArrayList<Mesure> lesMesures = new ArrayList<Mesure>();
	
	//Connection à la BDD
	private Donnee BDD = new Donnee();
	
	public Controller() throws ParseException, SQLException {
		
		
		ArrayList<String> stades = BDD.SelectAllStadiums();
		lesMesures = BDD.SelectMesuresStadium(stades.get(0));
		//lireCSV("data\\mesures.csv");
	}

	
	
	/**
	 * Renvoie le nombre de zone présente dans le stade mis en paramètre
	 * @param String 
	 * @return int
	 * @throws SQLException 
	 * 
	 */
	
	public int getNbZone(String stade) throws SQLException{
		int a = BDD.NbZoneStade(stade);
		return a;
	}
		
	/**
	 * <p>Renvoie une liste de mesure concernant un stade mis en paramètre </p>
	 * @param String
	 * @return ArrayList<Mesure>
	 * @throws SQLException
	 */
	public ArrayList<Mesure> getMesuresStade(String stade) throws SQLException{
		ArrayList<Mesure> tabMesure = BDD.SelectMesuresStadium(stade);
		lesMesures = tabMesure;
		return tabMesure;
	}
	
	/**
	 * <p>Renvoie la liste de tous les stades présent dans la BDD</p>
	 * @return une liste de string 
	 * @throws SQLException
	 */
	public ArrayList<String> getStades() throws SQLException {
		ArrayList<String> AllStades = BDD.SelectAllStadiums();
		return AllStades;
	}
	
	/**
	 * <p>Lit un fichier de type CSV (Comma Separated Values)</p>
	 * <p>Le fichier contient les mesures de temp&eacute;rature de la pelouse.</p>
	 * 
	 * @author Jérôme Valenti
	 * @throws ParseException
	 * @since 2.0.0
	 */
	public void lireCSV(String filePath) throws ParseException {

		try {
			File f = new File(filePath);
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);

			try {
				// Chaque ligne est un enregistrement de données
				String records = br.readLine();

				// Chaque enregistrement contient des champs
				String[] fields = null;
				String numZone = null;
				Date horoDate = null;
				float fahrenheit;

				while (records != null) {
					// Affecte les champs de l'enregistrement courant dans un
					// tableau de chaine
					fields = records.split(";");

					// Affecte les champs aux paramètre du constructeur de
					// mesure
					numZone = fields[0];
					horoDate = strToDate(fields[1]);
					fahrenheit = Float.parseFloat(fields[2]);

					// Instancie une Mesure
					Mesure laMesure = new Mesure(numZone, horoDate, fahrenheit);
					lesMesures.add(laMesure);

					// Enregistrement suivant
					records = br.readLine();
				}

				br.close();
				fr.close();
			} catch (IOException exception) {
				System.out.println("Erreur lors de la lecture : " + exception.getMessage());
			}
		} catch (FileNotFoundException exception) {
			System.out.println("Le fichier n'a pas été trouvé");
		}
	}

	/**
	 * <p>
	 * Filtre la collection des mesures en fonction des param&egrave;tres :
	 * </p>
	 * <ol>
	 * <li>la zone (null = toutes les zones)</li>
	 * <li>la date de d&eacute;but (null = &agrave; partir de l'origine)</li>
	 * <li>la date de fin (null = jusqu'&agrave; la fin)</li><br />
	 * </ol>
	 */
	// public void filtrerLesMesure(String laZone, Date leDebut, Date lafin) {
	public ArrayList<Mesure> filtrerLesMesure(String laZone) {
		// Parcours de la collection
		// Ajout à laSelection des objets qui correspondent aux paramètres
		// Envoi de la collection
		ArrayList<Mesure> laSelection = new ArrayList<Mesure>();
		for (Mesure mesure : lesMesures) {
			if (laZone.compareTo("*") == 0) {
				laSelection.add(mesure);
			} else {
				if (laZone.compareTo(mesure.getNumZone()) == 0) {
					laSelection.add(mesure);
				}
			}
		}
		return laSelection;
	}

	/**
	 * <p>
	 * Retourne la collection des mesures
	 * </p>
	 * 
	 * @return ArrayList<Mesure>
	 */
	public ArrayList<Mesure> getLesMesures() {

		return lesMesures;
	}

	/**
	 * <p>Convertion d'une String en Date</p>
	 * 
	 * @param strDate
	 * @return Date
	 * @throws ParseException
	 */
	private Date strToDate(String strDate) throws ParseException {

		SimpleDateFormat leFormat = null;
		Date laDate = new Date();
		leFormat = new SimpleDateFormat("yy-MM-dd kk:mm");

		laDate = leFormat.parse(strDate);
		return laDate;
	}
}
