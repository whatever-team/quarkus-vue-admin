package xyz.easyboot.web.business.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wujiawei
 * @see
 * @since 2021/8/7 3:46 下午
 */
@Path("mock")
@Produces(value = MediaType.APPLICATION_JSON)
public class MockResource {
    
    @GET
    @Path("/user/welcome")
    public Object welcome() {
        return new HashMap<>(2) {{
            put("timeFix", "你好哇");
            put("message", "要不要来一句DOTA");
        }};
    }
    
    @GET
    @Path("/project")
    public Object project() {
        Map<String, String> map = new HashMap<>(2) {{
            put("logo", "https://gw.alipayobjects.com/zos/rmsportal/WdGqmHpayyMjiEhcKoVE.png");
            put("desc", "那时候我只会想自己想要什么，从不想自己拥有什么");
        }};
        return newList(map);
    }
    
    @GET
    @Path("work/activity")
    public Object activity() {
        return null;
    }
    
    @GET
    @Path("work/team")
    public Object team() {
        Map<String, String> map = new HashMap<>(2) {{
            put("name", "科学搬砖组");
            put("avatar", "https://gw.alipayobjects.com/zos/rmsportal/WhxKECPNujWoWEFNdnJE.png");
        }};
        return newList(map);
    }
    
    private List<Map<String, String>> newList(Map<String, String> map) {
        return new ArrayList<Map<String, String>>(6) {{
            add(map);
            add(map);
            add(map);
            add(map);
            add(map);
            add(map);
        }};
    }
    
}
