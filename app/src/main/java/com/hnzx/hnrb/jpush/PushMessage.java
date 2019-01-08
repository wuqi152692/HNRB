package com.hnzx.hnrb.jpush;

import java.io.Serializable;

public class PushMessage implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// {"link_url":"","content_id":"content_24-123-1","is_link":0,"content_type":"content_content","list_type":"news"}
	public String link_url, content_id, content_type, list_type;
	public int is_link;
}
