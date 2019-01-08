package com.hnzx.hnrb.db.Dao;

import android.content.Context;

import com.hnzx.hnrb.db.DBHelper;
import com.hnzx.hnrb.responsebean.GetTopCategoryRsp;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * @author: mingancai
 * @Time: 2017/5/29 0029.
 */

public class GetTopCategoryRspDao {
    private Dao<GetTopCategoryRsp, Integer> categoryDaoOpe;
    private DBHelper helper;

    public GetTopCategoryRspDao(Context context) {
        try {
            helper = DBHelper.getInstance(context);
            categoryDaoOpe = helper.getDao(GetTopCategoryRsp.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 增加一一条记录
     *
     * @param category
     */
    public void add(GetTopCategoryRsp category) {
        try {
            categoryDaoOpe.create(category);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * 修改一条记录
     *
     * @param category
     */
    public void update(GetTopCategoryRsp category) {
        try {
            categoryDaoOpe.update(category);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除一条记录
     *
     * @param category
     */
    public boolean delete(GetTopCategoryRsp category) {

        try {
            categoryDaoOpe.delete(category);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 查询全部数据
     *
     * @return queryAll 记录列表
     */
    public List<GetTopCategoryRsp> queryAll() {
        try {
            return categoryDaoOpe.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 分页查询
     *
     * @param indexpage
     * @param pagesize
     * @return
     */
    public List<GetTopCategoryRsp> queryByPage(long indexpage, long pagesize) {
        try {
            return categoryDaoOpe.queryBuilder().orderBy("id", false).offset(indexpage * pagesize).limit(pagesize).query();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 关键字查询
     */
    public List<GetTopCategoryRsp> queryByKeyword(String keyword, long indexpage, long pagesize) {
        try {
            return categoryDaoOpe.queryBuilder().orderBy("id", false).offset(indexpage * pagesize).limit(pagesize).where().like("title", keyword + "%").or().like("title", "%" + keyword + "%").or().like("title", "%" + keyword).query();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * catid查询
     */
    public List<GetTopCategoryRsp> queryByCatid(String key, String value) {
        try {
            return categoryDaoOpe.queryBuilder().where().eq(key, value).query();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据Id查询数据
     */
    public GetTopCategoryRsp queryByID(int id) {
        try {
            return categoryDaoOpe.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
