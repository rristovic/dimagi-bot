package com.runit.dimagibot.ui.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.runit.dimagibot.Injector;
import com.runit.dimagibot.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEtInput;
    private RecyclerView mRvList;
    private MainViewModel mViewModel;
    private TodoListAdapter mAdapter;
    private View mNoDataView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        subsribeToViewModel();
    }

    private void subsribeToViewModel() {
        mViewModel.getItems().observe(this, items -> {
            if (items == null || items.size() == 0) {
                mNoDataView.setVisibility(View.VISIBLE);
                mRvList.setVisibility(View.GONE);
            } else {
                mRvList.setVisibility(View.VISIBLE);
                mNoDataView.setVisibility(View.GONE);
            }
            mAdapter.setData(items);
        });
        mViewModel.getMessage().observe(this, msg -> Toast.makeText(this, msg, Toast.LENGTH_LONG).show());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_go: {
                newCommandEntered();
                break;
            }
            default:
                break;
        }
    }


    private void init() {
        mEtInput = findViewById(R.id.et_input);
        mRvList = findViewById(R.id.rv_list);
        findViewById(R.id.btn_go).setOnClickListener(this);
        mViewModel = Injector.injectMainViewModel();
        mAdapter = new TodoListAdapter();
        mRvList.setAdapter(mAdapter);
        mRvList.setLayoutManager(new LinearLayoutManager(this));
        mEtInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                newCommandEntered();
                return true;
            }
            return false;
        });
        mNoDataView = findViewById(R.id.tv_noData);
    }

    private void newCommandEntered() {
        mViewModel.onNewCommand(mEtInput.getText().toString());
    }
}
