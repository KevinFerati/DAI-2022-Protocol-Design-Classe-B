package ch.heigvd.api.calc;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Calculator worker implementation
 */
public class ServerWorker implements Runnable {

    private final static Logger LOG = Logger.getLogger(ServerWorker.class.getName());
    private BufferedReader reader = null;
    private BufferedWriter writer = null;
    private Socket client = null;

    /**
     * Instantiation of a new worker mapped to a socket
     *
     * @param clientSocket connected to worker
     */
    public ServerWorker(Socket clientSocket) throws IOException {
        // Log output on a single line
        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s%6$s%n");
        client = clientSocket;
        reader = new BufferedReader(new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8));
        writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8));
    }

    /**
     * Run method of the thread.
     */
    @Override
    public void run() {

        /* TODO: implement the handling of a client connection according to the specification.
         *   The server has to do the following:
         *   - initialize the dialog according to the specification (for example send the list
         *     of possible commands)
         *   - In a loop:
         *     - Read a message from the input stream (using BufferedReader.readLine)
         *     - Handle the message
         *     - Send to result to the client
         */

        try {

            while (!client.isClosed()) {
                String message = reader.readLine();
                if (message.equals("BYE")) {
                    send("BYE BYE");
                    client.close();
                }

                send(message);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getMessage());
        } finally {
            closeAll();
        }
    }

    private void closeAll() {
        try {
            writer.close();
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Error while closing the writer : " +  e.getMessage());
        }

        try {
            reader.close();
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Error while closing the reader : " +  e.getMessage());
        }

        try {
            client.close();
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Error while closing the client : " +  e.getMessage());
        }
    }

    private void send(String s) throws IOException {
        writer.write("BYE BYE");
        writer.flush();
    }
}