package com.cloudok.bbs.mapping;

import com.cloudok.core.mapping.Mapping;
import com.cloudok.core.query.QueryOperator;

public class CommentMapping extends Mapping {

	private static final long serialVersionUID = 0L;

	public static final Mapping CONTENT = new Mapping("content", "t.content", QueryOperator.LIKE);

	public static final Mapping THUMBSUPCOUNT = new Mapping("thumbsUpCount", "t.thumbs_up_count");

	public static final Mapping REPLYCOUNT = new Mapping("replyCount", "t.reply_count");

	public static final Mapping COLLECTCOUNT = new Mapping("collectCount", "t.collect_count");

	public static final Mapping POSTID = new Mapping("postId", "t.post_id", QueryOperator.EQ);

	public static final Mapping PATH = new Mapping("path", "t.path", QueryOperator.LIKE);

	public static final Mapping PARENTID = new Mapping("parentId", "t.parent_id", QueryOperator.EQ);

}
