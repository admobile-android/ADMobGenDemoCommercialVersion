package cn.ecookshipuji.information;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import cn.admob.admobgensdk.ad.constant.InformationAdType;
import cn.admob.admobgensdk.ad.information.ADMobGenInformation;
import cn.admob.admobgensdk.ad.information.IADMobGenInformation;
import cn.admob.admobgensdk.ad.listener.SimpleADMobGenInformationAdListener;
import cn.admob.admobgensdk.common.ADMobGenSDK;
import cn.admob.admobgensdk.entity.TTNativeExpressParam;
import cn.ecookshipuji.MyApplication;
import cn.ecookshipuji.R;
import cn.ecookshipuji.widget.MySmartRefreshLayout;

/**
 * @author : ciba
 * @date : 2018/6/23
 * @description : 信息流广告是模板广告，返回的都是某种固定样式的广告视图
 */

public class InformationActivity extends Activity implements OnRefreshLoadMoreListener {
    private static final String TAG = "ADMobGen_Log";
    private List<Object> mDataList = new ArrayList<>();
    private CustomAdapter mAdapter;
    private MySmartRefreshLayout refreshLayout;
    private ADMobGenInformation adMobGenInformation;
    private int loadType;
    private int startPosition;

    public static void jumpHere(Context context, int type) {
        Intent intent = new Intent(context, InformationActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        refreshLayout = findViewById(R.id.refreshLayout);
        RecyclerView mRecyclerView = findViewById(R.id.lvList);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

//        默认信息流样式为上图下文
//        adMobGenInformation = new ADMobGenInformation(this);

        int informationAdType = getIntent().getIntExtra("type", InformationAdType.NORMAL);

        // 建议Activity设置configChanges
        // 第三个参数是广告位序号（默认为0，用于支持单样式多广告位，无需要可以填0或者使用其他构造方法）
        adMobGenInformation = new ADMobGenInformation(this, informationAdType, MyApplication.adIndex);

        // TODO: 2019/12/1之后，头条信息流只能新建模板广告，我们将无法对头条的信息流视图进行二次封装，微调也将无法支持头条，只能在后台调整。
        // 这之前创建的头条信息流广告位可以正常使用，微调逻辑可正常
        // TTNativeExpressParam参数为头条模板广告视图期望的宽度(单位为dp，即如果视图宽度希望是200dp则传入200)，小于等于0将传入屏幕宽度
        int ttWidth = (int) (ADMobGenSDK.instance().getScreenWidth(this) / getResources().getDisplayMetrics().density);
        TTNativeExpressParam ttNativeExpressParam = new TTNativeExpressParam(ttWidth);
        // 设置头条模板广告视图期望的高度，默认为0，0为自适应
        ttNativeExpressParam.setHeight(0);
        // 通过setTTNativeExpressParam方法设置为头条新版本的模板广告，在这之前(2019/12/01)创建的头条广告位或没有接入头条平台无需进行此切换
        adMobGenInformation.setTTNativeExpressParam(ttNativeExpressParam);

        // 设置广告曝光校验最小间隔时间(0~200)，默认为200ms，在RecyclerView或ListView这种列表中不建议设置更小值，在一些特定场景（如Dialog或者固定位置可根据要求设置更小值）
        // adMobGenInformation.setExposureDelay(200);

        adMobGenInformation.setListener(new SimpleADMobGenInformationAdListener() {
            @Override
            public void onADReceiv(IADMobGenInformation adMobGenInformation) {
                Log.e(TAG, "信息流广告获取成功 ::::: ");
                finishLoad(adMobGenInformation);
            }

            @Override
            public void onADExposure(IADMobGenInformation adMobGenInformation) {
                Log.e(TAG, "广告展示曝光回调，但不一定是曝光成功了，比如一些网络问题导致上报失败 	::::: ");
            }

            @Override
            public void onADClick(IADMobGenInformation adMobGenInformation) {
                Log.e(TAG, "广告被点击 ::::: ");
            }

            @Override
            public void onADFailed(String error) {
                Log.e(TAG, "广告数据获取失败时回调 ::::: " + error);
                finishLoad(null);
            }

            @Override
            public void onADClose(IADMobGenInformation iadMobGenInformation) {
                Log.e(TAG, "广告关闭回调 ::::: ");
                removeInformationAd(iadMobGenInformation);
            }

            @Override
            public void onADRenderFailed(IADMobGenInformation iadMobGenInformation) {
                // 渲染失败可以移除该广告对象
                Log.e(TAG, "广告渲染失败回调 ::::: ");
                removeInformationAd(iadMobGenInformation);
            }
        });
        mAdapter = new CustomAdapter(mDataList);
        mRecyclerView.setAdapter(mAdapter);

        refreshLayout.setOnRefreshLoadMoreListener(this);
        refreshLayout.autoRefresh();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        loadType = MySmartRefreshLayout.TYPE_LOAD_MORE;
        loadData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        loadType = MySmartRefreshLayout.TYPE_FRESH;
        clearData();
        loadData();
    }

    @Override
    protected void onDestroy() {
        // 广告资源释放
        if (adMobGenInformation != null) {
            adMobGenInformation.destroy();
            adMobGenInformation = null;
        }
        super.onDestroy();
    }

    /**
     * 模拟获取数据
     */
    private void loadData() {
        startPosition = mDataList.size();

        // 模拟数据请求
        List<String> tempDataList = new ArrayList<>();
        for (int i = 0; i < 20; ++i) {
            tempDataList.add("No." + i + " Normal Data");
        }
        // 将模拟数据添加到集合中
        mDataList.addAll(tempDataList);
        // 更新适配器
        notifyAdapter();

        // 开始加载广告
        startPosition = mDataList.size();
        adMobGenInformation.loadAd();
    }

    /**
     * 获取结束
     *
     * @param adMobGenInformation ：广告对象
     */
    private void finishLoad(IADMobGenInformation adMobGenInformation) {
        refreshLayout.finish(loadType, adMobGenInformation != null, false);
        if (adMobGenInformation != null) {
            mDataList.add(adMobGenInformation);
            notifyAdapter();
        }
    }

    /**
     * 刷新适配器
     */
    private void notifyAdapter() {
        if (startPosition <= 0) {
            mAdapter.notifyDataSetChanged();
        } else {
            mAdapter.notifyItemRangeInserted(startPosition + 1, mDataList.size() - startPosition);
        }
    }

    /**
     * 清除数据
     */
    private void clearData() {
        mDataList.clear();
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 移除该广告
     */
    private void removeInformationAd(IADMobGenInformation iadMobGenInformation) {
        if (iadMobGenInformation != null) {
            mDataList.remove(iadMobGenInformation);
            mAdapter.notifyDataSetChanged();
        }
    }
}
