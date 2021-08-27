package offlineSetting;

import connection.sender;
import launch.authKey;
import mainPages.Setting;

public class setSettingThread extends Thread{
    sender sender = new sender();
    jsonGeneratorSetting jsonSetting = new jsonGeneratorSetting();
    @Override
    public void run() {


        while (true) {

            String AuthKey = authKey.getter().substring(2);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            new settingOffline();


            sender.send(jsonSetting.generate("enable",AuthKey, String.valueOf(settingOffline.enable)));
            sender.send(jsonSetting.generate("account",AuthKey, String.valueOf(settingOffline.account)));

            sender.send(jsonSetting.generate("lastseen",AuthKey, String.valueOf(settingOffline.lastseen)));
            sender.send(jsonSetting.generate("birthday",AuthKey, String.valueOf(settingOffline.birthday)));
            sender.send(jsonSetting.generate("email",AuthKey, String.valueOf(settingOffline.email)));
            sender.send(jsonSetting.generate("phonenumber",AuthKey, String.valueOf(settingOffline.phonenumber)));

        }
    }

}
