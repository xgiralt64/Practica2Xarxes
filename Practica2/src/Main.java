import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

public class Main {

	private static final String CHARACTERS_DB_NAME = "src/charactersDB.dat";
	private static CharactersDB charactersDB;

	public static void main (String[] args) {
		try {
			charactersDB = new CharactersDB (CHARACTERS_DB_NAME);
		} catch (IOException ioe) {
			System.err.println ("Error obrint la base de dades!");
			System.exit (-1);
		}
		for (;;) {
			printMenu();
			int option = getOption();
			switch (option) {
				case 1:
					listCompleteNames();
					break;
				case 2:
					infoFromOneCharacter();
					break;
				case 3:
					addCharacter();
					break;
				case 4:
					deleteCharacter();
					break;
				case 5:
					quit();
					break;
			}
			System.out.println();
		}
	}

	private static void printMenu() {
		System.out.println ("Menú d'opcions:");
		System.out.println ("1 - Llista els noms complets dels personatges.");
		System.out.println ("2 - Obté la informació d'un personatge.");
		System.out.println ("3 - Afegeix un personatge.");
		System.out.println ("4 - Elimina un personatge.");
		System.out.println ("5 - Sortir.");
	}

	private static int getOption() {
		for (;;) {
			try {
				BufferedReader in = new BufferedReader (new InputStreamReader (System.in));
				System.out.println ("Escull una opció: ");
				String optionStr = in.readLine();
				int option = Integer.parseInt (optionStr);
				if (0 < option && option <= 5) {
					return option;
				}
			} catch (Exception e) {
				System.err.println ("Error llegint opció!");
			}
		}
	}

	private static void listCompleteNames() {
		int numCharacters = charactersDB.getNumCharacters();
		System.out.println();
		try {
			for (int i = 0; i < numCharacters; i++) {
				CharacterInfo ci = charactersDB.readCharacterInfo (i);
				System.out.println (ci.getName() + " " + ci.getSurname());
			}
		} catch (IOException ioe) {
			System.err.println ("Error a la base de dades!");
		}
	}

	private static void infoFromOneCharacter() {
		BufferedReader in = new BufferedReader (new InputStreamReader (System.in));
		System.out.println ("Escriu el nom del personatge: ");
		String name;
		try {
			name = in.readLine();
		} catch (IOException ioe) {
			System.err.println ("Error llegint el nom!");
			return;
		}
		try {
			int n = charactersDB.searchCharacter (name);
			if (n != -1) {
				CharacterInfo ci = charactersDB.readCharacterInfo (n);
				System.out.println (ci);
			} else {
				System.out.println ("Personatge no trobat.");
			}
		} catch (IOException ioe) {
			System.err.println ("Error a la base de dades!");
		}
	}

	private static void addCharacter() {
		BufferedReader in = new BufferedReader (new InputStreamReader (System.in));
		CharacterInfo ci;
		try {
			System.out.println ("Escriu el nom del personatge a afegir: ");
			String name = in.readLine();
			while (name == null || name.isEmpty()) {
				System.out.println ("El nom del personatge no pot ser buit.");
				System.out.println ("Escriu el nom del personatge a afegir: ");
				name = in.readLine();
			}
			System.out.println ("Escriu el cognom del personatge a afegir: ");
			String surname = in.readLine();

			int intelligence = -1;
			while (intelligence < 0) {
				System.out.println ("Introdueix la intel·ligència: ");
				String intelligenceStr = in.readLine();
				if (intelligenceStr != null) {
					try {
						intelligence = Integer.parseInt (intelligenceStr);
					} catch (NumberFormatException nfe) {
						// Ignore
					}
				}
			}
			int memory = -1;
			while (memory < 0) {
				System.out.println ("Introdueix la memòria: ");
				String memoryStr = in.readLine();
				if (memoryStr != null) {
					try {
						memory = Integer.parseInt (memoryStr);
					} catch (NumberFormatException nfe) {
						// Ignore
					}
				}
			}
			int strength = -1;
			while (strength < 0) {
				System.out.println ("Introdueix la força: ");
				String strengthStr = in.readLine();
				if (strengthStr != null) {
					try {
						strength = Integer.parseInt (strengthStr);
					} catch (NumberFormatException nfe) {
						// Ignore
					}
				}
			}
			int agility = -1;
			while (agility < 0) {
				System.out.println ("Introdueix l'agilitat: ");
				String agilityStr = in.readLine();
				if (agilityStr != null) {
					try {
						agility = Integer.parseInt (agilityStr);
					} catch (NumberFormatException nfe) {
						// Ignore
					}
				}
			}
			int constitution = -1;
			while (constitution < 0) {
				System.out.println ("Introdueix la constitució: ");
				String constitutionStr = in.readLine();
				if (constitutionStr != null) {
					try {
						constitution = Integer.parseInt (constitutionStr);
					} catch (NumberFormatException nfe) {
						// Ignore
					}
				}
			}
			ci = new CharacterInfo (name, surname,
		            intelligence, memory, strength, agility, constitution);
		} catch (IOException ioe) {
			System.err.println ("Error llegint la informació del personatge!");
			return;
		}
		try {
			boolean success = charactersDB.insertCharacter (ci);
			if (!success) {
				System.out.println ("Aquest personatge ja estava a la base de dades.");
			}
		} catch (IOException ioe) {
			System.err.println ("Error a la base de dades!");
		}
	}

	private static void deleteCharacter() {
		BufferedReader in = new BufferedReader (new InputStreamReader (System.in));
		System.out.println ("Escriu el nom del personatge a eliminar: ");
		String name;
		try {
			name = in.readLine();
		} catch (IOException ioe) {
			System.err.println ("Error llegint el nom!");
			return;
		}
		try {
			boolean success = charactersDB.deleteCharacter (name);
			if (!success) {
				System.out.println ("Personatge no trobat.");
			}
		} catch (IOException ioe) {
			System.err.println ("Error a la base de dades!");
		}
	}

	private static void quit() {
		try {
			charactersDB.close();
			System.exit (0);
		} catch (IOException ioe) {
			System.err.println ("Error tancant base de dades!");
			System.exit (-1);
		}
	}

}
