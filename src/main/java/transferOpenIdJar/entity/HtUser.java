package transferOpenIdJar.entity;

/**
 * @Author: suahe.
 * @Date:Created in 2020/4/27 10:08.
 * @Description: 用户实体
 */
public class HtUser {

    private String id;
    //原微信openId
    private String oldWeixinNo;
    //新的微信openId
    private String newWeixinNo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOldWeixinNo() {
        return oldWeixinNo;
    }

    public void setOldWeixinNo(String oldWeixinNo) {
        this.oldWeixinNo = oldWeixinNo;
    }

    public String getNewWeixinNo() {
        return newWeixinNo;
    }

    public void setNewWeixinNo(String newWeixinNo) {
        this.newWeixinNo = newWeixinNo;
    }
}
