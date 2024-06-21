package com.fastchar.openapi.rapidoc;

import com.fastchar.core.FastChar;
import com.fastchar.core.FastMapWrap;
import com.fastchar.core.FastResource;
import com.fastchar.utils.FastFileUtils;
import com.fastchar.utils.FastStringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FastRAPIDocHelper {

    /**
     * 构建 <a href="https://rapidocweb.com/">rapi-doc</a>  在线文档界面
     * @param docTitle 文档标题
     * @param openapiUrl 文档地址
     * @return 网页内容
     */
    public static String buildHtml(String openapiUrl,String docTitle,String primaryColor,String logo) {
        FastResource resource = FastChar.getResources().getResource("/com/fastchar/openapi/rapidoc/rapi-doc-temp.html");
        if (resource != null) {
            try {
                List<String> strings = FastFileUtils.readLines(resource.getInputStream(), StandardCharsets.UTF_8);
                String htmlContent = FastStringUtils.join(strings, "\n");

                Map<String, Object> holder = new HashMap<>();
                holder.put("title", docTitle);
                holder.put("openapiUrl", openapiUrl);
                holder.put("color", primaryColor);
                holder.put("logo", logo);

                return replacePlaceholder(holder, htmlContent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return "";
    }


    /**
     * 替换占位符 ${.*}
     *
     * @param placeholders 属性值
     * @param content      需要替换的内容
     * @return 替换后的内容
     */
    private static String replacePlaceholder(Map<String, Object> placeholders, String content) {
        if (FastStringUtils.isEmpty(content)) {
            return content;
        }
        Pattern compile = Pattern.compile("(\\$[{\\[][^{}\\[\\]]+[}\\]])");
        Matcher matcher = compile.matcher(content);
        FastMapWrap fastMapWrap = FastMapWrap.newInstance(placeholders);

        Map<String, String> keyValue = new HashMap<>();
        while (matcher.find()) {
            String realKey = matcher.group(1);
            String runKey = realKey.replace("[", "{").replace("]", "}");
            String value = fastMapWrap.getString(runKey, "");
            keyValue.put(realKey, value);
        }
        for (String key : keyValue.keySet()) {
            content = content.replace(key, keyValue.get(key));
        }
        return content;
    }



}
