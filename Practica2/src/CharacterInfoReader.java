import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class CharacterInfoReader {

	private static final int defaultValue = 100;

	public static CharacterInfo readCharacterFile (String dirName, String fileName) throws IOException {
		File file = new File (dirName, fileName);
		BufferedReader input = new BufferedReader (new FileReader (file));

		String name = input.readLine();
		if (name != null) {
			name = name.trim();
		} else {
			name = "";
		}
		if (name.isEmpty()) {
			System.err.println ("Camp nom buit al fitxer " + fileName);
		}

		String surname = input.readLine();
		if (surname != null) {
			surname = surname.trim();
		} else {
			surname = "";
		}

		int intelligence = defaultValue;
		String intelligenceStr = input.readLine();
		if (intelligenceStr != null) {
			try {
				intelligence = Integer.parseInt (intelligenceStr.trim());
			} catch (NumberFormatException nfe) {
				System.err.println ("Camp intel·ligència erroni al fitxer " + fileName);
			}
		} else {
			System.err.println ("Camp intel·ligència buit al fitxer " + fileName);
		}

		int memory = defaultValue;
		String memoryStr = input.readLine();
		if (memoryStr != null) {
			try {
				memory = Integer.parseInt (memoryStr.trim());
			} catch (NumberFormatException nfe) {
				System.err.println ("Camp memòria erroni al fitxer " + fileName);
			}
		} else {
			System.err.println ("Camp memòria buit al fitxer " + fileName);
		}

		int strength = defaultValue;
		String strengthStr = input.readLine();
		if (strengthStr != null) {
			try {
				strength = Integer.parseInt (strengthStr.trim());
			} catch (NumberFormatException nfe) {
				System.err.println ("Camp força erroni al fitxer " + fileName);
			}
		} else {
			System.err.println ("Camp força buit al fitxer " + fileName);
		}

		int agility = defaultValue;
		String agilityStr = input.readLine();
		if (agilityStr != null) {
			try {
				agility = Integer.parseInt (agilityStr.trim());
			} catch (NumberFormatException nfe) {
				System.err.println ("Camp agilitat erroni al fitxer " + fileName);
			}
		} else {
			System.err.println ("Camp agilitat buit al fitxer " + fileName);
		}

		int constitution = defaultValue;
		String constitutionStr = input.readLine();
		if (constitutionStr != null) {
			try {
				constitution = Integer.parseInt (constitutionStr.trim());
			} catch (NumberFormatException nfe) {
				System.err.println ("Camp constitució erroni al fitxer " + fileName);
			}
		} else {
			System.err.println ("Camp constitució buit al fitxer " + fileName);
		}

		input.close();

		return new CharacterInfo (name, surname,
		           intelligence, memory, strength, agility, constitution);
	}

}
