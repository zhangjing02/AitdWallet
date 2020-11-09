package com.tianqi.baselib.utils.eventbus;

/**
 * @创建者 Cai
 * @创时间 2018/11/1917:12
 * @描述
 * @版本 Rapidzpay
 * @更新者 rapidpay.tjchain.com.rapidzpay.eventbus
 * @更新时间 2018/11/19
 * @更新描述 TODO
 */
public class EventMessage {

    public static final int SCAN_EVENT = 401;
    public static final int ADAPTER_ITEM_CREATE = 305;
    public static final int ADAPTER_ITEM_DESTROY = 306;
    public static final int SEEKBAR_START_SCROllING=407; //进度条开始滑动
    public static final int SEEKBAR_STOP_SCROllING=408; //进度条结束滑动
    public static final int BIND_BANK_CARD_CHANGE =501; //用户的银行卡数量有变化（可能添加绑定，也可能解除绑定）

    public static final int GOTO_TAKE_MONEY_PAGER= 701;//跳转到提币界面

    public static final int TRANSACTION_RECORD_UPDATE = 601;//更新交易记录的通知。
    public static final int TRANSACTION_RECORD_UPDATE_USDT = 6001;//更新USDT交易记录的通知。
    public static final int NEW_COIN_UPDATE = 701;//有新的币种的通知。
    public static final int DELETE_CREATE_COIN_UPDATE = 602;//被创建的币种的有被删除了，需要更新。
    public static final int DELETE_IMPORT_COIN_UPDATE = 603;//被导入的币种的有被删除了，需要更新。
    public static final int HIDDEN_WALLET_UPDATE = 604;//有的钱包被隐藏了。
    public static final int HIDDEN_COIN_UPDATE = 605;//有的币种被隐藏了。
    public static final int COIN_NAME_UPDATE = 606;//修改了币种的别名

    private int type;
    private Object object;
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "EventMessage{" +
                "type=" + type +
                ", object=" + object +
                '}';
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public EventMessage(int type, Object object) {
        this.type = type;
        this.object = object;
    }

    public EventMessage(int type) {
        this.type = type;
    }

    public EventMessage() {
    }
}
