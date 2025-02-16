import java.io.*;
import java.net.*;
import java.util.*;

import DES_CBC.descbc;
//import destools.trial;
import diffie_hellman.difhel;

//This is the Bob side server
public class server {

    //predefined alpha and q values, and are publicly known 
    public static int q = 353, alpha = 3;

    //Public key and private key of Alice user
    public static int YBob, XBob;
    
    public static void main(String[] args){
        int port = 5001; //port number where server listens

        try(ServerSocket serverSocket = new ServerSocket(port)){
            System.out.println("Bob server is listening on port "+port);

            //Wait for client connection(Actually it is that Bloody Darth!!)
            Socket socket = serverSocket.accept();
            System.out.println("Client(MITM) is connected");

            //Create input and output Streams
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            //Get the pseudo public key of Alice from MITM
            String YDA = in.readLine();
            System.out.println("Pseudo public key of Alice(YDA) has been received from MITM...");

        //Ask the user to enter the private key
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your(Bob)private key: ");
        XBob = sc.nextInt();

        //Compute the public key of Bob
        YBob = difhel.fastExponen(alpha, XBob, q);
        out.println(YBob);

        System.out.println("Sharing Bob Public key with pseudo Alice(MITM server)...");

        //Send the public key of Bob to the MITM server
        out.println(YBob);
        

        //Compute the common secret key between Bob and Darth
        int K1 = difhel.fastExponen(Integer.parseInt(YDA), XBob, q);

        //Take the starting 8 bits of the secret key
        String masterKey = Integer.toBinaryString(K1);
        masterKey = String.format("%8s", masterKey).replace(' ', '0').substring(0, 8);

        System.out.println("Master key: " + masterKey);

        String message;
        while((message = in.readLine()) != null){
            System.out.println("Message from MITM server: " + descbc.cbc_decrypt(message, masterKey));

            //Send response to MITM client
            System.out.println("Enter a response to send to the pseudo client(MITM): ");
            String response = sc.nextLine();
            out.println(descbc.cbc_Encrypt(response, masterKey));

        }//while end


            sc.close();
            in.close();
            out.close();
            socket.close();
        } catch (IOException e){
            e.printStackTrace();
        }//try catch end

    }//main end
    
}//class end

