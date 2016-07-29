package com.atloginbutton;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import customview.ATLoginButton;
import customview.ATScrollDeleteTouchView;

public class MainActivity extends AppCompatActivity {
    private ATLoginButton mATLoginButton1;
    private ATLoginButton mATLoginButton2;
    private ATLoginButton mATLoginButton3;

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


        ATScrollDeleteTouchView atScrollDeleteView = (ATScrollDeleteTouchView) findViewById(R.id.deleteScroll);
        atScrollDeleteView.setScrollDeleteDesc("陈冠希");
        atScrollDeleteView.setScrollDeleteListener(new ATScrollDeleteTouchView.OnScrollDeleteListener() {
            @Override
            public void deleteAction() {
                Toast.makeText(MainActivity.this, "执行删除动作-->", Toast.LENGTH_SHORT).show();
            }
        });
        ATScrollDeleteTouchView atScrollDeleteView1 = (ATScrollDeleteTouchView) findViewById(R.id.deleteScroll_1);
        atScrollDeleteView1.setScrollDeleteDesc("张柏芝");
        atScrollDeleteView1.setScrollDeleteListener(new ATScrollDeleteTouchView.OnScrollDeleteListener() {
            @Override
            public void deleteAction() {
                Toast.makeText(MainActivity.this, "执行删除动作-->", Toast.LENGTH_SHORT).show();
            }
        });

        ATScrollDeleteTouchView atScrollDeleteView2 = (ATScrollDeleteTouchView) findViewById(R.id.deleteScroll_2);
        atScrollDeleteView2.setScrollDeleteDesc("钟欣桐");
        atScrollDeleteView2.setScrollDeleteListener(new ATScrollDeleteTouchView.OnScrollDeleteListener() {
            @Override
            public void deleteAction() {
                Toast.makeText(MainActivity.this, "执行删除动作-->", Toast.LENGTH_SHORT).show();
            }
        });

        ATScrollDeleteTouchView atScrollDeleteView3 = (ATScrollDeleteTouchView) findViewById(R.id.deleteScroll_3);
        atScrollDeleteView3.setScrollDeleteDesc("杨永晴");
        atScrollDeleteView3.setScrollDeleteListener(new ATScrollDeleteTouchView.OnScrollDeleteListener() {
            @Override
            public void deleteAction() {
                Toast.makeText(MainActivity.this, "执行删除动作-->", Toast.LENGTH_SHORT).show();
            }
        });

        ATScrollDeleteTouchView atScrollDeleteView4 = (ATScrollDeleteTouchView) findViewById(R.id.deleteScroll_4);
        atScrollDeleteView4.setScrollDeleteDesc("陈思慧");
        atScrollDeleteView4.setScrollDeleteListener(new ATScrollDeleteTouchView.OnScrollDeleteListener() {
            @Override
            public void deleteAction() {
                Toast.makeText(MainActivity.this, "执行删除动作-->", Toast.LENGTH_SHORT).show();
            }
        });
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
