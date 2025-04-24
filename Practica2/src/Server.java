import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	private static final String CHARACTERS_DB_NAME = "src/charactersDB.dat";
	private static CharactersDB charactersDB;

	public static void main (String[] args) {
		final int PORT = 12345;

		//Abans de fer res deixem que carregui la BD
		try {
			charactersDB = new CharactersDB (CHARACTERS_DB_NAME);
		} catch (IOException ioe) {
			System.err.println ("Error obrint la base de dades!");
			System.exit (-1);
		}

		//Per cada client acceptem la seva connexió i li creem un thread per a ell. (Nosaltres ho fem concurrent)
		try (ServerSocket serverSocket = new ServerSocket(PORT)) {
			System.out.println("Servidor escoltant al port " + PORT);

			while (true) {
				Socket clientSocket = serverSocket.accept();
				System.out.println("Nou client connectat.");
				new Thread(new FunctionHandler(clientSocket)).start();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}



	public static String listCompleteNames() { //ns si les funcions poden ser publiques. Mirar
		int numCharacters = charactersDB.getNumCharacters();
		//Construïm una String amb els Characters
		StringBuilder characters = new StringBuilder();

		try {
			//Per cada Character l'afegim a l'String
			for (int i = 0; i < numCharacters; i++) {
				CharacterInfo ci = charactersDB.readCharacterInfo(i);
				characters.append(ci.getName())
						.append(" ")
						.append(ci.getSurname());
				if (i < numCharacters - 1) {
					characters.append("\n");
				}
			}
		} catch (IOException ioe) {
			System.err.println("Error a la base de dades!");
		}

		return characters.toString();
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




class FunctionHandler implements Runnable {
	private Socket clientSocket;

	public FunctionHandler(Socket socket) {
		this.clientSocket = socket;
	}

	@Override
	public void run() {
		try (
				InputStream in = clientSocket.getInputStream();
				OutputStream out = clientSocket.getOutputStream()
		) {
			byte[] buffer = new byte[1024];
			int bytesRead;

			while ((bytesRead = in.read(buffer)) != -1) {
				String input = new String(buffer, 0, bytesRead).trim();
				System.out.println("Rebut: " + input);

				int numero;
				try {
					numero = Integer.parseInt(input);
				} catch (NumberFormatException e) {
					out.write("Error: Introdueix un número enter.".getBytes());
					continue;
				}

				String resposta = switch (numero) {
					case 1 -> Server.listCompleteNames();
						/*
						case 2 -> infoFromOneCharacter();
						case 3 -> addCharacter();
						case 4 -> deleteCharacter();
						*/
					default -> "Número no vàlid. Introdueix un valor del 1 al 5.";
				};

				out.write(resposta.getBytes());
			}

		} catch (IOException e) {
			System.out.println("Error de comunicació: " + e.getMessage());
		} finally {
			try {
				clientSocket.close();
				System.out.println("Client desconnectat.");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}


