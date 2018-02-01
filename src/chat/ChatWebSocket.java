package chat;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@SuppressWarnings("UnusedDeclaration")
@WebSocket
public class ChatWebSocket {
    private ChatService chatService;
    private Session session;

    public ChatWebSocket(ChatService chatService) {
        this.chatService = chatService;
    }

    @OnWebSocketConnect
    public void onOpen(Session session) {
        this.session = session;
        chatService.add(this);
    }

    @OnWebSocketMessage
    public void onMessage(String data) {
        chatService.sendMessage(data);
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        chatService.remove(this);
    }

    public void sendMessage(String data) {
        String[] result = {"message","[" + LocalDateTime.now().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)) + "]" + data};
        sendString(result);
    }

    public void sendCountUsers(int count){
        String[] result = {"count_users", Integer.toString(count)};
        sendString(result);
    }

    private void sendString(String[] source){
        try {
            Gson gson = new Gson();
            String out =  gson.toJson(source);
            session.getRemote().sendString(out);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
