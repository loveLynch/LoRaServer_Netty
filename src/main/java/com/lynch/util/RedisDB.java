package com.lynch.util;

import io.netty.channel.ChannelHandlerContext;
import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;


public class RedisDB {

    //	public static Jedis jedis;
    public static JedisPool jedisPool;
    static String username = "17380152222";
    private static String IP;
    private static int PORT;

    public static String getIP() {
        return IP;
    }

    public static void setIP(String IP) {
        RedisDB.IP = IP;
    }

    public static int getPORT() {
        return PORT;
    }

    public static void setPORT(int PORT) {
        RedisDB.PORT = PORT;
    }

    /**
     *
     *
     */
    public static JedisPool initialPool() {
        JedisPool jedisPool;
        JedisPoolConfig config = new JedisPoolConfig();//Jedis池配置
        config.setMaxActive(500);//最大活动的对象个数
        config.setMaxIdle(1000 * 60);//对象最大空闲时间
        config.setMaxWait(1000 * 10);//获取对象时最大等待时间

        config.setTestOnBorrow(true);
//        String ip = REDIS_ADDRESS;
        String ip = IP;
        String password = "lora127!";
        int port = PORT;
        jedisPool = new JedisPool(config, ip, port);
        //auth权限配置，启动redis时也需以启动redis.conf方式启动
        //jedisPool = new JedisPool(config, ip, port,3000,password);
        return jedisPool;
    }

    public void setup() {
        //连接redis服务器，192.168.0.100:6379
        jedisPool = initialPool();// = new Jedis("127.0.0.1", 6379);
        updateUserSet(username);
        updateUser(username, "ADMIN", "admin115");
        //权限认证
//		jedis.auth("lora127!");


    }

    public static void operateRequest(String gatewayid, String nodeid, String time, String content) {
//		String time = null, content = null;
//		Date currentTime = new Date();
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
//		time = formatter.format(currentTime);			
        updateGwTable(gatewayid);
        updateGwToNodeList(gatewayid, nodeid);
        updateUserNodeToTime(nodeid, time);
        updateSysNodeToTime(nodeid, time);
        saveSysInfoByNodeTime(nodeid, time, content);


    }

    public static void operateUp(String gatewayid, String nodeid, String time, String userInfo, String sysInfo) {
        // 存 User 信息
        updateUserNodeToTime(nodeid, time);
        saveUserInfoByNodeTime(nodeid, time, userInfo);
        updateUsertoGateway(username, gatewayid);

        // 存 Sys 信息
        updateUserNodeToTime(nodeid, time);
        saveSysInfoByNodeTime(nodeid, time, sysInfo);
        updateGw(gatewayid, sysInfo);


    }


    public static void saveKeyByNode(String nodeid, byte[] nwkSKey, byte[] appSkey) {
        String key = nodeid;
        String nwk = ObjectToString.byteToStr(nwkSKey);
        String app = ObjectToString.byteToStr(appSkey);
        jedisPool.getResource().set(key + ":NwkSKey", nwk);
        jedisPool.getResource().set(key + ":AppSKey", app);
    }

    /**
     * @param user
     */
    public static void updateUserSet(String user) {
        String key = "UserSet";
        Set<String> set = jedisPool.getResource().smembers(key); //用户集合
        if (set == null) {
            jedisPool.getResource().sadd(key, user);
            return;
        }
        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().equals(user))
                return;
        }
        jedisPool.getResource().sadd(key, user);

    }


    /**
     * 用户信息
     *
     * @param username
     * @param usertype
     * @param password
     */
    public static void updateUser(String username, String usertype, String password) {
        String key = "User:" + username;
        for (int i = 0; i < 3; i++) {
            String filed[] = {"UserName", "UserType", "Password"};
            String contxet[] = {username, usertype, password};
            jedisPool.getResource().hset(key, filed[i], contxet[i]);
        }

    }

    /**
     * @param username
     * @param gatewayid
     */
    public static void updateUsertoGateway(String username, String gatewayid) {
        String key = "User:" + username + ":GatewayList";
        jedisPool.getResource().sadd(key, gatewayid);

    }

    /**
     * @param nodeid
     * @param time
     */
    public static void updateSysNodeToTime(String nodeid, String time) {
        String key = "Node:" + nodeid + ":SysInfo:TimeSet";
        jedisPool.getResource().sadd(key, time);
    }


    /**
     * @param nodeid
     * @param time
     * @param content
     */
    public static void saveSysInfoByNodeTime(String nodeid, String time, String content) {
        String key = "Node:" + nodeid + ":SysInfoByTime:" + time;
        String syskey[] = {"freq", "channel", "modulation", "rssi"};
        String sysvalue[] = new String[4];
        JSONObject json = JSONObject.fromObject(content);
        for (int i = 0; i < 4; i++) {
            sysvalue[0] = json.getString("freq");
            sysvalue[1] = json.getString("chan");
            sysvalue[2] = json.getString("modu");
            sysvalue[3] = json.getString("rssi") + "dB";
            jedisPool.getResource().hset(key, syskey[i], sysvalue[i]);

        }
    }


    /**
     * @param nodeid
     * @param time
     * @param content
     */
//    public static void saveUserInfoByNodeTime(String nodeid, String time, String content) {
//        String key = "Node:" + nodeid + ":UserInfoByTime:" + time;
//        String userkey[] = {"water", "power", "longitude", "latitude"};
//        String uservalue[] = new String[4];
//        JSONObject json = JSONObject.fromObject(content);
//        for (int i = 0; i < 4; i++) {
//            uservalue[0] = json.getString("water");
//            uservalue[1] = json.getString("power");
//            uservalue[2] = json.getString("long");
//            uservalue[3] = json.getString("lati");
//            jedisPool.getResource().hset(key, userkey[i], uservalue[i]);
//
//        }
//    }
    public static void saveUserInfoByNodeTime(String nodeid, String time, String content) {
        String key = "Node:" + nodeid + ":UserInfoByTime:" + time;
        String userkey[] = {"lightswitch", "brightness", "longitude", "latitude"};
        String uservalue[] = new String[4];
        JSONObject json = JSONObject.fromObject(content);
        for (int i = 0; i < 4; i++) {
            uservalue[0] = json.getString("lightswitch");
            uservalue[1] = json.getString("brightness");
            uservalue[2] = json.getString("long");
            uservalue[3] = json.getString("lati");
            jedisPool.getResource().hset(key, userkey[i], uservalue[i]);

        }
    }

    /**
     * @param gatewayid
     * @param content
     */
    public static void updateGw(String gatewayid, String content) {
        String key = "Gateway:" + gatewayid;
        String locationkey[] = {"longitude", "latitude"};
        String location[] = new String[2];
        JSONObject json = JSONObject.fromObject(content);
        for (int i = 0; i < 2; i++) {
//            location[0] = json.getString("long");
//            location[1] = json.getString("lati");
            location[0] = "103.92750";
            location[1] = "30.75461";
            jedisPool.getResource().hset(key, locationkey[i], location[i]);
        }

    }

    /**
     * @param nodeid
     * @param time
     */
    public static void updateUserNodeToTime(String nodeid, String time) {
        String key = "Node:" + nodeid + ":UserInfo:TimeSet";
        jedisPool.getResource().sadd(key, time);
    }


    /**
     * @param gatewayid-
     * @param nodeid
     */
    public static void updateGwToNodeList(String gatewayid, String nodeid) {
        String key = "Gateway:" + gatewayid + ":NodeList";
        Set<String> set = jedisPool.getResource().smembers(key); //榛樿鏄崌搴�

        if (set == null) {
            jedisPool.getResource().sadd(key, gatewayid);
            return;
        }
        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().equals(nodeid))
                return;
        }
        jedisPool.getResource().sadd(key, nodeid);
    }

    /**
     * @param gatewayid
     */
    public static void updateGwTable(String gatewayid) {
        String key = "GatewayList";

        try {
            Set<String> set = jedisPool.getResource().smembers(key); //榛樿鏄崌搴�
            if (set == null) {
                jedisPool.getResource().sadd(key, gatewayid);
                return;
            }
            Iterator<String> iterator = set.iterator();
            while (iterator.hasNext()) {
                if (iterator.next().equals(gatewayid))
                    return;
            }
            jedisPool.getResource().sadd(key, gatewayid);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static void main(String[] args) {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHH:mm:ss");
        String dateString = formatter.format(currentTime);

        String sys1 = "{\"tmst\":23161628,\"time\":\"2017-04-19T05:58:11.195861Z\","
                + "\"tmms\":1176616710196,\"chan\":3,\"rfch\":0,"
                + "\"freq\":867.100000,\"stat\":1,\"modu\":\"LORA\","
                + "\"datr\":\"SF12BW125\",\"codr\":\"4\5\",\"lsnr\":-15.2,\"rssi\":-105,\"size\":17,"
                + "\"data\":\"QI3AeQAAAQADi9D2ZA1VRCQ=\"}";
        System.out.println(sys1);
        String sysInfo = "{\"chan\":3,"
                + "\"freq\":867.100000,\"modu\":\"LORA\","
                + "\"datr\":\"SF12BW125\",\"rssi\":-105}";
        System.out.println(sysInfo);
        String userInfo = "{\"lati\":30.75461,"
                + "\"long\":103.92750,\"alti\":531," + "\"temperature\":17," + "\"gatewayID\":\"AA555A0000000000\"}";
        System.out.println(userInfo);
        String sys2 = "{\"tmst\":27349404,\"time\":\"2017-04-19T05:58:15.383672Z\","
                + "\"tmms\":1176616714383,\"chan\":1,\"rfch\":1,\"freq\":868.300000,"
                + "\"stat\":1,\"modu\":\"LORA\",\"datr\":\"SF12BW125\",\"codr\":\"4/5\","
                + "\"lsnr\":-15.0,\"rssi\":-113,\"size\":17,"
                + "\"data\":\"QI3AeQAAAgADbOrMmLZLKs4=\"}";
        String stat = "{\"time\":\"1970-01-01 00:10:59 GMT\",\"lati\":30.75461,"
                + "\"long\":103.92750,\"alti\":531,\"rxnb\":5,\"rxok\":5,\"rxfw\":5,"
                + "\"ackr\":0.0,\"dwnb\":0,\"txnb\":0}";

        String syskey[] = {"feq", "channel", "modulation", "rssi"};
        String sysvalue[] = new String[4];
        JSONObject json = JSONObject.fromObject(sysInfo);
        for (int j = 0; j < 4; j++) {
            sysvalue[0] = json.getString("freq");
            sysvalue[1] = json.getString("chan");
            sysvalue[2] = json.getString("modu");
            sysvalue[3] = json.getString("rssi") + "dB";
            System.out.println(syskey[j] + " " + sysvalue[j]);
        }

        String nodeId = "8d:c0:79:00";
        String gwId = "AA555A0000000000";

        RedisDB redisDB = new RedisDB();
        redisDB.setup();
        Jedis jedis = redisDB.jedisPool.getResource();
        //遍历所有redis中数据
//        Set<String> keys = jedis.keys("*");
//        Iterator<String> it=keys.iterator() ;
//        while(it.hasNext()) {
//            String key = it.next();
//            System.out.println(key);
//        }
        //迭代器遍历集合，获取其元素名为UserSet
//        Set<String> sets =  jedis.smembers("UserSet");
//        Iterator<String> iterator = sets.iterator();
//        System.out.println("The members of UserSet are: ");
//        while(iterator.hasNext()){
//            String itset = iterator.next();
//            System.out.println(itset);
//        }
        //hash集合
//        Set<String> hashSets = jedis.hkeys("Node:42:b8:ed:01:SysInfoByTime:2018070216:31:36");
//        Iterator setIterator = hashSets.iterator();
//        while (setIterator.hasNext()) {
//            String objectName = setIterator.next() + "";
//            String objectValue = jedis.hget("Node:42:b8:ed:01:SysInfoByTime:2018070216:31:36", objectName);
//            System.out.println(objectName + ":" + objectValue);
//        }


        //redisDB.operateRequest(gwId, nodeId, dateString, sysInfo);
        //redisDB.operateUp(gwId, nodeId, dateString, userInfo, sysInfo);


    }

}
