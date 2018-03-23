import java.sql.*;
import java.util.Scanner;


public class GetCtrl extends DBConn {
	
	public GetCtrl() {
		connect();
	}
	
	public static void main(String[] args) {
		GetCtrl g = new GetCtrl();
		g.print�velseISammeGruppe();
	}
	
	public void print�velseISammeGruppe() {
		System.out.println("Velg gruppe du vil se �velser for (GruppeID): ");
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM �velsesgruppe");
			System.out.println("�velsegruppeliste: ");
			while(!rs.isLast()) {
				rs.next();
				System.out.println("GruppeID: "+rs.getString("GruppeID")+" "+"Navn: "+rs.getString("Navn"));
			}
		}catch(Exception e){
			System.out.println("db error during select of �velsegruppe list: "+e);
		}
		
		Scanner sc = new Scanner(System.in);
		String gruppe = sc.next();
		
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM �velse_i_gruppe NATURAL JOIN �velse WHERE GruppeID = "+gruppe);
			System.out.println("�velseliste for GruppeID "+gruppe+":");
			while(!rs.isLast()) {
				rs.next();
				System.out.println("�velseID: "+rs.getString("�velseID")+" "+"Navn: "+rs.getString("Navn")+
				" "+"Antall kilo: "+rs.getString("Antall_kilo")+" "+"Antall sett: "+
				rs.getString("Antall_sett")+" "+"Apparatnavn: "+rs.getString("Apparat_navn")+
				" "+"Beskrivelse: "+rs.getString("Beskrivelse")+" "+"Type: "+rs.getString("�velse_type"));
			}
		}catch(Exception e){
			System.out.println("db error during select of �velse list: "+e);
		}
	}
	
	public void printResultatLogg() {
		System.out.println("Hvilken �velse vil du ha resultatlogg for? Her er en liste over alle �velsesID-er:");
		
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM �velse");
			System.out.println("�velseliste: ");
			while(!rs.isLast()) {
				rs.next();
				System.out.println("�velseID: "+rs.getString("�velseID")+" "+"Navn: "+rs.getString("Navn")+
				" "+"Antall kilo: "+rs.getString("Antall_kilo")+" "+"Antall sett: "+rs.getString("Antall_sett")+
				" "+"Apparatnavn: "+rs.getString("Apparat_navn")+" "+"Beskrivelse: "+rs.getString("Beskrivelse")+
				" "+"Type: "+rs.getString("�velse_type"));
			}
		}catch(Exception e){
			System.out.println("db error during select of �velse list: "+e);
		}
		Scanner sc = new Scanner(System.in);
		String ovelseValg = "deafult";
		boolean selectingOvelseID = false;
		while(!selectingOvelseID) {
		System.out.print("Skriv inn �velsen (ID) du �nsker logg for: ");
		ovelseValg = sc.nextLine();
		if(eksistererOvelse(ovelseValg)) {
			selectingOvelseID = true;
		}
		else if(ovelseValg.equals("home")) {
			return;
		}
		else {
			System.out.println("Velg en ID som eksisterer, eller skriv 'home' for � returnere til hovedmenyen!");
		}
		}
		
		System.out.print("Fra hvilket tidspunkt �nsker du � hente logg? Oppgi p� formatet YYYY-MM-DD HH:MM:SS   Skriv her: ");
		String startdate = sc.nextLine();
		System.out.print("Til hvilket tidspunkt �nsker du � hente logg? Oppgi p� formatet YYYY-MM-DD HH:MM:SS   Skriv her: ");
		String enddate = sc.nextLine();
		
		
		try {
			Statement stmt = conn.createStatement();
			String query = "SELECT * FROM (�kt_har_�velse NATURAL JOIN �velse) NATURAL JOIN Trenings�kt WHERE �velseID = " +ovelseValg +" AND Dato > '"+ startdate +"' AND Dato < '" + enddate +"'";
			ResultSet rs = stmt.executeQuery(query);
			System.out.println("Resultatlogg: ");
			while(!rs.isLast()) {
				rs.next();
				System.out.println("�velseID: "+rs.getString("�velseID")+" | "+"Navn: "+rs.getString("Navn")+" | "+"Antall kilo: "+rs.getString("Antall_kilo")+" | "+"Antall sett: "+rs.getString("Antall_sett")+" | "+"Apparatnavn: "+rs.getString("Apparat_navn")+" | "+"Beskrivelse: "+rs.getString("Beskrivelse")+" | "+"Type: "+rs.getString("�velse_type") +" | " + "Pnr: " +rs.getString("Pnr") + " | " + "Dato: " + rs.getString("Dato"));
			}
		} catch (SQLException e) {
			System.out.println("db error during select of trenings�kt "+e);
		}
		
		
	}
	
	public void printNSisteTrenings�kter() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Hvor mange trenings�kter vil du se?");
		int n = sc.nextInt();
		try {
			Statement stmt = conn.createStatement();
			String query = "SELECT * FROM Trenings�kt LEFT JOIN Notat ON Trenings�kt.Trenings�ktID = Notat.Trenings�ktID ORDER BY Trenings�kt.Trenings�ktID DESC LIMIT "+String.valueOf(n);
			ResultSet rs = stmt.executeQuery(query);
			while(!rs.isLast()) {
				rs.next();
				if(rs.getString("NotatID") == null) {
					System.out.println("Trenings�ktID: " + rs.getString("Trenings�ktID") + " " +
				"Dato: " + rs.getString("Dato") + " " + "Varighet: " + rs.getString("Varighet") +
				" " + "Form: " + rs.getString("Form") + " " + "Prestasjon: " + rs.getString("Prestasjon") 
				+ " " + "Pnr: " + rs.getString("Pnr"));					
				}else {
					System.out.println("Trenings�ktID: " + rs.getString("Trenings�ktID") + " " +"Dato: " 
				+ rs.getString("Dato") + " " + "Varighet: " + rs.getString("Varighet") + " " + "Form: " 
				+ rs.getString("Form") + " " + "Prestasjon: " + rs.getString("Prestasjon") + " " + "Pnr: " 
				+ rs.getString("Pnr")+" | "+"NotatID: " + rs.getString("NotatID") + " " + "Treningsform�l: "
				+ rs.getString("Treningsform�l") + " " + "Opplevelse: " + rs.getString("Opplevelse") + " " +
				"Annet: " + rs.getString("Annet"));	
				}

			}
		} catch (SQLException e) {
			System.out.println("db error during select of trenings�kt "+e);
		}
		
	}
	
	public boolean eksistererOvelsesgruppe(String ovelsesgruppeID) {
		try {
			Statement stmt = conn.createStatement();
	        String query = "select * from tobiassk_treningsdagbok.�velsesgruppe where GruppeID ='"+ovelsesgruppeID+"'";
	        ResultSet rs = stmt.executeQuery(query);
	        if(rs.next()) {
	        		return true;
	        }
	        else {
	        		System.out.println("Ingen �velsesgrupper med denne IDen er registrert!");
	        		return false;
	        }
	         } catch (Exception e) {
	        	 System.out.println("db error during select of navn = "+e);
	         }
		return false;
	}
	
	public boolean eksistererOvelse(String OvelseID) {
		try {
			Statement stmt = conn.createStatement();
	        String query = "select * from tobiassk_treningsdagbok.�velse where �velseID ='"+OvelseID+"'";
	        ResultSet rs = stmt.executeQuery(query);
	        if(rs.next()) {
	        		return true;
	        }
	        else {
	        		System.out.println("Ingen �velser med dette navnet er registrert!");
	        		return false;
	        }
	         } catch (Exception e) {
	        	 System.out.println("db error during select of navn = "+e);
	         }
		return false;
	}
	
	public boolean eksistererApparat(String apparatNavn) {
		try {
			Statement stmt = conn.createStatement();
	        String query = "select Navn from tobiassk_treningsdagbok.Apparat where Navn ='"+apparatNavn+"'";
	        ResultSet rs = stmt.executeQuery(query);
	        if(rs.next()) {
	        		return true;
	        }
	        else {
	        		System.out.println("Ingen apparater med dette navnet er registrert!");
	        		return false;
	        }
	         } catch (Exception e) {
	        	 System.out.println("db error during select of navn = "+e);
	         }
		return false;
	}
	
	public boolean eksistererPerson(String Pnr) {
		try {
			Statement stmt = conn.createStatement();
	        String query = "select Navn from tobiassk_treningsdagbok.Person where Pnr ='"+Pnr+"'";
	        ResultSet rs = stmt.executeQuery(query);
	        if(rs.next()) {
	        		return true;
	        }
	        else {
	        		return false;
	        }
	         } catch (Exception e) {
	        	 System.out.println("db error during select of navn = "+e);
	         }
		return false;
	}
	
    public void printPersonNavn (String Pnr) {
    		// tar inn personnummer, printer ut navn i konsollen.
       try {
    	   
           Statement stmt = conn.createStatement();
           String query = "select Navn from Person  where Pnr = '"+Pnr+"'";
           //String query = "select Navn from tobiassk_treningsdagbok.Person where Pnr ='"+Pnr+"'";
           ResultSet rs = stmt.executeQuery(query);
           rs.next();
           System.out.println("Navnet for pnr: " + Pnr +" er "+ rs.getString("Navn"));
                } catch (Exception e) {
          System.out.println("db error during select of navn = "+e);
		}
    }
  
    public void printTreningsOkt (String OktID) {
		// tar inn OktID, printer ut all info om OKTEN.
	   try {
		   
	       Statement stmt = conn.createStatement();
	       String query = "select * from Trenings�kt where Trenings�ktID ='"+OktID+"'";
	       ResultSet rs = stmt.executeQuery(query);
	       rs.next();
	       System.out.println("Info om Trenings�ktID: " + OktID +":"+ "Dato: " + rs.getString("Dato") + " " + "Varighet: " + rs.getString("Varighet") + " " + "Form: " + rs.getString("Form") + " " + "Prestasjon: " + rs.getString("Prestasjon") + " " + "Pnr: " + rs.getString("Pnr"));
	            } catch (Exception e) {
	      System.out.println("db error during select of Trenings�kt = "+e);
		}
}
    
	public void printOvelse(String ovelseID) {
		// Ttar inn en id for �velse, printer ut info om �velsen.
		try {
			   
		       Statement stmt = conn.createStatement();
		       String query = "select * from �velse where �velseID ='"+ovelseID+"'";
		       ResultSet rs = stmt.executeQuery(query);
		       rs.next();
		       System.out.println("Info om �velse med ID: " + ovelseID +":"+ "Navn: " + rs.getString("Navn") + " " + "Antall kilo: " + rs.getString("Antall_kilo") + " " + "Antall sett: " + rs.getString("Antall_sett") + " " + "Apparat navn: " + rs.getString("Apparat_navn") + " " + "Beskrivelse : " + rs.getString("Beskrivelse") + "�velsetype: " + rs.getString("�velse_type"));
		            } catch (Exception e) {
		      System.out.println("db error during select of Trenings�kt = "+e);
			}
	}
	
	public void printApparat(String apparatID) {
		
		// tar inn ApparatID (navnet p� apparatet), printer ut all info om Apparatet.
		   try {
			   
		       Statement stmt = conn.createStatement();
		       String query = "select * from Apparat where Navn ='"+apparatID+"'";
		       ResultSet rs = stmt.executeQuery(query);
		       rs.next();
		       System.out.println("Info om Apparat med ID: " + apparatID +":"+ "Navn: " + rs.getString("Navn") + " " + "Beskrivelse: " + rs.getString("Beskrivelse"));
		            } catch (Exception e) {
		      System.out.println("db error during select of Trenings�kt = "+e);
			}
	}
	
	public void printOvelsesgruppe(String ovelsesgruppeID) {
		// TODO
		   try {
			   
		       Statement stmt = conn.createStatement();
		       String query = "select * from �velsesgruppe where GruppeID ='"+ovelsesgruppeID+"'";
		       ResultSet rs = stmt.executeQuery(query);
		       rs.next();
		       System.out.println("Info om �velsesgruppe med ID: " + ovelsesgruppeID +":"+ "Navn: " + rs.getString("Navn"));
		            } catch (Exception e) {
		      System.out.println("db error during select of Trenings�kt = "+e);
			}
		
	}
    
    
    
}
