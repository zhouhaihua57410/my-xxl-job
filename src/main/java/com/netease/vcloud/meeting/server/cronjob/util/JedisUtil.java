package com.netease.vcloud.meeting.server.cronjob.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;
import redis.clients.util.Pool;

import java.util.*;

@Component
public class JedisUtil {
    private static final Logger log = LoggerFactory.getLogger(JedisUtil.class);

    @Autowired
    private Pool<Jedis> jedisPool;
    @Value("${redis.lockExpireSecond}")
    private int lockExpireSecond;


    private Jedis getJedis(){
        if (jedisPool != null){
            return jedisPool.getResource();
        }
        return null;
    }

    private void close(Jedis jedis){
        if (jedis != null) {
            jedis.close();
        }
    }

    /**
     * 缓存 Map
     *
     * @param ttl 存活时间 单位:秒
     */
    public int setMap(String key, Map<String, String> map, int ttl) {
        if (map == null) {
            return 0;
        }
        Jedis jedis=null;
        String result= "OK";
        try {
            jedis= getJedis();
            if (jedis == null) {
                log.error("get redis error");
                return 0;
            }
            //hmset不是完全覆盖，只是覆盖已有field。此处根据业务需求，需要完全覆盖
            jedis.del(key);
            if (map.size() > 0) {
                result = jedis.hmset(key, map);
                jedis.expire(key, ttl);
            }
            if ("OK".equals(result)) {
                return 1;
            } else {
                return 0;
            }
        } catch (Exception e) {
            log.error("setMap key{}, value{}", key, new Gson().toJson(map), e);
            return 0;
        } finally {
            close(jedis);
        }
    }

    /**
     * 获取缓存的 Map
     */
    public Map<String, String> getMap(String key) {
        if (key == null) {
            return null;
        }
        Jedis jedis=null;
        Map<String, String> result=null;
        try {
            jedis=getJedis();
            if (jedis == null) {
                log.error("get redis error");
                return null;
            }
            result=jedis.hgetAll(key);
            return result;
        } catch (Exception e) {
            log.error("getMap key: {}", key, e);
            return result;
        } finally {
            close(jedis);
        }

    }

    /**
     * 递增
     * @param key
     * @param value
     * @return
     */
    public Long incBy(String key, long value) {
        Jedis jedis=null;
        try {
            jedis= getJedis();
            if (jedis == null) {
                log.error("get redis error");
                return null;
            }
            return jedis.incrBy(key,value);
        } catch (Exception e) {
            log.error("incBy key: {}, value: {}", key, value, e);
            return null;
        } finally {
            close(jedis);
        }
    }

    public Map<String, Long> incBy(Map<String, Long> map) {
        Jedis jedis=null;
        try {
            jedis= getJedis();
            if (jedis == null) {
                log.error("get redis error");
                return null;
            }
            Pipeline pipeline = jedis.pipelined();
            Map<String, Response<Long>> rMap = Maps.newHashMap();
            map.forEach((k, v) -> {
                rMap.put(k, pipeline.incrBy(k, v));
            });
            pipeline.sync();
            Map<String, Long> resultMap = Maps.newHashMap();
            rMap.forEach((k, v)-> resultMap.put(k, v.get()));
            return resultMap;
        } catch (Exception e) {
            log.error("set key{},value{},error:", e);
            return null;
        } finally {
            close(jedis);
        }
    }





    public Boolean hset(String key, String field, String value, int expireSecond) {
        Jedis jedis = null;
        Boolean result = false;
        Long ret1 = null, ret2 = null;
        try {
            jedis = getJedis();
            if (jedis == null) {
                log.error("get redis error");
                return false;
            }
            Pipeline pipeline = jedis.pipelined();
            Response<Long> r1 = pipeline.hset(key, field, value);
            Response<Long> r2 = pipeline.expire(key, expireSecond);
            pipeline.sync();
            ret1 = r1.get();
            ret2 = r2.get();
            log.debug("hset key: {}, value: {}, ret1: {}, ret2: {}", key, value, ret1, ret2);
            return true;
        } catch (Exception e) {
            log.error("hset key: {}, value: {}, ret1: {}, ret2: {}",  key, value, ret1, ret2, e);
            return result;
        } finally {
            close(jedis);
        }
    }

    public Boolean zadd(String key, String value, double score, int expireSecond) {
        Jedis jedis = null;
        Long ret1 = null, ret2 = null;
        try {
            jedis = getJedis();
            if (jedis == null) {
                log.error("get redis error");
                return false;
            }
            Pipeline pipeline = jedis.pipelined();

            Response<Long> r1 = pipeline.zadd(key, score, value);
            Response<Long> r2 = pipeline.expire(key, expireSecond);
            pipeline.sync();
            if(r1 != null){
                ret1 = r1.get();
                ret2 = r2.get();
            }
            log.debug("zadd key: {}, value: {}, ret1: {}, ret2: {}", key, value, ret1, ret2);
            return true;
        } catch (Exception e) {
            log.error("zadd key: {}, value: {}, ret1: {}, ret2: {}", key, value, ret1, ret2, e);
            return false;
        } finally {
            close(jedis);
        }
    }

    public Boolean zaddAndRem(String key, String value, double score, int expireSecond, double maxScore) {
        Jedis jedis = null;
        Long ret1 = null, ret2 = null, ret3 = null;
        try {
            jedis = getJedis();
            if (jedis == null) {
                log.error("get redis error");
                return false;
            }
            Pipeline pipeline = jedis.pipelined();

            Response<Long> r1 = pipeline.zadd(key, score, value);
            Response<Long> r2 = pipeline.zremrangeByScore(key, 0, maxScore);
            Response<Long> r3 = pipeline.expire(key, expireSecond);
            pipeline.sync();
            if(r1 != null){
                ret1 = r1.get();
                ret2 = r2.get();
                ret3 = r3.get();
            }
            log.debug("zadd key: {}, value: {}, ret1: {}, ret2: {}, ret3: {}", key, value, ret1, ret2, ret3);
            return true;
        } catch (Exception e) {
            log.error("zadd key: {}, value: {}, ret1: {}, ret2: {}, ret3: {}", key, value, ret1, ret2, ret3, e);
            return false;
        } finally {
            close(jedis);
        }
    }
    public Boolean zaddAndRem(Map<String, String> keyValues, double score, int expireSecond, double maxScore) {
        Jedis jedis = null;
        Map<String, List<Response<Long>>> rMap = Maps.newHashMap();
        try {
            jedis = getJedis();
            if (jedis == null) {
                log.error("get redis error");
                return false;
            }
            Pipeline pipeline = jedis.pipelined();
            keyValues.forEach((k, v)->{
                List<Response<Long>> rs = Lists.newArrayList();
                rs.add(pipeline.zadd(k, score, v)) ;
                rs.add(pipeline.zremrangeByScore(k, 0, maxScore));
                rs.add(pipeline.expire(k, expireSecond));
                rMap.put(k, rs);
            });
            pipeline.sync();
            for (List<Response<Long>> value : rMap.values()) {
                for (Response<Long> longResponse : value) {
                    longResponse.get();
                }
            }

            log.debug("zadd keyValues: {}, rMap: {}", keyValues, rMap);
            return true;
        } catch (Exception e) {
            log.error("zadd keyValues: {}, rMap: {}", keyValues, rMap, e);
            return false;
        } finally {
            close(jedis);
        }
    }

    public Boolean zrem(String key, String ... values) {
        Jedis jedis = null;
        Long ret = null;
        try {
            jedis = getJedis();
            if (jedis == null) {
                log.error("get redis error");
                return false;
            }
            ret = jedis.zrem(key, values);

            log.debug("zrem key: {}, values: {}, ret: {}", key, values, ret);
            return true;
        } catch (Exception e) {
            log.error("zrem key: {}, values: {}, ret: {}", key, values, ret, e);
            return false;
        } finally {
            close(jedis);
        }
    }

    public Boolean zrem(Map<String, String> keyValues) {
        Jedis jedis = null;
        Map<String, Response<Long>> rMap = Maps.newHashMap();
        try {
            jedis = getJedis();
            if (jedis == null) {
                log.error("get redis error");
                return false;
            }
            Pipeline pipeline = jedis.pipelined();

            keyValues.forEach((k, v)->{
                rMap.put(k, pipeline.zrem(k, v));
            });

            pipeline.sync();
            for (Response<Long> value : rMap.values()) {
                if(value.get() == null){
                    return false;
                }
            }
            log.debug("zrem keyValues: {}, retMap: {}", keyValues, rMap);
            return true;
        } catch (Exception e) {
            log.error("zrem keyValues: {}, retMap: {}", keyValues, rMap, e);
            return false;
        } finally {
            close(jedis);
        }
    }

    public Long zcount(String key, double min, double max ) {
        Jedis jedis = null;
        Long ret = null;
        try {
            jedis = getJedis();
            if (jedis == null) {
                log.error("get redis error");
                return null;
            }
            ret = jedis.zcount(key, min, max);

            log.debug("zcount key: {}, ret: {}", key, ret);
            return ret;
        } catch (Exception e) {
            log.error("zcount key: {}, ret: {}", key, ret, e);
            return ret;
        } finally {
            close(jedis);
        }
    }

    public Double zscore(String key, String member) {
        Jedis jedis = null;
        Double ret = null;
        try {
            jedis = getJedis();
            if (jedis == null) {
                log.error("get redis error");
                return null;
            }
            ret = jedis.zscore(key, member);

            log.debug("zscore, key: {}, member: {}, ret: {}", key, member, ret);
            return ret;
        } catch (Exception e) {
            log.error("zscore, key: {}, member: {}, ret: {}", key, member, ret, e);
            return ret;
        } finally {
            close(jedis);
        }
    }

    public String hget(String key, String field){
        Jedis jedis=null;
        String result=null;
        try {
            jedis=getJedis();
            if (jedis == null) {
                log.error("get redis error");
                return result;
            }
            result=jedis.hget(key, field);
            log.debug("hget key: {}, field: {}", key, field);
            return result;
        } catch (Exception e) {
            log.error("hget key: {}, field: {}", key, field, e);
            return result;
        } finally {
            close(jedis);
        }
    }


    /**
     * If the field was present in the hash it is deleted and 1 is returned, otherwise 0 is
     * returned and no operation is performed.
     * @param key
     * @param fields
     * @return
     */
    public long hdel(final String key, final String... fields){
        Jedis jedis=null;
        Long result=0L;
        try {
            jedis=getJedis();
            if (jedis == null) {
                log.error("get redis error");
                return result;
            }
            result=jedis.hdel(key, fields);
            return result;
        } catch (Exception e) {
            log.error("hdel key: {}, fields: {}", key, fields, e);
            return 0L;
        } finally {
            close(jedis);
        }
    }

    public int setEx(String key,String value,int exTime){
        Jedis jedis=null;
        String result=null;
        try {
            jedis= getJedis();
            if (jedis == null) {
                log.error("get redis error");
                return 0;
            }
            result=jedis.setex(key,exTime,value);
            if ("OK".equals(result)) {
                return 1;
            } else {
                return 0;
            }
        } catch (Exception e) {
            log.error("setEx key: {}, value: {}", key, value, e);
            return 0;
        } finally {
            close(jedis);
        }
    }

    public String get(String key){
        if (key == null) {
            return null;
        }
        Jedis jedis=null;
        String result=null;
        try {
            jedis=getJedis();
            if (jedis == null) {
                log.error("get redis error");
                return result;
            }
            result=jedis.get(key);
            return result;
        } catch (Exception e) {
            log.error("get key: {}", key, e);
            return result;
        } finally {
            close(jedis);
        }
    }

    public Long ttl(String key){
        if (key == null) {
            return null;
        }
        Jedis jedis=null;
        try {
            jedis=getJedis();
            if (jedis == null) {
                log.error("get redis error");
                return null;
            }
            return jedis.ttl(key);
        } catch (Exception e) {
            log.error("ttl key: {}", key, e);
            return null;
        } finally {
            close(jedis);
        }
    }

    public Boolean exists(String key){
        if (key == null) {
            return null;
        }
        Jedis jedis=null;
        Boolean result=null;
        try {
            jedis=getJedis();
            if (jedis == null) {
                log.error("get redis con from pool error");
            } else {
                result = jedis.exists(key);
            }
        } catch (Exception e) {
            log.error("exists key: {}", key, e);
        } finally {
            close(jedis);
        }

        return result;
    }

    public Boolean setNxEx(String key, String value, int exTime){
        if (key == null) {
            return null;
        }
        Jedis jedis = null;
        Boolean result = null;
        try {
            jedis = getJedis();
            if (jedis == null) {
                log.error("get redis con from pool error");
            } else {
                String statusReply = jedis.set(key, value, "NX", "EX", exTime);
                result = "OK".equals(statusReply);
            }
        } catch (Exception e) {
            log.error("exists key: {}", key, e);
        } finally {
            close(jedis);
        }

        return result;
    }

    public Long del(String key){
        Jedis jedis=null;
        Long result=null;
        try {
            jedis=getJedis();
            if (jedis == null) {
                log.error("get redis error");
                return null;
            }
            result=jedis.del(key);
            return result;
        } catch (Exception e) {
            log.error("del key: {}", key, e);
            return result;
        } finally {
            close(jedis);
        }
    }
    public Set<String> zrangeAndDel(String key){
        Jedis jedis=null;
        Set<String> result;
        try {
            jedis=getJedis();
            if (jedis == null) {
                log.error("get redis error key: {}", key);
                return null;
            }
            Pipeline pipeline = jedis.pipelined();
            Response<Set<String>> r1 = pipeline.zrange(key, 0, -1);
            Response<Long> r2 = pipeline.del(key);
            pipeline.sync();
            result = r1.get();
            log.debug("zrangeAndDel key: {}, size: {}, del: {}", key, result != null ? result.size() : null, r2.get());
            return result;
        } catch (Exception e) {
            log.error("zrangeAndDel key: {}", key, e);
            return null;
        } finally {
            close(jedis);
        }
    }

    /**
     * 创建或加入集合
     * @param key
     * @param maxKeyExpireSecond
     * @param values
     * @return
     */
    public Long sadd(String key, int maxKeyExpireSecond, String... values) {
        Jedis jedis=null;
        Long result=null;
        try {
            jedis=getJedis();
            if (jedis == null) {
                log.error("get redis error");
            }
            result=jedis.sadd(key, values);
            jedis.expire(key, maxKeyExpireSecond);
            return result;
        } catch (Exception e) {
            log.error("sadd key: {}, values: {}", key, values, e);
            return result;
        } finally {
            close(jedis);
        }
    }

    /**
     * 从set中移除values
     * @param key
     * @param values
     * @return
     */
    public Long srem(String key, String... values) {
        Jedis jedis=null;
        Long result=null;
        try {
            jedis=getJedis();
            if (jedis == null) {
                log.error("get redis error");
            }
            result=jedis.srem(key, values);
            return result;
        } catch (Exception e) {
            log.error("srem",e , "key", key, "values", values);
            return result;
        } finally {
            close(jedis);
        }
    }

    /**
     * 判断value是否在Set中
     * @param key
     * @param value
     * @return
     */
    public boolean sismember(String key, String value) {
        Jedis jedis=null;
        Boolean result=null;
        try {
            jedis=getJedis();
            if (jedis == null) {
                log.error("get redis error");
            }
            result=jedis.sismember(key, value);
            return result;
        } catch (Exception e) {
            log.error("sismember",e , "key", key, "value", value);
            return result;
        } finally {
            close(jedis);
        }
    }

    public Set<String> smembers(String key) {
        Jedis jedis=null;
        Set<String> result=null;
        try {
            jedis=getJedis();
            if (jedis == null) {
                log.error("get redis error");
            }
            result=jedis.smembers(key);
            return result;
        } catch (Exception e) {
            log.error("smembers key: {}", key, e);
            return result;
        } finally {
            close(jedis);
        }
    }

}
