# **Base de dades de personatges en línia**
## Practica 2
### Equipo: **XJ FORCES**

#### Miembros del equipo:
- **Xavier Giralt**
- **Jonas Obando**

## **Descripción de la Práctica**

La práctica consiste en implementar un modelo cliente-servidor donde el servidor es el único que tiene acceso a una base de datos de personajes. El cliente se conecta remotamente al servidor para consultar, añadir o eliminar personajes de la base de datos.

## **Estrategias en la Implementación**

### **Uso de threads y MultiThreading**

En el cliente, todo ocurre dentro del hilo principal (main), que se encarga de establecer la conexión con el servidor (Socket), mostrar un menú y leer la opción del usuario desde consola.  
También enviar la opción al servidor utilizando OutputStream y leer y mostrar la respuesta recibida del servidor usando InputStream.

En el servidor, se usan múltiples hilos para permitir la conexión simultánea de varios clientes:

- El hilo principal (main) abre un ServerSocket, escucha conexiones en el puerto 12345 y lanza un nuevo hilo ClientHandler para cada cliente que se conecta y los agrega a la lista clients.
- Cada instancia de ClientHandler gestiona de forma independiente la comunicación con su respectivo cliente, permitiendo la concurrencia.

### **Comunicación Cliente-Servidor**

- La comunicación se realiza mediante flujos (InputStream y OutputStream), enviando mensajes codificado en bytes.
- Se usa un protocolo simple basado en comandos, donde el cliente envía una opción y el servidor interpreta la acción a realizar.
- Se gestiona adecuadamente la desconexión del cliente cuando envía la opción 5, sin enviar respuesta y cerrando el socket.

### **Control de Recursos y Cierre Seguro**

- Se utiliza try-with-resources y bloques finally para asegurar que los sockets y flujos se cierran correctamente, incluso si ocurre una excepción.
- Cada ClientHandler dispone de un método tancar() para cerrar la conexión del cliente y registrar su desconexión.
- El servidor también permite cerrar todas las conexiones de forma ordenada con el método quit().
- La lista global clients, que almacena todos los ClientHandler activos, está protegida con synchronized para evitar problemas de concurrencia entre hilos al añadir o eliminar clientes también se usa synchronized para los accesos a la BD.

### **Manejo de Excepciones en la Comunicación**

Se usan bloques try-catch en todas las operaciones de red.

Manejo específico de errores:

- ConnectException: Cuando el cliente intenta conectarse y el servidor no está disponible.
- UnknownHostException: Cuando el cliente usa una IP inválida.
- IOException: Errores generales de red o flujo de datos.
- InterruptedException: Maneja la interrupción segura de hilos cuando se quiere detener el servidor.

## **Métodos Usados**

- **Sockets**: Usados para establecer la comunicación de red entre el cliente y el servidor.
- **DataInputStream y DataOutputStream**: Utilizados para leer y escribir mensajes y peticiones de manera eficiente entre cliente y servidor.
- **Hilos (Thread y Runnable)**: Permiten la ejecución concurrente de tareas, como escuchar mensajes del servidor o leer la consola, sin bloquear la ejecución principal.
- **Synchronized**: Usado para evitar problemas de concurrencia, es decir para evitar problemas cuando varios hilos acceden o modifican la misma lista al mismo tiempo, asegurando que el acceso a recursos compartidos sea controlado.
- **Try-catch**: Para manejar excepciones y errores durante las operaciones de red y entrada/salida (IOException, ConnectException), evitando bloqueos inesperados.
- **Finally**: Garantiza que los recursos (sockets, flujos de datos) se cierren correctamente, evitando pérdidas de recursos o conexiones no cerradas.


#### Servidor
```
Servidor engegat, escoltant al port 12345
[Servidor] --> Connexió acceptada de 127.0.0.1
[Servidor] --> Nou client afegit a la llista. Total clients connectats: 1
[Servidor] --> Connexió acceptada de 127.0.0.1
[Servidor] --> Nou client afegit a la llista. Total clients connectats: 2
[Servidor] --> Connexió acceptada de 127.0.0.1
[Servidor] --> Nou client afegit a la llista. Total clients connectats: 3
Rebut: 1
[CLIENT #1] --> Accedint a la base de dades per llistar noms complets.
Rebut: 2-yumi
[CLIENT #2] --> Accedint a la base de dades per obtenir informació d'un personatge.
[Servidor] --> Connexió acceptada de 127.0.0.1
[Servidor] --> Nou client afegit a la llista. Total clients connectats: 4
Rebut: 3-marco-polo-100-200-150-220-50
[CLIENT #3] --> Afegint un nou personatge a la base de dades.
Rebut: 4-marco
[CLIENT #4] --> Eliminant un personatge de la base de dades.
[Servidor] --> Connexió acceptada de 127.0.0.1
[Servidor] --> Nou client afegit a la llista. Total clients connectats: 5
Rebut: 5
[CLIENT #5] --> Client ha demanat desconnexió.
```
#### Cliente 1 - opcion 1
```
Menú d'opcions del Client:
1 - Llista els noms complets dels personatges.
2 - Obté la informació d'un personatge.
3 - Afegeix un personatge.
4 - Elimina un personatge.
5 - Sortir.
Escull una opció: 1
[Servidor]:
Vin Venture
Cephandrius Maxtori
Steris Harms
Warbreaker the Peaceful
Waxillium Ladrian
Raoden 
Shallan Davar
Khrissalla 
Vivenna 
Kaladin Stormblessed
Sylphrena 
Kelsier 
Glorf 
Mervin 
Yumi 
Sigzil 
Spensa Nightshade
```

#### Cliente 2 - opcion 2
```
Menú d'opcions del Client:
1 - Llista els noms complets dels personatges.
2 - Obté la informació d'un personatge.
3 - Afegeix un personatge.
4 - Elimina un personatge.
5 - Sortir.
Escull una opció: 2
Escriu el nom del personatge: 
yumi
[Servidor]:
Yumi 
Intel·ligència: 125
Memòria:        125
Força:          125
Agilitat:       110
Constitució:    115

```
#### Cliente 3 - opcion 3
```
Menú d'opcions del Client:
1 - Llista els noms complets dels personatges.
2 - Obté la informació d'un personatge.
3 - Afegeix un personatge.
4 - Elimina un personatge.
5 - Sortir.
Escull una opció: 3
Escriu el nom del personatge a afegir: 
marco
Escriu el cognom del personatge a afegir: 
polo
Introdueix la intel·ligència: 
100
Introdueix la memòria: 
200
Introdueix la força: 
150
Introdueix l'agilitat: 
220
Introdueix la constitució: 
50
[Servidor]:
OK, personatge afegit correctament

```
#### Cliente 4 - opcion 4
```
Menú d'opcions del Client:
1 - Llista els noms complets dels personatges.
2 - Obté la informació d'un personatge.
3 - Afegeix un personatge.
4 - Elimina un personatge.
5 - Sortir.
Escull una opció: 4
Escriu el nom del personatge a eliminar: 
marco
[Servidor]:
OK, personatge marco polo eliminat correctament

```
#### Cliente 5 - opcion 5
```
Menú d'opcions del Client:
1 - Llista els noms complets dels personatges.
2 - Obté la informació d'un personatge.
3 - Afegeix un personatge.
4 - Elimina un personatge.
5 - Sortir.
Escull una opció: 5
Sortint del client...

Process finished with exit code 0
```
