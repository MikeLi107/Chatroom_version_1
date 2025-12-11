package chatroom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Manager {
    private static final Manager instance = new Manager();
    private final List<ChatData> messages = new ArrayList<>();
    private Map<String, Long> onlineUsers = new HashMap<>();

//这其实是两部分，一个是左边显示谁在聊天，右边显示在线人数（侧重在线）。
    private Manager() {}

    public static Manager getInstance() {
        return instance;
    }
    public synchronized List<ChatData> getMessages() {
        return new ArrayList<>(messages);
    }

    public synchronized List<String> getOnlineUsers() {
        cleanupInactiveUsers(); // 获取前先清理
        return new ArrayList<>(onlineUsers.keySet());
    }


    public synchronized void addMessage(String username, String content) {
        ChatData cd = new ChatData(username, content);
        //这个时候创建setter的弊端就体现了。详见ChatData类
        messages.add(cd);
        if (onlineUsers.containsKey(username)) {
            onlineUsers.put(username, System.currentTimeMillis()); // 更新活动时间
        }

    }

    public synchronized void addUser(String username) {
        // 空值和去重判断
        if (username != null && !onlineUsers.containsKey(username)) {
            onlineUsers.put(username, System.currentTimeMillis()); // 用时间戳代替原来的 add
        }
    }


    public synchronized boolean userLogin(String username) {
        if (!onlineUsers.containsKey(username)) {
            onlineUsers.put(username, System.currentTimeMillis());
            return true;
        }
        return false;
    }

    public synchronized void userLogout(String username) {
        onlineUsers.remove(username);
    }

    public synchronized void refreshUser(String username) {
        if (onlineUsers.containsKey(username)) {
            onlineUsers.put(username, System.currentTimeMillis());
        }
    }


    public synchronized void cleanupInactiveUsers() {
        long now = System.currentTimeMillis();
        onlineUsers.entrySet().removeIf(entry -> (now - entry.getValue()) > 20000); // 超过20秒没活动移除
    }

}
