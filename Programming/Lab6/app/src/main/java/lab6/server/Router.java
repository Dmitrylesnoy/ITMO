package lab6.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayDeque;
import java.util.Deque;

import com.fasterxml.jackson.core.format.InputAccessor.Std;

import lab6.system.collection.CollectionManager;
import lab6.system.commands.Command;
import lab6.system.io.console.StdConsole;
import lab6.system.messages.Request;
import lab6.system.messages.Response;

/**
 * The Router class is responsible for routing commands to their corresponding
 * command handlers.
 * It maintains a collection of commands and executes them based on user
 * requests.
 * This class implements the Singleton pattern to ensure that only one instance
 * of Router exists.
 */
public class Router {
    private CollectionManager cm;
    private static Router instance;
    private Deque<Request> cmdsQueue;
    private Worker worker1;
    private final int PORT = 5000;

    /**
     * Default constructor for the Router class.
     * Initializes the Router instance and loads the collection manager.
     */
    public Router() {
        cm = CollectionManager.getInstance();
        cm.load();
        cmdsQueue = new ArrayDeque<Request>(1);
        StdConsole.writeln("server started");
        worker1 = new Worker();
    }

    public void run() {
        try (DatagramSocket socket = new DatagramSocket(PORT)) {
            StdConsole.writeln("    Waiting packet");
            byte[] buffer = new byte[65535];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);

            StdConsole.writeln("    Deseralization message");
            ByteArrayInputStream byteInput = new ByteArrayInputStream(packet.getData(), 0, packet.getLength());
            ObjectInputStream objectInputStream = new ObjectInputStream(byteInput);
            Request request = (Request) objectInputStream.readObject();

            StdConsole.writeln("    processing request");
            cmdsQueue.add(request);
            Response response = worker1.processCommand(cmdsQueue.pop().command());

            StdConsole.writeln("    Serialize responce ");
            ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteOutput);
            objectOutputStream.writeObject(response);
            byte[] responseData = byteOutput.toByteArray();

            StdConsole.writeln("    Sending answer packet");
            DatagramPacket responcePacket = new DatagramPacket(responseData, 0, responseData.length,
                    packet.getAddress(), packet.getPort());
            socket.send(responcePacket);StdConsole.writeln("Response sent to " + packet.getAddress() + ":" + packet.getPort());
            StdConsole.writeln("Response sent to " + packet.getAddress() + ":" + packet.getPort());
        } catch (NullPointerException e){
            StdConsole.writeln("Client disconnected");
        } catch (Exception e) {
            StdConsole.writeln(e.toString());
        }
    }

    /**
     * Executes the command based on the provided request.
     *
     * @param request the request containing the command and its arguments
     * @return the response after executing the command
     * @throws IOException
     * @throws FileNotFoundException
     * @throws ClassNotFoundException
     */
    public Response runCommand(Request request) {
        cmdsQueue.add(request);
        Response response = worker1.processCommand(cmdsQueue.pop().command());
        return response;
    }

    public Router getInstance() {
        return instance == null ? new Router() : instance;
    }
}
