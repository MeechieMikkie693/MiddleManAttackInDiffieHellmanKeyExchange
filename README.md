# MiddleManAttackInDiffieHellmanKeyExchange
This is a representation of Middleman attack in the Diffie-Hellman Key Exchange Algorithm. Has a lot of scope for improvement in the coding part

(I have referred to the following diagram for coding)

![image](https://github.com/user-attachments/assets/7b8c76df-b77a-49b7-b4a6-dd991513b86b)

NOTE: I am not able to properly manage communication between the three terminals when it comes to message communication. Improvement required..

Use the following commandLines to execute the code(Assuming you are working on Windows OS)

1. Navigate to your project folder using cd command and "del /S *.class" to delete all the .class files
2. "javac -d . destools/trial.java" and "javac -d . des_cbc/descbc.java" for the DES algorithm using CBC encryption mode which is used to encrypt and decrypt the messages in this keyExchange algorithm.
3. "javac -d . diffie_hellman/difhel.java" to have the compiled class file in the same folder
4. "javac server.java mitm.java client.java"
5. first run the server, then the mitm and then the client using java command (java sourcefilename)
