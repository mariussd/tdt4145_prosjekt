import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main{
	static Scanner scanner = new Scanner(System.in);
	static List<String> commands = Arrays.asList("help", "shutoff", "register", "login", "checklogin", "load");
	static boolean shutoff = false;

	static GetCtrl getController = new GetCtrl();
	static RegCtrl registerController = new RegCtrl();
	
	static String loggedInPnr = "Ingen bruker p�logget enda.";


	public static void printHelp() {
		// Metode for aa printe ut alternativer i programmet
		
		System.out.println("Du skrev hjelp.\nHer er ting du kan gjoere i programmet:\n"+
				"'help' - Viser denne listen over funksjonalitet i programmet.\n"
				+"'shutoff' - avslutter programmet.\n'load' - starter innlasting av �kt/�velse/apparat/�velsesgruppe.\n'register' - starter registrering av ny bruker/�kt/�velse/apparat/�velsesgruppe.\n'login' - logger p� �nsket bruker gjennom personnummer.\n'home' - tar deg tilbake til hovedmeny.\n'checklogin' - sjekker hvilken bruker som er p�logget.\n");
	}
	
	public static void registerOkt() {
		try {
			if(checkIfInt(loggedInPnr)) {
				registerController.regTreningsokt(Integer.parseInt(loggedInPnr));
			}
			
		} catch (Exception e) {
			System.out.println("Logged in pnr er ikke en integer. Har du logget inn?");
		}
	}
	
	public static void registerOvelse() {
		registerController.regovelseutenapparat();
	}
	
	public static void registerOvelseMedApparat() {
		registerController.regovelsemedapparat();
	}
	
	public static void registerApparat() {
		registerController.regApparat();
	}
	
	public static void registerOvelsesgruppe() {
		registerController.regOvelseGruppe();
	}
	
	public static void register() {
		List<String> localCommands = Arrays.asList("bruker", "trenings�kt", "�velse", "apparat", "�velsesgruppe");
		System.out.println("----Hva vil du registerere?----");
		System.out.println("Valg: \n1 - bruker - skriv 'bruker'.\n2 - Trenings�kt - skriv 'trenings�kt'.\n3 - �velse - skriv '�velse'.\n4 - Apparat - skriv 'apparat'.\n5 - �velsesgruppe - skriv '�velsesgruppe'.\n");
		System.out.print("Skriv inn valg: ");
		String registerValg = scanner.next();
		if(registerValg.toLowerCase().equals("home")) {
			return;
		}
		if(localCommands.contains(registerValg.toLowerCase())) {
			// Bruker skrev inn gyldig command
			
			if(registerValg.toLowerCase().equals("trenings�kt")) {
				registerOkt();
			}

			if(registerValg.toLowerCase().equals("�velse")) {
				registerOvelse();
			}
			
			if(registerValg.toLowerCase().equals("apparat")) {
				registerApparat();
			}
			
			if(registerValg.toLowerCase().equals("�velsesgruppe")) {
				registerOvelsesgruppe();
			}
		}
		else {
			System.out.println("Skriv inn en gyldig kommando, eller home for � g� til hovedmenyen.");
			register();
		}
	}
	
	
	public static void performAction(String action) {
		// Denne metoden m� inneholde if-setninger for hver av actions, og deretter kj�re hver action sin metode.
		if(action.equals("help")) {
			printHelp();
		}
		
		if(action.equals("shutoff")) {
			shutOff();
		}
		
		if(action.equals("register")) {
			register();
		}
		
		
		if(action.equals("load")) {
			load();
		}
	}
	
	
	public static boolean checkIfInt(String inputString) {
		try {
			Integer.parseInt(inputString);
			return true;
		} catch (Exception e) {
			System.out.println("Dette var ikke en integer");
			return false;
		}
	}
	
	public static void getTreningsokt() {
		System.out.print("Skriv inn trenings�ktID: ");
		String oktID = scanner.next();
		if(checkIfInt(oktID)) {
			getController.printTreningsOkt(oktID);
		}

	}
	
	public static void getOvelse() {
		System.out.print("Skriv inn �velseID: ");
		String ovelseID = scanner.next();
		if(checkIfInt(ovelseID)) {
			if(getController.eksistererOvelse(ovelseID)) {
				getController.printOvelse(ovelseID);
			}
		} else {
			return;
		}

	}
	
	public static void getApparat() {
		System.out.print("Skriv inn apparatnavn: ");
		String apparatID = scanner.next();
		try {
			Integer.parseInt(apparatID);
			System.out.println("Skriv inn et navn (string), ikke en int.");
		} catch (Exception e){
			if(getController.eksistererApparat(apparatID)) {
				getController.printApparat(apparatID);
			}
		}

	}
	
	public static void getOvelsesgruppe() {
		System.out.print("Skriv inn �velsesgruppeID: ");
		String ovelsesgruppeID = scanner.next();
		if(checkIfInt(ovelsesgruppeID)) {
			if(getController.eksistererOvelsesgruppe(ovelsesgruppeID)) {
				getController.printOvelsesgruppe(ovelsesgruppeID);
			}
		
		} else {
			return;
		}
	}
	
	public static void getResultatlogg() {
		getController.printResultatLogg();
	}
	
	public static void getNTreningsokter() {
		getController.printNSisteTrenings�kter();
	}
	
	public static void getSammeOvelsesgruppe() {
		getController.print�velserIGruppe();
	}
	
	public static void load() {
		// kalles av bruker for � entre valgmenyen for � laste inn �kter/�velser/apparater/�velsesgrupper
		
		List<String> localLoadCommands = Arrays.asList("trenings�kt", "�velse", "apparat", "�velsesgruppe", "resultatlogg", "ntrenings�kter", "samme�velsesgruppe");
		System.out.println("Hva vil du laste inn? Her er valgene:\n - Trenings�kt - skriv 'trenings�kt'.\n - �velse - skriv '�velse'.\n - Apparat - skriv 'apparat'\n - �velsesgruppe - skriv '�velsesgruppe'.\n - Resultatlogg - skriv 'resultatlogg'.\n - 'N' Siste trenings�kter - skriv 'ntrenings�kter'.\n - �velser i samme �velsesgruppe - skriv 'samme�velsesgruppe'.");
		System.out.print("Skriv valg her: ");
		String loadInput = scanner.next();
		if(loadInput.equals("home")) {
			return;
		}
		else if(!(localLoadCommands.contains(loadInput))) {
			System.out.println("Dette var ikke en gyldig kommando. Velg en av de som er listet, eller skriv 'home' for � komme til hovedmenyen.");
		}
		else {
			if(loadInput.equals("trenings�kt")) {
				getTreningsokt();
			}
			else if(loadInput.equals("�velse")) {
				getOvelse();
			}
			else if(loadInput.equals("apparat")) {
				getApparat();
			}
			else if(loadInput.equals("�velsesgruppe")) {
				getOvelsesgruppe();
			}
			else if(loadInput.equals("resultatlogg")) {
				getResultatlogg();
			}
			else if(loadInput.equals("ntrenings�kter")) {
				getNTreningsokter();
			}
			else if(loadInput.equals("samme�velsesgruppe")) {
				getSammeOvelsesgruppe();
			}
		}
		
	}
	
	
	public static void shutOff() {
		shutoff = true;
	}
	
	public static void waitForUserAction() {
		// Ber brukeren skrive en handling, og sjekker om det er gyldig. Hvis gyldig kj�res performaction.
		System.out.println("--- HOVEDMENY ---");
		System.out.print("Skriv en handling:");
		String action = scanner.next();
		action = action.toLowerCase();
		if(commands.contains(action)) {
			performAction(action);
		}
		else {
			System.out.println("Ugyldig action. Skriv 'help' om du trenger hjelp.");
		}	
	}
	
	public static void main(String args[]) {
		//Main metoden.

		System.out.println("Velkommen til treningsdagbok\n");
		System.out.println("For hjelp skriv inn 'help'");

		
		
		while(!shutoff) {
			waitForUserAction();
		}
		scanner.close();
		System.out.println("Farvel!");
	}
	
}
