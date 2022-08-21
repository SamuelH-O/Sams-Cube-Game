package com.example.samscubegame;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputLayout;

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

        // Set the value of the dropdown to be the one of the preferences
        TextInputLayout blockDropdown = view.findViewById(R.id.dropdown_blocks);
        AutoCompleteTextView  blockDropdownTextView = (AutoCompleteTextView) blockDropdown.getEditText();
        String[] arrayOfBlockStyles = getResources().getStringArray(R.array.dropdown_block_settings);
        assert blockDropdownTextView != null;
        blockDropdownTextView.setText(sharedPref.getString(getString(R.string.block_dropdown_key), arrayOfBlockStyles[1]), false);

        // Change the pref on click of the item of the dropdown
        blockDropdownTextView.setOnItemClickListener((adapterView, view1, i, l) -> {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(getString(R.string.block_dropdown_key), String.valueOf(blockDropdownTextView.getText()));
            editor.apply();
        });

        // Create a HashMap of debug settings elements and their keys
        optionToKey = new HashMap<>();
        optionToKey.put(view.findViewById(R.id.switch_show_grid), R.string.show_grid_key);
        optionToKey.put(view.findViewById(R.id.switch_show_grid_numbers), R.string.show_grid_numbers_key);

        // Group of debug settings elements
        View linearLayoutSettings = view.findViewById(R.id.linearLayout_debug_settings);

        // Loop through all debug settings elements and set them up
        for(int i = 0; i < ((ViewGroup) linearLayoutSettings).getChildCount(); i++) {
            CompoundButton currentSwitch = (CompoundButton) ((ViewGroup) linearLayoutSettings).getChildAt(i);
            Integer keyOfCurrentButton = optionToKey.get(currentSwitch);
            if (keyOfCurrentButton != null) {
                currentSwitch.setChecked(sharedPref.getBoolean(getString(keyOfCurrentButton), false));
            } else {
                throw new NullPointerException();
            }

            currentSwitch.setOnCheckedChangeListener(this::changeDebugPref);
        }
        super.onViewCreated(view, savedInstanceState);
    }

    private void changeDebugPref(CompoundButton compoundButton, boolean isChecked) {
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
