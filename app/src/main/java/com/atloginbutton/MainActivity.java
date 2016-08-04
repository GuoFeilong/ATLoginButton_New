package com.atloginbutton;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import customview.ATLoginButton;
import customview.ATProgressView;
import customview.ATScrollDeleteNewView;


public class MainActivity extends AppCompatActivity {
    private ATLoginButton mATLoginButton1;
    private ATLoginButton mATLoginButton2;
    private ATLoginButton mATLoginButton3;

    private ATProgressView mATProgressView;
    private LinearLayout linearLayout;
//    private ATScrollDeleteView test;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        button = (Button) findViewById(R.id.btn_test);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                final ATScrollDeleteView   test = new ATScrollDeleteView(MainActivity.this);
//                LinearLayout.LayoutParams lp4 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 200);
//                test.setLayoutParams(lp4);
//                test.setScrollDeleteDesc("陈冠希4", "身份证号: 130491190008244523");
//                linearLayout.addView(test);
//
//                test.setScrollDeleteListener(new ATScrollDeleteView.OnScrollDeleteListener() {
//                    @Override
//                    public void deleteAction() {
//                        Toast.makeText(MainActivity.this,"点击了-->>握草",Toast.LENGTH_SHORT).show();
//                        linearLayout.removeView(test);
//                    }
//                });



                final ATScrollDeleteNewView test = new ATScrollDeleteNewView(MainActivity.this);
                LinearLayout.LayoutParams lp4 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 200);
                test.setLayoutParams(lp4);
                test.setContentView(LayoutInflater.from(MainActivity.this).inflate(R.layout.view_test,null,false));
                linearLayout.addView(test);

                test.setScrollDeleteListener(new ATScrollDeleteNewView.OnScrollDeleteListener() {
                    @Override
                    public void deleteAction() {
                        Toast.makeText(MainActivity.this,"点击了-->>握草",Toast.LENGTH_SHORT).show();
                        linearLayout.removeView(test);
                    }
                });
            }
        });
        mATProgressView = (ATProgressView) findViewById(R.id.progressView);
        mATProgressView.setCountdownTime(10);
        mATProgressView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mATProgressView.startCountdown(new ATProgressView.OnCountDownFinishListener() {
                    @Override
                    public void countDownFinished() {
                        Toast.makeText(MainActivity.this, "倒计时结束了--->该UI处理界面逻辑了", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        linearLayout = (LinearLayout) findViewById(R.id.ll_test);
        mATLoginButton1 = (ATLoginButton) findViewById(R.id.atb_1);
//        mATLoginButton2 = (ATLoginButton) findViewById(R.id.atb_2);
        mATLoginButton3 = (ATLoginButton) findViewById(R.id.atb_3);
//        addListener2Button(mATLoginButton1, true);
//        addListener2Button(mATLoginButton2, false);
//        addListener2Button(mATLoginButton3, false);


        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.rl_change);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mATLoginButton3.setViewState(ATLoginButton.LoginViewState.READY_STATE);
            }
        });

        mATLoginButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mATLoginButton3.buttonLoginAction();
                mATLoginButton3.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mATLoginButton3.buttonLoaginResultAciton(false, new ATLoginButton.AnimationEndListener() {
                            @Override
                            public void animationEnd() {
                                Intent intent = new Intent(MainActivity.this, CardActivity.class);
                                startActivity(intent);
                            }
                        });
                    }
                }, 2000);

            }
        });


//        test = new ATScrollDeleteView(this);
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 200);
//        test.setLayoutParams(lp);
//        test.setScrollDeleteDesc("陈冠希", "身份证号: 130491190008244523");
//        linearLayout.addView(test);

//        test = new ATScrollDeleteView(this);
//        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 200);
//        test.setLayoutParams(lp1);
//        test.setScrollDeleteDesc("陈冠希1", "身份证号: 130491190008244523");
//        linearLayout.addView(test);
//
//
//        test = new ATScrollDeleteView(this);
//        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 200);
//        test.setLayoutParams(lp2);
//        test.setScrollDeleteDesc("陈冠希2", "身份证号: 130491190008244523");
//        linearLayout.addView(test);
//
//
//        test = new ATScrollDeleteView(this);
//        LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 200);
//        test.setLayoutParams(lp3);
//        test.setScrollDeleteDesc("陈冠希3", "身份证号: 130491190008244523");
//        linearLayout.addView(test);



//        ATScrollDeleteView atScrollDeleteView = (ATScrollDeleteView) findViewById(R.id.deleteScroll);
//        atScrollDeleteView.setScrollDeleteDesc("陈冠希", "身份证号: 130491190008244523");
//        atScrollDeleteView.setScrollDeleteListener(new ATScrollDeleteView.OnScrollDeleteListener() {
//            @Override
//            public void deleteAction() {
//                Toast.makeText(MainActivity.this, "执行删除动作-->", Toast.LENGTH_SHORT).show();
//            }
//        });
//        ATScrollDeleteView atScrollDeleteView1 = (ATScrollDeleteView) findViewById(R.id.deleteScroll_1);
//        atScrollDeleteView1.setScrollDeleteDesc("张曼玉");
//        atScrollDeleteView1.setScrollDeleteListener(new ATScrollDeleteView.OnScrollDeleteListener() {
//            @Override
//            public void deleteAction() {
//                Toast.makeText(MainActivity.this, "执行删除动作-->", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        ATScrollDeleteView atScrollDeleteView4 = (ATScrollDeleteView) findViewById(R.id.deleteScroll_4);
//        atScrollDeleteView4.setScrollDeleteDesc("陈思慧", "陈冠希", "阿娇");
//        atScrollDeleteView4.setScrollDeleteListener(new ATScrollDeleteView.OnScrollDeleteListener() {
//            @Override
//            public void deleteAction() {
//                Toast.makeText(MainActivity.this, "执行删除动作-->", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    private void addListener2Button(final ATLoginButton atLoginButton, final boolean loaginStatus) {
        atLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                atLoginButton.buttonLoginAction();
                atLoginButton.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        atLoginButton.buttonLoaginResultAciton(loaginStatus, null);
                        String notice = loaginStatus ? "登陆成功,重置button状态" : "登录失败,显示失败状态";
                        if (loaginStatus) {
                            Intent intent = new Intent(MainActivity.this, CardActivity.class);
                            startActivity(intent);

                        } else {
                            Toast.makeText(getApplicationContext(), notice, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, 3000);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
