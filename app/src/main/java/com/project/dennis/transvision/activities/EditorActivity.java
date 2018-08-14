package com.project.dennis.transvision.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;

import com.project.dennis.transvision.data.ConfigLink;
import com.project.dennis.transvision.models.Peminjaman;
import com.project.dennis.transvision.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditorActivity extends AppCompatActivity {

    private EditText mTujuanEditText, mKeperluanEditText, mJumPenumpangEditText, mTglPemakaianEditText;
    private SimpleDateFormat dateFormatter;

    /** Untuk mengetahui form peminjaman sudah diedit (true) atau belum (false) */
    private boolean mPeminjamanHasChanged = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mPeminjamanHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        initView();

        // Mengeset OnTouchListeners di semua input field, jadi bisa tahu apakah user
        // telah menyentuh atau mengeditnya. Hal ini memberitahu kita apakah ada perubahan yang
        // belum disimpan atau tidak, jika user akan meninggalkan editor tanpa menyimpannya.
        mTujuanEditText.setOnTouchListener(mTouchListener);
        mKeperluanEditText.setOnTouchListener(mTouchListener);
        mJumPenumpangEditText.setOnTouchListener(mTouchListener);
        mTglPemakaianEditText.setOnTouchListener(mTouchListener);

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        mTglPemakaianEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                showDateDialog();
            }
        });
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void showDateDialog() {
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                mTglPemakaianEditText.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    private void initView() {
        mTujuanEditText = findViewById(R.id.edit_tujuan);
        mKeperluanEditText = findViewById(R.id.edit_keperluan);
        mJumPenumpangEditText = findViewById(R.id.edit_jum_penumpang);
        mTglPemakaianEditText = findViewById(R.id.edit_tgl_pemakaian);
    }

    private void simpanPeminjaman() {
        String tujuan = mTujuanEditText.getText().toString().trim();
        String keperluan = mKeperluanEditText.getText().toString().trim();
        String jumPenumpang = mJumPenumpangEditText.getText().toString().trim();
        String tglPemakaian = mTglPemakaianEditText.getText().toString().trim();

        Peminjaman peminjaman = new Peminjaman(tujuan, keperluan, jumPenumpang, tglPemakaian);

        if (TextUtils.isEmpty(tujuan) || TextUtils.isEmpty(keperluan) ||
                TextUtils.isEmpty(jumPenumpang) || TextUtils.isEmpty(tglPemakaian)) {
            showUncompletedFormDialog();
        } else {
            Intent intentConfirmAct = new Intent(this, ConfirmationActivity.class);
            intentConfirmAct.putExtra(ConfigLink.TUJUAN, peminjaman.getTujuan());
            intentConfirmAct.putExtra(ConfigLink.KEPERLUAN, peminjaman.getKeperluan());
            intentConfirmAct.putExtra(ConfigLink.JUM_PENUMPANG, peminjaman.getJumPenumpang());
            intentConfirmAct.putExtra(ConfigLink.TGL_PEMAKAIAN, peminjaman.getTglPemakaian());
            startActivity(intentConfirmAct);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                simpanPeminjaman();
                return true;
            case android.R.id.home:
                if (!mPeminjamanHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }

                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };

                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!mPeminjamanHasChanged) {
            super.onBackPressed();
            return;
        }

        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                };

        showUnsavedChangesDialog(discardButtonClickListener);
    }

    private void showUncompletedFormDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Mohon isi semua form");
        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Batalkan peminjaman dan keluar?");
        builder.setPositiveButton("Keluar", discardButtonClickListener);
        builder.setNegativeButton("Lanjutkan", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
