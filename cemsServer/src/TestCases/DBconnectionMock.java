package TestCases;

import logic.LogInInfo;

public class DBconnectionMock {
    public boolean isConnected(LogInInfo loginInfo) {
        if (loginInfo.getUserName().equals("jsm") && loginInfo.getPassword().equals("456")) {
            return true;

        }
        return false;

    }
}
