import java.sql.*;
import java.util.Scanner;


public class GetCtrl extends DBConn {
	
	public GetCtrl() {
		connect();
	}
	
	public static void main(String[] args) {
		GetCtrl g = new GetCtrl();
		g.print�velserIGruppe();
	}
	
	public void print�velserIGruppe() {
		System.out.println("Velg gruppe du vil se �velser for (GruppeID): ");
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM �velsesgruppe");
			System.out.println("�velsegruppeliste: ");
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
			ResultSet rs = st.executeQuery("SELECT * FROM �velseIGruppe INNER JOIN (SELECT ID, Navn from �velseMedApparat UNION SELECT ID, Navn from �velseUtenApparat ) �velse ON �velseIGruppe.�velseID = �velse.ID WHERE GruppeID = "+gruppe);
			System.out.println("�velseliste for GruppeID "+gruppe+":");
			while(!rs.isLast()) {
				rs.next();
				System.out.println("�velseID: "+rs.getString("�velseID")+" "+"Navn: "+rs.getString("Navn"));
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void printResultatLogg() {
		System.out.println("Hvilken �velse vil du ha resultatlogg for? Her er en liste over alle �velsesID-er:");
		
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT ID, Navn from �velseMedApparat UNION SELECT ID, Navn from �velseUtenApparat");
			System.out.println("�velseliste: ");
			while(!rs.isLast()) {
				rs.next();
				System.out.println("�velseID: "+rs.getString("�velseID")+" "+"Navn: "+rs.getString("Navn");
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
			System.out.print("Skriv inn �velsen (ID) du �nsker logg for: ");
			ovelseValg = sc.nextLine();
			if(eksistererOvelse(ovelseValg)) {
				valgt = true;
				apparat = ovelseErMedApparat(ovelseValg);
			}
			else if(ovelseValg.equals("home")) {
				return;
			}
			else {
				System.out.println("Velg en ID som eksisterer, eller skriv 'home' for � returnere til hovedmenyen!");
			}
		}
		
		System.out.print("Fra hvilket tidspunkt �nsker du � hente logg? Oppgi p� formatet YYYY-MM-DD   Skriv her: ");
		String startdate = sc.nextLine();
		System.out.print("Til hvilket tidspunkt �nsker du � hente logg? Oppgi p� formatet YYYY-MM-DD   Skriv her: ");
		String enddate = sc.nextLine();
		
		
		try {
			Statement stmt = conn.createStatement();
			String query = "SELECT * FROM " + (apparat ? "Gjennomf�relseMedApparat" : "Gjennomf�relseUtenApparat") + " NATURAL JOIN Trenings�kt WHERE �velseID = " +ovelseValg +" AND Dato > '"+ startdate +"' AND Dato < '" + enddate +"'";
			ResultSet rs = stmt.executeQuery(query);
			System.out.println("Resultatlogg: ");
			while(!rs.isLast()) {
				rs.next();
				if (apparat)
					System.out.println("Vekt: "+rs.getString("Vekt")+" | "+"Antall sett: "+rs.getString("AntallSett") + " | " + "Dato: " + rs.getString("Dato"));
				else
					System.out.println("Utf�relse: "+rs.getString("Utf�relse") + " | " + "Dato: " + rs.getString("Dato"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void printNSisteTrenings�kter() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Hvor mange trenings�kter vil du se?");
		int n = sc.nextInt();
		try {
			Statement stmt = conn.createStatement();
			String query = "SELECT * FROM Trenings�kt ORDER BY Trenings�ktID DESC LIMIT "+String.valueOf(n);
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
	        String query = "select * from �velsesgruppe where GruppeID ='"+ovelsesgruppeID+"'";
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
	        String query = "SELECT * FROM (SELECT ID, Navn from �velseMedApparat UNION SELECT ID, Navn from �velseUtenApparat) where �velseID ='"+OvelseID+"'";
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
			String query = "SELECT ID, Navn from �velseMedApparat where �velseID ='"+ovelseID+"'";
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
			String query = "SELECT * FROM Trenings�kt WHERE Trenings�ktID = '"+treningsoktID+"'";
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
			String query = "SELECT * FROM " + (apparat ? "�velseMedApparat" : "Gjennomf�relseUtenApparat") + " WHERE �velseID = " +ovelseID;
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
		
		// tar inn ApparatID (navnet p� apparatet), printer ut all info om Apparatet.
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
		   String query = "select * from �velsesgruppe where GruppeID ='"+gruppeID+"'";
		   ResultSet rs = stmt.executeQuery(query);
		   rs.next();
		   System.out.println("Navn: " + rs.getString("Navn"));

		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
    
    
    
}
