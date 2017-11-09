package com.farukydnn.weatherplus.core.network.request;

import java.util.List;


abstract class BaseRequest {

    private String url;

    public BaseRequest(List<String> params) {
        this.url = createUrl(params);
    }

    abstract String createUrl(List<String> params);

    public String getUrl() {
        return url;
    }

}