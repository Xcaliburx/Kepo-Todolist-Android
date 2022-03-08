package com.example.finalprojectandroid.bottomdialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.finalprojectandroid.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetDialog extends BottomSheetDialogFragment {

    TextView tvMessage;
    Button btnOke;
    View v;
    public static String message = "null";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.bottom_sheet_layout, container, false);

        tvMessage = v.findViewById(R.id.tv_message);
        tvMessage.setText(message);

        btnOke = v.findViewById(R.id.btn_oke);

        btnOke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return v;
    }

}
