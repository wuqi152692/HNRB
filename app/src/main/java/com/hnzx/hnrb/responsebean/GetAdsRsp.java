package com.hnzx.hnrb.responsebean;

import java.io.Serializable;

/**
 * @author: mingancai
 * @Time: 2017/5/17 0017.
 */

public class GetAdsRsp implements Serializable {
    public String content_id, content_type, title, link_url, thumb, internal_type, internal_id;
    public int is_link;
}
