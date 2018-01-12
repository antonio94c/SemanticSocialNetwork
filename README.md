# Semantic Harmony Social Network

## Descrizione del progetto
Semantic Harmony Social Network è un social network basato sull’interesse degli utenti che sfrutta una rete Peer-to-Peer. 
Il sistema colleziona il profilo degli utenti e automaticamente crea amicizie in accordo ad una strategia di matching basata sulle risposte ad un questionario che sarà generato dal primo Peer che accede alla rete e a cui saranno sottoposti, all’accesso, tutti i Peer.
Ogni Peer sarà identificato da un nickname ed ognuno conoscerà la lista di Peer con cui condivide una relazione di amicizia.


## Funzionalità del sistema
La classe SemanticHarmonySocialNetworkImpl prevede i 4 metodi dell'interfaccia SemanticHarmonySocialNetwork e altri due metodi public da noi aggiunti.
Il primo di questi metodi è putUserProfileQuestions che aggiunge una nuova room alla rete creando delle domande sottoposte a tutti gli altri nodi intenzionati ad entrare nella rete. Questo metodo viene invocato solo una volta dal primo nodo che accede.
Il secondo di questi metodi è leaveNetwork che invocato da un nodo richiama il metodo leaveRooms che lo rimuove da ogni room alla quale esso è associato e lo esclude dalla rete.
I metodi dell'interfaccia lavorano come segue:
* getUserProfileQuestions preleva e restituisce la lista delle domande dalla room corrispondente.
* createAuserProfileKey prende in input la lista delle risposte e crea la chiave del nodo concatenando 1 se la risposta è "si" e 0 se è "no".
* join è il metodo che consente al nodo di associarsi ad una room. Il metodo invoca generateRooms che restituisce la lista delle room in cui il nodo può associarsi in base alla sua chiave (la sequenza di bit generata dalle sue risposte alle domande) e alla stringa binaria che identifica la room. Se la chiave del nodo differisce al più di un bit dalla sequenza che identifica la room, il nodo viene ad essa associato, altrimenti viene prima generata una nuova room con la sua chiave e poi associato ad essa. Ad esempio un nodo che ha come chiave 111 verrà associato alle room 111,011,101,110.
* getFriends restituisce la lista dei nodi che sono associati ad almeno una room in comune al nodo stesso e, quindi, gli amici del nodo.


## JUnit Test
Per testare la rete creiamo 5 peer, il primo crea le domande e le aggiunge alla rete, successivamente risponde alle domande, genera la chiave tramite le risposte e si aggiunge alle room usando join. Gli altri 4 peer eseguono le stesse operazioni tranne la creazione delle domande. Viene controllato se gli amici di ogni peer sono quelli che ci aspettiamo, successivamente viene fatta la leave da parte del peer0 e vengono controllati di nuovo gli amici di ogni peer per controllare se il peer0 è presente da qualche parte.

## Test
Oltre a testare il sistema con JUnit abbiamo sviluppato una classe in cui generiamo 4 nodi e vengono prese in input le domande dal primo nodo. Successivamente vengono proposte le domande a ogni nodo che fornisce le risposte grazie alle quali stabilisce le amicizie con i rispettivi metodi. Infine vengono stampate le amicizie di ogni nodo prima e dopo aver fatto effettuare al peer1 la leave dal sistema.


## Guida all’installazione
1. Dalla console digitare git clone https://github.com/antonio94c/SemanticSocialNetwork
2. Avviare l’applicazione Eclipse
3. Dal Menù di Eclipse, import->Maven->Existing Maven Projects
4. Fare clic destro sul progetto appena importato
5. Selezionare la voce "Build Path"
6. Selezionare "add Libraries"
7. Selezionare "JUnit", cliccare su next
8. Scegliere la versione 4 di JUnit, infine cliccare su Finish
9. Dal Menù di Eclipse Run->Run

## Contenuto del pacchetto HarmonySocialNetwork
* README.md
* pom.xml
* src/main
* src/main/java
* src/main/java/it
* src/main/java/it/isislab
* src/main/java/it/isislab/p2p
* src/main/java/it/isislab/p2p/SemanticSocialNetwork
* src/main/java/it/isislab/p2p/SemanticSocialNetwork/MessageListener.java
* src/main/java/it/isislab/p2p/SemanticSocialNetwork/Peer_nick_address.java
* src/main/java/it/isislab/p2p/SemanticSocialNetwork/QuestionsInit.java
* src/main/java/it/isislab/p2p/SemanticSocialNetwork/QuestionInitImpl.java
* src/main/java/it/isislab/p2p/SemanticSocialNetwork/SemanticHarmonySocialNetwork.java
* src/main/java/it/isislab/p2p/SemanticSocialNetwork/SemanticHarmonySocialNetworkImpl.java
* src/test/java/it/isislab/p2p/SemanticSocialNetwork/TestSemanticHarmonySocialNetworkImpl.java
* src/test/java/it/isislab/p2p/SemanticSocialNetwork/JunitTest.java


## Informazioni riguardo al tipo di licenza e il copyright

The MIT License (MIT)

Copyright (c) 2018 Ivan Rizzo, Antonio Calabria, Lorenzo Vitale

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
Software is furnished to do so, subject to the following conditions:
The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
