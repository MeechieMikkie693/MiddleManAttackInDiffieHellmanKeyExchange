package DES_CBC;
//import java.util.*;
import destools.trial;

public class descbc {

    public static String iv = "AAAAAAAA";
    //public static String IV = trial.initialTextMut(iv);
    //public static List<String> cipherTexts = new ArrayList<>();
    //ensure that the plaintext charcater length is a multiple of 8(else padd it)
        public static String ensureMultipleOf8(String text){
        int length = text.length();
        int paddingNeeded = (8-(length%8)) % 8; //calculate the padding needed
        return text + "X".repeat(paddingNeeded);
    }//method end 

    public static String cbc_Encrypt(String plaintext, String masterkey){
         plaintext = ensureMultipleOf8(plaintext);
         String operand = trial.initialTextMut(iv);
         StringBuilder cipherTexts = new StringBuilder();

        //Extract 8 bit character from the plain text 
        for(int i = 0; i < plaintext.length(); i += 8){
          
            //Extract 8 bit character from the plain text
            String text = plaintext.substring(i, i+8);
            //System.out.println("Extracted plain text for encryption is: " + text);

            //Perform the xor operation with the plaintext block and iv(initially, will be later replaced with cipher text from previous encryption)
            //First convert them into binary string using initalTextMut and then apply the xorStrings...
            String sxor = trial.xorStrings(trial.initialTextMut(text), operand);

            //Perform encryption on the XOR-ed output
            String cipher = trial.encryption(sxor, masterkey);
            //Binary strings(encrpyted text) are stored in the cipherTexts list(so must by of 64 bit sized blocks of encrypted (binary string) message)
            cipherTexts.append(cipher);

            //The second string involved in the XOR operatioon with the plain text
            operand = cipher;
        }//for end

         return cipherTexts.toString();
    }//method end

     public static String cbc_decrypt(String ciphertext, String masterKey){

        String operand = trial.initialTextMut(iv);
        StringBuilder plainTexts = new StringBuilder();

        //Extracting 64 bits of the ciphered text or rather 8 character length of cipher text block
        //64 bit cause the cipher text here in the function is of binaryString form
        for(int i = 0; i < ciphertext.length(); i += 64){
            String cipher = ciphertext.substring(i, i+64);

            //decrypt the ciphered text
            String output = trial.decryption(cipher, masterKey);

            //Perform the xor operation with the cipher text and iv(initially, will be later replaced with cipher text from previous decryption)
            String text = trial.xorStrings(operand, output);

            plainTexts.append(trial.toActualString(text));
            operand = cipher;

        }//for end
        return plainTexts.toString();
     }//method end

}//class end

