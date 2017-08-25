package org.dean.vo;

import java.io.Serializable;

public class Request implements Serializable {
    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
    //请求服务名（对应evenbus的messageid
    private String service;
}
