package com.example.samscubegame;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.HashMap;

public class ModalBottomSheetFragment extends BottomSheetDialogFragment {

    HashMap<CompoundButton, Integer> optionToKey;
    SharedPreferences sharedPref;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return getLayoutInflater().inflate(R.layout.bottom_sheet_view, container, false);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        sharedPref = requireActivity().getSharedPreferences(String.valueOf(R.string.pref_file), Context.MODE_PRIVATE);

        // Create a HashMap of settings elements and their keys
        optionToKey = new HashMap<>();
        optionToKey.put(view.findViewById(R.id.switchShowGrid), R.string.show_grid_key);

        // Group of settings elements
        View linearLayoutSettings = view.findViewById(R.id.linearLayoutSettings);

        // Loop through all settings elements and set them up
        for(int index = 0; index < ((ViewGroup) linearLayoutSettings).getChildCount(); index++) {
            CompoundButton currentChild = (CompoundButton) ((ViewGroup) linearLayoutSettings).getChildAt(index);
            currentChild.setChecked(sharedPref.getBoolean(getString(R.string.show_grid_key), false));

            Log.d("show_grid_value", "" + sharedPref.getBoolean(getString(R.string.show_grid_key), false));
            Log.d("btn", "" + currentChild.isChecked());

            currentChild.setOnCheckedChangeListener(this::changePref);
        }

        super.onViewCreated(view, savedInstanceState);
    }

    public void changePref(CompoundButton compoundButton, boolean isChecked) {
        SharedPreferences.Editor editor = sharedPref.edit();
        Integer tmpInteger = optionToKey.get(compoundButton);
        String tmpStr;
        if (tmpInteger != null) {
            if (optionToKey.containsKey(compoundButton) && sharedPref.getBoolean(tmpStr = getString(tmpInteger), false) != isChecked) {
                editor.putBoolean(tmpStr, isChecked);
            }
            editor.apply();
        } else {
            Log.e("changePref", "" + compoundButton.getId());
            throw new NullPointerException();
        }
    }
}
