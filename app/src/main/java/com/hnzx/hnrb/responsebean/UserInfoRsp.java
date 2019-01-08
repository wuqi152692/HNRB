package com.hnzx.hnrb.responsebean;

import java.io.Serializable;

/**
 * 注册用户以及登录返回用户信息字段
 * Created by FoAng on 17/5/4 下午4:13;
 */
public class UserInfoRsp implements Serializable {

    /**
     * username : 13607698804
     * user_id : 47
     * nickname : Melody
     * auth_key : 4b4e4b9f02dd2fa015204281b8356542
     * sex : 1  1表示男 0表示女
     * is_vip : 0
     * realname :
     * auth_name :
     * auth_type : 0
     * cookie : user=f9dapRqKeE_qSWYm1HAyQMR9NR5GjMTtH88hSDgNgfJHdVwpSFb_kg
     * avatar : http://125.46.11.56/uploadfile/avatar/1/1/47/30006d268f9411966771e5b5ff952d846d6.jpg
     */

    public String username;
    public String user_id;
    public String nickname;
    public String auth_key;
    public String sex;
    public int is_vip;
    public String realname;
    public String auth_name;
    public String auth_type;
    public String cookie;
    public String avatar;
    public String invite_num;
    public String invite_url;
}
