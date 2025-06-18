package com.VinoHouse.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.connection.DataType;

//@SpringBootTest  // 为了防止每次启动都运行，因此注释掉
public class SpringDataRedisTest {
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testRedisTemplate() {
        System.out.println(redisTemplate);
        // string 数据操作
        ValueOperations valueOperations = redisTemplate.opsForValue();
        // hash 类型的数据操作
        HashOperations hashOperations = redisTemplate.opsForHash();
        // list 类型的数据操作
        ListOperations listOperations = redisTemplate.opsForList();
        // set 类型数据操作
        SetOperations setOperations = redisTemplate.opsForSet();
        // zset 类型数据操作
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();
    }

    /**
     * 操作字符串类型的数据
     */
    @Test
    public void testString() {
        redisTemplate.opsForValue().set("name", "木又");  // set：设置
        String city = (String) redisTemplate.opsForValue().get("name");  // get：查询
        System.out.println(city);
        redisTemplate.opsForValue().set("code", "1234", 3, TimeUnit.MINUTES);  // setex：带 TTL 的设置
        redisTemplate.opsForValue().setIfAbsent("lock", "1");  // setnx：key 不存在时设置。设置成功
        redisTemplate.opsForValue().setIfAbsent("lock", "2");  // setnx：设置失败
    }

    /**
     * 操作哈希类型的数据
     */
    @Test
    public void testHash() {
        HashOperations hashOperations = redisTemplate.opsForHash();

        hashOperations.put("100", "name", "tom");  // hset
        hashOperations.put("100", "age", "20");  // hset

        String name = (String) hashOperations.get("100", "name");  // hget
        System.out.println(name);

        Set keys = hashOperations.keys("100");  // hkeys：获取 key 下的所有 field
        System.out.println(keys);

        List values = hashOperations.values("100");  // hvals：获取 key 下的所有 value
        System.out.println(values);

        hashOperations.delete("100", "age");  // hdel
    }

    /**
     * 操作列表类型的数据
     */
    @Test
    public void testList() {
        ListOperations listOperations = redisTemplate.opsForList();

        listOperations.leftPushAll("mylist", "a", "b", "c");  // lpush：左插
        listOperations.leftPush("mylist", "d");  // lpush

        List mylist = listOperations.range("mylist", 0, -1);  // lrange：获取范围内的 value
        System.out.println(mylist);

        listOperations.rightPop("mylist");  // rpop：右移除

        Long size = listOperations.size("mylist");  // llen：长度
        System.out.println(size);
    }

    /**
     * 操作集合类型的数据
     */
    @Test
    public void testSet() {
        SetOperations setOperations = redisTemplate.opsForSet();

        setOperations.add("set1", "a", "b", "c", "d");  // sadd
        setOperations.add("set2", "a", "b", "x", "y");  // sadd

        Set members = setOperations.members("set1");  // smembers：获取 key 下的所有 value
        System.out.println(members);

        Long size = setOperations.size("set1");  // scard：key 中 value 的个数
        System.out.println(size);

        Set intersect = setOperations.intersect("set1", "set2");  // sinter：交集
        System.out.println(intersect);

        Set union = setOperations.union("set1", "set2");  // sunion：并集
        System.out.println(union);

        setOperations.remove("set1", "a", "b");  // srem
    }

    /**
     * 操作有序集合类型的数据
     */
    @Test
    public void testZset() {
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();

        zSetOperations.add("zset1", "a", 10);  // zadd
        zSetOperations.add("zset1", "b", 12);  // zadd
        zSetOperations.add("zset1", "c", 9);  // zadd

        Set zset1 = zSetOperations.range("zset1", 0, -1);  // zrange：获取范围内的 value
        System.out.println(zset1);

        zSetOperations.incrementScore("zset1", "c", 10);  // zincrby：为 key 加 score

        zSetOperations.remove("zset1", "a", "b");  // zrem
    }

    /**
     * 通用命令操作
     */
    @Test
    public void testCommon() {
        Set keys = redisTemplate.keys("*");  // keys：查询指定 key
        System.out.println(keys);

        Boolean name = redisTemplate.hasKey("name");  // exists
        Boolean set1 = redisTemplate.hasKey("set1");  // exists

        for (Object key : keys) {
            DataType type = redisTemplate.type(key);  // type
            System.out.println(type.name());
        }

        redisTemplate.delete("mylist");  // del
    }
}
