package com.duke.random.randommachine;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;

public class MainActivity extends AppCompatActivity implements BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemLongClickListener {

    private HomeAdapter mAdapter;
    private BasePopupView mAddDialog;
    private BasePopupView mDeleteDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                if (mAddDialog == null) {
                    mAddDialog = new XPopup.Builder(MainActivity.this)
                            .asInputConfirm(getString(R.string.add_random_title)
                                    , getString(R.string.add_random_content)
                                    , new OnInputConfirmListener() {
                        @Override
                        public void onConfirm(String text) {
                            if (mAdapter != null) {
                                mAdapter.addData(new BaseModel(text, 1));
                            }
                        }
                    }).show();
                } else {
                    mAddDialog.show();
                }
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter = new HomeAdapter(this));
        mAdapter.bindToRecyclerView(recyclerView);
        mAdapter.setMyEmptyView(findViewById(R.id.empty_text));
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemLongClickListener(this);

        findViewById(R.id.start_random).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAdapter.getData() != null && mAdapter.getItemCount() > 1) {
                    if (mAdapter.getCheckedData().size() > 1) {
                        Intent intent = new Intent(MainActivity.this, RandomActivity.class);
                        intent.putExtra(Constants.KEY_DATA_ARRAY, mAdapter.getCheckedData());
                        startActivity(intent);
                    } else  {
                        Toast.makeText(MainActivity.this, R.string.toast_check_empty_data, Toast.LENGTH_SHORT).show();
                    }
                } else  {
                    Toast.makeText(MainActivity.this, R.string.toast_empty_data, Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.history).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        if (id == R.id.action_select_all) {
            if (mAdapter != null) {
                mAdapter.toggle();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        mAdapter.toggle(position);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAdapter.onPause();
    }

    @Override
    public boolean onItemLongClick(BaseQuickAdapter adapter, View view, final int position) {
        if (mDeleteDialog == null && mAdapter != null && mAdapter.getItem(position) != null) {
            mDeleteDialog = new XPopup.Builder(MainActivity.this)
                    .asConfirm(getString(R.string.delete_title)
                            , getString(R.string.delete_content, mAdapter.getItem(position).text)
                            , new OnConfirmListener() {
                                @Override
                                public void onConfirm() {
                                    if (mAdapter != null) {
                                        mAdapter.remove(position);
                                    }
                                }

                            }).show();
        } else {
            if (mDeleteDialog != null) {
                mDeleteDialog.show();
            }
        }
        return false;
    }
}
