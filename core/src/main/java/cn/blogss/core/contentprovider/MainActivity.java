package cn.blogss.core.contentprovider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.blogss.core.R;
import cn.blogss.core.base.BaseActivity;
import cn.blogss.core.base.BaseRVAdapter;
import cn.blogss.core.base.BaseRvHolder;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private Button btCallPhone;

    private Button btReadPhoneContacts;

    private Button btHidePhoneContacts;

    private RecyclerView rvContactsList;

    private List<PhoneContactsBean> phoneContactsBeans = new ArrayList<>();

    BaseRVAdapter<PhoneContactsBean> adapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_content_provider;
    }

    @Override
    protected void initView() {
        btCallPhone = findViewById(R.id.bt_call_phone);
        btReadPhoneContacts = findViewById(R.id.bt_read_phone_contacts);
        btHidePhoneContacts = findViewById(R.id.bt_hide_phone_contacts);
        rvContactsList = findViewById(R.id.rv_phone_contacts_list);

        btCallPhone.setOnClickListener(this);
        btReadPhoneContacts.setOnClickListener(this);
        btHidePhoneContacts.setOnClickListener(this);

        adapter = new BaseRVAdapter<PhoneContactsBean>(this,
                android.R.layout.two_line_list_item, phoneContactsBeans) {
            @Override
            protected void convert(BaseRvHolder holder, PhoneContactsBean itemData, int position) {
                TextView name = holder.getView(android.R.id.text1);
                TextView tel = holder.getView(android.R.id.text2);
                name.setText(itemData.getName());
                tel.setText(itemData.getTel());
            }
        };
        rvContactsList.setAdapter(adapter);
        rvContactsList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.bt_call_phone){/*拨打电话：10086*/
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) !=
                    PackageManager.PERMISSION_GRANTED) {/*用户没有授权*/
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
            }else{
                call();
            }
        }else if(id == R.id.bt_read_phone_contacts){/*读取电话联系人*/
            rvContactsList.setVisibility(View.VISIBLE);
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) !=
                    PackageManager.PERMISSION_GRANTED) {/*用户没有授权*/
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 2);
            }else{
                phoneContactsBeans.clear();
                readPhoneContacts();
            }
        }else if(id == R.id.bt_hide_phone_contacts){/*隐藏电话联系人*/
            phoneContactsBeans.clear();
            rvContactsList.setVisibility(View.GONE);
        }
    }

    /**
     * 跳转到拨打电话界面
     */
    private void call() {
        try {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:10086"));
            startActivity(intent);
        }catch (SecurityException e){
            e.printStackTrace();
        }
    }

    /**
     * 读取电话联系人
     */
    private void readPhoneContacts() {
        Cursor cursor = null;
        try {
            /*查询联系人数据*/
            cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, null, null, null);
            if(cursor != null){
                while(cursor.moveToNext()){
                    PhoneContactsBean phoneContactsBean = new PhoneContactsBean();
                    /*获取联系人姓名*/
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    /*获取联系人手机号*/
                    String tel = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    phoneContactsBean.setName(name);
                    phoneContactsBean.setTel(tel);
                    phoneContactsBeans.add(phoneContactsBean);
                }
                adapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(cursor != null){
                cursor.close();
            }
        }
    }

    /**
     *
     * @param requestCode 请求码和我们传入的一致
     * @param permissions
     * @param grantResults 授权的结果会封装到这里面
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if(requestCode == 1)
                call();
            else if(requestCode == 2)
                readPhoneContacts();
        }else{
            Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
        }
    }

    private static class PhoneContactsBean {
        private String name;
        private String tel;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }
    }
}