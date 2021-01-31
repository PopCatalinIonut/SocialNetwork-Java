package main.socialnetwork.ui;

import main.socialnetwork.domain.validators.ValidationException;
import main.socialnetwork.repository.RepositoryException;
import main.socialnetwork.service.ServiceException;
import main.socialnetwork.service.SocialNetworkService;
import main.socialnetwork.utils.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

public class Console {
    private SocialNetworkService service;
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    public Console(SocialNetworkService service) {
        this.service = service;
    }

    public void run() {
        int option=0;
        boolean running = true;

        while(running){
            System.out.println("1. Afisati utilizatorii");
            System.out.println("2. Afisati prieteniile");
            System.out.println("3. Adaugati un utilizator");
            System.out.println("4. Stergeti un utilizator");
            System.out.println("5. Adaugati o prietenie");
            System.out.println("6. Stergeti o prietenie");
            System.out.println("7. Afisati numarul de comunitati");
            System.out.println("8. Afisati conversatiile a doi utilizatori");
            System.out.println("9. Raspundeti unei cereri de prietenie");
            System.out.println("10. Raspundeti la un mesaj");
            System.out.println("0. Iesire");
            System.out.println("Alegeti optiunea: ");
            try {
                option = Integer.parseInt(in.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println();
            try {
                switch (option) {
                    case 1: showUsers(); break;
                    case 2: showFriendships(); break;
                    case 3: addUser(); break;
                    case 4: removeUser(); break;
                    case 5: addFriendship(); break;
                    case 6: removeFrienship(); break;
                    case 7: countCommunities(); break;
                    case 8: showConvos(); break;
                    case 9: respondToRequest(); break;
                    case 10: respondToMessage(); break;
                    case -1: showAllRequests(); break;
                    case -2: showAllMessages(); break;
                    case 0: running = false; break;
                    default: System.out.println("Optiune invalida!");
                }
            } catch (IOException | NumberFormatException | DateTimeParseException | ValidationException | RepositoryException | ServiceException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void respondToMessage() throws IOException {
        System.out.print("Introduceti ID-ul mesajului la care doriti sa raspundeti: ");
        long IDmsg=0;
        try {
            IDmsg = Integer.parseInt(in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print("Introduceti ID-ul user-ului: ");
        long Iduser=0;
        try {
            Iduser = Integer.parseInt(in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print("Introduceti mesajul: ");
        String message= in.readLine();
        try{
            this.service.respondToMessage(IDmsg,Iduser,message);

        }catch (ValidationException | RepositoryException | ServiceException e) {
            System.out.println(e.getMessage());
        }
    }
    public void showAllMessages(){ service.getAllMessages().forEach(System.out::println);}
    public void showAllRequests(){
        service.getAllRequests().forEach(System.out::println);
    }
    public void respondToRequest() throws  IOException{

        System.out.print("Introduceti ID-ul cererii la care doriti sa raspundeti: ");
        long ID=0;
        try {
            ID = Integer.parseInt(in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print("Introduceti noul status: ");
        String status=in.readLine();
        try{
        this.service.respondToRequest(ID,status);
            System.out.println("Cerere actualizata cu succes!");
        }catch (ValidationException | RepositoryException | ServiceException e) {
            System.out.println(e.getMessage());
        }
    }
    public void showConvos() throws IOException {
        System.out.print("Introduceti numele primului utilizator: ");
        String firstName1 = in.readLine();
        System.out.print("Introduceti prenumele primului utilizator: ");
        String lastName1= in.readLine();
        System.out.print("Introduceti numele celui de-al doilea utilizator: ");
        String firstName2=in.readLine();
        System.out.print("Introduceti prenumele celui de-al doilea utilizator: ");
        String lastName2=in.readLine();
        try{
           // List<String> msgList=service.getConvos(firstName1,lastName1,firstName2,lastName2);
          //  msgList.forEach(System.out::println);
        } catch (ValidationException | RepositoryException | ServiceException e) {
            System.out.println(e.getMessage());
        }

    }
    public void showFriendships(){

        System.out.println("1. Afisati toate prieteniile\n2. Afisati toti prietenii unui utilizator\n3. Afisati toti prietenii unui utilizator dintr-o anumita luna a anului\n");
        int option=0;
        try {
            option = Integer.parseInt(in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try{
        switch (option){
            case 1: showAllFriendships(); break;
            case 2: showFriendshipsByName(); break;
            case 3: showFriendshipsByDate(); break;
            default: System.out.println("Optiune invalida");
        }}catch (IOException | RepositoryException | ServiceException e) {
            System.out.println(e.getMessage());
        }

    }

    public void showFriendshipsByDate() throws  IOException{
        System.out.print("Introduceti numele utilizatorului: ");
        String firstName = in.readLine();
        System.out.print("Introduceti prenumele utilizatorului: ");
        String lastName=in.readLine();
        System.out.print("Introduceti luna: ");
        int month=0;
        try {
            month = Integer.parseInt(in.readLine());
            service.getFriendList(firstName,lastName,month).forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void showFriendshipsByName ()throws IOException{

        System.out.print("Introduceti numele utilizatorului: ");
        String firstName = in.readLine();
        System.out.print("Introduceti prenumele utilizatorului: ");
        String lastName=in.readLine();
        service.getFriendList(firstName,lastName).forEach(System.out::println);
    }
    public void showUsers() {
        service.getAllUsers().forEach(System.out::println);
        System.out.println();
    }

    public void showAllFriendships() {
        service.getAllFriendships().forEach(System.out::println);
        System.out.println();
    }

    public void addUser() throws IOException {
        System.out.print("Introduceti numele: ");
        String firstName = in.readLine();
        System.out.print("Introduceti prenumele: ");
        String lastName = in.readLine();
        service.addUser(firstName, lastName);
        System.out.println("Ati introdus utilizatorul cu succes!");
    }

    public void removeUser() throws IOException {
        System.out.println("1. Stergeti dupa nume si prenume\n2. Stergeti dupa ID\n");
        int option = Integer.parseInt(in.readLine());
        switch (option) {
            case 1:
                System.out.print("Introduceti numele: ");
                String firstName = in.readLine();
                System.out.print("Introduceti prenumele ");
                String lastName = in.readLine();
                service.removeUser(firstName, lastName);
                break;
            case 2:
                System.out.print("Introduceti ID-ul: ");
                Long id = Long.parseLong(in.readLine());
                service.removeUser(id);
                break;
            default:
                System.out.println("Optiune invalida!");
        }
        System.out.println("Ati sters utilizatorul cu succes!");
    }

    public void addFriendship() throws IOException {
        System.out.println("1. Adaugati dupa nume\n2. Adaugati dupa ID:\n");
        int option = Integer.parseInt(in.readLine());
        LocalDateTime date;
        switch (option) {
            case 1:
                System.out.println("Introduceti datele primului utilizator: ");
                System.out.print("Nume: ");
                String firstName1 = in.readLine();
                System.out.print("Prenume: ");
                String lastName1 = in.readLine();
                System.out.println("Introduceti datele celui de-al doilea utilizator: ");
                System.out.print("Nume: ");
                String firstName2 = in.readLine();
                System.out.print("Prenume: ");
                String lastName2 = in.readLine();
                System.out.print("Data: ");
                date = LocalDateTime.parse(in.readLine(), Constants.DATE_TIME_FORMATTER);
                service.addFriendship(firstName1, lastName1,  firstName2, lastName2, date);
                break;
            case 2:
                System.out.print("Introduceti ID-ul primului utilizator: ");
                Long id1 = Long.parseLong(in.readLine());
                System.out.println("\nIntroduceti ID-ul celui de-al doilea utilizator: ");
                Long id2 = Long.parseLong(in.readLine());
                System.out.print("\nData: ");
                date = LocalDateTime.parse(in.readLine(), Constants.DATE_TIME_FORMATTER);
                service.addFriendship(id1, id2, date);
                break;
            default:
                System.out.println("Optiune invalida!");
        }
        System.out.println("Ati adaugat prietenia cu succes!");
    }

    public void removeFrienship() throws IOException {

        System.out.println("Introduceti datele primului utilizator: ");
        System.out.print("Nume: ");
        String firstName1 = in.readLine();
        System.out.print("Prenume: ");
        String lastName1 = in.readLine();
        System.out.println("Introduceti datele celui de-al doilea utilizator: ");
        System.out.print("Nume: ");
        String firstName2 = in.readLine();
        System.out.print("Prenume: ");
        String lastName2 = in.readLine();
        service.removeFriendship(firstName1, lastName1,  firstName2, lastName2);
        System.out.println("Ati sters prietenia cu succes!");
    }

    void countCommunities() {
        System.out.println("Numarul de comunitati din retea este: " + service.countCommunities());
    }
}
