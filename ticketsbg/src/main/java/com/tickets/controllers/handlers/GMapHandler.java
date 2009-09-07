package com.tickets.controllers.handlers;

import java.io.Serializable;
import java.net.URLDecoder;

public class GMapHandler implements Serializable {

    private String toMapUrl;
    private String fromMapUrl;

    public String getToMapLat() {
        return getMapLat(toMapUrl);
    }

    public String getToMapLng() {
        return getMapLng(toMapUrl);
    }

    public String getFromMapLat() {
        return getMapLat(fromMapUrl);
    }

    public String getFromMapLng() {
        return getMapLng(fromMapUrl);
    }

    public String getMapLat(String mapUrl) {
        String coords = getCoordsPortion(mapUrl);
        String result = coords.split(",")[0];

        return result;
    }

    public String getMapLng(String mapUrl) {
        String coords = getCoordsPortion(mapUrl);
        String result = coords.split(",")[1];
        return result;
    }

    private String getCoordsPortion(String mapUrl) {
        try {
            String s = mapUrl.substring(
                    mapUrl.indexOf("ll="));

            return s.substring("ll=".length(), s.indexOf("&"));
        } catch (Exception ex) {
            // on any exception (caused by incorrect url, empty map, etc),
            // return 0,0
            return "0,0";
        }
    }

    public String getFromAddress() {
        return getAddress(fromMapUrl);
    }

    public String getToAddress() {
        return getAddress(toMapUrl);
    }

    private String getAddress(String url) {
         try {
             String s = url.substring(url.indexOf("q="));

            String result = "\"" + URLDecoder.decode(s.substring("q=".length(), s
                            .indexOf("&")), "utf-8") + "\"";

            return result;
         } catch (Exception ex) {
             // on any exception (caused by incorrect url, empty map, etc),
             // return null-string
             return "null";
         }
    }

    public String getToMapUrl() {
        return toMapUrl;
    }

    public void setToMapUrl(String toMapUrl) {
        this.toMapUrl = toMapUrl;
    }

    public String getFromMapUrl() {
        return fromMapUrl;
    }

    public void setFromMapUrl(String fromMapUrl) {
        this.fromMapUrl = fromMapUrl;
    }
}
