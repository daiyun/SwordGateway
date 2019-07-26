package com.doctorwork.doctorwork.admin.api.res;

import java.util.function.Function;

/**
 * @Author:czq
 * @Description:
 * @Date: 15:23 2019/7/26
 * @Modified By:
 */
public class Mapper<T, R> {
    private T fromT;

    private Mapper() {
    }

    public Mapper(T fromT) {
        this.fromT = fromT;
    }

    public R convert(Function<T, R> mapper) {
        if (fromT == null)
            return null;
        return mapper.apply(fromT);
    }
}
