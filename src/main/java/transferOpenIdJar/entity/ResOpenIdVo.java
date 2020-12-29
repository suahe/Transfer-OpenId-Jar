package transferOpenIdJar.entity;

/**
 * @Author: suahe.
 * @Date:Created in 2020/4/27 10:08.
 * @Description: 获取新的公众号主体新的openId接口响应结果实体
 */
public class ResOpenIdVo {

    //旧的openId
    private String ori_openid;
    //新的openId
    private String new_openid;
    //错误信息
    private String err_msg;


    public String getOri_openid() {
        return ori_openid;
    }

    public void setOri_openid(String ori_openid) {
        this.ori_openid = ori_openid;
    }

    public String getNew_openid() {
        return new_openid;
    }

    public void setNew_openid(String new_openid) {
        this.new_openid = new_openid;
    }

    public String getErr_msg() {
        return err_msg;
    }

    public void setErr_msg(String err_msg) {
        this.err_msg = err_msg;
    }
}
