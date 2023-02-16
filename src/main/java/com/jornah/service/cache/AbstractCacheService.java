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

    protected abstract RBucket<T> getBucket(ID id);

    protected abstract RList<T> getList();

    protected abstract void hitLog(ID id);


    public T get(ID id) {
        return getBucket(id).get();
    }

    public boolean remove(ID id) {
        return getBucket(id).delete();
    }

    public void save(T t) {
        RBucket<T> bucket = getBucket(t.getCacheId());
        bucket.set(t);
    }

    public void save(T t, long timeToLive, TimeUnit timeUnit) {
        RBucket<T> bucket = getBucket(t.getCacheId());
        bucket.set(t, timeToLive, timeUnit);
    }

    public T getOrSaveCache(ID id, Function<ID, T> function) {
        return getOrSaveCache(id, function, 0, null);
    }

    public T getOrSaveCache(ID id, Function<ID, T> function, long timeToLive, TimeUnit timeUnit) {
        T fromCache = this.get(id);
        if (Objects.nonNull(fromCache)) {
            hitLog(id);
            return fromCache;
        }

        T fromDB = function.apply(id);
        if (Objects.isNull(timeUnit)) {
            this.save(fromDB);
        } else {
            this.save(fromDB, timeToLive, timeUnit);
        }
        return fromDB;
    }


    public RList<T> getOrSaveCache(Supplier<List<T>> supplier, long timeToLive, TimeUnit timeUnit) {
        RList<T> fromCache = this.getList();
        if (fromCache.isExists()) {
            return fromCache;
        }

        List<T> fromDB = supplier.get();
        return Objects.isNull(timeUnit) ?
                this.saveList(fromDB) :
                this.saveList(fromDB, timeToLive, timeUnit);
    }

    public RList<T> getOrSaveCache(Supplier<List<T>> supplier) {
        return this.getOrSaveCache(supplier, 0, null);
    }

    public RList<T> saveList(List<T> list) {
        RList<T> rList = this.getList();
        rList.addAll(list);
        return rList;
    }

    public RList<T> saveList(List<T> list, long timeToLive, TimeUnit timeUnit) {
        RList<T> rList = this.getList();
        rList.addAll(list);
        rList.expire(timeToLive, timeUnit);
        return rList;
    }

    public void clearList() {
        this.getList().delete();
    }

}
