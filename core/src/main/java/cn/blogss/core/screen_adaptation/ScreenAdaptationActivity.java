package cn.blogss.core.screen_adaptation;

import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cn.blogss.core.R;
import cn.blogss.helper.base.BaseActivity;

public class ScreenAdaptationActivity extends BaseActivity implements View.OnClickListener {
    private Button btPrint, btHide;

    private TextView tvInfo;

    @Override
    public int getLayoutId() {
        return R.layout.activity_screen_adaptation;
    }

    @Override
    protected void initView() {
        btPrint  = findViewById(R.id.bt_print_metrics_info);
        btHide = findViewById(R.id.bt_hide_metrics_info);
        tvInfo = findViewById(R.id.tv_metrics_info);

        btPrint.setOnClickListener(this);
        btHide.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.bt_print_metrics_info) {
            String info = getDevMetricsInfo();
            tvInfo.setText(info);
            tvInfo.setVisibility(View.VISIBLE);
        }else if(id == R.id.bt_hide_metrics_info){
            tvInfo.setVisibility(View.GONE);
        }
    }

    private String getDevMetricsInfo() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int widthPixels = displayMetrics.widthPixels;
        int heightPixels = displayMetrics.heightPixels;
        int densityDpi = displayMetrics.densityDpi;
        float xDpi = displayMetrics.xdpi;
        float yDpi = displayMetrics.ydpi;
        float density = displayMetrics.density;
        float scaleDensity = displayMetrics.scaledDensity;
        return "屏幕宽度: "+widthPixels+"px\n"+"屏幕高度: "+heightPixels+"px\n"+"densityDpi: "+densityDpi+
                "(px/ich)\n"+"xdpi(精确的物理像素): "+xDpi+"(px/ich)\n"+"ydpi(精确的物理像素): "+yDpi+"(px/ich)\n"+"density: "+density+"\n"+"scaleDensity: "+scaleDensity+"\n";
    }
}