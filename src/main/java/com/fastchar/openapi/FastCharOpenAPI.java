package com.fastchar.openapi;

import com.fastchar.core.FastAction;
import com.fastchar.core.FastChar;
import com.fastchar.core.FastRoute;
import com.fastchar.openapi.annotation.*;
import com.fastchar.utils.FastBooleanUtils;
import com.fastchar.utils.FastEnumUtils;
import com.fastchar.utils.FastStringUtils;

import java.lang.reflect.Method;
import java.util.*;

public class FastCharOpenAPI {


    public Object buildOpenAPI(String defaultServer) {

        Map<String, Object> openAPI = new HashMap<>();
        FastCharOpenAPIConfig openAPIConfig = FastChar.getConfig(FastCharOpenAPIConfig.class);

        openAPI.put("openapi", openAPIConfig.getOpenApiVersion());
        List<Map<String, Object>> server = openAPIConfig.getServer();
        if (server.isEmpty()) {
            Map<String, Object> serverItem = new HashMap<>();
            serverItem.put("url", defaultServer);
            serverItem.put("description", "默认接口地址");
            server.add(serverItem);
        }
        openAPI.put("servers", server);
        Map<String, Object> info = openAPIConfig.getInfo();
        Map<String, Object> contact = openAPIConfig.getContact();
        if (!contact.isEmpty()) {
            info.put("contact", contact);
        }
        Map<String, Object> license = openAPIConfig.getLicense();
        if (!license.isEmpty()) {
            info.put("license", license);
        }
        if (!info.isEmpty()) {
            openAPI.put("info", info);
        }

        List<FastRoute> allRoute = FastChar.getRoutes();

        Map<String, Object> paths = new HashMap<>();
        for (FastRoute fastRoute : allRoute) {
            Map<String, Object> pathInfo = new HashMap<>();

            List<String> tags = new ArrayList<>();

            List<Object> parameters = new ArrayList<>();

            Class<? extends FastAction> actionClass = fastRoute.getActionClass();
            AFastAPIGroup[] actionClassAFastAPIGroup = actionClass.getAnnotationsByType(AFastAPIGroup.class);
            for (AFastAPIGroup annotation : actionClassAFastAPIGroup) {
                tags.add(annotation.value());
            }


            Method method = fastRoute.getMethod();

            if (method.isAnnotationPresent(Deprecated.class)) {
                pathInfo.put("deprecated", true);
            }

            if (method.isAnnotationPresent(AFastAPITitle.class)) {
                AFastAPITitle annotation = method.getAnnotation(AFastAPITitle.class);
                pathInfo.put("summary", annotation.value());
                pathInfo.put("description", FastStringUtils.defaultValue(annotation.desc(), annotation.value()));
            } else {
                continue;
            }

            AFastAPIHeader[] methodAFastAPIHeader = method.getAnnotationsByType(AFastAPIHeader.class);
            for (AFastAPIHeader annotation : methodAFastAPIHeader) {
                Map<String, Object> header = new LinkedHashMap<>();
                header.put("name", annotation.name());
                header.put("in", "header");
                header.put("description", FastStringUtils.defaultValue(annotation.name(), annotation.desc()));
                header.put("required", annotation.required());
                header.put("example", annotation.example());
                header.put("schema", safeType(annotation.type()));

                parameters.add(header);
            }


            AFastAPIGroup[] methodAFastAPIGroup = method.getAnnotationsByType(AFastAPIGroup.class);
            for (AFastAPIGroup annotation : methodAFastAPIGroup) {
                tags.add(annotation.value());
            }

            Map<String, Object> requestParams = new LinkedHashMap<>();
            boolean hasFile = false;

            List<String> requiredParams = new ArrayList<>();
            AFastAPIParam[] methodAFastAPIParam = method.getAnnotationsByType(AFastAPIParam.class);
            for (AFastAPIParam annotation : methodAFastAPIParam) {
                List<String> values = this.safeValues(annotation.value());
//                格式：name type desc required example

                String name = annotation.name();
                String type = annotation.type();
                String desc = annotation.desc();
                boolean required = annotation.required();
                String example = annotation.example();
                if (!values.isEmpty()) {
                    name = values.get(0);
                }
                if (FastStringUtils.isEmpty(name)) {
                    continue;
                }
                if (values.size() > 1) {
                    type = values.get(1);
                }
                if (values.size() > 2) {
                    desc = values.get(2);
                }
                if (values.size() > 3) {
                    required = FastBooleanUtils.formatToBoolean(values.get(3), required);
                }
                if (values.size() > 4) {
                    example = values.get(4);
                }

                Map<String, Object> param = new HashMap<>(safeType(type));
                param.put("description", desc);
                param.put("example", example);

                Class aClass = annotation.enumClass();
                if (aClass.isEnum()) {
                    String real_type = String.valueOf(param.get("type"));

                    List<String> enumValues = new ArrayList<>();
                    Enum<?>[] enums = FastEnumUtils.getEnumValues(aClass);
                    for (Enum<?> anEnum : enums) {

                        if (real_type.equalsIgnoreCase("integer")) {
                            enumValues.add(anEnum.ordinal() + "（" + anEnum.name() + "）");
                        } else {
                            enumValues.add(anEnum.name());
                        }
                    }
                    param.put("enum", enumValues);
                }

                String[] enums = annotation.enums();
                if (enums.length > 0) {
                    param.put("enum", enums);
                }

                requestParams.put(name, param);
                hasFile = FastStringUtils.defaultValue(param.get("format"), "").equalsIgnoreCase("binary");
                if (required) {
                    requiredParams.add(name);
                }
            }

            Map<String, Object> requestBody = new LinkedHashMap<>();
            Map<String, Object> requestBodyContent = new LinkedHashMap<>();

            Map<String, Object> requestBodyContentSchema = new LinkedHashMap<>();
            requestBodyContentSchema.put("type", "object");
            requestBodyContentSchema.put("required", requiredParams);
            requestBodyContentSchema.put("properties", requestParams);

            if (hasFile) {
                Map<String, Object> mediaTypeContent = new HashMap<>();
                mediaTypeContent.put("schema", requestBodyContentSchema);
                requestBodyContent.put("multipart/form-data", mediaTypeContent);
            } else {
                Map<String, Object> mediaTypeContent = new HashMap<>();
                mediaTypeContent.put("schema", requestBodyContentSchema);
                requestBodyContent.put("application/x-www-form-urlencoded", mediaTypeContent);
            }

            requestBody.put("content", requestBodyContent);
            pathInfo.put("requestBody", requestBody);


            Map<Integer, Object> response = new HashMap<>();

            AFastAPIResponse[] methodAFastAPIResponse = method.getAnnotationsByType(AFastAPIResponse.class);
            for (AFastAPIResponse aFastAPIResponse : methodAFastAPIResponse) {
                Map<String, Object> responseMap = new HashMap<>();
                responseMap.put("description", aFastAPIResponse.desc());

                Map<String, Object> responseContent = new HashMap<>();

                Map<String, Object> responseExamples = new HashMap<>();
                for (int i = 0; i < aFastAPIResponse.examples().length; i++) {
                    String example = aFastAPIResponse.examples()[i];
                    if (FastStringUtils.isEmpty(example)) {
                        continue;
                    }
                    Map<String, Object> exampleContent = new HashMap<>();
                    exampleContent.put("summary", aFastAPIResponse.desc());
                    exampleContent.put("value", example);
                    responseExamples.put(String.valueOf(i + 1), exampleContent);
                }
                Map<String, Object> responseExamplesMap = new HashMap<>();
                if (!responseExamples.isEmpty()) {
                    responseExamplesMap.put("examples", responseExamples);
                }
                if (!responseExamplesMap.isEmpty()) {
                    responseContent.put(aFastAPIResponse.contentType(), responseExamplesMap);
                }
                if (!responseContent.isEmpty()) {
                    responseMap.put("content", responseContent);
                }
                response.put(aFastAPIResponse.statusCode(), responseMap);
            }


            pathInfo.put("responses", response);
            pathInfo.put("tags", tags);
            pathInfo.put("parameters", parameters);


            Map<String, Object> pathItem = new HashMap<>();
            pathItem.put("post", pathInfo);
            paths.put(fastRoute.getRoute(), pathItem);
        }
        openAPI.put("paths", paths);
        return openAPI;
    }


    private Map<String, String> safeType(String type) {
        Map<String, String> map = new HashMap<>();
        if (type.equalsIgnoreCase("int")) {
            map.put("type", "integer");
            map.put("format", "int32");
        } else if (type.equalsIgnoreCase("long")) {
            map.put("type", "integer");
            map.put("format", "int64");
        } else if (type.equalsIgnoreCase("float")) {
            map.put("type", "number");
            map.put("format", "float");
        } else if (type.equalsIgnoreCase("double")) {
            map.put("type", "number");
            map.put("format", "double");
        } else if (type.equalsIgnoreCase("string")) {
            map.put("type", "string");
        } else if (type.equalsIgnoreCase("byte")) {
            map.put("type", "string");
            map.put("format", "byte");
        } else if (type.equalsIgnoreCase("binary") || type.equalsIgnoreCase("file")) {
            map.put("type", "string");
            map.put("format", "binary");
        } else if (type.equalsIgnoreCase("boolean")) {
            map.put("type", "boolean");
        } else if (type.equalsIgnoreCase("date")) {
            map.put("type", "string");
            map.put("format", "date");
        } else if (type.equalsIgnoreCase("dateTime")) {
            map.put("type", "string");
            map.put("format", "dateTime");
        } else if (type.equalsIgnoreCase("password")) {
            map.put("type", "string");
            map.put("format", "password");
        }
        return map;
    }


    private List<String> safeValues(String value) {
        List<String> values = new ArrayList<>();
        String[] splitArray = value.split(" ");
        for (String item : splitArray) {
            if (FastStringUtils.isEmpty(item)) {
                continue;
            }
            values.add(item);
        }
        return values;
    }

}
