import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class GenerateCharactersDB {

	private static final String CHARACTERS_DB_NAME = "charactersDB.dat";
	private static final String CHARACTERS_FILES = "characters.txt";
	private static final String CHARACTERS_DIR = "Characters";
	private static CharactersDB charactersDB;

	public static void main (String[] args) {
		try {
			charactersDB = new CharactersDB (CHARACTERS_DB_NAME);
			loadFromFiles();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.err.println ("Error generant base de dades!");
		}
	}

	private static void loadFromFiles() throws IOException {
		charactersDB.reset();
		BufferedReader input = new BufferedReader (new FileReader (CHARACTERS_FILES));
		String fileName = input.readLine();
		while (fileName != null) {
			System.out.println ("Llegint " + fileName);
			CharacterInfo ci = CharacterInfoReader.readCharacterFile (CHARACTERS_DIR, fileName);
			charactersDB.appendCharacterInfo (ci);
			fileName = input.readLine();
		}
		input.close();
		System.out.println ("Fet!");
	}

}
