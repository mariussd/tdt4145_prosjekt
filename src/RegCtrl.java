import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class RegCtrl extends DBCon {
	
	public RegCtrl() {
		connect();
	}
	
	public void regApparat() {
		String navn;
		String beskrivelse;
		
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		System.out.println("Apparatnavn: ");
		navn = "'" + sc.nextLine() + "'";
		if (navn.equals("'null'")) {
			navn = "null";
		}
		
		System.out.println("Beskrivelse: ");
		beskrivelse = "'" + sc.nextLine() + "'";
		if (beskrivelse.equals("'null'")) {
			beskrivelse = "null";
		}

		
		try {
			Statement st = conn.createStatement();
			st.executeUpdate("INSERT INTO Apparat VALUES("+navn+","+beskrivelse+")");
		}catch(Exception e) {
			System.out.println("db error during insert of apparat: "+e);
		}
		
	}
	
	//Registrerer en øvelse – ikke gjennomførelse
	public void regovelsemedapparat() {
		String navn;
		String apparat;
		String beskrivelse;
		
		Scanner sc = new Scanner(System.in);
		System.out.println("Øvelsenavn: ");
		navn = "'" + sc.nextLine() + "'";
		if (navn.equals("'null'")) {
			navn = "null";
		}
		
		
		System.out.println("Apparatnavn: ");
		apparat = "'" + sc.nextLine() + "'";
		if (apparat.equals("'null'")) {
			apparat = "null";
		}
		
		System.out.println("Beskrivelse: ");
		beskrivelse = "'" + sc.nextLine() + "'";
		if (beskrivelse.equals("'null'")) {
			beskrivelse = "null";
		}
		

		
		Integer autoKey = null;
		try {
			java.sql.PreparedStatement ps = conn.prepareStatement("INSERT INTO ØvelseMedApparat (Navn, Apparat_navn, Beskrivelse) VALUES("+navn+","+apparat+","+beskrivelse+")",Statement.RETURN_GENERATED_KEYS);
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			if(rs.next()){
				autoKey = rs.getInt(1);
			}
		}catch(Exception e) {
			System.out.println("db error during insert of øvelse: "+e);
		}
		
		while (true) {
			System.out.println("Øvelsesgruppe tilhørlighet (oppgi ØvelsesgruppeID (Skriv end når ferdig)):");
			
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
			
			String øvelsesgruppe = sc.next(); 
			if (øvelsesgruppe.toLowerCase().equals("end")) {
				break;
			}
			
			try {
				Statement st = conn.createStatement();
				st.executeUpdate("INSERT INTO Øvelse_i_gruppe VALUES("+"'"+autoKey+"'"+","+"'"+øvelsesgruppe+"'"+")");
			}catch(Exception e) {
				System.out.println("db error during insert of øvelse_i_gruppe: "+e);
			}
			
		}
		
	}
	
	public void regovelseutenapparat() {
		String navn;
		String beskrivelse;
		
		Scanner sc = new Scanner(System.in);
		System.out.println("Øvelsenavn: ");
		navn = "'" + sc.nextLine() + "'";
		if (navn.equals("'null'")) {
			navn = "null";
		}
		
		System.out.println("Beskrivelse: ");
		beskrivelse = "'" + sc.nextLine() + "'";
		if (beskrivelse.equals("'null'")) {
			beskrivelse = "null";
		}
		

		
		Integer autoKey = null;
		try {
			java.sql.PreparedStatement ps = conn.prepareStatement("INSERT INTO Øvelse (Navn, Apparat_navn, Beskrivelse) VALUES("+navn+","+beskrivelse+")",Statement.RETURN_GENERATED_KEYS);
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			if(rs.next()){
				autoKey = rs.getInt(1);
			}
		}catch(Exception e) {
			System.out.println("db error during insert of øvelse: "+e);
		}
		
		while (true) {
			System.out.println("Øvelsesgruppe tilhørlighet (oppgi ØvelsesgruppeID (Skriv end når ferdig)):");
			
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
			
			String øvelsesgruppe = sc.next(); 
			if (øvelsesgruppe.toLowerCase().equals("end")) {
				break;
			}
			
			try {
				Statement st = conn.createStatement();
				st.executeUpdate("INSERT INTO Øvelse_i_gruppe VALUES("+"'"+autoKey+"'"+","+"'"+øvelsesgruppe+"'"+")");
			}catch(Exception e) {
				System.out.println("db error during insert of øvelse_i_gruppe: "+e);
			}
			
		}
		
	}
	
	@SuppressWarnings("resource")
	public void regTreningsokt(int pnrFK) {
		String dateTime; // format: YYYY-MM-DD HH:MM:SS
		String varighet;
		String form; // format: tall mellom 1 og 10
		String prestasjon; // format: tall mellom 1 og 10
		String pnrFKStr = "'" + Integer.toString(pnrFK) + "'";
		
		ArrayList<String> ovelser = new ArrayList<String>();
		
		Scanner sc = new Scanner(System.in);
		System.out.println("Dato og tid (format: YYYY-MM-DD HH:MM:SS): ");
		dateTime = sc.nextLine();
		if (!(dateTime.equals("null"))) {
			if (!(dateTime.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"))) {
				throw new IllegalArgumentException("Formatet er ikke korrekt, format: YYYY-MM-DD HH:MM:SS");
			}
			dateTime = "'" + dateTime + "'";
		}
		
		System.out.println("Varighet (i minutter): ");
		varighet ="'" + sc.nextLine() + "'";
		if (varighet.equals("'null'")) {
			varighet = "null";
		}
		
		System.out.println("Form (tall mellom 1 og 10): ");
		form = sc.nextLine();
		if (!(form.equals("null"))) {
			if (Integer.parseInt(form) <= 0 || Integer.parseInt(form) > 10) {
				throw new IllegalArgumentException("Formatet er ikke korrekt, format: tall mellom 1-10 ");
			}
			form = "'" + form + "'";
		}
		
		System.out.println("Prestasjon (tall mellom 1 og 10): ");
		prestasjon = sc.nextLine();
		if (!(form.equals("null"))) {
			if (Integer.parseInt(prestasjon) <= 0 || Integer.parseInt(prestasjon) > 10) {
				throw new IllegalArgumentException("Formatet er ikke korrekt, format: tall mellom 1-10 ");
			}
			prestasjon = "'" + prestasjon + "'";
		}
		
		while (true) {
			System.out.println("Øvelse ID (Skriv end når ferdig):");
			try {
				Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery("SELECT * FROM Øvelse");
				System.out.println("Øvelseliste: ");
				while(!rs.isLast()) {
					rs.next();
					System.out.println("ØvelseID: "+rs.getString("ØvelseID")+" "+"Navn: "+rs.getString("Navn")+" "+"Antall kilo: "+rs.getString("Antall_kilo")+" "+"Antall sett: "+rs.getString("Antall_sett")+" "+"Apparatnavn: "+rs.getString("Apparat_navn")+" "+"Beskrivelse: "+rs.getString("Beskrivelse")+" "+"Type: "+rs.getString("Øvelse_type"));
				}
			}catch(Exception e){
				System.out.println("db error during select of øvelse list: "+e);
			}
			String ovelseID = sc.nextLine(); 
			if (ovelseID.toLowerCase().equals("end")) {
				break;
			}
			ovelser.add(ovelseID);
		}
		
		
		Integer autoKey = null;
		try {
			java.sql.PreparedStatement ps = conn.prepareStatement("INSERT INTO Treningsøkt (Dato, Varighet, Form, Prestasjon, Pnr) VALUES(" + dateTime + "," + varighet + "," + form + "," + prestasjon + "," + pnrFKStr + ")", Statement.RETURN_GENERATED_KEYS);
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			if(rs.next()){
					autoKey = rs.getInt(1);
				}
		}catch(Exception e) {
			System.out.println("db error during insert of treningsøkt " + e);
		}
		
		for (String ovelse : ovelser) {
			try {
				Statement st = conn.createStatement();
				st.executeUpdate("INSERT INTO Økt_har_øvelse VALUES(" + ovelse + "," + "'" + autoKey + "'" + ")");
			}catch(Exception e) {
				System.out.println("db error during insert of Økt_har_øvelse " + e);
			}
		}
		
		while (true) {
			System.out.println("Vil du registrere notat? (Y/N): ");
			String branch = sc.nextLine().toLowerCase();
			if (branch.equals("n")) {
				break;
			}
			System.out.println("Formål: ");
			String formål = "'" + sc.nextLine() + "'";
			if (formål.equals("'null'")) {
				formål = "null";
			}
			System.out.println("Opplevelse: ");
			String opplevelse = "'" + sc.nextLine() + "'";
			if (opplevelse.equals("'null'")) {
				opplevelse = "null";
			}
			System.out.println("Annet: ");
			String annet = "'" + sc.nextLine() + "'";
			if (annet.equals("'null'")) {
				annet = "null";
			}
			
			try {
				Statement st = conn.createStatement();
				st.executeUpdate("INSERT INTO Notat (Treningsformål, Opplevelse, Annet, TreningsøktID) VALUES(" + formål + "," + opplevelse + "," + annet + "," + "'" + autoKey + "'" + ")");
			}catch(Exception e) {
				System.out.println("db error during insert of Notat " + e);
			}
		}
		
	}
	

	public void regOvelseGruppe() {
		String navn;
		
		Scanner sc = new Scanner(System.in);
		System.out.println("Øvelsesgruppenavn: ");
		navn = "'" + sc.nextLine() + "'";
		if (navn.equals("'null'")) {
			navn = "null";
		}
		
		try {
			Statement st = conn.createStatement();
			st.executeUpdate("INSERT INTO Øvelsesgruppe (Navn) VALUES("+navn+")");
		}catch(Exception e) {
			System.out.println("db error during insert of øvelsesgruppe: "+e);
		}
		
		
		
	}
	
	
	public static void main(String[] args) {
		RegCtrl r = new RegCtrl();
		
	}
	
	
}
