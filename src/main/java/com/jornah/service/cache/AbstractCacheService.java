package com.jornah.service.cache;

import org.redisson.api.RBucket;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author licong
 * @date 2023/2/15 00:19
 */
public abstract class AbstractCacheService<ID, T extends Cacheable<ID>> implements CacheService<ID, T> {
    @Autowired
    protected RedissonClient redissonClient;

    protected abstract RBucket<T> getBucketByKey(ID id);

    protected abstract RList<T> getListByKey();

    protected abstract void hitLog(ID id);


    @Override
    public T get(ID id) {
        return getBucketByKey(id).get();
    }

    @Override
    public boolean remove(ID id) {
        return getBucketByKey(id).delete();
    }

    @Override
    public void save(T t) {
        RBucket<T> bucket = getBucketByKey(t.cacheId());
        bucket.set(t);
    }

    public void save(T t, long timeToLive, TimeUnit timeUnit) {
        RBucket<T> bucket = getBucketByKey(t.cacheId());
        bucket.set(t, timeToLive, timeUnit);
    }

    public T getOrSaveCache(ID id, Function<ID, T> function) {
        return getOrSaveCache(id, function, 0, null);
    }

    public T getOrSaveCache(ID id, Function<ID, T> function, long timeToLive, TimeUnit timeUnit) {
        //todo disable cache due to serialization issue
//        T fromCache = this.get(id);
//        if (Objects.nonNull(fromCache)) {
//            hitLog(id);
//            return fromCache;
//        }

        T fromDB = function.apply(id);
//        if (Objects.isNull(timeUnit)) {
//            this.save(fromDB);
//        } else {
//            this.save(fromDB, timeToLive, timeUnit);
//        }
        return fromDB;
    }


    public List<T> getOrSaveCache(Supplier<List<T>> supplier, long timeToLive, TimeUnit timeUnit) {
        //todo disable cache due to serialization issue
//        RList<T> fromCache = this.getListByKey();
//        if (fromCache.isExists()) {
//            return fromCache.readAll();
//        }

        List<T> fromDB = supplier.get();
//        if (Objects.isNull(timeUnit)) {
//            this.saveList(fromDB);
//        } else {
//            this.saveList(fromDB, timeToLive, timeUnit);
//        }
        return fromDB;
    }

    public List<T> getOrSaveCache(Supplier<List<T>> supplier) {
        return this.getOrSaveCache(supplier, 0, null);
    }

    @Override
    public void saveList(List<T> list) {
        RList<T> rList = this.getListByKey();
        rList.addAll(list);
    }

    @Override
    public void saveList(List<T> list, long timeToLive, TimeUnit timeUnit) {
        RList<T> rList = this.getListByKey();
        rList.addAll(list);
        rList.expire(timeToLive, timeUnit);
    }

    @Override
    public List<T> getList() {
        return this.getListByKey().readAll();
    }

    @Override
    public void clearList() {
        this.getListByKey().delete();
    }

}
