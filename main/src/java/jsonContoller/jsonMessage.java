package jsonContoller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import objects.objMessage;
import threadsControl.Lock;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class jsonMessage {
    public static ReentrantLock messageLock = new ReentrantLock();

    public jsonMessage() {
    }
    List<objects.objMessage> messages;
    public jsonMessage(List<objects.objMessage> users) {
        messageLock.lock();
        try {
            Gson messagesObj = new GsonBuilder().setPrettyPrinting().create();
            try {
                Writer writer = Files.newBufferedWriter(Paths.get("Message.json"));
                messagesObj.toJson(users, writer);
                writer.close();
            } catch (Exception ex) {

            }
        }finally {
            messageLock.unlock();
        }

    }
    public List<objects.objMessage> get() {
        messageLock.lock();
        try {
            Gson gson = new Gson();
            Reader reader = Files.newBufferedReader(Paths.get("Message.json"));
            messages = new Gson().fromJson(
                    reader, new TypeToken<List<objects.objMessage>>() {
                    }.getType());
            reader.close();
        } catch (IOException ignored) {
        }finally {
            messageLock.unlock();
        }

        return messages;
    }
}
