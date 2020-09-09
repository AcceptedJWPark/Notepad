package com.accepted.notepad.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.accepted.notepad.R;
import com.accepted.notepad.SaveSharedPreference;
import com.accepted.notepad.VolleySingleton;
import com.accepted.notepad.addmemo.Addmemo_MainActivity;
import com.accepted.notepad.backgound.Background_MainActivity;
import com.accepted.notepad.join.LostID1_MainActivity;
import com.accepted.notepad.password.Password_MainActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<Listitem_Memo> arrayList;
    ListAdapter_Memo listAdapter_memo;

    Context mContext;
    String memID;

    String color1_basic = SaveSharedPreference.getBackColor1_basic();
    String color2_basic = SaveSharedPreference.getBackColor2_basic();
    String color3_basic = SaveSharedPreference.gettxtColor1_basic();
    String color4_basic = SaveSharedPreference.geticonColor1_basic();

    String color1_night = SaveSharedPreference.getBackColor1_night();
    String color2_night = SaveSharedPreference.getBackColor2_night();
    String color3_night = SaveSharedPreference.getTxtColor1_night();
    String color4_night = SaveSharedPreference.getIconColor1_night();

    String choosedColor1;
    String choosedColor2;
    String choosedColor3;
    String choosedColor4;
    int colorMode = 1;

    MainActivity mainActivity;

    DrawerLayout dl;
    View v_drawerlayout;
    View footer;

    boolean ismenu;
    boolean ismemo;
    boolean issearch;
    boolean isdate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        mainActivity = this;

        ((TextView)findViewById(R.id.tv_maintitle_home)).setText("Notepad");
        mContext = getApplicationContext();

//        memID = SaveSharedPreference.getUserID(mContext);
        memID = "mkh9012";
        arrayList = new ArrayList<>();
        listView = findViewById(R.id.lv_memo);

        dl = (DrawerLayout)findViewById(R.id.drawer);
        v_drawerlayout = (View)findViewById(R.id.view_drawerlayout);

        ((ImageView)findViewById(R.id.img_open_dl)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dl.openDrawer(v_drawerlayout);
            }
        });

        Intent intent = getIntent();
        colorMode = intent.getIntExtra("ColorMode",1);

        ismenu = intent.getBooleanExtra("ismenu",true);
        ismemo = intent.getBooleanExtra("ismemo",true);
        issearch = intent.getBooleanExtra("issearch",true);
        isdate = intent.getBooleanExtra("isdate",true);


        if(issearch)
        {
            ((LinearLayout)findViewById(R.id.ll_searchContainer)).setVisibility(View.VISIBLE);
        }else
        {
            ((LinearLayout)findViewById(R.id.ll_searchContainer)).setVisibility(View.GONE);
        }

        if(colorMode == 1)
        {
            choosedColor1 = color1_basic;
            choosedColor2 = color2_basic;
            choosedColor3 = color3_basic;
            choosedColor4 = color4_basic;
        }else if(colorMode ==2)
        {
            choosedColor1 = color1_night;
            choosedColor2 = color2_night;
            choosedColor3 = color3_night;
            choosedColor4 = color4_night;
        }

        ((ImageView)findViewById(R.id.btn_addmemo)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, Addmemo_MainActivity.class);
                intent.putExtra("ColorMode",colorMode);
                startActivity(intent);
            }
        });

        ((TextView)findViewById(R.id.tv_right_home)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, Background_MainActivity.class);
                startActivity(intent);
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(mContext, Password_MainActivity.class);
                intent.putExtra("ColorMode",colorMode);
                intent.putExtra("isTutorial",true);
                startActivity(intent);
            }
        });

        getBasicMemoList();
    }

    public void drawerLayout()
    {
        ((LinearLayout)findViewById(R.id.ll_drawer_login)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ((LinearLayout)findViewById(R.id.ll_drawer_pw)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, LostID1_MainActivity.class);
                intent.putExtra("ColorMode",colorMode);
                intent.putExtra("isLostLock",true);
                startActivity(intent);
            }
        });

        ((LinearLayout)findViewById(R.id.ll_drawer_click)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ClickChange_MainActivity.class);
                intent.putExtra("ColorMode",colorMode);
                startActivity(intent);
            }
        });

        ((LinearLayout)findViewById(R.id.ll_drawer_bgr)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ((LinearLayout)findViewById(R.id.ll_drawer_manual)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }


    public void colorChange(String color1, String color2, String color3, String color4)
    {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(color2));
            int flags = getWindow().getDecorView().getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            getWindow().getDecorView().setSystemUiVisibility(flags);
        }

        GradientDrawable shape =  new GradientDrawable();
        shape.setCornerRadius( 30 );
        shape.setColor(Color.parseColor(color1));

        ((RelativeLayout)findViewById(R.id.rl_toolbar)).setBackgroundColor(Color.parseColor(color2));
        ((TextView)findViewById(R.id.tv_maintitle_home)).setTextColor(Color.parseColor(color3));
        ((LinearLayout)findViewById(R.id.ll_searchContainer)).setBackgroundColor(Color.parseColor(color2));
        ((LinearLayout)findViewById(R.id.ll_searchContainer2)).setBackgroundColor(Color.parseColor(color2));

        ((RelativeLayout)findViewById(R.id.rl_searchContainer)).setBackground(shape);

        ((ImageView)findViewById(R.id.iv_search)).setColorFilter(Color.parseColor(color3));
        ((EditText)findViewById(R.id.et_search)).setHintTextColor(Color.parseColor(color3));
        ((EditText)findViewById(R.id.et_search)).setTextColor(Color.parseColor(color3));

        ((LinearLayout)findViewById(R.id.container)).setBackgroundColor(Color.parseColor(color1));
        ((RelativeLayout)findViewById(R.id.rl_container)).setBackgroundColor(Color.parseColor(color1));

        ((ImageView)findViewById(R.id.btn_addmemo)).setColorFilter(Color.parseColor(color4));

        ((TextView)findViewById(R.id.tv_right_home)).setText("배경설정");
        ((TextView)findViewById(R.id.tv_right_home)).setTextColor(Color.parseColor(color4));
        ((TextView)findViewById(R.id.tv_right_home)).setVisibility(View.VISIBLE);

        ((LinearLayout)findViewById(R.id.ll_drawer)).setBackgroundColor(Color.parseColor(color2));

        ((TextView)findViewById(R.id.tv_drawer_login)).setTextColor(Color.parseColor(color3));
        ((TextView)findViewById(R.id.tv_drawer_bgr)).setTextColor(Color.parseColor(color3));
        ((TextView)findViewById(R.id.tv_drawer_pw)).setTextColor(Color.parseColor(color3));
        ((TextView)findViewById(R.id.tv_drawer_click)).setTextColor(Color.parseColor(color3));
        ((TextView)findViewById(R.id.tv_drawer_manual)).setTextColor(Color.parseColor(color3));

        ((ImageView)findViewById(R.id.iv_drawer_login)).setColorFilter(Color.parseColor(color3));
        ((ImageView)findViewById(R.id.iv_drawer_bgr)).setColorFilter(Color.parseColor(color3));
        ((ImageView)findViewById(R.id.iv_drawer_pw)).setColorFilter(Color.parseColor(color3));
        ((ImageView)findViewById(R.id.iv_drawer_click)).setColorFilter(Color.parseColor(color3));
        ((ImageView)findViewById(R.id.iv_drawer_manual)).setColorFilter(Color.parseColor(color3));

        footer.setBackgroundColor(Color.parseColor(color1));

        if(ismenu)
        {
            ((ImageView)findViewById(R.id.img_open_dl)).setColorFilter(Color.parseColor(color3));
        }else
        {
            ((ImageView)findViewById(R.id.img_open_dl)).setColorFilter(Color.parseColor(color2));
        }
    }

    public void getBasicMemoList() {
        RequestQueue postRequestQueue = VolleySingleton.getInstance(mContext).getRequestQueue();
        StringRequest postJsonRequest = new StringRequest(Request.Method.POST, SaveSharedPreference.getServerIp() + "/Memo/getBasicMemoList.do", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray objArray = new JSONArray(response);

                    for (int i = 0; i < objArray.length(); i++) {
                        JSONObject obj = objArray.getJSONObject(i);
                        Listitem_Memo memo;

                        long dateTimestamp = obj.getLong("RegDate");
                        Date regDate = new Date(dateTimestamp);

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        String regDateStr = sdf.format(regDate);

                        memo = new Listitem_Memo(obj.getString("RTitle"), regDateStr, obj.getString("RContent"));

                        arrayList.add(memo);
                    }

                    listAdapter_memo = new ListAdapter_Memo (mContext,arrayList,choosedColor1,choosedColor2,choosedColor3,ismemo,isdate);
                    footer = getLayoutInflater().inflate(R.layout.memolist_footer,null,false);
                    listView.addFooterView(footer);
                    listView.setAdapter(listAdapter_memo);
                    colorChange(choosedColor1,choosedColor2,choosedColor3,choosedColor4);
                    drawerLayout();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, SaveSharedPreference.getErrorListener(mContext)) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("MemID", memID);
                return params;
            }
        };
        postRequestQueue.add(postJsonRequest);
    }

}
