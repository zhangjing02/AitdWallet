package com.tianqi.baselib.dbgreendao.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.tianqi.baselib.dao.ContactsInfo;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "CONTACTS_INFO".
*/
public class ContactsInfoDao extends AbstractDao<ContactsInfo, Long> {

    public static final String TABLENAME = "CONTACTS_INFO";

    /**
     * Properties of entity ContactsInfo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property ContactsID = new Property(1, String.class, "contactsID", false, "CONTACTS_ID");
        public final static Property ContactsName = new Property(2, String.class, "contactsName", false, "CONTACTS_NAME");
        public final static Property ContactsCoinName = new Property(3, String.class, "contactsCoinName", false, "CONTACTS_COIN_NAME");
        public final static Property ContactsCoinAddress = new Property(4, String.class, "contactsCoinAddress", false, "CONTACTS_COIN_ADDRESS");
        public final static Property Remark = new Property(5, String.class, "remark", false, "REMARK");
        public final static Property ContactsCoinArray = new Property(6, String.class, "contactsCoinArray", false, "CONTACTS_COIN_ARRAY");
        public final static Property CoinResourceId = new Property(7, int.class, "coinResourceId", false, "COIN_RESOURCE_ID");
    }


    public ContactsInfoDao(DaoConfig config) {
        super(config);
    }
    
    public ContactsInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"CONTACTS_INFO\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"CONTACTS_ID\" TEXT," + // 1: contactsID
                "\"CONTACTS_NAME\" TEXT," + // 2: contactsName
                "\"CONTACTS_COIN_NAME\" TEXT," + // 3: contactsCoinName
                "\"CONTACTS_COIN_ADDRESS\" TEXT," + // 4: contactsCoinAddress
                "\"REMARK\" TEXT," + // 5: remark
                "\"CONTACTS_COIN_ARRAY\" TEXT," + // 6: contactsCoinArray
                "\"COIN_RESOURCE_ID\" INTEGER NOT NULL );"); // 7: coinResourceId
        // Add Indexes
        db.execSQL("CREATE UNIQUE INDEX " + constraint + "IDX_CONTACTS_INFO_CONTACTS_ID ON \"CONTACTS_INFO\"" +
                " (\"CONTACTS_ID\" ASC);");
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"CONTACTS_INFO\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, ContactsInfo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String contactsID = entity.getContactsID();
        if (contactsID != null) {
            stmt.bindString(2, contactsID);
        }
 
        String contactsName = entity.getContactsName();
        if (contactsName != null) {
            stmt.bindString(3, contactsName);
        }
 
        String contactsCoinName = entity.getContactsCoinName();
        if (contactsCoinName != null) {
            stmt.bindString(4, contactsCoinName);
        }
 
        String contactsCoinAddress = entity.getContactsCoinAddress();
        if (contactsCoinAddress != null) {
            stmt.bindString(5, contactsCoinAddress);
        }
 
        String remark = entity.getRemark();
        if (remark != null) {
            stmt.bindString(6, remark);
        }
 
        String contactsCoinArray = entity.getContactsCoinArray();
        if (contactsCoinArray != null) {
            stmt.bindString(7, contactsCoinArray);
        }
        stmt.bindLong(8, entity.getCoinResourceId());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, ContactsInfo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String contactsID = entity.getContactsID();
        if (contactsID != null) {
            stmt.bindString(2, contactsID);
        }
 
        String contactsName = entity.getContactsName();
        if (contactsName != null) {
            stmt.bindString(3, contactsName);
        }
 
        String contactsCoinName = entity.getContactsCoinName();
        if (contactsCoinName != null) {
            stmt.bindString(4, contactsCoinName);
        }
 
        String contactsCoinAddress = entity.getContactsCoinAddress();
        if (contactsCoinAddress != null) {
            stmt.bindString(5, contactsCoinAddress);
        }
 
        String remark = entity.getRemark();
        if (remark != null) {
            stmt.bindString(6, remark);
        }
 
        String contactsCoinArray = entity.getContactsCoinArray();
        if (contactsCoinArray != null) {
            stmt.bindString(7, contactsCoinArray);
        }
        stmt.bindLong(8, entity.getCoinResourceId());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public ContactsInfo readEntity(Cursor cursor, int offset) {
        ContactsInfo entity = new ContactsInfo( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // contactsID
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // contactsName
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // contactsCoinName
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // contactsCoinAddress
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // remark
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // contactsCoinArray
            cursor.getInt(offset + 7) // coinResourceId
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, ContactsInfo entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setContactsID(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setContactsName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setContactsCoinName(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setContactsCoinAddress(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setRemark(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setContactsCoinArray(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setCoinResourceId(cursor.getInt(offset + 7));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(ContactsInfo entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(ContactsInfo entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(ContactsInfo entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
