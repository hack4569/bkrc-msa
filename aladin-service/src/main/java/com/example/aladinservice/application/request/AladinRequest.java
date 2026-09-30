package com.example.aladinservice.application.request;

import lombok.Builder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Builder
public record AladinRequest(
        Integer maxResults,
        String itemId,
        String itemIdType,
        Integer start,
        String cover,
        String searchTarget,
        String queryType,
        String output,
        String version,
        String query,
        String optResult
) {
    public static AladinRequest search(String query) {
        return builder().query(query).queryType("Keyword").searchTarget("Book")
                .maxResults(10).start(1).cover("MidBig").output("js").version("20131101").build();
    }

    public static AladinRequest detail(String isbn13) {
        return builder().itemId(isbn13).itemIdType("ISBN13").output("js").version("20131101")
                .optResult("ebookList,usedList,reviewList,fulldescription,fulldescription2,phraseList,mdrecommend,toc")
                .build();
    }

    public MultiValueMap<String, String> toQueryParams() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        put(params, "MaxResults", maxResults);
        put(params, "ItemId", itemId);
        put(params, "ItemIdType", itemIdType);
        put(params, "start", start);
        put(params, "Cover", cover);
        put(params, "SearchTarget", searchTarget);
        put(params, "QueryType", queryType);
        put(params, "output", output);
        put(params, "Version", version);
        put(params, "Query", query);
        put(params, "OptResult", optResult);
        return params;
    }

    private static void put(MultiValueMap<String, String> params, String name, Object value) {
        if (value != null) {
            params.set(name, value.toString());
        }
    }
}
