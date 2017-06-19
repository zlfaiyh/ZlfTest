package com.example.zlf.zlftest.testRecycleView.baseadapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.zlf.zlftest.testRecycleView.TestBean;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Daimhim on 2017/4/13.
 */

public abstract class ItemLineartHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener, CurrencyRecyclerAdapter.RViewAdapter<TestBean, MultiViewHolder> {
    protected final static int TYPE_RIGHT_IMG = 150000; //左文右图
    protected final static int TYPE_UP_IMG = 150001; //上图下文


    public Context mContext;
    protected TestBean bean;
    protected List<String> mBeanList;

    public View title_line;
    public RelativeLayout rl_title;
    public TextView tv_title_left;
    public TextView tv_title_centent;
    public TextView tv_title_right;
    public ImageView iv_title;
    public View line;
    public RecyclerView mRecyclerView;
    public View line1;
    public LayoutInflater mLayoutInflater;
    public ImageView ivTitleTop;
//    private ItemViewType mItemViewType;
    public CurrencyRecyclerAdapter<TestBean, MultiViewHolder> mRecyclerAdapter;
    private Map<String, String> mNameMap;

    public ItemLineartHolder(View itemView) {
        super(itemView);
        this.mContext = itemView.getContext();
        initItemView(itemView);
    }

    protected void initItemView(View itemView) {

    }

    public void onRefresh(TestBean bean) {
        this.bean=bean;
        testingTitle();
        this.mBeanList.clear();
        this.mBeanList.addAll(bean.getList());
        mRecyclerAdapter.notifyDataSetChanged();
        tv_title_centent.setText(getItemName());
    }


    //初始化统一视图，若标题布局不同可选择不执行
    public View initView(View v) {
        title_line = v.findViewById(R.id.title_line);
        rl_title = (RelativeLayout) v.findViewById(R.id.rl_title);
        tv_title_left = (TextView) v.findViewById(R.id.tv_title_left);
        tv_title_centent = (TextView) v.findViewById(R.id.tv_title_centent);
        tv_title_right = (TextView) v.findViewById(R.id.tv_title_right);
        iv_title = (ImageView) v.findViewById(R.id.iv_title);
        line = v.findViewById(R.id.line);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerview_layoutId);
        line1 = v.findViewById(R.id.line1);
        ivTitleTop= (ImageView) v.findViewById(R.id.iv_title_top);
        mBeanList = new ArrayList<>();
        mRecyclerAdapter = new CurrencyRecyclerAdapter<>(mContext, mBeanList, -1, this);
        mRecyclerView.setAdapter(mRecyclerAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext,getColumnCount());
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        RecycleDecoration builder = DecorationBuilder.Builder(new GridDecoration())
                .setOrientation(RecycleDecoration.VERTICAL_CROSS)
                .setContext(mContext)
                .setDividerColor(R.color.light_dark_gray)
                .setDividerWidth(1)
                .builder();
        mRecyclerView.addItemDecoration(builder);

        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int itemViewType = getItemViewType(position, null);
                return itemViewType == TYPE_RIGHT_IMG ? 2 : 1;
            }
        });
        return v;
    }
    public int getColumnCount(){
        return 4;
    }
    public void testingTitle() {
        if (null != bean && !TextUtils.isEmpty(bean.imgUrl)) {
            iv_title.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams layoutParams = iv_title
                    .getLayoutParams();
            layoutParams.width = SystemUtil.getScreenWidth(mContext);
            layoutParams.height = (int) (layoutParams.width * 120 / 750);// 设置幻灯片的宽高比例
            iv_title.setLayoutParams(layoutParams);
            Glide.with(mContext)
                    .load("")
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .centerCrop()
                    .placeholder(ContextCompat.getDrawable(mContext, R.drawable.defalut_banner))
                    .into(iv_title);
            if (!TextUtils.isEmpty(bean.status) && "1".equals(bean.status)) {
                iv_title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, WebPageActivity.class);
                        intent.putExtra("startUrl", "file:///android_asset/widget/html/goodsDeta_win.html");
                        intent.putExtra("serviceUrl", StartApp.JAVA_BASE_TO_SERVICE);
                        intent.putExtra("goodsId", bean.goodsId);
                        intent.putExtra("goodsName", bean.imgUrl);//广告这里没有orderSource
//                        if (!TextUtils.isEmpty(bean.orderSource)) {
//                            intent.putExtra("orderSource", bean.orderSource);
//                        }
                        mContext.startActivity(intent);
                    }
                });
            }
        }
    }

    @Override
    public int getItemViewType(int position, itemInfoBean itemInfoBean) {
        return TYPE_UP_IMG;
    }


    //子类获取数据
    public List<itemInfoBean> getBeanList() {
        return mBeanList;
    }

    //子类获取当前Iten的Name
    public String getItemName() {
        Log.i("00000000s", bean.toString());
        return TextUtils.isEmpty(bean.itemName) ? "未知标题" : bean.itemName;
        //return "未知标题";
    }

    //子类获取当前Item的ID
    public String getBeanId() {
        return TextUtils.isEmpty(bean.itemId) ? "1" : bean.itemId;
    }

    //返回相应ViewType的ViewHolder
    @Override
    public MultiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MultiViewHolder holder = null;
        switch (viewType) {
            //新版本迭代的item
            case TYPE_RIGHT_IMG: //左文又图
                holder = new MultiViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_right_img, parent, false), viewType);
                break;
            case TYPE_UP_IMG: //上图下文
                holder = new MultiViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_up_img, parent, false), viewType);
                break;
        }
        return holder;
    }
    //包装onBindViewHolder
    @Override
    public void onBindViewHolder(MultiViewHolder holder, int position, itemInfoBean itemInfoBean) {
        switch (getItemViewType(position, itemInfoBean)) {
            //新版本布局
            case TYPE_RIGHT_IMG: //左右布局比上下布局多了一个规格显示
                TextView tvSpecName= (TextView) holder.itemView.findViewById(R.id.tv_specName);
                tvSpecName.setText(itemInfoBean.defaultSpecName);
                if(tvSpecName.getText().length()>0){
                    tvSpecName.setVisibility(View.VISIBLE);
                }else{
                    tvSpecName.setVisibility(View.GONE);
                }
            case TYPE_UP_IMG:
                ImageView iv = (ImageView) holder.itemView.findViewById(R.id.iv_img);

                Glide.with(mContext)
                        .load(AddressManagement.getPicturesPath(itemInfoBean.goodsImg))
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(iv);

                if("1".equals(itemInfoBean.isConsumer)){
                    holder.itemView.findViewById(R.id.iv_coucon_state).setVisibility(View.VISIBLE);
                }else{
                    holder.itemView.findViewById(R.id.iv_coucon_state).setVisibility(View.GONE);
                }
                TextView tvName = (TextView) holder.itemView.findViewById(R.id.tv_name);

                tvName.setText(itemInfoBean.goodsName);
                if(tvName.getText().length()>0){
                    tvName.setVisibility(View.VISIBLE);
                }else{
                    tvName.setVisibility(View.GONE);
                }
                TextView tvPrice = (TextView) holder.itemView.findViewById(R.id.tv_price);

                if(TextUtils.isEmpty(itemInfoBean.goodsPrice)){
                    tvPrice.setVisibility(View.GONE);
                }else{
                    tvPrice.setVisibility(View.VISIBLE);
                }
                tvPrice.setText("¥"+ EaseCommonUtils.subZeroAndDot(itemInfoBean.goodsPrice) /*+ "/" + itemInfoBean.goodsUnit*/);

                holder.itemView.setOnClickListener(new onItemClick(position, itemInfoBean));
                break;
        }
    }

    //统一处理RecyclerView的Item点击事件,如有不同可重写
    public void onItemClick(View v, int position, itemInfoBean bean) {
        Intent intent = new Intent(mContext, WebPageActivity.class);
        intent.putExtra("startUrl", "file:///android_asset/widget/html/goodsDeta_win.html");
        intent.putExtra("serviceUrl", StartApp.JAVA_BASE_TO_SERVICE);
        intent.putExtra("goodsId", bean.goodsId);
        intent.putExtra("goodsName", bean.goodsName);
        if (!TextUtils.isEmpty(bean.orderSource)) {
            intent.putExtra("orderSource", bean.orderSource);
        }
        mContext.startActivity(intent);
    }

    //自定义点击事件
    public class onItemClick implements View.OnClickListener {
        private int position;
        private itemInfoBean mBean;

        public onItemClick(int position, itemInfoBean bean) {
            this.position = position;
            this.mBean = bean;
        }

        @Override
        public void onClick(View v) {
            onItemClick(v, position, mBean);
        }

    }

    public void initName() {
        mNameMap = new HashMap<>();
        mNameMap.put("1", "时令抢鲜");
        mNameMap.put("2", "新品预售");
        mNameMap.put("3", "限时抢购");
        mNameMap.put("4", "特色推荐");
        mNameMap.put("12", "特色馆");
        mNameMap.put("5", "人气热销");
        mNameMap.put("6", "调味首选");
        mNameMap.put("11", "特色酱菜");
        mNameMap.put("7", "绿色果蔬");
        mNameMap.put("8", "精品干货");
        mNameMap.put("9", "原生态土特产");
        mNameMap.put("10", "名优小吃");
        mNameMap.put("13", "区县特产馆");
    }

    private String getDefaultName(String itemId) {
        if (null == mNameMap || 0 == mNameMap.size()) {
            initName();
        }
        if (mNameMap.containsKey(itemId)) {
            return mNameMap.get(itemId);
        } else {
            return "未知标题";
        }
    }

}
