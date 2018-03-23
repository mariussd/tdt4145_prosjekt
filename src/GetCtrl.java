import java.sql.*;
import java.util.Scanner;


public class GetCtrl extends DBConn {
	
	public GetCtrl() {
		connect();
	}
	
	public static void main(String[] args) {
		GetCtrl g = new GetCtrl();
		g.printØvelseISammeGruppe();
	}
	
	public void printØvelseISammeGruppe() {
		System.out.println("Velg gruppe du vil se øvelser for (GruppeID): ");
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM Øvelsesgruppe");
			System.out.println("Øvelsegruppeliste: ");
			while(!rs.isLast()) {
				rs.next();
				System.out.println("GruppeID: "+rs.getString("GruppeID")+" "+"Navn: "+rs.getString("Navn"));
			}
		}catch(Exception e){
			System.out.println("db error during select of øvelsegruppe list: "+e);
		}
		
		Scanner sc = new Scanner(System.in);
		String gruppe = sc.next();
		
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM Øvelse_i_gruppe NATURAL JOIN Øvelse WHERE GruppeID = "+gruppe);
			System.out.println("Øvelseliste for GruppeID "+gruppe+":");
			while(!rs.isLast()) {
				rs.next();
				System.out.println("ØvelseID: "+rs.getString("ØvelseID")+" "+"Navn: "+rs.getString("Navn")+
				" "+"Antall kilo: "+rs.getString("Antall_kilo")+" "+"Antall sett: "+
				rs.getString("Antall_sett")+" "+"Apparatnavn: "+rs.getString("Apparat_navn")+
				" "+"Beskrivelse: "+rs.getString("Beskrivelse")+" "+"Type: "+rs.getString("Øvelse_type"));
			}
		}catch(Exception e){
			System.out.println("db error during select of øvelse list: "+e);
		}
	}
	
	public void printResultatLogg() {
		System.out.println("Hvilken øvelse vil du ha resultatlogg for? Her er en liste over alle øvelsesID-er:");
		
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM Øvelse");
			System.out.println("Øvelseliste: ");
			while(!rs.isLast()) {
				rs.next();
				System.out.println("ØvelseID: "+rs.getString("ØvelseID")+" "+"Navn: "+rs.getString("Navn")+
				" "+"Antall kilo: "+rs.getString("Antall_kilo")+" "+"Antall sett: "+rs.getString("Antall_sett")+
				" "+"Apparatnavn: "+rs.getString("Apparat_navn")+" "+"Beskrivelse: "+rs.getString("Beskrivelse")+
				" "+"Type: "+rs.getString("Øvelse_type"));
			}
		}catch(Exception e){
			System.out.println("db error during select of øvelse list: "+e);
		}
		Scanner sc = new Scanner(System.in);
		String ovelseValg = "deafult";
		boolean selectingOvelseID = false;
		while(!selectingOvelseID) {
		System.out.print("Skriv inn øvelsen (ID) du ønsker logg for: ");
		ovelseValg = sc.nextLine();
		if(eksistererOvelse(ovelseValg)) {
			selectingOvelseID = true;
		}
		else if(ovelseValg.equals("home")) {
			return;
		}
		else {
			System.out.println("Velg en ID som eksisterer, eller skriv 'home' for å returnere til hovedmenyen!");
		}
		}
		
		System.out.print("Fra hvilket tidspunkt ønsker du å hente logg? Oppgi på formatet YYYY-MM-DD HH:MM:SS   Skriv her: ");
		String startdate = sc.nextLine();
		System.out.print("Til hvilket tidspunkt ønsker du å hente logg? Oppgi på formatet YYYY-MM-DD HH:MM:SS   Skriv her: ");
		String enddate = sc.nextLine();
		
		
		try {
			Statement stmt = conn.createStatement();
			String query = "SELECT * FROM (Økt_har_øvelse NATURAL JOIN Øvelse) NATURAL JOIN Treningsøkt WHERE ØvelseID = " +ovelseValg +" AND Dato > '"+ startdate +"' AND Dato < '" + enddate +"'";
			ResultSet rs = stmt.executeQuery(query);
			System.out.println("Resultatlogg: ");
			while(!rs.isLast()) {
				rs.next();
				System.out.println("ØvelseID: "+rs.getString("ØvelseID")+" | "+"Navn: "+rs.getString("Navn")+" | "+"Antall kilo: "+rs.getString("Antall_kilo")+" | "+"Antall sett: "+rs.getString("Antall_sett")+" | "+"Apparatnavn: "+rs.getString("Apparat_navn")+" | "+"Beskrivelse: "+rs.getString("Beskrivelse")+" | "+"Type: "+rs.getString("Øvelse_type") +" | " + "Pnr: " +rs.getString("Pnr") + " | " + "Dato: " + rs.getString("Dato"));
			}
		} catch (SQLException e) {
			System.out.println("db error during select of treningsøkt "+e);
		}
		
		
	}
	
	public void printNSisteTreningsøkter() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Hvor mange treningsøkter vil du se?");
		int n = sc.nextInt();
		try {
			Statement stmt = conn.createStatement();
			String query = "SELECT * FROM Treningsøkt LEFT JOIN Notat ON Treningsøkt.TreningsøktID = Notat.TreningsøktID ORDER BY Treningsøkt.TreningsøktID DESC LIMIT "+String.valueOf(n);
			ResultSet rs = stmt.executeQuery(query);
			while(!rs.isLast()) {
				rs.next();
				if(rs.getString("NotatID") == null) {
					System.out.println("TreningsøktID: " + rs.getString("TreningsøktID") + " " +
				"Dato: " + rs.getString("Dato") + " " + "Varighet: " + rs.getString("Varighet") +
				" " + "Form: " + rs.getString("Form") + " " + "Prestasjon: " + rs.getString("Prestasjon") 
				+ " " + "Pnr: " + rs.getString("Pnr"));					
				}else {
					System.out.println("TreningsøktID: " + rs.getString("TreningsøktID") + " " +"Dato: " 
				+ rs.getString("Dato") + " " + "Varighet: " + rs.getString("Varighet") + " " + "Form: " 
				+ rs.getString("Form") + " " + "Prestasjon: " + rs.getString("Prestasjon") + " " + "Pnr: " 
				+ rs.getString("Pnr")+" | "+"NotatID: " + rs.getString("NotatID") + " " + "Treningsformål: "
				+ rs.getString("Treningsformål") + " " + "Opplevelse: " + rs.getString("Opplevelse") + " " +
				"Annet: " + rs.getString("Annet"));	
				}

			}
		} catch (SQLException e) {
			System.out.println("db error during select of treningsøkt "+e);
		}
		
	}
	
	public boolean eksistererOvelsesgruppe(String ovelsesgruppeID) {
		try {
			Statement stmt = conn.createStatement();
	        String query = "select * from tobiassk_treningsdagbok.Øvelsesgruppe where GruppeID ='"+ovelsesgruppeID+"'";
	        ResultSet rs = stmt.executeQuery(query);
	        if(rs.next()) {
	        		return true;
	        }
	        else {
	        		System.out.println("Ingen øvelsesgrupper med denne IDen er registrert!");
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
	        String query = "select * from tobiassk_treningsdagbok.Øvelse where ØvelseID ='"+OvelseID+"'";
	        ResultSet rs = stmt.executeQuery(query);
	        if(rs.next()) {
	        		return true;
	        }
	        else {
	        		System.out.println("Ingen øvelser med dette navnet er registrert!");
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
	       String query = "select * from Treningsøkt where TreningsøktID ='"+OktID+"'";
	       ResultSet rs = stmt.executeQuery(query);
	       rs.next();
	       System.out.println("Info om TreningsøktID: " + OktID +":"+ "Dato: " + rs.getString("Dato") + " " + "Varighet: " + rs.getString("Varighet") + " " + "Form: " + rs.getString("Form") + " " + "Prestasjon: " + rs.getString("Prestasjon") + " " + "Pnr: " + rs.getString("Pnr"));
	            } catch (Exception e) {
	      System.out.println("db error during select of Treningsøkt = "+e);
		}
}
    
	public void printOvelse(String ovelseID) {
		// Ttar inn en id for øvelse, printer ut info om øvelsen.
		try {
			   
		       Statement stmt = conn.createStatement();
		       String query = "select * from Øvelse where ØvelseID ='"+ovelseID+"'";
		       ResultSet rs = stmt.executeQuery(query);
		       rs.next();
		       System.out.println("Info om Øvelse med ID: " + ovelseID +":"+ "Navn: " + rs.getString("Navn") + " " + "Antall kilo: " + rs.getString("Antall_kilo") + " " + "Antall sett: " + rs.getString("Antall_sett") + " " + "Apparat navn: " + rs.getString("Apparat_navn") + " " + "Beskrivelse : " + rs.getString("Beskrivelse") + "Øvelsetype: " + rs.getString("Øvelse_type"));
		            } catch (Exception e) {
		      System.out.println("db error during select of Treningsøkt = "+e);
			}
	}
	
	public void printApparat(String apparatID) {
		
		// tar inn ApparatID (navnet på apparatet), printer ut all info om Apparatet.
		   try {
			   
		       Statement stmt = conn.createStatement();
		       String query = "select * from Apparat where Navn ='"+apparatID+"'";
		       ResultSet rs = stmt.executeQuery(query);
		       rs.next();
		       System.out.println("Info om Apparat med ID: " + apparatID +":"+ "Navn: " + rs.getString("Navn") + " " + "Beskrivelse: " + rs.getString("Beskrivelse"));
		            } catch (Exception e) {
		      System.out.println("db error during select of Treningsøkt = "+e);
			}
	}
	
	public void printOvelsesgruppe(String ovelsesgruppeID) {
		// TODO
		   try {
			   
		       Statement stmt = conn.createStatement();
		       String query = "select * from Øvelsesgruppe where GruppeID ='"+ovelsesgruppeID+"'";
		       ResultSet rs = stmt.executeQuery(query);
		       rs.next();
		       System.out.println("Info om øvelsesgruppe med ID: " + ovelsesgruppeID +":"+ "Navn: " + rs.getString("Navn"));
		            } catch (Exception e) {
		      System.out.println("db error during select of Treningsøkt = "+e);
			}
		
	}
    
    
    
}
