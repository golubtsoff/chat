package chat;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ChatService {
    private Set<ChatWebSocket> webSockets;

    public ChatService() {
        this.webSockets = Collections.newSetFromMap(new ConcurrentHashMap<>());
    }

    public void sendMessage(String data) {
        for (ChatWebSocket user : webSockets) {
            try {
                user.sendMessage(data);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void add(ChatWebSocket webSocket) {
        webSockets.add(webSocket);
        updateCountUsers();
    }

    public void remove(ChatWebSocket webSocket) {
        webSockets.remove(webSocket);
        updateCountUsers();
    }

    private void updateCountUsers(){
        for (ChatWebSocket user : webSockets) {
            try {
                user.sendCountUsers(webSockets.size());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}