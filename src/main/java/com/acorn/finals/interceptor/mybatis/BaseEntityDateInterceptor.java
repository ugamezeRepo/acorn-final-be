package com.acorn.finals.interceptor.mybatis;

import com.acorn.finals.model.entity.BaseEntity;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.springframework.stereotype.Component;

@Component
@Intercepts({@Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class })})
public class BaseEntityDateInterceptor implements Interceptor {
    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object arg = invocation.getArgs()[1];

        if (arg instanceof BaseEntity entity) {
            switch (mappedStatement.getSqlCommandType()) {
                case INSERT -> {
                    if (entity.createdAt == null) {
                        entity.initDate();
                    }
                }
                case UPDATE -> {
                    if (entity.updatedAt == null) {
                        entity.updateDate();
                    }
                }
            }

        }
        return invocation.proceed();
    }
}