package cn.ecookshipuji.nativead;

/**
 * @author : ciba
 * @date : 2018/7/13
 * @description : replace your description
 */

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import cn.admob.admobgensdk.ad.AdLogoUtil;
import cn.admob.admobgensdk.entity.IADMobGenNativeAd;
import cn.ecookshipuji.R;

/**
 * RecyclerView的Adapter
 */
public class NativeCustomAdapter extends RecyclerView.Adapter<NativeCustomAdapter.CustomViewHolder> {
    static final int TYPE_DATA = 0;
    static final int TYPE_AD = 1;
    private final Activity activity;
    private List<Object> mData;

    public NativeCustomAdapter(Activity activity, List<Object> list) {
        this.activity = activity;
        this.mData = list;
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position) instanceof IADMobGenNativeAd ? TYPE_AD : TYPE_DATA;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder customViewHolder, final int position) {
        int type = getItemViewType(position);
        if (TYPE_AD == type) {
            IADMobGenNativeAd nativeAd = (IADMobGenNativeAd) mData.get(position);

            customViewHolder.tvTitle.setText(nativeAd.getTitle());
            customViewHolder.tvSubTitle.setText(nativeAd.getDescription());

            // 可以被点击的view, 也可以把convertView放进来意味整个item可被点击，点击会跳转到落地页
            List<View> clickViews = new ArrayList<>();
            clickViews.add(customViewHolder.itemView);

            // 创意点击区域的view 点击根据不同的创意进行下载或拨打电话动作
            // 如果需要点击图文区域也能进行下载或者拨打电话动作，请将图文区域的view传入creativeViewList
            List<View> creativeClickViews = new ArrayList<>();
            creativeClickViews.add(customViewHolder.itemView);

            nativeAd.registerViewForInteraction((ViewGroup) customViewHolder.itemView
                    , clickViews
                    , creativeClickViews);

            Glide.with(customViewHolder.ivImage.getContext())
                    .load(nativeAd.getImage())
                    .into(customViewHolder.ivImage);

            // 根据平台获取广告来源标识图标，第二个参数是这个图标是带有广告两个字还是不带
            customViewHolder.ivLogo.setImageResource(AdLogoUtil.getPlatformAdLogo(nativeAd.getPlatform(), true));
        } else {
            customViewHolder.title.setText((mData.get(position) + ""));
        }
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        int layoutId = (viewType == TYPE_AD) ? R.layout.item_native_ad : R.layout.item_data;
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layoutId, viewGroup, false);
        return new CustomViewHolder(view);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        public TextView title;

        public TextView tvTitle;
        public TextView tvSubTitle;
        public ImageView ivImage;
        public ImageView ivLogo;

        public CustomViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            tvTitle = view.findViewById(R.id.tvTitle);
            tvSubTitle = view.findViewById(R.id.tvSubTitle);
            ivImage = view.findViewById(R.id.ivImage);
            ivLogo = view.findViewById(R.id.ivLogo);
        }
    }
}