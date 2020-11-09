package com.tianqi.aitdwallet.adapter.pager_adapter;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.tianqi.aitdwallet.R;
import com.tianqi.aitdwallet.bean.CurrencyCardBean;
import com.tianqi.aitdwallet.ui.activity.wallet.property.CoinAddressQrActivity;
import com.tianqi.aitdwallet.utils.Constants;
import com.tianqi.baselib.utils.Constant;
import com.tianqi.baselib.utils.display.GlideUtils;
import com.tianqi.baselib.utils.display.ToastUtil;

import java.util.List;

public class ViewPagerCardAdapter extends PagerAdapter {
    ImageView ivCoin;
    TextView tvCoinName;
    TextView tvCoinAddress;
    ImageView ivAddressCopy;
    ImageView ivQrCode;
    TextView tvCoinFiatValue;
    ConstraintLayout layoutCurrencyCard;
    private List<CurrencyCardBean> mList;
    private Context mContext;
    private OnViewPagerItemClickListener onViewPagerItemClickListener;
    private Typeface typeFace;

    public ViewPagerCardAdapter(List<CurrencyCardBean> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mList.size();
    }


    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_viewpager, container, false);
        container.addView(view);

        layoutCurrencyCard=view.findViewById(R.id.layout_currency_card);
        ivCoin=view.findViewById(R.id.iv_coin);
        tvCoinName=view.findViewById(R.id.tv_coin_name);
        ivAddressCopy=view.findViewById(R.id.iv_address_copy);
        ivQrCode=view.findViewById(R.id.iv_qr_code);
        tvCoinFiatValue=view.findViewById(R.id.tv_coin_fiat_value);
        tvCoinAddress=view.findViewById(R.id.tv_coin_address);


        GlideUtils.loadResourceImage(mContext,mList.get(position).getCoin_icon_white_id(),ivCoin);
        layoutCurrencyCard.setBackground(mContext.getResources().getDrawable(mList.get(position).getCurrency_bg_id()));
        typeFace = Typeface.createFromAsset(mContext.getAssets(), Constant.FONT_PATH);

        tvCoinName.setText(mList.get(position).getCoin_alias_name());
        tvCoinAddress.setText(mList.get(position).getCoin_address());
        tvCoinFiatValue.setText(mList.get(position).getCurrency_to_fiat_num());

        ivQrCode.setOnClickListener(v -> {
            onViewPagerItemClickListener.onViewPagerItemClick(position);

            Intent intent=new Intent(mContext, CoinAddressQrActivity.class);
            intent.putExtra(Constants.TRANSACTION_COIN_ADDRESS,mList.get(position).getCoin_address());
            intent.putExtra(Constants.TRANSACTION_COIN_NAME,mList.get(position).getCoin_name());
            mContext.startActivity(intent);
        });

        ivAddressCopy.setOnClickListener(view1 -> {
            ClipboardManager cmb = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
            cmb.setText(tvCoinAddress.getText().toString());
            ToastUtil.showToast(mContext,mContext.getString(R.string.notice_copy_success));
        });
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public interface OnViewPagerItemClickListener {
        void onViewPagerItemClick(int position);
    }

    public void setOnShowItemClickListener(OnViewPagerItemClickListener onViewPagerItemClickListener) {
        this.onViewPagerItemClickListener = onViewPagerItemClickListener;
    }

}
