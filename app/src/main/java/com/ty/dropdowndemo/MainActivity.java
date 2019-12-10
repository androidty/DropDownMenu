package com.ty.dropdowndemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ty.DropBean;
import com.ty.adapter.MyAdapter;
import com.ty.dropdownmenu2.DropDownMenu;
import com.ty.listener.OnMenuClickListener;
import com.ty.listener.OnMenuListSelectedListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMenuClickListener {
    private DropDownMenu mDropDownMenu;
    private int city_index;
    private int sex_index;
    private int age_index;
    List<String[]> items = new ArrayList<>();
    final String[] arr1 = new String[]{"全部城市", "北京", "上海", "广州", "深圳"};
    final String[] arr2 = new String[]{"性别", "男", "女"};
    final String[] arr3 = new String[]{"全部年龄", "10", "20", "30", "40", "50", "60", "70", "80", "90"};

    final String[] strings = new String[]{"选择城市", "选择性别", "选择年龄"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDropDownMenu = findViewById(R.id.mDropDownMenu);
        initDropDownMenu();
    }

    private void initDropDownMenu() {
        mDropDownMenu.setMenuCount(3);
        mDropDownMenu.setmShowCount(7);
        mDropDownMenu.setDefaultMenuTitle(strings);

        mDropDownMenu.setMenuSelectedListener(new OnMenuListSelectedListener() {
            @Override
            public void onSelected(View listview, int RowIndex, int ColumnIndex) {
                if (ColumnIndex == 0) {
                    city_index = RowIndex;
                } else if (ColumnIndex == 1) {
                    sex_index = RowIndex;
                } else {
                    age_index = RowIndex;
                }
                Toast.makeText(MainActivity.this, "" + items.get(ColumnIndex)[RowIndex], Toast.LENGTH_SHORT).show();
                //刷新列表
            }
        });
        items.add(arr1);
        items.add(arr2);
        items.add(arr3);
        mDropDownMenu.setmMenuItems(items);
        mDropDownMenu.setIsDebug(false);
        mDropDownMenu.setDropWindow();
        mDropDownMenu.setOnMenuClickListener(this);
    }

    @Override
    public void onMenuClickListener(Context context, final PopupWindow popupWindow, RecyclerView recyclerView, final int index) {
        LinearLayoutManager manager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(manager);
        List<DropBean> mlist = new ArrayList<>();
        mlist.add(new DropBean());
        mlist.add(new DropBean());
        mlist.add(new DropBean());
        mlist.add(new DropBean());
        MyAdapter adapter = new MyAdapter(com.ty.dropdownmenu.R.layout.menu_list_item, mlist);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                popupWindow.dismiss();
                Toast.makeText(MainActivity.this, index+"  "+position, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
