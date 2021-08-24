package cn.blogss.core.view;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import cn.blogss.core.R;
import cn.blogss.core.base.BaseActivity;
import cn.blogss.core.base.recyclerview.BaseRVAdapter;
import cn.blogss.core.base.recyclerview.BaseRvHolder;
import cn.blogss.core.base.recyclerview.OnItemClickListener;
import cn.blogss.core.view.customview.HorizontalScrollViewEx;
import cn.blogss.helper.DensityUtilKt;

/**
 * View 滑动冲突测试
 */
public class SlideConflictActivity extends BaseActivity {

    private HorizontalScrollViewEx container;

    @Override
    public int getLayoutId() {
        return R.layout.activity_slide_conflict;
    }

    @Override
    protected void initView() {
        container = findViewById(R.id.container);

        LayoutInflater inflater = getLayoutInflater();
        int screenWidth = DensityUtilKt.getScreenMetrics(this).widthPixels;
        for(int i=0; i<3; i++){
            ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.content_layout,container,false);
            layout.getLayoutParams().width = screenWidth;
            TextView title = (TextView)layout.findViewById(R.id.title);
            title.setText("page " + (i + 1));
            layout.setBackgroundColor(Color.rgb(255 / (i + 1), 255 / (i + 1), 0));
            createList(layout);
            container.addView(layout);
        }
    }

    private void createList(ViewGroup layout) {
        RecyclerView rv = (RecyclerView)layout.findViewById(R.id.rv_list);
        ArrayList<String> data = new ArrayList<>();
        for (int i=0;i<50;i++){
            data.add("name " + i);
        }

        BaseRVAdapter adapter = new BaseRVAdapter<String>(this,R.layout.content_list_item,data){
            @Override
            protected void convert(BaseRvHolder holder, String itemData, int position) {
                TextView name = holder.getView(R.id.name);
                name.setText(itemData);
            }
        };
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object itemData, int position) {
                Toast.makeText(SlideConflictActivity.this,"click item",Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object itemData, int position) {
                return false;
            }
        });
    }
}