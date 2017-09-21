package com.oit.test.demodemoproject;

import android.widget.TextView;

/**
 * Created by OPTLPTP061 on 05-09-2017.
 */

public class GenderRadio {
    boolean setFemaleFlag = false;
    boolean setMaleFlag = false;
    TextView textViewFemale;
    TextView textViewMale;
    GenderRadio(TextView textViewFemale, TextView textViewMale) {
        this.textViewMale = textViewMale;
        this.textViewFemale = textViewFemale;
    }
    public boolean SettingGenderFlag(boolean setFemaleFlag, boolean setMaleFlag) {
        if (setFemaleFlag == true && setMaleFlag == false) {
            this.setFemaleFlag = setFemaleFlag;
            this.setMaleFlag = setMaleFlag;
            settingcolor();
            return setMaleFlag;
        }
        if (setFemaleFlag == false && setMaleFlag == true) {
            this.setFemaleFlag = setFemaleFlag;
            this.setMaleFlag = setMaleFlag;
            settingcolor();
            return setMaleFlag;
        }
        return false;
    }
    public void settingcolor() {
        if (setFemaleFlag == true && setMaleFlag == false) {
            textViewFemale.setBackgroundResource(R.drawable.borderselected);
            textViewMale.setBackgroundResource(R.drawable.border);
        }
        if (setFemaleFlag == false && setMaleFlag == true) {
            textViewMale.setBackgroundResource(R.drawable.borderselected);
            textViewFemale.setBackgroundResource(R.drawable.border);
        }
    }
}
