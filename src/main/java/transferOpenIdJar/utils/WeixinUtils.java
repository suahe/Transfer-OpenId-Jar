package transferOpenIdJar.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import transferOpenIdJar.entity.ResOpenIdVo;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author: suahe.
 * @Date:Created in 2020/5/8 15:49.
 * @Description: 微信工具类
 */
public class WeixinUtils {

    //旧的微信openId集合
    private static List<String> oldOpenIdList = new ArrayList<String>();
    //获取新的微信openId响应数据集合
    private static List<ResOpenIdVo> resResultList = new ArrayList<ResOpenIdVo>();

    /**
     * 获取公众号的accessToken
     */
    public static String getAccessToken(String appid, String appsecret) {
        try {
            String accessTokenUrl = String.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s",
                    appid,appsecret);
            System.out.println("获取access_token接口的url:" + accessTokenUrl);
            String res = HttpUtils.doGet(accessTokenUrl);
            System.out.println("获取access_token接口返回的结果：" + res);
            JSONObject jsonObject = JSON.parseObject(res);
            if (jsonObject != null) {
                String errcode = jsonObject.getString("errcode");
                if (StringUtilsEx.isNotEmpty(errcode)) {
                    System.out.println("获取accessToken接口错误，错误信息：" + jsonObject.getString("errmsg"));
                    return null;
                } else {
                    String accessToken = jsonObject.getString("access_token");
                    System.out.println("获取公众号access_token:" + accessToken);
                    return accessToken;
                }
            }
            return null;
        } catch (Exception e) {
            System.out.println("获取accessToken接口异常，异常信息：" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取微信用户列表
     */
    public static List<String> getOldOpenIds(String accessToken, String nextOpenid) {
        try {
            String url = String.format("https://api.weixin.qq.com/cgi-bin/user/get?access_token=%s&next_openid=%s",
                    accessToken, nextOpenid);
            System.out.println("获取微信用户列表接口的url:" + url);
            String res = HttpUtils.doGet(url);
            System.out.println("获取access_token接口返回的结果：" + res);
            JSONObject jsonObject = JSON.parseObject(res);
            if (jsonObject != null) {
                String errcode = jsonObject.getString("errcode");
                if (StringUtilsEx.isNotEmpty(errcode)) {
                    System.out.println("获取微信用户列表接口错误，错误信息：" + jsonObject.getString("errmsg"));
                    return null;
                } else {
                    String total = jsonObject.getString("total");
                    String count = jsonObject.getString("count");
                    String data = jsonObject.getString("data");
                    List<String> openIds = JSONObject.parseObject(data, List.class);
                    String resNextOpenid = jsonObject.getString("next_openid");
                    oldOpenIdList.addAll(openIds);
                    //当获取到最后一个微信关注用户是next_openid为空
                    if (resNextOpenid != null) {
                        getOldOpenIds(accessToken, resNextOpenid);
                    }
                }
            }
            return oldOpenIdList;
        } catch (Exception e) {
            System.out.println("获取微信用户列表接口异常，异常信息：" + e.getMessage());
            e.printStackTrace();
            return oldOpenIdList;
        }
    }

    /**
     * 获取新公众号的openId,每次最多获取100个
     * ps:确保获取完所有用户
     */
    public static List<ResOpenIdVo> getNewOpenId(List<List<String>> oldOpenIdLists, String accessToken) {
        try {
            for (List oldOpenIdList : oldOpenIdLists) {
                String url = String.format("http://api.weixin.qq.com/cgi-bin/changeopenid?access_token=%s",
                        accessToken);
                System.out.println("获取微信用户新的openId列表接口的url:" + url);
                JSONObject reqData = new JSONObject();
                reqData.put("from_appid", ConfigUtils.getProperties("oldAppid"));//旧的公众号appid
                reqData.put("openid_list", oldOpenIdList);
                String res = HttpUtils.doPostJson(url, reqData);
                System.out.println("获取微信用户新的openId列表接口返回的结果：" + res);
                JSONObject jsonObject = JSON.parseObject(res);
                if (jsonObject != null) {
                    String errcode = jsonObject.getString("errcode");
                    if (StringUtilsEx.isNotEmpty(errcode) && !"0".equals(errcode)) {
                        System.out.println("获取微信用户新的openId列表接口错误，错误信息：" + jsonObject.getString("errmsg"));
                    } else {
                        String resultList = jsonObject.getString("result_list");
                        List<ResOpenIdVo> resList = JSONObject.parseArray(resultList, ResOpenIdVo.class);
                        for (ResOpenIdVo resOpenIdVo : resList) {
                            if ("ok".equals(resOpenIdVo.getErr_msg())) {
                                resResultList.add(resOpenIdVo);
                            }
                        }
                    }
                }
            }
            System.out.println("获取微信用户新的openId列表:"+JSON.toJSONString(resResultList));
            return resResultList;
        } catch (Exception e) {
            System.out.println("获取微信用户新的openId列表接口异常，异常信息：" + e.getMessage());
            e.printStackTrace();
        }
        return resResultList;
    }

    /**
     * 分批次处理
     */
    public static List separateBatchHolder(List list, int maxNumber) {
        List newList = new ArrayList();
        if (StringUtilsEx.isEmpty(list)){
            return newList;
        }
        int limit = (list.size()+maxNumber-1)/maxNumber;
        Stream.iterate(0, n -> n + 1).limit(limit).forEach(i -> {
            newList.add(list.stream().skip(i * maxNumber).limit(maxNumber).collect(Collectors.toList()));
        });
        return newList;
    }

}

