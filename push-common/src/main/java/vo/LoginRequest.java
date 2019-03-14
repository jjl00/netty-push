package vo;


import lombok.Data;

@Data
public class LoginRequest{
    long userId;
    String userName;
    String server;
}
