import java.util.*;
import java.io.*;
import java.net.*;
//import destools.trial;
import DES_CBC.descbc;
import diffie_hellman.difhel;

//Consider this to be Alice
public class client {

    //predefined alpha and q values, and are publicly known 
    public static int q = 353, alpha = 3;

    //Public key and private key of Alice user
    public static int YAlice, XAlice;

    public static void main(String[] args){
       String serverAddress = "localhost";
       int port = 5000;

       try(Socket socket = new Socket(serverAddress, port)){

        //create input and output streams
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        //Send message to server(but it is actually Darth!)
        Scanner sc = new Scanner(System.in);
        System.out.println("Connected to the server. Please type as required...");

        //Ask the user to enter the private key
        System.out.println("Enter your(Alice)private key: ");
        XAlice = sc.nextInt();

        //Compute the public key of Alice
        YAlice = difhel.fastExponen(alpha, XAlice, q);
        out.println(YAlice);

        //Recives response(psuedo public key of Bob) from the MITM Server
        String YDB = in.readLine();
        System.out.println("Public key of Bob: " + YDB);

        //Compute the common secret key
        int K2 = difhel.fastExponen(Integer.parseInt(YDB), XAlice, q);

        //Take the starting 8 bits of the secret key
        String masterKey = Integer.toBinaryString(K2);
        masterKey = String.format("%8s", masterKey).replace(' ', '0').substring(0, 8);

        System.out.println("Master key: " + masterKey);

        System.out.println("Please enter a message to be sent to the server, or type 'exit' to quit...");

        String message;
        while(true){
            System.out.print("Alice: ");
            message = sc.nextLine();
            if("exit".equalsIgnoreCase(message)) break;

            //Send the ciphered text to the pseudo server
            out.println(descbc.cbc_Encrypt(message, masterKey));//Send message to the psuedo server
            String response = in.readLine(); //Receive response from the psuedo server
            System.out.println("Decryption of the text(sent from server)...");
            System.out.println("\nDecrypted text: " + descbc.cbc_decrypt(response, masterKey));

        }//while end

        sc.close();

       } catch(IOException e){
        e.printStackTrace();
       }//try catch end

    }//main end
}//class end

