
package chatroom;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/chat")
public class ChatServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();


        // 登录验证
        if (!"ok".equals(session.getAttribute("login"))) {
            response.sendRedirect("login.jsp");
            return;
        }

        String username = (String) session.getAttribute("username");
        String content = request.getParameter("content");

        if (username != null && content != null && !content.trim().isEmpty()) {
            // 添加消息
            Manager.getInstance().addMessage(username, content);
        }

        //关键修改：重定向到 /chat，而不是 chat.jsp
        response.sendRedirect("chat");
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        // 登录验证
        if (!"ok".equals(session.getAttribute("login"))) {
            // **【修改】重定向到 LoginServlet 的映射 /login**
            response.sendRedirect("login");
            return;
        }

        String username = (String) session.getAttribute("username");

        // 1. 检查是否是心跳请求
        if ("true".equals(request.getParameter("ping"))) {
            Manager.getInstance().refreshUser(username);
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        //  2. 普通页面访问时再执行下面这些
        try {
                // 只要页面被刷新（doGet被调用），就证明用户还活着，刷新他的时间戳
                Manager.getInstance().refreshUser(username);

                // 获取消息和列表（原有的代码）
                List<ChatData> messages = Manager.getInstance().getMessages();
                List<String> onlineUsers = Manager.getInstance().getOnlineUsers();

                request.setAttribute("messages", messages);
                request.setAttribute("onlineUsers", onlineUsers);

                request.getRequestDispatcher("/WEB-INF/chat.jsp").forward(request, response);
        } catch (Exception e) {
            // ... (错误处理保持不变)
            e.printStackTrace();
            request.setAttribute("error", "聊天室内部错误，请重试。");
            // **【修改】明确转发到 /WEB-INF/login.jsp**
            request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
        }
    }
}

