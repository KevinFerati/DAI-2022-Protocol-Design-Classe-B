package ch.heigvd.api.calc;

import common.Constants;
import common.Errors;
import common.Operator;
import common.Results;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
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
        try {

            while (!client.isClosed()) {
                String message = reader.readLine();
                LOG.info("Received message : " + message + " of size " + message.length());
                if (message.trim().equals(Operator.STP.getOp())) {
                    LOG.info("Closing connection ...");
                    client.close();
                    break;
                }
                String[] parts = message.split(";");

                // Check the errors
                if (parts.length != 3) {
                    sendError(Errors.MESSAGE_MALFORMED);
                    continue;
                }
                String firstOperand = parts[0], operator = parts[1], secondOperand = parts[2];

                // Check if the operator is valid
                if (Operator.STP.equals(operator)
                        || Arrays.stream(Operator.values()).noneMatch(availableOp -> availableOp.equals(operator))) {
                    sendError(Errors.UNKNOWN_OPERATOR);
                    continue;
                }

                // Check if the operands are valid
                double firstOperandDbl, secondOperandDouble;
                try {
                    firstOperandDbl = Double.parseDouble(firstOperand);
                    secondOperandDouble = Double.parseDouble(secondOperand);
                } catch (NumberFormatException e) {
                    sendError(Errors.OPERAND_NOT_ACCEPTED);
                    continue;
                }
                // Check divide by zero
                if (secondOperandDouble == 0 && Operator.DIV.equals(operator)) {
                    sendError(Errors.DIVIDE_BY_ZERO);
                    continue;
                }

                // From here, everything is fine

                if (Operator.SUB.equals(operator)) {
                    sendResult(firstOperandDbl - secondOperandDouble);
                    continue;
                }

                if (Operator.DIV.equals(operator)) {
                    sendResult(firstOperandDbl / secondOperandDouble);
                    continue;
                }

                if (Operator.ADD.equals(operator)) {
                    sendResult(firstOperandDbl + secondOperandDouble);
                    continue;
                }

                sendResult(firstOperandDbl * secondOperandDouble);
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
            if (!client.isClosed())
                client.close();
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Error while closing the client : " +  e.getMessage());
        }
    }

    private void send(String s) throws IOException {
        writer.write(s + "\n");
        writer.flush();
    }

    private void sendError(int err) throws IOException {
        send(Results.ER.getResult() + Constants.SEPARATOR + err);
    }
    private void sendResult(double res) throws IOException {
        send(Results.OK.getResult() + Constants.SEPARATOR + res);
    }
}