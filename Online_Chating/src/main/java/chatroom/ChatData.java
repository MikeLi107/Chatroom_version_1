package chatroom;

public class ChatData {
    //谁发了什么消息，一一对应，不然会出乱子
    //不能单一的使用string
    private String username;
    private String content;

    public ChatData(String username, String content) {
        this.username = username;
        this.content = content;
    }
    //why 不用setter？？？
//在调用 setUsername() 和 setContent() 之间，对象是不完整的；
//有可能有人忘了调用某个 setter，导致对象中某个字段是 null；
//对于这种不该被修改的值（比如消息发送者和内容），提供 setter 反而会带来风险。

    //使用构造方法传参来说会更好、更安全一点

    //而且对于chat data而言，应该属于不可变类型数据，防止他人随便篡改
    public String getUsername() {
        return username;
    }

    public String getContent() {
        return content;
    }
}
