public class CharacterInfo {

	private String name;
	private String surname;
	private int intelligence;
	private int memory;
	private int strength;
	private int agility;
	private int constitution;

	private static final int NAME_LIMIT = 15;
	public  static final int SIZE = 2 * (2 * NAME_LIMIT) + 5 * 4;

	public CharacterInfo (String name, String surname, 
	                      int intelligence, int memory,
	                      int strength, int agility, int constitution) {
		this.name         = name;
		this.surname      = surname;
		this.intelligence = intelligence;
		this.memory       = memory;
		this.strength     = strength;
		this.agility      = agility;
		this.constitution = constitution;
	}

	// Getters
	public String getName         () { return name;         }
	public String getSurname      () { return surname;      }
	public  int   getIntelligence () { return intelligence; }
	public  int   getMemory       () { return memory;       }
	public  int   getStrength     () { return strength;     }
	public  int   getAgility      () { return agility;      }
	public  int   getConstitution () { return constitution; }

	public byte[] toBytes() {
		byte[] record = new byte[SIZE];
		int offset = 0;
		// Name
		PackUtils.packString (name, NAME_LIMIT, record, offset);
		offset += 2 * NAME_LIMIT;
		// Surname
		PackUtils.packString (surname, NAME_LIMIT, record, offset);
		offset += 2 * NAME_LIMIT;
		// Intelligence
		PackUtils.packInt (intelligence, record, offset);
		offset += 4;
		// Memory
		PackUtils.packInt (memory, record, offset);
		offset += 4;
		// Strength
		PackUtils.packInt (strength, record, offset);
		offset += 4;
		// Agility
		PackUtils.packInt (agility, record, offset);
		offset += 4;
		// Constitution
		PackUtils.packInt (constitution, record, offset);
		// offset += 4;
		return record;
	}

	public static CharacterInfo fromBytes (byte[] record) {
		int offset = 0;
		// Name
		String name = PackUtils.unpackString (NAME_LIMIT, record, offset);
		offset += 2 * NAME_LIMIT;
		// Surname
		String surname = PackUtils.unpackString (NAME_LIMIT, record, offset);
		offset += 2 * NAME_LIMIT;
		// Intelligence
		int intelligence = PackUtils.unpackInt (record, offset);
		offset += 4;
		// Memory
		int memory = PackUtils.unpackInt (record, offset);
		offset += 4;
		// Strength
		int strength = PackUtils.unpackInt (record, offset);
		offset += 4;
		// Agility
		int agility = PackUtils.unpackInt (record, offset);
		offset += 4;
		// Constitution
		int constitution = PackUtils.unpackInt (record, offset);
		// offset += 4;
		return new CharacterInfo (name, surname,
		           intelligence, memory, strength, agility, constitution);
	}

	public String toString() {
		String result = name + " " + surname;
		String ls = System.lineSeparator();
		result +=
			ls + "Intel·ligència: " + intelligence +
			ls + "Memòria:        " + memory       +
			ls + "Força:          " + strength     +
			ls + "Agilitat:       " + agility      +
			ls + "Constitució:    " + constitution ;
		return result;
	}

}
