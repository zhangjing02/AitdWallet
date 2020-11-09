package com.tianqi.baselib.dbgreendao.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.tianqi.baselib.dao.CoinInfo;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "COIN_INFO".
*/
public class CoinInfoDao extends AbstractDao<CoinInfo, Long> {

    public static final String TABLENAME = "COIN_INFO";

    /**
     * Properties of entity CoinInfo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Coin_id = new Property(1, String.class, "coin_id", false, "COIN_ID");
        public final static Property Coin_name = new Property(2, String.class, "coin_name", false, "COIN_NAME");
        public final static Property Coin_fullName = new Property(3, String.class, "coin_fullName", false, "COIN_FULL_NAME");
        public final static Property Alias_name = new Property(4, String.class, "alias_name", false, "ALIAS_NAME");
        public final static Property Coin_type = new Property(5, int.class, "coin_type", false, "COIN_TYPE");
        public final static Property Coin_address = new Property(6, String.class, "coin_address", false, "COIN_ADDRESS");
        public final static Property IsCollect = new Property(7, boolean.class, "isCollect", false, "IS_COLLECT");
        public final static Property Coin_totalAmount = new Property(8, double.class, "coin_totalAmount", false, "COIN_TOTAL_AMOUNT");
        public final static Property Coin_blockHeight = new Property(9, String.class, "coin_blockHeight", false, "COIN_BLOCK_HEIGHT");
        public final static Property Coin_ComeType = new Property(10, int.class, "coin_ComeType", false, "COIN__COME_TYPE");
        public final static Property Coin_url = new Property(11, String.class, "coin_url", false, "COIN_URL");
        public final static Property Wallet_id = new Property(12, String.class, "wallet_id", false, "WALLET_ID");
        public final static Property WalletLimit = new Property(13, int.class, "walletLimit", false, "WALLET_LIMIT");
        public final static Property KeystoreStr = new Property(14, String.class, "keystoreStr", false, "KEYSTORE_STR");
        public final static Property PrivateKey = new Property(15, String.class, "privateKey", false, "PRIVATE_KEY");
        public final static Property PublicKey = new Property(16, String.class, "publicKey", false, "PUBLIC_KEY");
        public final static Property IsUpChain = new Property(17, boolean.class, "isUpChain", false, "IS_UP_CHAIN");
        public final static Property ResourceId = new Property(18, int.class, "resourceId", false, "RESOURCE_ID");
        public final static Property IsMerge = new Property(19, boolean.class, "isMerge", false, "IS_MERGE");
        public final static Property Math_amount = new Property(20, double.class, "math_amount", false, "MATH_AMOUNT");
        public final static Property IsHidden = new Property(21, boolean.class, "isHidden", false, "IS_HIDDEN");
    }


    public CoinInfoDao(DaoConfig config) {
        super(config);
    }
    
    public CoinInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"COIN_INFO\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"COIN_ID\" TEXT," + // 1: coin_id
                "\"COIN_NAME\" TEXT," + // 2: coin_name
                "\"COIN_FULL_NAME\" TEXT," + // 3: coin_fullName
                "\"ALIAS_NAME\" TEXT," + // 4: alias_name
                "\"COIN_TYPE\" INTEGER NOT NULL ," + // 5: coin_type
                "\"COIN_ADDRESS\" TEXT," + // 6: coin_address
                "\"IS_COLLECT\" INTEGER NOT NULL ," + // 7: isCollect
                "\"COIN_TOTAL_AMOUNT\" REAL NOT NULL ," + // 8: coin_totalAmount
                "\"COIN_BLOCK_HEIGHT\" TEXT," + // 9: coin_blockHeight
                "\"COIN__COME_TYPE\" INTEGER NOT NULL ," + // 10: coin_ComeType
                "\"COIN_URL\" TEXT," + // 11: coin_url
                "\"WALLET_ID\" TEXT," + // 12: wallet_id
                "\"WALLET_LIMIT\" INTEGER NOT NULL ," + // 13: walletLimit
                "\"KEYSTORE_STR\" TEXT," + // 14: keystoreStr
                "\"PRIVATE_KEY\" TEXT," + // 15: privateKey
                "\"PUBLIC_KEY\" TEXT," + // 16: publicKey
                "\"IS_UP_CHAIN\" INTEGER NOT NULL ," + // 17: isUpChain
                "\"RESOURCE_ID\" INTEGER NOT NULL ," + // 18: resourceId
                "\"IS_MERGE\" INTEGER NOT NULL ," + // 19: isMerge
                "\"MATH_AMOUNT\" REAL NOT NULL ," + // 20: math_amount
                "\"IS_HIDDEN\" INTEGER NOT NULL );"); // 21: isHidden
        // Add Indexes
        db.execSQL("CREATE UNIQUE INDEX " + constraint + "IDX_COIN_INFO_COIN_ID ON \"COIN_INFO\"" +
                " (\"COIN_ID\" ASC);");
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"COIN_INFO\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, CoinInfo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String coin_id = entity.getCoin_id();
        if (coin_id != null) {
            stmt.bindString(2, coin_id);
        }
 
        String coin_name = entity.getCoin_name();
        if (coin_name != null) {
            stmt.bindString(3, coin_name);
        }
 
        String coin_fullName = entity.getCoin_fullName();
        if (coin_fullName != null) {
            stmt.bindString(4, coin_fullName);
        }
 
        String alias_name = entity.getAlias_name();
        if (alias_name != null) {
            stmt.bindString(5, alias_name);
        }
        stmt.bindLong(6, entity.getCoin_type());
 
        String coin_address = entity.getCoin_address();
        if (coin_address != null) {
            stmt.bindString(7, coin_address);
        }
        stmt.bindLong(8, entity.getIsCollect() ? 1L: 0L);
        stmt.bindDouble(9, entity.getCoin_totalAmount());
 
        String coin_blockHeight = entity.getCoin_blockHeight();
        if (coin_blockHeight != null) {
            stmt.bindString(10, coin_blockHeight);
        }
        stmt.bindLong(11, entity.getCoin_ComeType());
 
        String coin_url = entity.getCoin_url();
        if (coin_url != null) {
            stmt.bindString(12, coin_url);
        }
 
        String wallet_id = entity.getWallet_id();
        if (wallet_id != null) {
            stmt.bindString(13, wallet_id);
        }
        stmt.bindLong(14, entity.getWalletLimit());
 
        String keystoreStr = entity.getKeystoreStr();
        if (keystoreStr != null) {
            stmt.bindString(15, keystoreStr);
        }
 
        String privateKey = entity.getPrivateKey();
        if (privateKey != null) {
            stmt.bindString(16, privateKey);
        }
 
        String publicKey = entity.getPublicKey();
        if (publicKey != null) {
            stmt.bindString(17, publicKey);
        }
        stmt.bindLong(18, entity.getIsUpChain() ? 1L: 0L);
        stmt.bindLong(19, entity.getResourceId());
        stmt.bindLong(20, entity.getIsMerge() ? 1L: 0L);
        stmt.bindDouble(21, entity.getMath_amount());
        stmt.bindLong(22, entity.getIsHidden() ? 1L: 0L);
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, CoinInfo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String coin_id = entity.getCoin_id();
        if (coin_id != null) {
            stmt.bindString(2, coin_id);
        }
 
        String coin_name = entity.getCoin_name();
        if (coin_name != null) {
            stmt.bindString(3, coin_name);
        }
 
        String coin_fullName = entity.getCoin_fullName();
        if (coin_fullName != null) {
            stmt.bindString(4, coin_fullName);
        }
 
        String alias_name = entity.getAlias_name();
        if (alias_name != null) {
            stmt.bindString(5, alias_name);
        }
        stmt.bindLong(6, entity.getCoin_type());
 
        String coin_address = entity.getCoin_address();
        if (coin_address != null) {
            stmt.bindString(7, coin_address);
        }
        stmt.bindLong(8, entity.getIsCollect() ? 1L: 0L);
        stmt.bindDouble(9, entity.getCoin_totalAmount());
 
        String coin_blockHeight = entity.getCoin_blockHeight();
        if (coin_blockHeight != null) {
            stmt.bindString(10, coin_blockHeight);
        }
        stmt.bindLong(11, entity.getCoin_ComeType());
 
        String coin_url = entity.getCoin_url();
        if (coin_url != null) {
            stmt.bindString(12, coin_url);
        }
 
        String wallet_id = entity.getWallet_id();
        if (wallet_id != null) {
            stmt.bindString(13, wallet_id);
        }
        stmt.bindLong(14, entity.getWalletLimit());
 
        String keystoreStr = entity.getKeystoreStr();
        if (keystoreStr != null) {
            stmt.bindString(15, keystoreStr);
        }
 
        String privateKey = entity.getPrivateKey();
        if (privateKey != null) {
            stmt.bindString(16, privateKey);
        }
 
        String publicKey = entity.getPublicKey();
        if (publicKey != null) {
            stmt.bindString(17, publicKey);
        }
        stmt.bindLong(18, entity.getIsUpChain() ? 1L: 0L);
        stmt.bindLong(19, entity.getResourceId());
        stmt.bindLong(20, entity.getIsMerge() ? 1L: 0L);
        stmt.bindDouble(21, entity.getMath_amount());
        stmt.bindLong(22, entity.getIsHidden() ? 1L: 0L);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public CoinInfo readEntity(Cursor cursor, int offset) {
        CoinInfo entity = new CoinInfo( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // coin_id
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // coin_name
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // coin_fullName
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // alias_name
            cursor.getInt(offset + 5), // coin_type
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // coin_address
            cursor.getShort(offset + 7) != 0, // isCollect
            cursor.getDouble(offset + 8), // coin_totalAmount
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // coin_blockHeight
            cursor.getInt(offset + 10), // coin_ComeType
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // coin_url
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // wallet_id
            cursor.getInt(offset + 13), // walletLimit
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // keystoreStr
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // privateKey
            cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16), // publicKey
            cursor.getShort(offset + 17) != 0, // isUpChain
            cursor.getInt(offset + 18), // resourceId
            cursor.getShort(offset + 19) != 0, // isMerge
            cursor.getDouble(offset + 20), // math_amount
            cursor.getShort(offset + 21) != 0 // isHidden
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, CoinInfo entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setCoin_id(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setCoin_name(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setCoin_fullName(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setAlias_name(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setCoin_type(cursor.getInt(offset + 5));
        entity.setCoin_address(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setIsCollect(cursor.getShort(offset + 7) != 0);
        entity.setCoin_totalAmount(cursor.getDouble(offset + 8));
        entity.setCoin_blockHeight(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setCoin_ComeType(cursor.getInt(offset + 10));
        entity.setCoin_url(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setWallet_id(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setWalletLimit(cursor.getInt(offset + 13));
        entity.setKeystoreStr(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setPrivateKey(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setPublicKey(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
        entity.setIsUpChain(cursor.getShort(offset + 17) != 0);
        entity.setResourceId(cursor.getInt(offset + 18));
        entity.setIsMerge(cursor.getShort(offset + 19) != 0);
        entity.setMath_amount(cursor.getDouble(offset + 20));
        entity.setIsHidden(cursor.getShort(offset + 21) != 0);
     }
    
    @Override
    protected final Long updateKeyAfterInsert(CoinInfo entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(CoinInfo entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(CoinInfo entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
