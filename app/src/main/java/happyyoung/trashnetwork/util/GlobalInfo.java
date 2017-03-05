package happyyoung.trashnetwork.util;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import happyyoung.trashnetwork.database.model.SessionRecord;
import happyyoung.trashnetwork.model.User;
import happyyoung.trashnetwork.service.MqttService;

/**
 * Created by shengyun-zhou <GGGZ-1101-28@Live.cn> on 2017-02-20
 */
public class GlobalInfo {
    public static String token;
    public static User user;
    public static List<User> groupWorkers = new ArrayList<>();
    public static SessionRecord currentSession;

    public static void logout(Context context){
        token = null;
        user = null;
        groupWorkers.clear();
        Intent mqttIntent = new Intent(context, MqttService.class);
        context.stopService(mqttIntent);
    }

    public static User findUserById(long userId){
        if(userId == user.getUserId())
            return user;
        for(User u : groupWorkers){
            if(u.getUserId() == userId)
                return u;
        }
        return null;
    }
}