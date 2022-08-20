package com.example.samscubegame;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.HashMap;

public class ModalBottomSheetFragment extends BottomSheetDialogFragment {

    private HashMap<CompoundButton, Integer> optionToKey;
    private SharedPreferences sharedPref;

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
        optionToKey.put(view.findViewById(R.id.switchShowGridNumbers), R.string.show_grid_numbers_key);

        // Group of settings elements
        View linearLayoutSettings = view.findViewById(R.id.linearLayoutDebugSettings);

        // Loop through all debug settings elements and set them up
        for(int i = 0; i < ((ViewGroup) linearLayoutSettings).getChildCount(); i++) {
            CompoundButton currentButton = (CompoundButton) ((ViewGroup) linearLayoutSettings).getChildAt(i);
            Integer keyOfCurrentButton = optionToKey.get(currentButton);
            if (keyOfCurrentButton != null) {
                currentButton.setChecked(sharedPref.getBoolean(getString(keyOfCurrentButton), false));
            } else {
                throw new NullPointerException();
            }

            currentButton.setOnCheckedChangeListener(this::changePref);
        }
        super.onViewCreated(view, savedInstanceState);
    }

    private void changePref(CompoundButton compoundButton, boolean isChecked) {
        SharedPreferences.Editor editor = sharedPref.edit();
        Integer keyOfButton = optionToKey.get(compoundButton);
        String tmpStr;
        if (keyOfButton != null) {
            if (optionToKey.containsKey(compoundButton) && sharedPref.getBoolean(tmpStr = getString(keyOfButton), false) != isChecked) {
                editor.putBoolean(tmpStr, isChecked);
            }
            editor.apply();
        } else {
            throw new NullPointerException();
        }
    }
}
