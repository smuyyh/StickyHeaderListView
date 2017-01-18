package com.yuyh.stickyheader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yuyh.stickyheader.view.StickyHeaderListView;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MainActivity extends AppCompatActivity {

    private StickyHeaderListView mListView;
    private SectionAdapter mAdapter;
    private CheckBox mCheckAll;

    private boolean isOnLoadMore = false;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initListView();

        initChechBox();
    }

    private void initListView() {
        mListView = (StickyHeaderListView) findViewById(R.id.lv);

        Entity row = new Gson().fromJson(json, Entity.class);

        mListView.setAdapter(mAdapter = new SectionAdapter(this, row.rows));

        mAdapter.setOnValueChangedListener(new OnValueChangeListener() {
            @Override
            public void onChange(int totalCount, double totalAmount, boolean isCheckAll) {
                BigDecimal bd = new BigDecimal(totalAmount).setScale(2, RoundingMode.UP);
                ((TextView) findViewById(R.id.tv_desc)).setText(totalCount + "个行程，共" + bd.doubleValue() + "元");
                // 防止调用onCheckedChanged
                mCheckAll.setOnCheckedChangeListener(null);
                mCheckAll.setChecked(isCheckAll);
                mCheckAll.setOnCheckedChangeListener(onCheckedChangeListener);
            }
        });

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public synchronized void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        // 判断滚动到底部
                        if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                            if (!isOnLoadMore) {

                                if(count >= 3){
                                    Toast.makeText(MainActivity.this, "没有更多数据啦~", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                count++;

                                isOnLoadMore = true;

                                Entity row = new Gson().fromJson(json, Entity.class);
                                mAdapter.addData(row.rows);

                                isOnLoadMore = false;
                            }
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    private void initChechBox() {
        mCheckAll = (CheckBox) findViewById(R.id.cb_check_all);
        mCheckAll.setOnCheckedChangeListener(onCheckedChangeListener);
    }

    CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            mAdapter.setCheckAll(isChecked);
        }
    };

    String json = "{\n" +
            "    \"rows\":[\n" +
            "        {\n" +
            "            \"timestamp\":1484060400,\n" +
            "            \"src\":\"杭州-茂源大厦\",\n" +
            "            \"dest\":\"杭州-高新软件园\",\n" +
            "            \"amount\":24.01\n" +
            "        },\n" +
            "        {\n" +
            "            \"timestamp\":1483974000,\n" +
            "            \"src\":\"杭州-茂源大厦\",\n" +
            "            \"dest\":\"杭州-高新软件园\",\n" +
            "            \"amount\":24.01\n" +
            "        },\n" +
            "        {\n" +
            "            \"timestamp\":1483887600,\n" +
            "            \"src\":\"杭州-茂源大厦\",\n" +
            "            \"dest\":\"杭州-高新软件园\",\n" +
            "            \"amount\":24.01\n" +
            "        },\n" +
            "        {\n" +
            "            \"timestamp\":1483628400,\n" +
            "            \"src\":\"杭州-茂源大厦\",\n" +
            "            \"dest\":\"杭州-高新软件园\",\n" +
            "            \"amount\":24.01\n" +
            "        },\n" +
            "        {\n" +
            "            \"timestamp\":1481814000,\n" +
            "            \"src\":\"杭州-阿里巴巴滨江园区\",\n" +
            "            \"dest\":\"杭州-高新软件园\",\n" +
            "            \"amount\":25.00\n" +
            "        },\n" +
            "        {\n" +
            "            \"timestamp\":1479222000,\n" +
            "            \"src\":\"杭州-茂源大厦\",\n" +
            "            \"dest\":\"杭州-阿里巴巴滨江园区\",\n" +
            "            \"amount\":28.01\n" +
            "        },\n" +
            "        {\n" +
            "            \"timestamp\":1477407600,\n" +
            "            \"src\":\"杭州-龙翔桥\",\n" +
            "            \"dest\":\"杭州-定安路\",\n" +
            "            \"amount\":19.20\n" +
            "        },\n" +
            "        {\n" +
            "            \"timestamp\":1475039023,\n" +
            "            \"src\":\"杭州-茂源大厦\",\n" +
            "            \"dest\":\"杭州-高新软件园\",\n" +
            "            \"amount\":24.01\n" +
            "        },\n" +
            "        {\n" +
            "            \"timestamp\":1474779823,\n" +
            "            \"src\":\"杭州-茂源大厦\",\n" +
            "            \"dest\":\"杭州-高新软件园\",\n" +
            "            \"amount\":24.01\n" +
            "        },\n" +
            "        {\n" +
            "            \"timestamp\":1474347823,\n" +
            "            \"src\":\"杭州-武林广场\",\n" +
            "            \"dest\":\"杭州-西湖文化广场\",\n" +
            "            \"amount\":24.10\n" +
            "        },\n" +
            "        {\n" +
            "            \"timestamp\":1468991023,\n" +
            "            \"src\":\"杭州-星光大道\",\n" +
            "            \"dest\":\"杭州-滨康路\",\n" +
            "            \"amount\":23.57\n" +
            "        },\n" +
            "        {\n" +
            "            \"timestamp\":1466399143,\n" +
            "            \"src\":\"杭州-AppStore体验店\",\n" +
            "            \"dest\":\"杭州-高新软件园\",\n" +
            "            \"amount\":20\n" +
            "        },\n" +
            "        {\n" +
            "            \"timestamp\":1465967143,\n" +
            "            \"src\":\"杭州-茂源大厦\",\n" +
            "            \"dest\":\"杭州-西湖\",\n" +
            "            \"amount\":81.07\n" +
            "        },\n" +
            "        {\n" +
            "            \"timestamp\":1465362343,\n" +
            "            \"src\":\"杭州-星光大道\",\n" +
            "            \"dest\":\"杭州-滨康路\",\n" +
            "            \"amount\":23.57\n" +
            "        },\n" +
            "        {\n" +
            "            \"timestamp\":1465275943,\n" +
            "            \"src\":\"杭州-AppStore体验店\",\n" +
            "            \"dest\":\"杭州-高新软件园\",\n" +
            "            \"amount\":20\n" +
            "        },\n" +
            "        {\n" +
            "            \"timestamp\":1465103143,\n" +
            "            \"src\":\"杭州-茂源大厦\",\n" +
            "            \"dest\":\"杭州-西湖\",\n" +
            "            \"amount\":81.07\n" +
            "        },\n" +
            "        {\n" +
            "            \"timestamp\":1462424743,\n" +
            "            \"src\":\"杭州-星光大道\",\n" +
            "            \"dest\":\"杭州-滨康路\",\n" +
            "            \"amount\":23.57\n" +
            "        },\n" +
            "        {\n" +
            "            \"timestamp\":1462125943,\n" +
            "            \"src\":\"杭州-AppStore体验店\",\n" +
            "            \"dest\":\"杭州-高新软件园\",\n" +
            "            \"amount\":20\n" +
            "        },\n" +
            "        {\n" +
            "            \"timestamp\":1462035943,\n" +
            "            \"src\":\"杭州-茂源大厦\",\n" +
            "            \"dest\":\"杭州-西湖\",\n" +
            "            \"amount\":81.07\n" +
            "        }\n" +
            "    ]\n" +
            "}";
}
