package com.example.productservice.util;

import co.elastic.clients.elasticsearch._types.query_dsl.MatchAllQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import lombok.val;


import java.util.List;
import java.util.function.Supplier;

public class ESUtil {
    public static Supplier<Query> supplier(){
        Supplier<Query> supplier = ()->Query.of(q->q.matchAll(matchAllQuery()));
        return supplier;
    }

    public static MatchAllQuery matchAllQuery(){
        val matchAllQuery = new MatchAllQuery.Builder();
        return matchAllQuery.build();
    }
    public static Supplier<Query> supplierQueryForMultiMatchQuery(String key,List<String>fields){
        Supplier<Query> supplier=()->Query.of(q->q.multiMatch(multiMatchQuery(key,fields)));
        return supplier;
    }
    public static MultiMatchQuery multiMatchQuery(String key, List<String> fields){
        return new MultiMatchQuery.Builder().query(key).fields(fields).build();

    }
}
