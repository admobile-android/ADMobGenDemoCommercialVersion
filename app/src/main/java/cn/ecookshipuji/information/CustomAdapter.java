package cn.ecookshipuji.information;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.admob.admobgensdk.ad.information.IADMobGenInformation;
import cn.ecookshipuji.R;

/**
 * RecyclerView的Adapter
 */
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    static final int TYPE_DATA = 0;
    static final int TYPE_AD = 1;
    private List<Object> mData;

    public CustomAdapter(List list) {
        mData = list;
    }

    @Override
    public int getItemCount() {
        if (mData != null) {
            return mData.size();
        } else {
            return 0;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position) instanceof IADMobGenInformation ? TYPE_AD : TYPE_DATA;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder customViewHolder, final int position) {
        int type = getItemViewType(position);
        if (TYPE_AD == type) {
            final IADMobGenInformation information = (IADMobGenInformation) mData.get(position);
            if (information == null || information.isDestroy()) {
                customViewHolder.container.removeAllViews();
                return;
            }

            View informationAdView = information.getInformationAdView();
            if (customViewHolder.container.getChildCount() > 0 && customViewHolder.container.getChildAt(0) == informationAdView) {
                return;
            }

            if (customViewHolder.container.getChildCount() > 0) {
                customViewHolder.container.removeAllViews();
            }

            if (informationAdView.getParent() != null) {
                ((ViewGroup) informationAdView.getParent()).removeView(informationAdView);
            }

            customViewHolder.container.addView(informationAdView);
            information.render();
        } else {
            customViewHolder.title.setText((mData.get(position) + ""));
        }
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        int layoutId = (viewType == TYPE_AD) ? R.layout.item_express_ad : R.layout.item_data;
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layoutId, viewGroup, false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ViewGroup container;

        public CustomViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            container = view.findViewById(R.id.express_ad_container);
        }
    }
}
