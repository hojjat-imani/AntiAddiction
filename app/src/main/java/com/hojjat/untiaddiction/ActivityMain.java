package com.hojjat.untiaddiction;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.hojjat.untiaddiction.Util.PersianReshape;
import com.hojjat.untiaddiction.Util.Util;
import java.util.ArrayList;

public class ActivityMain extends AppCompatActivity {
    private static final String Tag = ActivityMain.class.getName();
    ArrayList<ResolveInfo> appsInfo;
    ListView appsList;
    Adapter listAdapter;
    BlockHelper blockHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        blockHelper = new BlockHelper(this);
        initViews();
        setListeners();
        new SetData().execute();
        Util.startMainService(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (listAdapter == null)
            new SetData().execute();
        else
            listAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
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

    private void setListeners() {
        appsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final Dialog d = new Dialog(ActivityMain.this);
                if (blockHelper.isLimited(listAdapter.getItem(position).packageName)) {
                } else {
                    d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    d.setContentView(R.layout.dialog_limit_app);
                    d.setCancelable(false);
                    d.getWindow().getAttributes().width = WindowManager.LayoutParams.MATCH_PARENT;
                    TextView tv = (TextView) d.findViewById(R.id.textView);
                    final EditText maxTime = (EditText) d.findViewById(R.id.editText);
                    final TextView err = (TextView) d.findViewById(R.id.error);
                    TextView minute = (TextView) d.findViewById(R.id.min);
                    Button ok = (Button) d.findViewById(R.id.ok);
                    Button cancel = (Button) d.findViewById(R.id.cancel);
                    Util.setFont(ActivityMain.this, Util.FontFamily.Default, Util.FontWeight.Regular, tv, ok, cancel, err, minute);
                    Util.setText(tv, "حداکثر زمان استفاده روزانه:", ok, "تأیید", cancel, "لغو", err, "زمان وارد شده نامعتبر است!", minute, "دقیقه");
                    d.show();
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            d.dismiss();
                        }
                    });

                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!Util.isNumeric(maxTime.getText().toString()) || Integer.parseInt(maxTime.getText().toString()) < 1)
                                err.setVisibility(View.VISIBLE);
                            else {
                                blockHelper.limitApp(listAdapter.getItem(position).packageName, Integer.parseInt(maxTime.getText().toString()) * 60 * 1000);
                                d.dismiss();
                                Toast.makeText(ActivityMain.this, PersianReshape.reshape("انجام شد!"), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    maxTime.setOnKeyListener(new View.OnKeyListener() {
                        @Override
                        public boolean onKey(View v, int keyCode, KeyEvent event) {
                            err.setVisibility(View.GONE);
                            return false;
                        }
                    });
                }
            }
        });
    }

    private void prepareToolbar() {
//        setSupportActionBar(toolbar);
//        toolbar.setTitle("");
    }

    private void initViews() {
        appsList = (ListView) findViewById(R.id.appsList);
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    class SetData extends AsyncTask {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(ActivityMain.this);
            progressDialog.setMessage(PersianReshape.reshape("کمی صبر کنید"));
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            setData();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    appsList.setAdapter(listAdapter);
                }
            });
            return null;
        }

        private void setData() {
            ArrayList<PkgInfo> installedApps = new InstalledPackagesInfo(ActivityMain.this).getInstalledApps();
            listAdapter = new Adapter(installedApps, ActivityMain.this);
        }

        @Override
        protected void onPostExecute(Object o) {
            progressDialog.dismiss();
        }
    }
}
