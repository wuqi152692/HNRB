package com.hnzx.hnrb.network;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.hnzx.hnrb.App;
import com.hnzx.hnrb.requestbean.BaseBeanArrayReq;
import com.hnzx.hnrb.requestbean.BaseBeanReq;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by Ming on 16/12/19 16:49.
 * Email: mingancai8869@gmail.com
 * 朝看水东流,暮看日西坠。
 */
public class GetData {

    public static final String url = "https://api.henandaily.cn/"; // 正式服务器
    // public static final String url = "http://api.chinaheritage.cn/"; // test

    public static final String md5Key = "www.henandaily.cn";

    public static final String encode = "UTF-8";

    private static final String ForGetUrl(String Method, String Params)
            throws Exception {
        String[] arrays = getSortKeyValues(Params);
        StringBuilder builder = new StringBuilder();
        for (String str : arrays) {
            builder.append(str + "&");
        }
        if (App.getInstance().isLogin()) {
            String path = url
                    + Method
                    + builder
                    + "token="
                    + Algorithm
                    .Md5Encrypt(
                            builder.append(
                                    App.getInstance().getLoginInfo().auth_key)
                                    .toString(), encode).toLowerCase();
            App.getInstance().log(path);
            Log.d("http_1", "ForGetUrl: "+path);
            return path;
        } else {
            String path = url
                    + Method
                    + builder
                    + "token="
                    + Algorithm
                    .Md5Encrypt(
                            Algorithm.Md5Encrypt(md5Key, encode)
                                    .toLowerCase(), encode)
                    .toLowerCase().substring(0, 24);
            App.getInstance().log(path);
            Log.d("http_1", "ForGetUrl: "+path);

            return path;
        }


    }

    private static String[] getSortKeyValues(String params) throws Exception {
        JSONObject jsonObject = new JSONObject(params);
        Iterator<String> it = jsonObject.keys();
        ArrayList<String> keyValues = new ArrayList<String>();
        keyValues
                .add("user_id="
                        + (App.getInstance().isLogin() ? App.getInstance()
                        .getLoginInfo().user_id : 0));
        keyValues.add("device_type=android");
        while (it.hasNext()) {
            String key = it.next();
            keyValues.add(key + "=" + jsonObject.getString(key));
        }
        String[] arrays = new String[keyValues.size()];
        for (int i = 0; i < keyValues.size(); i++) {
            arrays[i] = keyValues.get(i);
        }
        Arrays.sort(arrays);
        return arrays;
    }

    /*----------------------------------------------------------------------------------------------*/

    /**
     * 请求数据
     *
     * @param bean 对象
     * @return
     */

    public static <T> String requestJsonUrlGet(BaseBeanArrayReq<T> bean) {
        try {
            return ForGetUrl(bean.myAddr(), JSON.toJSONString(bean));
        } catch (Exception e) {
            App.getInstance().log(e.getMessage());
            return null;
        }

    }

    public static <T> String requestJsonUrlGetClass(BaseBeanReq<T> bean) {
        try {
            return ForGetUrl(bean.myAddr(), JSON.toJSONString(bean));
        } catch (Exception e) {
            App.getInstance().log(e.getMessage());
            return null;
        }

    }

    public static <T> String requestJsonUrlPostClass(BaseBeanReq<T> bean) {
        try {
            return url + bean.myAddr();
        } catch (Exception e) {
            App.getInstance().log(e.getMessage());
            return null;
        }

    }

    /************************************ 以下是接口value *****************************************/

    /**
     * 欢迎页广告
     */
    public static String GetAdsIndexBean = "v2/content/getAdsIndex?";
    /**
     * 登录页广告
     */
    public static String GetAdsLogin = "v2/content/getadslogin?";
    /**
     * 弹窗广告
     */
    public static String GetAdsPopup = "/v2/content/getadspopup?";

    /**
     * 首页精选更多
     */
    public static String GetFeaturedNewsList = "v2/content/getfeatured?";
    /**
     * 频道焦点图
     */
    public static String GetCategoryPositon = "v2/content/getCategoryPosition?";
    /**
     * 新闻侧边栏
     */
    public static String GetNewsSideBar = "v2/content/getallcategory?";
    /**
     * 获得栏目列表(带从属关系)
     */
    public static String GetCategory = "v2/content/getCategory?";
    /**
     * 新闻24小时
     */
    public static String GetLatestNews = "v2/content/getLatestNews?";
    /**
     * 推送列表
     */
    public static String GetPushList = "v2/content/getPushList?";
    /**
     * Top 10
     */
    public static String GetTop10List = "v2/content/getTopTenList?";
    /**
     * 新闻类型分页
     */
    public static String GetTopCategory = "v2/content/gettopcategory?";
    /**
     * 用户添加订阅接口
     */
    public static String SetUserDingyueColumn = "v2/content/makeOrderCategory?";
    /**
     * 用户取消订阅
     */
    public static String SetUserCancelDingyueColumn = "v2/content/cancelordercategory?";
    /**
     * 新闻单类型数据list
     */
    public static String GetCategoryList = "v2/content/getcategorylist?";
    /**
     * 首页数据
     */
    public static String GetHomePagerData = "v2/content/getindexinfo?";
    /**
     * 高层动态
     */
    public static String GetHighDynamicList = "v2/content/getDynamicList?";
    /**
     * 新闻专题
     */
    public static String GetNewsSpecial = "v2/content/getSpecialContent?";
    /**
     * 新闻详情
     */
    public static String GetNewsDetalis = "v2/content/getnewsbyid?";
    /**
     * 相关新闻
     */
    public static String GetAboutNewsList = "v2/content/getrelations?";
    /**
     * 收藏新闻
     */
    public static String SetAddFavourite = "v2/content/addfavourite?";
    /**
     * 取消新闻收藏
     */
    public static String SetCancelFavourite = "v2/content/cancelfavourite?";
    /**
     * 作者个人信息
     */
    public static String GetAuthorCenter = "v2/content/authorCenter?";
    /**
     * 作者新闻列表
     */
    public static String GetAuthorNewsList = "v2/content/getAuthorNewsList?";
    /**
     * 关注作者
     */
    public static String SetMakeOrderAuthor = "v2/content/makeorderauthor?";
    /**
     * 取消关注作者
     */
    public static String SetCancelOrderAuthor = "v2/content/cancelorderauthor?";
    /**
     * 关注作者列表
     */
    public static String GetOrderAuthorList = "v2/ucenter/myOrderedReporter?";

    /**
     * 履历列表
     */
    public static String GetResumeList = "v2/content/resumelist?";
    /**
     * 履历 相关新闻
     */
    public static String GetResumeRelation = "v2/content/resumerelation?";
    /**
     * 履历 履历详情
     */
    public static String GetResumeContent = "v2/content/resumecontent?";

    /**
     * 政务 机构列表 政务发布中心首页 列表
     */
    public static String GetPoliticsNews = "/v2/content/getpoliticsnews?";
    /**
     * 广场机构推荐
     */
    public static String GetPoliticsRecommend = "v2/content/getPoliticsRecommend?";

    /**
     * 政务 机构列表
     */
    public static String GetPoliticsList = "v2/content/getpoliticslist?";

    /**
     * 机构详情
     */
    public static String GetOfficalDetails = "v2/content/getOfficalInfo?";

    /**
     * 机构订阅
     */
    public static String MakeorderCategory = "v2/content/makeordercategory?";

    /**
     * 取消机构订阅
     */
    public static String CancelorderCategory = "v2/content/cancelordercategory?";
    /**
     * 广场头部轮播&精选推荐
     */
    public static String getPoliticsFeatured = "v2/content/getPoliticsFeatured?";
    /**
     * 政务服务接口
     */
    public static String GetPoliticsservice = "v2/content/getpoliticsservice?";
    /**
     * 新闻热门评论
     */
    public static String GetNewsHotComment = "v2/comments/gethotlist?";
    /**
     * 新闻评论列表
     */
    public static String GetNewsComment = "v2/comments/getlist?";
    /**
     * 评论回复列表
     */
    public static String GetCommentsMore = "v2/comments/getListMore?";
    /**
     * 新闻评价
     */
    public static String SetNewsComment = "v2/comments/create?";
    /**
     * 新闻评价点赞
     */
    public static String SetNewsCommentSupport = "v2/comments/support?";
    /**
     * 新闻点赞
     */
    public static String SetNewsSupport = "v2/content/support?";
    /**
     * 用户登录
     */
    public static String GetMemberLogin = "v2/member/login?";
    /**
     * 搜索
     */
    public static String getSearchData = "v2/content/search?";
    /**
     * 搜索热词
     */
    public static String getSearchHotWords = "v2/content/getsearchhotwords?";
    /**
     * 获取注册验证码
     */
    public static String GetSendSmsCode = "v2/member/send_sms?";
    /**
     * 用户注册
     */
    public static String SetMemberRegister = "v2/member/register?";
    /**
     * 忘记密码 重置密码
     */
    public static String SetResetForgetPassword = "v2/member/forget_password?";
    /**
     * 修改密码
     */
    public static String SetChangePassword = "v2/ucenter/changepassword?";
    /**
     * 修改头像
     */
    public static String SetPhotoModify = "v2/ucenter/changeavatar?";
    /**
     * 修改昵称
     */
    public static String SetNickModify = "v2/ucenter/changenickname?";
    /**
     * 修改性别
     */
    public static String SetSexModify = "v2/ucenter/changesex?";
    /**
     * 修改手机 确认老手机
     */
    public static String SetEnsureOldPhone = "v2/ucenter/confirm_mobile?";
    /**
     * 修改手机 确认新手机
     */
    public static String setEnsureNewPhone = "v2/ucenter/changemobile?";
    /**
     * 更改VIP用户显示昵称还是认证名v2/ucenter/changeShowName
     */
    public static String SetChangeShowName = "v2/ucenter/changeShowName?";
    /**
     * 我的收藏
     */
    public static String GetMineCollection = "v2/ucenter/myfavorite?";
    /**
     * 我参与的活动
     */
    public static String GetMyActivity = "v2/ucenter/myactivity?";
    /**
     * 我的任务列表
     */
    public static String GetMyTaskList = "v2/ucenter/taskinfo?";
    /**
     * 我的阅历
     */
    public static String GetMyViews = "v2/ucenter/myviews?";
    /**
     * 签到列表
     */
    public static String GetUserSignHistory = "v2/ucenter/signhistory?";
    /**
     * 签到
     */
    public static String UCenterSign = "v2/ucenter/sign?";
    /**
     * 我的消息
     */
    public static String GetUserMessage = "v2/ucenter/myMessage?";
    /**
     * 站内信检查更新
     */
    public static String GetCheckUserMessage = "v2/ucenter/checkMessageNew?";
    /**
     * 用户反馈
     */
    public static String SetUserFeedBack = "v2/ucenter/feedback?";
    /**
     * 关于
     */
    public static String GetAppAboutData = "v2/utils/getAboutUs?";

    // 互动

    /**
     * 活动详情
     */
    public static String GetActivityDetails = "v2/interactive/getActivityById?";

    /**
     * 活动提交
     */
    public static String GetActivityAdd = "v2/interactive/joinActivity?";

    /**
     * 直播列表
     */
    public static String GetLiveList = "v2/live/getlivelist?";

    /**
     * 直播详情
     */
    public static String GetLiveContentById = "v2/live/getLiveContentById?";

    /**
     * 直播大厅
     */
    public static String GetLiveHallList = "v2/live/getLiveHallList?";

    /**
     * 直播讨论
     */
    public static String GetLiveDiscussList = "v2/live/getLiveDiscussList?";
    /**
     * 直播讨论回复列表
     */
    public static String GetLiveDiscussMore = "v2/live/getLiveDiscussMore?";
    /**
     * 直播发布
     */
    public static String CreateHostStatement = "v2/live/createHostStatement?";
    /**
     * 直播新增评论
     */
    public static String CreateLiveDiscuss = "v2/live/createDiscuss?";
    /**
     * 直播评论点赞
     */
    public static String SetSupportLiveDiscuss = "v2/live/supportLive?";
    /**
     * 版本更新
     */
    public static String GetCheckUpdate = "v2/utils/checkUpdate?";
    /**
     * 用户 快速登录
     */
    public static String GetFastLogin = "v2/member/fast_login?";
    /**
     * 推荐码
     */
    public static String SetWirteInvite = "v2/invite/wirteInvite?";

    /*************************视听*********************************/
    /**
     * 获取图集列表
     */
    public static String GetMediaImage = "v2/media/getpics?";
    /**
     * 获取视频列表
     */
    public static String GetMediaVedio = "v2/media/getvideo?";
    /**
     * 获取音频列表
     */
    public static String GetMediaAudio = "v2/media/getaudio?";
    /**
     * 热门视频
     */
    public static String GetHotVedioList = "v2/media/getHotVideo?";
    /**
     * 获取视频相关新闻
     */
    public static String GetVideoRelations = "v2/content/getVideoRelations?";

    /**************************互动*****************************/
    /**
     * 获取互动列表
     */
    public static String GetInteractList = "v2/interactive/getInteractiveList?";
    /**
     * 列表页投票
     */
    public static String SetInteractVote = "v2/interactive/vote?";
    /**
     * 获取话题详情
     */
    public static String GetTopicInfo = "v2/interactive/getTopicInfo?";
    /**
     * 获取话题评论列表
     */
    public static String GetTopicCommentList = "v2/interactive/getTopicCommentList?";
    /**
     * 话题评论回复列表
     */
    public static String GetTopicCommentMore = "v2/interactive/getTopicCommentMore?";
    /**
     * 话题新增评论
     */
    public static String CreateTopicComment = "v2/interactive/createTopicComment?";
    /**
     * 话题评论点赞
     */
    public static String SupportTopicComment = "v2/interactive/supportTopicComment?";

    public static String DAY_NEWS_ONLINE = "http://dzb.henandaily.cn/";
}
