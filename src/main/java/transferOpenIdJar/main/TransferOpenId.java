package transferOpenIdJar.main;

import transferOpenIdJar.dao.Dao;
import transferOpenIdJar.entity.HtUser;
import transferOpenIdJar.entity.ResOpenIdVo;
import transferOpenIdJar.utils.ConfigUtils;
import transferOpenIdJar.utils.StringUtilsEx;
import transferOpenIdJar.utils.WeixinUtils;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: suahe.
 * @Date:Created in 2020/5/8 16:30.
 * @Description: 公众号迁移
 */
public class TransferOpenId {
	
	public static Dao dao = new Dao();

	public static void main(String[] args) throws Exception {
		//获取新公众的accessToken
		String newAccessToken = WeixinUtils.getAccessToken(ConfigUtils.getProperties("newAppid"),
				ConfigUtils.getProperties("newAppsecret"));
		if (StringUtilsEx.isNotEmpty(newAccessToken)){
			//获取微信关注用户列表
			//List<String> oldOpenIdList = WeixinUtils.getOldOpenIds(oldAccessToken, "");
			List<HtUser> htUserList = dao.getOldOpenId();
			//处理旧的openId分批次封装成一个集合
			List<List<String>> oldOpenIdLists = WeixinUtils.separateBatchHolder(htUserList, 100);
			//分批次调用获取新公众号新的openId,每次最多获取100个
			List<ResOpenIdVo> resOpenIdVoList = WeixinUtils.getNewOpenId(oldOpenIdLists, newAccessToken);
			//封装新的openId集合对象
			List<HtUser> newHtUserList = resOpenIdVoList.stream()
					.map(resOpenIdVo -> htUserList.stream()
							.filter(htUser -> resOpenIdVo.getOri_openid().equals(htUser.getOldWeixinNo()))
							.findFirst()
							.map(htUser -> {
								htUser.setNewWeixinNo(resOpenIdVo.getNew_openid());
								return htUser;
							}).orElse(null))
					.collect(Collectors.toList());
			//移除为空的元素
			newHtUserList.removeIf(newHtUser -> StringUtilsEx.isEmpty(newHtUser));
			//分批次处理
			List<List> lists = WeixinUtils.separateBatchHolder(newHtUserList, 200);
			//批量更新数据库
			for (List<HtUser> resultList : lists) {
				dao.batchUpdate(resultList);
			}
		}
	}

}
