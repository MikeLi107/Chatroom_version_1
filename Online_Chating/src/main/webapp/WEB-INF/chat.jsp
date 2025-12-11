<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="chatroom.ChatData" %>
<html>
<head>
    <title>在线聊天室</title>
    <style>
        .container { display: flex; gap: 20px; }
        .chat-area { flex: 3; }
        .user-list { flex: 1; }
        .messages { border: 1px solid #ccc; height: 400px; overflow-y: auto; padding: 10px; }
        .message { margin: 5px 0; }
        .username { font-weight: bold; color: #0066cc; }
        .system { color: #666; font-style: italic; }
    </style>
</head>
<body>
<h1>在线聊天室</h1>
<p>当前用户: <%= session.getAttribute("username") %> | <a href="logout">退出登录</a></p>

<div class="container">
    <div class="chat-area">
        <div class="messages">
            <%
                // 获取消息列表并循环展示
                List<ChatData> messages = (List<ChatData>) request.getAttribute("messages");
                if (messages != null && !messages.isEmpty()) {
                    for (ChatData msg : messages) {
            %>
            <div class="message">
                <span class="username"><%= msg.getUsername() %>:</span>
                <span class="content"><%= msg.getContent() %></span>
            </div>
            <%
                }
            } else {
            %>
            <div class="message system">还没有消息，发送第一条消息吧~</div>
            <%
                }
            %>
        </div>

        <form action="chat" method="post">
            <input type="text" name="content" style="width: 80%;" placeholder="输入消息...">
            <input type="submit" value="发送">
        </form>
    </div>

    <div class="user-list">
        <h3>在线用户</h3>
        <ul>
            <%
                // 获取在线用户列表并循环展示
                List<String> onlineUsers = (List<String>) request.getAttribute("onlineUsers");
                if (onlineUsers != null && !onlineUsers.isEmpty()) {
                    for (String user : onlineUsers) {
            %>
            <li><%= user %></li>
            <%
                }
            } else {
            %>
            <li class="system">当前无在线用户</li>
            <%
                }
            %>
        </ul>
    </div>
</div>

<!-- 自动刷新获取新消息（每5秒） -->
<script>
    // 每5秒刷新页面获取新消息
    setTimeout(function(){
        window.location.reload();
    }, 5000);
</script>

</body>
</html>