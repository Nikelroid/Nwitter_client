package connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class sender {

    private Socket socket = null;
    private DataInputStream input, inFromServer = null;
    private DataOutputStream out = null;
    private JSONObject inputJson = new JSONObject();
    String address;
    int port;
    String inputLine = "";
    private static final Logger logger = LogManager.getLogger(sender.class);

    public JSONObject send(JSONObject outputJson) {

        address = "localhost";
        port = 8000;
       new connectionCfg();
       if (!connectionCfg.address.isEmpty())
           address = connectionCfg.address;
        if (connectionCfg.port!=0)
            port = connectionCfg.port;
        try {
            try {
                socket = new Socket(address, port);
            } catch (Exception ignored) {
                logger.error("error in client side");
            }

            try {
                input = new DataInputStream(System.in);
                inFromServer = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                out = new DataOutputStream(socket.getOutputStream());
            } catch (Exception exception) {
                logger.error("error in client side : "+ exception);
            }
            try {
                String s = outputJson.toString();
                out.write(s.getBytes(StandardCharsets.UTF_8));
                socket.shutdownOutput();
            } catch (NullPointerException noConnection) {
                logger.error("error in connection of client side : " + noConnection);
            }

            byte[] bytes = new byte[1024];
            int len;
            StringBuilder sb = new StringBuilder();
            while ((len = inFromServer.read(bytes)) != -1) {
                sb.append(new String(bytes, 0, len, StandardCharsets.UTF_8));
            }
            inputLine = sb.toString();
            try {
                input.close();
                out.close();
                socket.close();
            } catch (IOException i) {
                logger.error("error in connection of client side : " + i);
            }
            try {
                inputJson = new JSONObject(inputLine);
                return inputJson;
            } catch (Exception e) {
                logger.error("error in connection of client side : " + e);
            }

        }catch (Exception e){
            logger.error("error in connection of client side : " + e);
        }

        return inputJson;
    }

    }

