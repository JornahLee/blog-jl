package com.jornah.utils;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author licong
 * @date 2021/10/5 10:29
 */
public class PageUtil {
    public static <R, T> PageInfo<R> toVo(PageInfo<T> source, Function<T, R> converter) {
        Page<R> page = new Page<>(source.getPageNum(), source.getPageSize());
        List<R> toVos = source.getList().stream().map(converter).collect(Collectors.toList());
        page.setTotal(source.getTotal());
        page.addAll(toVos);
        return page.toPageInfo();
    }
}
