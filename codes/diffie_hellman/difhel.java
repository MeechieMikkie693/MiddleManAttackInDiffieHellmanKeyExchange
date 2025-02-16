package diffie_hellman;
import java.util.*;
public class difhel{

    //predefined alpha and q values, and are publicly known 
    public static int q = 353, alpha = 3;
    public static int YAlice, YBob, YDarth;
    public static int XAlice, XBob, XDarth;

    public static int fastExponen(int base, int pow, int n){

        //Convert the power into the binary form
         String binaryString = Integer.toBinaryString(pow);

         int f = 1;
         for(int i = 0; i < binaryString.length(); i++){
            f = (f * f) % n;
            if(binaryString.charAt(i) == '1'){
                f = (f * base) % n;
            }
         }//outer for end

        //System.out.println("The value of the modular exponentiation is: " + f);
        return f;

    }//method end

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);

        //Get the public key values of the three people from the user
        System.out.println("Enter the private key values of Alice, Bob and Darth Vader: ");
        XAlice = sc.nextInt();
        XBob = sc.nextInt();
        XDarth = sc.nextInt();

        //Calculate the public key values of Alice, Bob and Darth Vader
        YAlice = fastExponen(alpha, XAlice, q);
        YBob = fastExponen(alpha, XBob, q);
        YDarth = fastExponen(alpha, XDarth, q);

        //Man-In-Middle Attack done by Darth


        sc.close();
    }//main end
}//class end