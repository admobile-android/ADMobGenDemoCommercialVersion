package cn.ecookshipuji.nativead;

/**
 * @author : ciba
 * @date : 2018/7/13
 * @description : replace your description
 */

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.admob.admobgensdk.ad.AdLogoUtil;
import cn.admob.admobgensdk.ad.listener.ADMobGenNativeUnifiedVideoListener;
import cn.admob.admobgensdk.biz.widget.nativeunified.ADMobGenNativeUnifiedContainer;
import cn.admob.admobgensdk.entity.IADMobGenNativeUnifiedAd;
import cn.ecookshipuji.R;

/**
 * RecyclerView的Adapter
 */
public class NativeUnifiedCustomAdapter extends RecyclerView.Adapter<NativeUnifiedCustomAdapter.CustomViewHolder> {
    private static final String TAG = "ADMobGen_Log";
    static final int TYPE_DATA = 0;
    static final int TYPE_AD = 1;
    private List<Object> mData;

    public NativeUnifiedCustomAdapter(List<Object> list) {
        this.mData = list;
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position) instanceof IADMobGenNativeUnifiedAd ? TYPE_AD : TYPE_DATA;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder customViewHolder, final int position) {
        int type = getItemViewType(position);
        if (TYPE_AD == type) {
            IADMobGenNativeUnifiedAd nativeUnifiedAd = (IADMobGenNativeUnifiedAd) mData.get(position);
            customViewHolder.tvTitle.setText(nativeUnifiedAd.getTitle());
            customViewHolder.tvSubTitle.setText(nativeUnifiedAd.getDescription());

            View mediaView = nativeUnifiedAd.getMediaView();
            if (mediaView != null) {
                customViewHolder.ivImage.setVisibility(View.GONE);
                customViewHolder.flMedia.setVisibility(View.VISIBLE);

                if (customViewHolder.flMedia.getChildCount() > 0 && customViewHolder.flMedia.getChildAt(0) == mediaView) {
                    return;
                }

                if (customViewHolder.flMedia.getChildCount() > 0) {
                    customViewHolder.flMedia.removeAllViews();
                }

                if (mediaView.getParent() != null) {
                    ((ViewGroup) mediaView.getParent()).removeView(mediaView);
                }
                customViewHolder.flMedia.addView(mediaView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            } else {
                customViewHolder.ivImage.setVisibility(View.VISIBLE);
                customViewHolder.flMedia.setVisibility(View.GONE);
                List<String> imageList = nativeUnifiedAd.getImageList();
                Glide.with(customViewHolder.ivImage.getContext())
                        .load(imageList != null && imageList.size() > 0 ? imageList.get(0) : "")
                        .into(customViewHolder.ivImage);
            }

            // 根据平台获取广告来源标识图标，第二个参数是这个图标是带有广告两个字还是不带
            customViewHolder.ivLogo.setImageResource(AdLogoUtil.getPlatformAdLogo(nativeUnifiedAd.getPlatform(), true));

            // 如果是视频类型的广告，添加视频的监听,其回调不一定都有
            if (nativeUnifiedAd.isVideo()) {
                nativeUnifiedAd.registerVideoLister(new ADMobGenNativeUnifiedVideoListener() {
                    @Override
                    public void onVideoError(IADMobGenNativeUnifiedAd iadMobGenNativeUnifiedAd, String s) {
                        Log.d(TAG, "onVideoError: " + s);
                    }

                    @Override
                    public void onVideoLoad(IADMobGenNativeUnifiedAd iadMobGenNativeUnifiedAd) {

                    }

                    @Override
                    public void onVideoStart(IADMobGenNativeUnifiedAd iadMobGenNativeUnifiedAd) {

                    }

                    @Override
                    public void onVideoPause(IADMobGenNativeUnifiedAd iadMobGenNativeUnifiedAd) {

                    }

                    @Override
                    public void onVideoComplete(IADMobGenNativeUnifiedAd iadMobGenNativeUnifiedAd) {

                    }
                });
            }

            nativeUnifiedAd.registerViewForInteraction((ADMobGenNativeUnifiedContainer) customViewHolder.itemView);
        } else {
            customViewHolder.title.setText((mData.get(position) + ""));
        }
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        int layoutId = (viewType == TYPE_AD) ? R.layout.item_native_unified_ad : R.layout.item_data;
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layoutId, viewGroup, false);
        return new CustomViewHolder(view);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        public TextView title;

        public TextView tvTitle;
        public TextView tvSubTitle;
        public ImageView ivImage;
        public FrameLayout flMedia;
        public ImageView ivLogo;

        public CustomViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            tvTitle = view.findViewById(R.id.tvTitle);
            tvSubTitle = view.findViewById(R.id.tvSubTitle);
            ivImage = view.findViewById(R.id.ivImage);
            flMedia = view.findViewById(R.id.flMedia);
            ivLogo = view.findViewById(R.id.ivLogo);
        }
    }
}