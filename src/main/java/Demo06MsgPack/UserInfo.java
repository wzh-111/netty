package Demo06MsgPack;

import org.msgpack.annotation.Message;

// 使用Msgpack序列化的类，要使用@Message标注，否则无效
@Message
public class UserInfo {
    private String name;
    private int age;

    public UserInfo() {
    }

    public UserInfo(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
