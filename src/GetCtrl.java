import java.sql.*;
import java.util.Scanner;


public class GetCtrl extends DBConn {
	
	public GetCtrl() {
		connect();
	}
	
	public static void main(String[] args) {
		GetCtrl g = new GetCtrl();
		g.printØvelserIGruppe();
	}
	
	public void printØvelserIGruppe() {
		System.out.println("Velg gruppe du vil se øvelser for (GruppeID): ");
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM Øvelsesgruppe");
			System.out.println("Øvelsegruppeliste: ");
			while(!rs.isLast()) {
				rs.next();
				System.out.println("GruppeID: "+rs.getString("ID")+" "+"Navn: "+rs.getString("Navn"));
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		Scanner sc = new Scanner(System.in);
		String gruppe = sc.next();
		
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM ØvelseIGruppe INNER JOIN (SELECT ID, Navn from ØvelseMedApparat UNION SELECT ID, Navn from ØvelseUtenApparat ) øvelse ON ØvelseIGruppe.ØvelseID = øvelse.ID WHERE GruppeID = "+gruppe);
			System.out.println("Øvelseliste for GruppeID "+gruppe+":");
			while(!rs.isLast()) {
				rs.next();
				System.out.println("ØvelseID: "+rs.getString("ØvelseID")+" "+"Navn: "+rs.getString("Navn"));
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void printResultatLogg() {
		System.out.println("Hvilken øvelse vil du ha resultatlogg for? Her er en liste over alle øvelsesID-er:");
		
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT ID, Navn from ØvelseMedApparat UNION SELECT ID, Navn from ØvelseUtenApparat");
			System.out.println("Øvelseliste: ");
			while(!rs.isLast()) {
				rs.next();
				System.out.println("ØvelseID: "+rs.getString("ØvelseID")+" "+"Navn: "+rs.getString("Navn");
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		Scanner sc = new Scanner(System.in);
		String ovelseValg = "";
		boolean valgt = false;
		boolean apparat = false;

		while(!valgt) {
			System.out.print("Skriv inn øvelsen (ID) du ønsker logg for: ");
			ovelseValg = sc.nextLine();
			if(eksistererOvelse(ovelseValg)) {
				valgt = true;
				apparat = ovelseErMedApparat(ovelseValg);
			}
			else if(ovelseValg.equals("home")) {
				return;
			}
			else {
				System.out.println("Velg en ID som eksisterer, eller skriv 'home' for å returnere til hovedmenyen!");
			}
		}
		
		System.out.print("Fra hvilket tidspunkt ønsker du å hente logg? Oppgi på formatet YYYY-MM-DD   Skriv her: ");
		String startdate = sc.nextLine();
		System.out.print("Til hvilket tidspunkt ønsker du å hente logg? Oppgi på formatet YYYY-MM-DD   Skriv her: ");
		String enddate = sc.nextLine();
		
		
		try {
			Statement stmt = conn.createStatement();
			String query = "SELECT * FROM " + (apparat ? "GjennomførelseMedApparat" : "GjennomførelseUtenApparat") + " NATURAL JOIN Treningsøkt WHERE ØvelseID = " +ovelseValg +" AND Dato > '"+ startdate +"' AND Dato < '" + enddate +"'";
			ResultSet rs = stmt.executeQuery(query);
			System.out.println("Resultatlogg: ");
			while(!rs.isLast()) {
				rs.next();
				if (apparat)
					System.out.println("Vekt: "+rs.getString("Vekt")+" | "+"Antall sett: "+rs.getString("AntallSett") + " | " + "Dato: " + rs.getString("Dato"));
				else
					System.out.println("Utførelse: "+rs.getString("Utførelse") + " | " + "Dato: " + rs.getString("Dato"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void printNSisteTreningsøkter() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Hvor mange treningsøkter vil du se?");
		int n = sc.nextInt();
		try {
			Statement stmt = conn.createStatement();
			String query = "SELECT * FROM Treningsøkt ORDER BY TreningsøktID DESC LIMIT "+String.valueOf(n);
			ResultSet rs = stmt.executeQuery(query);
			while(!rs.isLast()) {
				rs.next();

				System.out.println("Dato: "+rs.getString("Dato")+" "+"Tidspunkt: "+rs.getString("Tidspunkt")+" "+"Form: "+rs.getString("Form")+" "+"Prestasjon: "+rs.getString("Prestasjon")+" "+"Notat: "+rs.getString("Notat"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public boolean eksistererOvelsesgruppe(String ovelsesgruppeID) {
		try {
			Statement stmt = conn.createStatement();
	        String query = "select * from Øvelsesgruppe where GruppeID ='"+ovelsesgruppeID+"'";
	        ResultSet rs = stmt.executeQuery(query);
	        if(rs.next()) {
	        		return true;
	        }
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean eksistererOvelse(String OvelseID) {
		try {
			Statement stmt = conn.createStatement();
	        String query = "SELECT * FROM (SELECT ID, Navn from ØvelseMedApparat UNION SELECT ID, Navn from ØvelseUtenApparat) where ØvelseID ='"+OvelseID+"'";
	        ResultSet rs = stmt.executeQuery(query);
	        if(rs.next()) {
	        	return true;
	        }
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean ovelseErMedApparat(String ovelseID){
		try {
			Statement stmt = conn.createStatement();
			String query = "SELECT ID, Navn from ØvelseMedApparat where ØvelseID ='"+ovelseID+"'";
			ResultSet rs = stmt.executeQuery(query);
			if(rs.next()) {
				return true;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean eksistererApparat(String apparatID) {
		try {
			Statement stmt = conn.createStatement();
	        String query = "select * from Apparat where ApparatID ='"+apparatID+"'";
	        ResultSet rs = stmt.executeQuery(query);
	        if(rs.next()) {
	        		return true;
	        }
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
  
    public void printTreningsOkt (String treningsoktID) {
		try {
			Statement stmt = conn.createStatement();
			String query = "SELECT * FROM Treningsøkt WHERE TreningsøktID = '"+treningsoktID+"'";
			ResultSet rs = stmt.executeQuery(query);
			while(!rs.isLast()) {
				rs.next();

				System.out.println("Dato: "+rs.getString("Dato")+" "+"Tidspunkt: "+rs.getString("Tidspunkt")+" "+"Form: "+rs.getString("Form")+" "+"Prestasjon: "+rs.getString("Prestasjon")+" "+"Notat: "+rs.getString("Notat"));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}
    
	public void printOvelse(String ovelseID) {
		boolean apparat = ovelseErMedApparat(ovelseID);
		try {

			Statement stmt = conn.createStatement();
			String query = "SELECT * FROM " + (apparat ? "ØvelseMedApparat" : "GjennomførelseUtenApparat") + " WHERE ØvelseID = " +ovelseID;
			ResultSet rs = stmt.executeQuery(query);
			rs.next();
			if (apparat)
				System.out.println("Navn: "+rs.getString("Navn")+" ApparatID: "+rs.getString("ApparatID"));
			else
				System.out.println("Navn: "+rs.getString("Navn")+" Beskrivelse: "+rs.getString("TekstligBeskrivelse"));

		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void printApparat(String apparatID) {
		
		// tar inn ApparatID (navnet på apparatet), printer ut all info om Apparatet.
	   try {

		   Statement stmt = conn.createStatement();
		   String query = "select * from Apparat where Navn ='"+apparatID+"'";
		   ResultSet rs = stmt.executeQuery(query);
		   rs.next();
		   System.out.println("Navn: " + rs.getString("Navn") + " " + "Beskrivelse: " + rs.getString("Beskrivelse"));

	   }
	   catch (Exception e) {
		   e.printStackTrace();
	   }
	}
	
	public void printOvelsesgruppe(String gruppeID) {
		try {

		   Statement stmt = conn.createStatement();
		   String query = "select * from Øvelsesgruppe where GruppeID ='"+gruppeID+"'";
		   ResultSet rs = stmt.executeQuery(query);
		   rs.next();
		   System.out.println("Navn: " + rs.getString("Navn"));

		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
    
    
    
}
