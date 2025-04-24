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
						//FALTA
						//Enviem la resposta de la funció del client.
						//Format:2-<NomPersonatge> (Despres el servidor ja tallarà cada part del missatge)
						out.write(infoFromOneCharacter().getBytes());

						break;
					case 3:
						//FALTA
						out.write("3".getBytes());
						break;
					case 4:
						//FALTA
						out.write("4".getBytes());
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



	private static void quit() {
		System.out.println("Sortint del client...");
		System.exit(0);
	}
}
