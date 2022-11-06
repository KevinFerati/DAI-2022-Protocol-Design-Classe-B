package ch.heigvd.api.calc;

import common.*;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Calculator client implementation
 */
public class Client {

    private static final Logger LOG = Logger.getLogger(Client.class.getName());

    private static Socket clientSocket = null;
    private static BufferedReader reader = null;
    private static BufferedWriter writer = null;
    /**
     * Main function to run client
     *
     * @param args no args required
     */
    public static void main(String[] args) {

        // Log output on a single line
        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s%6$s%n");
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        /* TODO: Implement the client here, according to your specification
         *   The client has to do the following:
         *   - connect to the server
         *   - initialize the dialog with the server according to your specification
         *   - In a loop:
         *     - read the command from the user on stdin (already created)
         *     - send the command to the server
         *     - read the response line from the server (using BufferedReader.readLine)
         */
        try {
            // connect to the server
            clientSocket = new Socket("127.0.0.1", Constants.PORT);
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
            writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8));

            // explain available operations
            initUserInput();
            String operation = "";

            do {
                // get operation from user and send it
                operation = getUserCommand(stdin);
                System.out.printf("you sent : %s \n", operation);
                writer.write(operation);
                writer.flush();
                // receive server response and print it
                String response = reader.readLine();
                System.out.printf("server responded with : %s \n", response);

            } while(!operation.equals(Operator.STP.toString()));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch(RuntimeException e) {
            System.out.println("invalid usage : " + e.getMessage());
        }
        finally {
            closeAll();
        }
    }

    private static void closeAll() {
        try {
            if (reader != null) {
                reader.close();
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Error while closing the writer : " + e.toString(), e);
        }
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Error while closing the reader : " + e.toString(), e);
        }
        try {
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Error while closing the socket : " + e.toString(), e);
        }
    }

    /**
     * explain available operations to the user before asking them
     */
    private static void initUserInput() {
        System.out.println("Deep Thought is now ready to answer your question");
        System.out.println("operators available :");
        for (Operator op : Operator.values()) {
            System.out.printf("%s ", op);
        }
        System.out.print("\n");
    }

    /**
     * get operation from the user
     * @param stdin input stream
     * @return operation ready to be sent to the server
     * @throws IOException during input stream manipulation
     */
    private static String getUserCommand(BufferedReader stdin) throws IOException {
        String[] elements;
        System.out.println("usage : <number1> <operator> <number2>");
        System.out.printf("%s to quit \n", Operator.STP);
        System.out.print("%%");
        elements = stdin.readLine().split(" ");
        if(elements.length == 1 && elements[0].equals(Operator.STP.toString())) {
            return elements[0] + "\n";
        }
        if(elements.length < 3) {
            throw new RuntimeException("arguments mising");
        }
        return buildMessage(elements[0], elements[1], elements[2]);
    }

    /**
     * format operation to send it to the server
     * @param num1 1st number to use
     * @param op operator to apply to the numbers
     * @param num2 2nd number to use
     * @return formatted operation
     */
    private static String buildMessage(String num1, String op, String num2) {
        try {
            Double.parseDouble(num1);
            Double.parseDouble(num2);
        } catch(NumberFormatException e){
            throw new RuntimeException("wrong type of arguments");
        }
        return String.format("%s;%s;%s\n", num1, op, num2);
    }
}
