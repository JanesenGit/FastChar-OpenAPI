package com.fastchar.openapi;

import com.fastchar.interfaces.IFastConfig;
import com.fastchar.utils.FastStringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FastCharOpenAPIConfig implements IFastConfig {


    private String uiPrimaryColor;

    private String uiLogo;



    private final Map<String, Object> info = new HashMap<>();

    private final List<Map<String, Object>> server = new ArrayList<>();

    private final Map<String, Object> contact = new HashMap<>();

    private final Map<String, Object> license = new HashMap<>();

    public FastCharOpenAPIConfig addServer(String url, String description) {
        Map<String, Object> serverItem = new HashMap<>();
        serverItem.put("url", url);
        serverItem.put("description", description);
        server.add(serverItem);
        return this;
    }

    public FastCharOpenAPIConfig setTitle(String title) {
        info.put("title", title);
        return this;
    }

    public String getTitle() {
        return FastStringUtils.defaultValue(info.get("title"), "OpenAPI");
    }

    public FastCharOpenAPIConfig setDescription(String description) {
        info.put("description", description);
        return this;
    }

    public FastCharOpenAPIConfig setVersion(String version) {
        info.put("version", version);
        return this;
    }


    public FastCharOpenAPIConfig setContactName(String name) {
        contact.put("name", name);
        return this;
    }

    public FastCharOpenAPIConfig setContactUrl(String url) {
        contact.put("url", url);
        return this;
    }

    public FastCharOpenAPIConfig setContactEmail(String email) {
        contact.put("email", email);
        return this;
    }

    public FastCharOpenAPIConfig setLicenseName(String name) {
        contact.put("name", name);
        return this;
    }

    public FastCharOpenAPIConfig setLicenseUrl(String url) {
        contact.put("url", url);
        return this;
    }


    public String getOpenApiVersion() {
        return "3.0";
    }

    public Map<String, Object> getInfo() {
        return info;
    }

    public List<Map<String, Object>> getServer() {
        return server;
    }

    public Map<String, Object> getContact() {
        return contact;
    }

    public Map<String, Object> getLicense() {
        return license;
    }

    public String getUiPrimaryColor() {
        return uiPrimaryColor;
    }

    public FastCharOpenAPIConfig setUiPrimaryColor(String uiPrimaryColor) {
        this.uiPrimaryColor = uiPrimaryColor;
        return this;
    }

    public String getUiLogo() {
        return uiLogo;
    }

    public FastCharOpenAPIConfig setUiLogo(String uiLogo) {
        this.uiLogo = uiLogo;
        return this;
    }
}
