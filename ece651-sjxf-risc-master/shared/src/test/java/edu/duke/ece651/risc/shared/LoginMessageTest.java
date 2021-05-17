package edu.duke.ece651.risc.shared;



import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LoginConsumer extends Thread{
    private Player player;
    private LoginMessage loginMessage;
    private String[] loginInfo;

    public LoginConsumer (Player player, LoginMessage loginMessage) {
        this.player = player;
        this.loginMessage = loginMessage;
        this.loginInfo = null;
    }

    public void run () {
        loginInfo = loginMessage.getInformation();
    }

    public String[] getLoginInfo() {
        return loginInfo;
    }

}

class LoginProducer extends Thread{
    private Player player;
    private LoginMessage loginMessage;

    public LoginProducer (Player player, LoginMessage loginMessage) {
        this.player = player;
        this.loginMessage = loginMessage;
    }

    public void run () {
        String userName = "Junqi";
        String password = "19961111";
        loginMessage.setInformation(userName, password);
    }
}

public class LoginMessageTest {
    @Test
    public void test_LoginMessage_Get_And_Set() {
        try {
            LoginMessage loginMessage = new LoginMessage();
            Player player1 = new Player("Junqi");

            LoginProducer loginProducer = new LoginProducer(player1, loginMessage);
            LoginConsumer loginConsumer = new LoginConsumer(player1, loginMessage);

            loginConsumer.start();
            Thread.sleep(100);

            String[] ans = loginConsumer.getLoginInfo();
            assertNull(ans);

            loginProducer.start();
            Thread.sleep(100);
            ans = loginConsumer.getLoginInfo();

            assertEquals("Junqi", ans[0]);
            assertEquals("19961111", ans[1]);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
