/*
 *    Copyright (C) 2026 lazycece<lazycece@gmail.com>. All rights reserved.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
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
        Filter.Expression specificFilter =
                and(eq("permission_type", "specific"), eq("permission_users", userId));

        if (userDepts == null || userDepts.isEmpty()) {
            return or(publicFilter, specificFilter);
        }

        Filter.Expression departmentFilter =
                and(eq("permission_type", "department"), in("permission_depts", userDepts));
        return or(or(publicFilter, departmentFilter), specificFilter);
    }

    private static Filter.Expression eq(String key, Object value) {
        return new Filter.Expression(
                Filter.ExpressionType.EQ, new Filter.Key(key), new Filter.Value(value));
    }

    private static Filter.Expression in(String key, List<String> values) {
        return new Filter.Expression(
                Filter.ExpressionType.IN, new Filter.Key(key), new Filter.Value(values));
    }

    private static Filter.Expression and(Filter.Expression left, Filter.Expression right) {
        return new Filter.Expression(Filter.ExpressionType.AND, left, right);
    }

    private static Filter.Expression or(Filter.Expression left, Filter.Expression right) {
        return new Filter.Expression(Filter.ExpressionType.OR, left, right);
    }
}
