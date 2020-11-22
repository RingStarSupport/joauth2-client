import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.setting.dialect.Props;
import com.joauth2.AbstractRequester;
import com.joauth2.Attr;
import com.joauth2.Client;
import org.junit.Test;

import java.util.Map;

/**
 * @author wujiawei
 * @see
 * @since 2020/11/20 下午11:05
 */
public class ClientRestTest extends AbstractRequester {

    static {
        Attr.TOKEN = "392c58b4bce5bb0c7ee0d811a7fe1a01";
    }

    /**
     * 拉取最新应用数据
     */
    @Test
    public void pullData(){
        String requestUrl = "http://localhost:926/connect/oauth2/data";
        Map<String, Object> params = MapUtil.newHashMap();
        params.put("access_token", Attr.TOKEN);

        JSONObject resultJson = doPost(requestUrl, params);
        System.out.println(resultJson.toStringPretty());

        if (resultJson.getInt("code") == 10000) {
            JSONObject json = resultJson.getJSONObject("object");
            Attr.MAX_USER = json.getInt("maxUser");
            Attr.INTERVALS = json.getInt("intervals");
            Attr.DEBUG_MODE = json.getJSONObject("debug").getInt("value") == 1;
            Attr.canEncrypt = true;
        } else {
            String message = resultJson.getStr("msg");
            Attr.setMessage(message);
            Attr.canEncrypt = false;
            Attr.DEBUG_MODE = false;
            Attr.MAX_USER = 0;
        }
        System.out.println(Attr.MAX_USER);
        System.out.println(Attr.INTERVALS);
        System.out.println(Attr.DEBUG_MODE);
    }

    @Test
    public void checkPropsTest(){
        checkProps();
    }

    public String checkProps() {
        StringBuilder sb = new StringBuilder("");
        Props props = null;

        // 读取配置文件
        try {
            Attr.props = new Props("application.properties");
            props = Attr.props;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (props == null) {
            System.out.println("缺少配置项，请检查配置文件application.properties和授权相关配置");
        }

        if (!props.containsKey("auth.app_key")) {
            sb.append("[auth.app_key] ");
        }

        if (!props.containsKey("auth.app_secret")) {
            sb.append("[auth.app_secret] ");
        }

        if (!props.containsKey("auth.url")) {
            sb.append("[auth.url] ");
        }

        if (!props.containsKey("auth.app_encrypt")) {
            sb.append( "[auth.app_encrypt] " );
        }

        System.out.println(sb.toString());

        return sb.toString();
    }

    @Test
    public void getCode() {
        checkProps();
        String code = Client.getCode();
        System.out.println(code);
    }

}
