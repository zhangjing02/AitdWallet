package com.tianqi.aitdwallet.adapter.list_adapter;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tianqi.aitdwallet.R;
import com.tianqi.aitdwallet.utils.Constants;
import com.tianqi.aitdwallet.widget.dialog.DelAddressDialog;
import com.tianqi.baselib.dao.ContactsInfo;
import com.tianqi.baselib.dbManager.ContactsInfoManager;
import com.tianqi.baselib.utils.Constant;
import com.tianqi.baselib.utils.display.ToastUtil;
import com.tianqi.baselib.utils.eventbus.EventMessage;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


public class ContactsAddressChildAdapter extends BaseAdapter {

    private Context mContext;
    private List<ContactsInfo> maps;
    private Typeface typeFace;

    private String gate_way_id;
    private static final int TRUST_GATE_WAY = 2;
    private static final int UN_TRUST_GATE_WAY = 1;
    private int function_type;

    public ContactsAddressChildAdapter(Context context, List<ContactsInfo> beanList,int type) {
        mContext = context;
        maps = beanList;
        typeFace = Typeface.createFromAsset(context.getAssets(), Constant.FONT_PATH);
        function_type=type;
    }

    public void refreshData(List<ContactsInfo> couponTypeBeans) {
        maps = couponTypeBeans;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return maps.size();
    }

    @Override
    public Object getItem(int position) {
        return maps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.adapter_contacts_address_child, null);

            viewHolder.tv_coin_address_name=view.findViewById(R.id.tv_coin_address_name);
            viewHolder.tv_coin_address_content=view.findViewById(R.id.tv_coin_address_content);
            viewHolder.iv_address_copy=view.findViewById(R.id.iv_address_copy);
            viewHolder.btnDelete=view.findViewById(R.id.btnDelete);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.tv_coin_address_name.setText(maps.get(position).getContactsName());
        viewHolder.tv_coin_address_content.setText(maps.get(position).getContactsCoinAddress());

        viewHolder.iv_address_copy.setOnClickListener(view1 -> {
            ClipboardManager cmb = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
            cmb.setText(viewHolder.tv_coin_address_content.getText().toString());
            ToastUtil.showToast(mContext,mContext.getString(R.string.notice_copy_success));
        });

        viewHolder.tv_coin_address_content.setOnClickListener(view13 -> {
            if (function_type== Constants.INTENT_PUT_TRANSACTION){
                EventMessage eventMessage=new EventMessage();
                eventMessage.setType(EventMessage.SELECT_ADDRESS_UPDATE);
                eventMessage.setMsg(maps.get(position).getContactsCoinAddress());
                EventBus.getDefault().post(eventMessage);
                ((Activity) (mContext)).finish();
            }
        });


        viewHolder.btnDelete.setOnClickListener(view12 -> {
            DelAddressDialog delAddressDialog=new DelAddressDialog((Activity) mContext, R.style.MyDialog3);
            delAddressDialog.show();
            delAddressDialog.setOnExitClickLitener(new DelAddressDialog.OnExitClickLitener() {
                @Override
                public void onItemClick(View view) {
                    List<ContactsInfo> coinFrIdAddressInfo = ContactsInfoManager.getCoinFrCoinNameAddressInfo(maps.get(position).getContactsCoinName(), maps.get(position).getContactsCoinAddress());
                    if (coinFrIdAddressInfo != null && coinFrIdAddressInfo.size() > 0) {
                        ContactsInfoManager.deleteScaleRecord(coinFrIdAddressInfo.get(0));
                    }
//                    maps.remove(position);
//                    notifyDataSetChanged();
                    EventMessage eventMessage=new EventMessage();
                    eventMessage.setType(EventMessage.ADD_ADDRESS_UPDATE);
                    EventBus.getDefault().post(eventMessage);
                }
            });
        });
        return view;
    }
    static public class ViewHolder {
        TextView tv_coin_address_name,tv_coin_address_content,btnDelete;
        ImageView iv_address_copy;
    }
}
