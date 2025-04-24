package Client;

import java.io.*;
import java.net.*;


public class Client {
	public static void main(String[] args) {
		try (
				Socket socket = new Socket("localhost", 12345);
				InputStream in = socket.getInputStream();
				OutputStream out = socket.getOutputStream();
				BufferedReader consola = new BufferedReader(new InputStreamReader(System.in))
		) {

			for (;;) {
				printMenu();
				int option = getOption();

				switch (option) {
					case 1:
						//Envia "1" per llistar noms
						out.write("1".getBytes());
						break;
					case 2:
						//Enviem la resposta de la funció del client.
						//Format:2-<NomPersonatge> (Despres el servidor ja tallarà cada part del missatge)
						out.write(infoFromOneCharacter().getBytes());
						break;
					case 3:
						out.write(addCharacter().getBytes());
						break;
					case 4:
						out.write(deleteCharacter().getBytes());
						break;
					case 5:
						quit();
						break;
				}

				// Llegeix i mostra la resposta del servidor
				byte[] buffer = new byte[2048];
				int bytesRead = in.read(buffer);
				if (bytesRead != -1) {
					String resposta = new String(buffer, 0, bytesRead);
					System.out.println("Servidor:\n" + resposta);
				}

				System.out.println();
			}

		} catch (IOException e) {
			System.err.println("Error de connexió amb el servidor.");
			e.printStackTrace();
		}
	}



	private static void printMenu() {
		System.out.println("Menú d'opcions del Client:");
		System.out.println("1 - Llista els noms complets dels personatges.");
		System.out.println("2 - Obté la informació d'un personatge.");
		System.out.println("3 - Afegeix un personatge.");
		System.out.println("4 - Elimina un personatge.");
		System.out.println("5 - Sortir.");
	}

	private static int getOption() {
		for (;;) {
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
				System.out.print("Escull una opció: ");
				String optionStr = in.readLine();
				int option = Integer.parseInt(optionStr);
				if (option >= 1 && option <= 5) {
					return option;
				}
			} catch (Exception e) {
				System.err.println("Error llegint opció!");
			}
		}
	}


	private static String infoFromOneCharacter() {
		BufferedReader in = new BufferedReader (new InputStreamReader (System.in));
		System.out.println ("Escriu el nom del personatge: ");
		String name = "";
		try {
			name = in.readLine();
		} catch (IOException ioe) {
			System.err.println ("Error llegint el nom!");
		}
		return "2-" + name; //Retornem amb el format 2-<NomPersonatge> (apunt per enviar al server)

	}


	private static String addCharacter() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String name;
        String surname;
        int intelligence;
        int memory;
        int strength;
        int agility;
        int constitution;
        try {
            System.out.println("Escriu el nom del personatge a afegir: ");
            name = in.readLine();
            while (name == null || name.isEmpty()) {
                System.out.println("El nom del personatge no pot ser buit.");
                System.out.println("Escriu el nom del personatge a afegir: ");
                name = in.readLine();
            }
            System.out.println("Escriu el cognom del personatge a afegir: ");
            surname = in.readLine();

            intelligence = -1;
            while (intelligence < 0) {
                System.out.println("Introdueix la intel·ligència: ");
                String intelligenceStr = in.readLine();
                if (intelligenceStr != null) {
                    try {
                        intelligence = Integer.parseInt(intelligenceStr);
                    } catch (NumberFormatException nfe) {
                        // Ignore
                    }
                }
            }
            memory = -1;
            while (memory < 0) {
                System.out.println("Introdueix la memòria: ");
                String memoryStr = in.readLine();
                if (memoryStr != null) {
                    try {
                        memory = Integer.parseInt(memoryStr);
                    } catch (NumberFormatException nfe) {
                        // Ignore
                    }
                }
            }
            strength = -1;
            while (strength < 0) {
                System.out.println("Introdueix la força: ");
                String strengthStr = in.readLine();
                if (strengthStr != null) {
                    try {
                        strength = Integer.parseInt(strengthStr);
                    } catch (NumberFormatException nfe) {
                        // Ignore
                    }
                }
            }
            agility = -1;
            while (agility < 0) {
                System.out.println("Introdueix l'agilitat: ");
                String agilityStr = in.readLine();
                if (agilityStr != null) {
                    try {
                        agility = Integer.parseInt(agilityStr);
                    } catch (NumberFormatException nfe) {
                        // Ignore
                    }
                }
            }
            constitution = -1;
            while (constitution < 0) {
                System.out.println("Introdueix la constitució: ");
                String constitutionStr = in.readLine();
                if (constitutionStr != null) {
                    try {
                        constitution = Integer.parseInt(constitutionStr);
                    } catch (NumberFormatException nfe) {
                        // Ignore
                    }
                }
            }

        } catch (IOException ioe) {
            System.err.println("Error llegint la informació del personatge!");
            return "";
        }

		//Format de retorn:
        return "3-" + name +"-"+ surname +"-"+ intelligence +"-"+ memory +"-"+ strength +"-"+ agility +"-"+ constitution;
    }

	private static String deleteCharacter() {
		BufferedReader in = new BufferedReader (new InputStreamReader (System.in));
		System.out.println ("Escriu el nom del personatge a eliminar: ");
		String name = "";
		try {
			name = in.readLine();
		} catch (IOException ioe) {
			System.err.println ("Error llegint el nom!");
		}
		return "4-" + name; //Retornem amb el format 4-<NomPersonatge> (apunt per enviar al server)

	}

	private static void quit() {
		System.out.println("Sortint del client...");
		System.exit(0);
	}
}
