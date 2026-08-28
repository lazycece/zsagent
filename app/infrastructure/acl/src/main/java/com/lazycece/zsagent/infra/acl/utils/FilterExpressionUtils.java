package com.lazycece.zsagent.infra.acl.utils;

import java.util.List;
import org.springframework.ai.vectorstore.filter.Filter;

/**
 * @author lazycece
 */
public class FilterExpressionUtils {

    /**
     * 构建权限过滤条件： permission_type == 'public' OR (permission_type == 'department' AND
     * permission_depts IN userDepts) OR (permission_type == 'specific' AND permission_users ==
     * userId)
     */
    public static Filter.Expression permissionFilter(String userId, List<String> userDepts) {
        Filter.Expression publicFilter = eq("permission_type", "public");
        Filter.Expression specificFilter = and(
                eq("permission_type", "specific"),
                eq("permission_users", userId));

        if (userDepts == null || userDepts.isEmpty()) {
            return or(publicFilter, specificFilter);
        }

        Filter.Expression departmentFilter = and(
                eq("permission_type", "department"),
                in("permission_depts", userDepts));
        return or(or(publicFilter, departmentFilter), specificFilter);
    }

    private static Filter.Expression eq(String key, Object value) {
        return new Filter.Expression(Filter.ExpressionType.EQ, new Filter.Key(key),
                new Filter.Value(value));
    }

    private static Filter.Expression in(String key, List<String> values) {
        return new Filter.Expression(Filter.ExpressionType.IN, new Filter.Key(key),
                new Filter.Value(values));
    }

    private static Filter.Expression and(Filter.Expression left, Filter.Expression right) {
        return new Filter.Expression(Filter.ExpressionType.AND, left, right);
    }

    private static Filter.Expression or(Filter.Expression left, Filter.Expression right) {
        return new Filter.Expression(Filter.ExpressionType.OR, left, right);
    }
}
