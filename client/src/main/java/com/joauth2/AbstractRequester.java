package com.joauth2;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

import java.util.Map;

/**
 * 基础请求器
 *
 * @author wujiawei0926@yeah.net
 * @see
 * @since 2019/4/29
 */
public class AbstractRequester {

    private static Log log = LogFactory.get(AbstractRequester.class);

    /**
     * POST请求
     * @param url
     * @param params
     * @param encrypt 是否加密
     * @return
     */
    protected static JSONObject doPost(String url, Map params, boolean encrypt){
        if (encrypt) {
            params = AuthSecureUtils.encodeKeysToMap(params);
        }

        String result = "";
        try {
            result = HttpUtil.createPost(url)
                    .header("Content-Type", ContentType.FORM_URLENCODED.toString())
                    .form(params)
                    .execute()
                    .body();
        } catch (Exception e) {
            log.error("网络连接失败", e.getCause());
        }

        if (StrUtil.isBlank(result) || !StrUtil.startWith(result, "{")) {
            return new JSONObject();
        }
        JSONObject resultJson = JSONUtil.parseObj(result);

        // 解密
        if (resultJson.containsKey("object") && resultJson.getJSONObject("object").containsKey(OAuth2Constants.AES_PARAM)) {
            resultJson.getJSONObject("object")
                    .putAll(
                            AuthSecureUtils.decodeKeys(
                                    resultJson.getJSONObject("object").getStr(OAuth2Constants.AES_PARAM))
                    );
        }

        return resultJson;
    }

    /**
     * POST请求
     * @param url
     * @param params
     * @return
     */
    protected static JSONObject doPost(String url, Map params) {
        return doPost(url, params, true);
    }

    /**
     * GET请求
     * @param url
     * @param params
     * @return
     */
    public static HttpResponse doGet(String url, Map params) {
        HttpResponse httpResponse =  HttpUtil.createGet(AuthSecureUtils.encodeRequestUrl(url, params)).execute();
        return httpResponse;
    }

}
