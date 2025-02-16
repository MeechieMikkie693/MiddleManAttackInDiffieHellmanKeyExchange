//import java.util.*;
import java.io.*;
import java.net.*;
//import destools.trial;
import DES_CBC.descbc;
import diffie_hellman.difhel;

//Consider this to be Darth
public class mitm {
    
    //predefined alpha and q values, and are publicly known 
    public static int q = 353, alpha = 3;

    //Public key and private key of Alice user
    public static int YDA, YDB, XDA=97, XDB=233;

        public static void main(String[] args){
        int portAlice = 5000, portBob = 5001;

        //Compute the pseudo public keys of Alice and Bob
        YDA = difhel.fastExponen(alpha, XDA, q);
        YDB = difhel.fastExponen(alpha, XDB, q);

        try{
            ServerSocket serverSocketAlice = new ServerSocket(portAlice);
            System.out.println("MITM listening on port 5000....");

            Socket socketAlice = serverSocketAlice.accept();
            System.out.println("Connection from Alice established....");

            Socket serverSocketBob = new Socket("localhost", portBob);
            System.out.println("Connected to Server.");

            //Create input and output Streams
            BufferedReader Alicein = new BufferedReader(new InputStreamReader(socketAlice.getInputStream()));
            PrintWriter Aliceout = new PrintWriter(socketAlice.getOutputStream(), true);

            BufferedReader Bobin = new BufferedReader(new InputStreamReader(serverSocketBob.getInputStream()));
            PrintWriter Bobout = new PrintWriter(serverSocketBob.getOutputStream(), true);

            //Read the public key sent from the Alice Server
            String YAlice = Alicein.readLine();
            System.out.println("Public key of Alice(recieved): " + YAlice);

            //Send the pseudo public key of bob back to alice
            System.out.println("Pseudo public key of Bob(YDB) has been computed and sent to Alice...");
            Aliceout.println(YDB);

            //Compute the common secret key between alice and Darth
            int K2 = difhel.fastExponen(Integer.parseInt(YAlice), XDB, q);
            String masterKey2 = Integer.toBinaryString(K2);
            masterKey2 = String.format("%8s", masterKey2).replace(' ', '0').substring(0, 8);
            System.out.println("Master key(wrt Alice): " + masterKey2);
            //trial.keyGeneration(masterKey2);
            
            //Send the psuedo public key of Alice to Bob Server
            System.out.println("Pseudo public key of Alice(YDA) has been computed and sent to Bob...");
            Bobout.println(YDA);

            //Get the public key of Bob from the Bob server
            String YBob = Bobin.readLine();
            System.out.println("Public key of Bob(recieved): " + YBob);

            //Compute the common secret key between Bob and Darth
            int K1 = difhel.fastExponen(Integer.parseInt(YBob), XDA, q);
            String masterKey1 = Integer.toBinaryString(K1);
            masterKey1 = String.format("%8s", masterKey1).replace(' ', '0').substring(0, 8);
            System.out.println("Master key(wrt Bob): " + masterKey1);
            //trial.keyGeneration(masterKey1);

            String message;
            while((message = Alicein.readLine()) != null){
                System.out.println("Message from Alice: " + descbc.cbc_decrypt(message, masterKey2)); //decrypt the message from alice 

                //Encrpyt the decrypted message sent from alice using the masterkey1 and send it to bob
                String bobmessage = descbc.cbc_Encrypt(descbc.cbc_decrypt(message, masterKey2), masterKey1);
                Bobout.println(bobmessage);
                System.out.println("(Encrpypted) Message sent to Bob: " + bobmessage);

                //Recive the encrypted message from bob and decrypt it using masterkey1
                //String fromBobServer = descbc.cbc_decrypt(Bobin.readLine(), masterKey1);
                String fromBobServer = Bobin.readLine();
                if(fromBobServer == null){
                    System.out.println("Bob closed the connection. Exiting MITM.");
                    break;
                }
                String decryptedFromBob = descbc.cbc_decrypt(fromBobServer, masterKey1);
                System.out.println("(Decrypted) Message from Bob: " + decryptedFromBob);

                //Send the encrypted response(using masterKey2) to alice and wait for her response
                Aliceout.println(descbc.cbc_Encrypt(decryptedFromBob, masterKey2));
                System.out.println("Message from Bob forwarded to Alice: " + descbc.cbc_Encrypt(decryptedFromBob, masterKey2));


            }//while end

            socketAlice.close();
            serverSocketBob.close();
            serverSocketAlice.close();

        }catch(IOException e){
            e.printStackTrace();
        }//try-catch end

        }//main end

            
}//class end

