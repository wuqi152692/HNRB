package com.hnzx.hnrb.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.hnzx.hnrb.responsebean.GetAllCategoryRsp;
import com.hnzx.hnrb.responsebean.GetTopCategoryRsp;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: mingancai
 * @Time: 2017/4/14 0014.
 */

public class DBHelper extends OrmLiteSqliteOpenHelper {
    private final static String databaseName = "hndaily.db";

    private final static int databaseVersion = 2;

    private static DBHelper instance;

    private Map<String, Dao> maps = new HashMap<>();

    public DBHelper(Context context) {
        super(context, databaseName, null, databaseVersion);
    }

    public static synchronized DBHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (DBHelper.class) {
                if (instance == null)
                    instance = new DBHelper(context);
            }
        }
        return instance;
    }

    public synchronized Dao getDao(Class cls) throws SQLException {
        Dao dao = null;
        String className = cls.getSimpleName();
        if (maps.containsKey(className)) {
            dao = maps.get(className);
        } else {
            dao = super.getDao(cls);
            maps.put(className, dao);
        }
        return dao;
    }

    /**
     * 释放资源
     */
    public void close() {
        super.close();
        for (String key : maps.keySet()) {
            Dao dao = maps.get(key);
            dao = null;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, GetAllCategoryRsp.class);
            TableUtils.createTable(connectionSource, GetTopCategoryRsp.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, GetAllCategoryRsp.class, true);
            TableUtils.dropTable(connectionSource, GetTopCategoryRsp.class, true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
