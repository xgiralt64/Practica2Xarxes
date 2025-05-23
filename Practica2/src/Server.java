import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Server {
	private static final List<ClientHandler> clients = Collections.synchronizedList(new ArrayList<>());
	private static final String CHARACTERS_DB_NAME = "src/charactersDB.dat"; //Si no troba la BD canviar per la ruta absoluta
	private static CharactersDB charactersDB;
	private static ServerSocket serverSocket = null; //ServerSocket per acceptar connexions
	private static final int PORT = 12345;
	private static int ClientIdCounter = 1;

	public static void main (String[] args) {
		//Abans de fer res deixem que carregui la BD
		try {
			charactersDB = new CharactersDB (CHARACTERS_DB_NAME);
		} catch (IOException ioe) {
			System.err.println ("Error obrint la base de dades!");
			System.exit (-1);
		}

		//Per cada client acceptem la seva connexió i li creem un thread per a ell. (Nosaltres ho fem concurrent)
		try{
			System.out.println("Servidor engegat, escoltant al port " + PORT);
			serverSocket = new ServerSocket(PORT);

			while (true) {
				//esperem i aceptem una nova conexio de client
				Socket newSocket = serverSocket.accept();
				gestionarNouClient(newSocket);
			}

		} catch (IOException e) {
			//e.printStackTrace();
		}
	}

	private static void gestionarNouClient(Socket newSocket) {
		System.out.println("[Servidor] --> Connexió acceptada de " + newSocket.getInetAddress().getHostAddress());
		ClientHandler clientHandler = new ClientHandler(newSocket, ClientIdCounter++);
		//s'afegeix un nou client a la llista de clients
		synchronized (clients) {
			clients.add(clientHandler);
			System.out.println("[Servidor] --> Nou client afegit a la llista. Total clients connectats: " + clients.size());
		}
		//es fa servir un nou fil per atendre el client, i així pugui funcionar de manera concurrent
		Thread clientThread = new Thread(clientHandler);
		clientThread.start();
	}


	public static synchronized String listCompleteNames() { //ns si les funcions poden ser publiques. Mirar
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

	public static synchronized String infoFromOneCharacter(String name) {
		try {
			int n = charactersDB.searchCharacter (name);
			if (n != -1) {
				CharacterInfo ci = charactersDB.readCharacterInfo (n);
				return ci.toString(); //Retorno la String del personatge

			} else {
				System.out.println ("Personatge no trobat.");
				return "Personatge no trobat.";
			}
		} catch (IOException ioe) {
			System.err.println ("Error a la base de dades!");
			return "Error a la base de dades!";
		}
    }

	public static synchronized String addCharacter(String Character) {

		CharacterInfo ci;

		//Separo cada part del Character per crear un objecte Character
		String[] parts = Character.split("-");

		String name = parts[0];
		String surname = parts[1];
		int intelligence = Integer.parseInt(parts[2]);
		int memory = Integer.parseInt(parts[3]);
		int strength = Integer.parseInt(parts[4]);
		int agility = Integer.parseInt(parts[5]);
		int constitution = Integer.parseInt(parts[6]);

		ci = new CharacterInfo (name, surname, intelligence, memory, strength, agility, constitution);

		try {
			boolean success = charactersDB.insertCharacter (ci);
			if (!success) {
				System.out.println ("S'ha intentat afegir un personatge que ja estava a la base de dades.");
				return "ERROR: Personatge ja existent a la base de dades"; //Si falla li enviem al client un missatge d'error
			}
		} catch (IOException ioe) {
			System.err.println ("Error a la base de dades!");
			return "Error a la base de dades!";
		}

		return "OK, personatge afegit correctament"; //Si tot va bé li diem al client
	}

	public static synchronized String deleteCharacter(String name) {

        String fullName = "";
        try {
            //Abans d'eliminar agafo el nom complet simplement per dir-li al client quin personatge s'ha eliminat exactament
            int n = charactersDB.searchCharacter(name);
			if (n != -1) {
				fullName = charactersDB.readCharacterInfo(n).getName() +" "+ charactersDB.readCharacterInfo(n).getSurname();
			}

            boolean success = charactersDB.deleteCharacter(name);
            if (!success) {
                System.out.println("Personatge no trobat.");
                return "ERROR: Personatge no trobat";
            }
        } catch (IOException ioe) {
            System.err.println("Error a la base de dades!");
            return "Error a la base de dades!";
        }
        return "OK, personatge " + fullName + " eliminat correctament";
    }
}


class ClientHandler implements Runnable {
	private Socket clientSocket;
	private int clientId;

	public ClientHandler(Socket socket, int clientId) {
		this.clientSocket = socket;
		this.clientId = clientId;
	}
	private void log(String message) {
		System.out.println("[CLIENT #" + clientId + "] --> " + message);
	}

	@Override
	public void run() {
		try (
				// Obrim els canals d'entrada i sortida del socket
				InputStream in = clientSocket.getInputStream();
				OutputStream out = clientSocket.getOutputStream()
		) {
			byte[] buffer = new byte[1024]; // Buffer per rebre dades del client
			int bytesRead;

			//llegim contínuament del client mentre estigui connectat
			while ((bytesRead = in.read(buffer)) != -1) {
				String input = new String(buffer, 0, bytesRead).trim();
				System.out.println("Rebut: " + input);

				int numero;
				try {
					//Agafo només el primer caracter seguint el format:
					//<OpcióMenu>-<Missatge>
					numero = Integer.parseInt(String.valueOf(input.charAt(0)));

				} catch (NumberFormatException e) {
					out.write("Error: Introdueix un número enter.".getBytes());
					continue;
				}
				// Si el client envia 5, es descoencta voluntàriament
				if (numero == 5) {
					log("Client ha demanat desconnexió.");
					break; // no enviem cap resposta, sortim del bucle
				}

				String resposta;
				// Tractem les diferents opcions del menú del client
				switch (numero) {
					case 1 -> {
						log("Accedint a la base de dades per llistar noms complets.");
						resposta = Server.listCompleteNames();
					}
					case 2 -> {
						log("Accedint a la base de dades per obtenir informació d'un personatge.");
						resposta = Server.infoFromOneCharacter(input.substring(2));
					}
					case 3 -> {
						log("Afegint un nou personatge a la base de dades.");
						resposta = Server.addCharacter(input.substring(2));
					}
					case 4 -> {
						log("Eliminant un personatge de la base de dades.");
						resposta = Server.deleteCharacter(input.substring(2));
					}
					default -> {
						log("Número d'opció no vàlid rebut: " + numero);
						resposta = "Número no vàlid. Introdueix un valor del 1 al 5.";
					}
				};
				//enviem la resposta al client
				out.write(resposta.getBytes());
			}

		} catch (IOException e) {
			log("Error de comunicació: " + e.getMessage());
		} finally {
			//Tanquem el socket del client i mostrem missatge de desconnexio
			tancar();
		}
	}

	public void tancar() {
		try {
			if (clientSocket != null && !clientSocket.isClosed()) {
				clientSocket.close();
				log("desconnectat");
			}
		} catch (IOException e) {
			System.err.println("Error tancant la connexió del client: " + e.getMessage());
		}
	}
}


