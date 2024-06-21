package com.fastchar.openapi.action;

import com.fastchar.core.FastAction;
import com.fastchar.core.FastChar;
import com.fastchar.openapi.FastCharOpenAPI;
import com.fastchar.openapi.FastCharOpenAPIConfig;
import com.fastchar.openapi.rapidoc.FastRAPIDocHelper;

public class FastOpenAPIAction extends FastAction {
    @Override
    protected String getRoute() {
        return "/openapi";
    }

    /**
     * 显示在线文档的UI界面，采用了<a href="https://rapidocweb.com/">rapi-doc</a>插件
     */
    public void index() {
        FastCharOpenAPIConfig config = FastChar.getConfig(FastCharOpenAPIConfig.class);
        responseHtml(FastRAPIDocHelper.buildHtml(getProjectHost() + "openapi/json",
                config.getTitle(), config.getUiPrimaryColor(), config.getUiLogo()));
    }


    /**
     * 获取文档的json文件描述，符合openapi3.0规范
     */
    public void json() {
        FastCharOpenAPI fastCharOpenAPI = FastChar.getOverrides().newInstance(FastCharOpenAPI.class);
        responseJson(fastCharOpenAPI.buildOpenAPI(getProjectHost()));
    }
}
