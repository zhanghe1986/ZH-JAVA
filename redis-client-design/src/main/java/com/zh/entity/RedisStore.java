package com.zh.entity;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisSentinelPool;

/**
 * 操作Redis缓存的接口实现类
 */
@SuppressWarnings("unchecked")
public class RedisStore implements IDataStore{

	private Logger log = LoggerFactory.getLogger(RedisStore.class);
	private static final String KEYPRE = "Redis";
	
	JedisPool jedisPool;
	JedisSentinelPool jedisSentinelPool;
	boolean cluster = false;

	static int DEFAULT_PORT = 6379;

	@SuppressWarnings("unused")
	public RedisStore() {
		
		/*String thirdpart = ConfigManager.getConfiguration("cache-store","cache.thirdpart");*/
		String thirdpart = "true";
		if("true".equals(thirdpart)){
			/*String server = ConfigManager.getConfiguration("cache-connect",
					"cache.cluster.0.server");*/
			String server = "10.33.47.20:6379";
			server = server.trim().replaceAll(" +", " ");
			GenericObjectPoolConfig config = new GenericObjectPoolConfig();
			/*config.setMaxTotal(NumberUtils.toInt(ConfigManager.getConfiguration(
					"cache-connect", "cache.cluster.0.connection-pool-size"), 10));*/
			config.setMaxTotal(Integer.parseInt("10"));
			/*config.setMinIdle(NumberUtils.toInt(ConfigManager.getConfiguration(
					"cache-connect", "cache.cluster.0.connection-min-idle"), 2));
			config.setMaxIdle(NumberUtils.toInt(ConfigManager.getConfiguration(
					"cache-connect", "cache.cluster.0.connection-max-idle"), 5));*/
			config.setMinIdle(Integer.parseInt("2"));
			config.setMaxIdle(Integer.parseInt("5"));
			/*String clusterType = ConfigManager.getConfiguration("cache-connect", "cache.deploy.default.cluster");*/
			String clusterType = "0";
			if(clusterType !=null && clusterType.equals("1")){
				cluster = true;
				String masterName = "redis";
				if(log.isDebugEnabled()){
					if(masterName == null){
						log.error("redis cluster masterName null");
					}
				}
				Set<String> sentinels = new HashSet<String>();
				String [] IpAndPort = server.split(" ");
				for(String info: IpAndPort){
					sentinels.add(info);
				}
				String passWord = "foobared";
				jedisSentinelPool = new JedisSentinelPool(masterName, sentinels , config, 4000, passWord);
			}else {
				String ip = server.split(":")[0];
				int port = DEFAULT_PORT;
				
				/*jedisPool = new JedisPool(config, ip, port, 4000, ConfigManager.getConfiguration(
						"cache-connect", "cache.cluster.0.auth"));*/
				jedisPool = new JedisPool(config, ip, port, 40000, "foobared");
				
			}
		}
	}

	@Override
	public boolean set(String key, String value, long exp) {
		if(value == null){
			log.error("the key:"+key+" value is"+value);
			return false;
		}
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.setex(KEYPRE +key, (int) (exp / 1000), value);
		} catch (Exception e) {
			returnBrokenJedis(jedis);
		} finally {
			returnJedis(jedis);
		}
		return true;
	}
	
	@Override
	public String get(String key) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.get(KEYPRE +key);
		} catch (Exception e) {
			returnBrokenJedis(jedis);
			return StringUtils.EMPTY;
		} finally {
			returnJedis(jedis);
		}
	}
	
	private Jedis getJedis() {
		if(cluster){
			return jedisSentinelPool.getResource();
		}
		return jedisPool.getResource();
	}
	
	private void returnJedis(Jedis jedis) {
		if (jedis != null)
			jedis.close();
	}

	private void returnBrokenJedis(Jedis jedis) {
		if(jedis != null){
			jedis.close();
		}
	}
}

