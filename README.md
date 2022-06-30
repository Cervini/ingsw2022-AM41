# Prova Finale Ingegneria del Software 2022
## Gruppo AM41

- ###   10628276   Simone Cervini ([@Cervini](https://github.com/Cervini))<br>simone1.cervini@mail.polimi.it
- ###   10660176    Ludovica Cova ([@LudovicaCova](https://github.com/LudovicaCova))<br>ludovica.cova@mail.polimi.it
- ###   10661539    Davide Fugazza ([@davofuga](https://github.com/davofuga))<br>davide2.fugazza@mail.polimi.it

| Functionality  |                       State                        |
|:---------------|:--------------------------------------------------:|
| Basic rules    | 🟢 |
| Complete rules | 🟢 |
| Socket         | 🟢 |
| GUI            | 🔴 |
| CLI            | 🟢 |
| Multiple games | 🟢 |
| Persistence    | 🔴 |
| 12 Characters  | 🟢 |
| 4 Players game | 🟢 |

🔴
🟢
🟡

* To start the server, choose a port (e.g. 1234) and use the command:

    java -jar Application.jar server chosen port

    e.g. java -jar [file path] server 1234

Server will automatically print its ip address (e.g. Server opened at: 192.168.1.1).

* To start the client, use the command:

    java -jar Application.jar client server ip address chosen port

    e.g. java -jar [file path] client 192.168.1.1 1234