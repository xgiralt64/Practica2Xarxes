import java.io.RandomAccessFile;
import java.io.IOException;

public class CharactersDB {

	private RandomAccessFile charactersDB;
	private int numCharacters;

	public CharactersDB (String fileName) throws IOException {
		charactersDB = new RandomAccessFile (fileName, "rw");
		numCharacters = (int)charactersDB.length() / CharacterInfo.SIZE;
	}

	public int getNumCharacters() {
		return numCharacters;
	}

	public void close() throws IOException {
		charactersDB.close();
	}

	public void reset() throws IOException {
		charactersDB.setLength (0);
		numCharacters = 0;
	}

	public CharacterInfo readCharacterInfo (int n) throws IOException {
		charactersDB.seek (n * CharacterInfo.SIZE);
		byte[] record = new byte[CharacterInfo.SIZE];
		charactersDB.read (record);
		return CharacterInfo.fromBytes (record);
	}

	public int searchCharacter (String name) throws IOException {
		for (int i = 0; i < numCharacters; i++) {
			CharacterInfo character = readCharacterInfo (i);
			if (name.equalsIgnoreCase (character.getName())) {
				return i;
			}
		}
		return -1; // Not found
	}

	public void writeCharacterInfo (int n, CharacterInfo ci) throws IOException {
		charactersDB.seek (n * CharacterInfo.SIZE);
		byte[] record = ci.toBytes();
		charactersDB.write (record);
	}

	public void appendCharacterInfo (CharacterInfo ci) throws IOException {
		writeCharacterInfo (numCharacters, ci);
		numCharacters++;
	}

	public boolean insertCharacter (CharacterInfo ci) throws IOException {
		int n = searchCharacter (ci.getName());
		if (n == -1) {
			appendCharacterInfo (ci);
			return true;
		}
		return false; // The character was already in the file
	}

	public boolean deleteCharacter (String name) throws IOException {
		int n = searchCharacter (name);
		if (n != -1) {
			deleteCharacter (n);
			return true;
		}
		return false; // Not found
	}

	private void deleteCharacter (int n) throws IOException {
		CharacterInfo lastCharacter = readCharacterInfo (numCharacters - 1);
		writeCharacterInfo (n, lastCharacter);
		charactersDB.setLength ((numCharacters - 1) * CharacterInfo.SIZE);
		numCharacters--;
	}

}
