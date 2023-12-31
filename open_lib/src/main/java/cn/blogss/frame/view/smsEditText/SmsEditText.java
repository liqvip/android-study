package cn.blogss.frame.view.smsEditText;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.blogss.frame.R;


/**
 * author weioule
 * Create on 2018/6/8.
 * 文案大小及颜色或背景在这里面自行设置，app里的验证码输入框基本都是统一的，没必要自定义属性
 */
public class SmsEditText extends RelativeLayout {

    private String digits = "0123456789abcdefghigklmnopqrstuvwxyzABCDEFGHIGKLMNOPQRSTUVWXYZ";
    private LinearLayout mRootLayout;
    private SendButton mSendButton;
    private ImageView mClearBtn;
    private EditText mEditText;
    private TextView mLabel;
    private Boolean mClear;
    private View mDivider;

    public SmsEditText(Context context) {
        super(context, null);
    }

    public SmsEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate();
        initView();
        setAttribute(context, attrs);
    }

    private void inflate() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.sms_edit_text, this, true);
    }

    private void initView() {
        mDivider = findViewById(R.id.divider);
        mLabel = findViewById(R.id.label);
        mEditText = findViewById(R.id.editText);
        mClearBtn = findViewById(R.id.clearbtn);
        mSendButton = findViewById(R.id.sms_button);
        mRootLayout = findViewById(R.id.root_layout);

        mClearBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                getEditText().setText("");
                getEditText().requestFocus();
            }
        });

        initWatch();
    }

    private void setAttribute(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SmsEditText);

        String txt = typedArray.getString(R.styleable.SmsEditText_lable);
        if (!TextUtils.isEmpty(txt)) {
            mLabel.setVisibility(VISIBLE);
            mLabel.setText(txt);
        }

        mClear = typedArray.getBoolean(R.styleable.SmsEditText_clear, true);

        boolean sendBtnEnable = typedArray.getBoolean(R.styleable.SmsEditText_send_btn_enable, true);
        mSendButton.setVisibility(sendBtnEnable ? VISIBLE : GONE);

        boolean dividerEnable = typedArray.getBoolean(R.styleable.SmsEditText_divider_enable, true);
        mDivider.setVisibility(dividerEnable ? VISIBLE : GONE);

        int leftPadding = (int) typedArray.getDimension(R.styleable.SmsEditText_padding_left, 0);
        int rightPadding = (int) typedArray.getDimension(R.styleable.SmsEditText_padding_right, 0);
        mRootLayout.setPadding(leftPadding, 0, rightPadding, 0);

        String hit = typedArray.getString(R.styleable.SmsEditText_hit);
        if (!TextUtils.isEmpty(hit)) {
            getEditText().setHint(hit);
        }

        int length = typedArray.getInteger(R.styleable.SmsEditText_length, -1);
        if (length > 0) {
            getEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(length)});
        }

        boolean editable = typedArray.getBoolean(R.styleable.SmsEditText_editable, true);
        mEditText.setEnabled(editable);

        String type = typedArray.getString(R.styleable.SmsEditText_input_typed);
        if (!TextUtils.isEmpty(type)) {
            getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
            if ("number_letter".equals(type)) {
                getEditText().setKeyListener(DigitsKeyListener.getInstance(digits));
            }
        }
        typedArray.recycle();
    }

    private void initWatch() {
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mClear && getEditText().isEnabled() && getEditText().getText().length() > 0) {
                    mClearBtn.setVisibility(View.VISIBLE);
                } else {
                    mClearBtn.setVisibility(View.GONE);
                }
            }
        });
    }

    public EditText getEditText() {
        return mEditText;
    }

    public SendButton getSendButton() {
        return mSendButton;
    }

}
