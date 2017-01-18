package com.yuyh.stickyheader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.yuyh.stickyheader.view.StickyHeaderAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SectionAdapter extends StickyHeaderAdapter {

    private Context mContext;
    private OnValueChangeListener listener;

    private List<Entity.Row> entityRows;

    private LinkedHashMap<String, List<Entity.Row>> map = new LinkedHashMap<String, List<Entity.Row>>();

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy年-MM月", Locale.getDefault());
    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

    public SectionAdapter(Context context, List<Entity.Row> entityRows) {
        this.mContext = context;
        this.entityRows = entityRows;

        addData(entityRows);
    }

    /**
     * 添加数据，并进行分类
     *
     * @param list
     */
    public void addData(List<Entity.Row> list) {
        for (Entity.Row row : list) {

            String head = sdf.format(new Date(row.timestamp * 1000)); // time

            if (map.get(head) == null) {
                List<Entity.Row> newRows = new ArrayList<>();
                newRows.add(row);
                map.put(head, newRows);
            } else {
                List<Entity.Row> newRows = map.get(head);
                newRows.add(row);
          }
        }

        updateValue();
        notifyDataSetChanged();
    }

    public void setCheckAll(boolean checkAll) {
        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, List<Entity.Row>> entry = (Map.Entry<String, List<Entity.Row>>) iter.next();
            String key = entry.getKey();
            List<Entity.Row> val = entry.getValue();

            for (Entity.Row row : val) {
                row.isChecked = checkAll;
            }
        }

        updateValue();

        notifyDataSetChanged();
    }

    public void setOnValueChangedListener(OnValueChangeListener listener) {
        this.listener = listener;
    }

    private void updateValue() {
        if (listener == null)
            return;

        int count = 0;
        double amount = 0;
        boolean isCheckAll = true;

        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, List<Entity.Row>> entry = (Map.Entry<String, List<Entity.Row>>) iter.next();
            String key = entry.getKey();
            List<Entity.Row> val = entry.getValue();

            for (Entity.Row row : val) {
                if (row.isChecked) {
                    count++;
                    amount += row.amount;
                } else {
                    isCheckAll = false;
                }
            }
        }

        listener.onChange(count, amount, isCheckAll);
    }

    @Override
    public int sectionCounts() {
        return map.keySet().toArray().length;
    }

    @Override
    public int rowCounts(int section) {
        if (section < 0)
            return 0;

        Object[] key = map.keySet().toArray();


        return map.get(key[section]).size();
    }

    @Override
    public View getRowView(int section, int row, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_row, null);

        Object[] keys = map.keySet().toArray();
        String key = (String) keys[section];
        final Entity.Row item = map.get(key).get(row);

        ((TextView) view.findViewById(R.id.time)).setText(sdf1.format(new Date(item.timestamp * 1000)));
        ((TextView) view.findViewById(R.id.src)).setText(item.src);
        ((TextView) view.findViewById(R.id.dest)).setText(item.dest);
        ((TextView) view.findViewById(R.id.amount)).setText(item.amount + "");
        final CheckBox checkBox = ((CheckBox) view.findViewById(R.id.cb));
        checkBox.setChecked(item.isChecked);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                item.isChecked = isChecked;
                updateValue();
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBox.setChecked(!checkBox.isChecked());
            }
        });

        return view;
    }

    @Override
    public Object getRowItem(int section, int row) {
        if (section < 0)
            return null;

        Object[] key = map.keySet().toArray();

        return map.get((String) key[section]).get(row);
    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_header, null);

        Object[] keys = map.keySet().toArray();

        String key = (String) keys[section];

        ((TextView) view.findViewById(R.id.month)).setText(key.split("-")[1]);

        return view;
    }

    @Override
    public boolean hasSectionHeaderView(int section) {
        return true;
    }
}