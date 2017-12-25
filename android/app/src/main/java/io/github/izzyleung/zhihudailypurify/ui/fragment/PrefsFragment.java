package io.github.izzyleung.zhihudailypurify.ui.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import io.github.izzyleung.zhihudailypurify.R;
import io.github.izzyleung.zhihudailypurify.ZhihuDailyPurifyApplication;
import io.github.izzyleung.zhihudailypurify.support.Check;
import io.github.izzyleung.zhihudailypurify.support.Constants;

public class PrefsFragment extends PreferenceFragment
        implements Preference.OnPreferenceClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.prefs);
        findPreference("about").setOnPreferenceClickListener(this);

        if (!Check.isZhihuClientInstalled()) {
            ((PreferenceCategory) findPreference("settings_settings"))
                    .removePreference(findPreference("using_client?"));
        }

        if (!ZhihuDailyPurifyApplication.getSharedPreferences()
                .getBoolean(Constants.SharedPreferencesKeys.KEY_SHOULD_ENABLE_ACCELERATE_SERVER, false)) {
            ((PreferenceScreen) findPreference("preference_screen"))
                    .removePreference(findPreference("settings_network_settings"));
        }
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference.getKey().equals("about")) {
            showApacheLicenseDialog();
            return true;
        }
        return false;
    }

    private void showApacheLicenseDialog() {
        final Dialog apacheLicenseDialog = new Dialog(getActivity());
        apacheLicenseDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        apacheLicenseDialog.setCancelable(true);
        apacheLicenseDialog.setContentView(R.layout.dialog_apache_license);

        TextView textView = apacheLicenseDialog.findViewById(R.id.dialog_text);

        StringBuilder sb = new StringBuilder();
        sb.append(getString(R.string.licences_header)).append("\n");

        String[] basedOnProjects = getResources().getStringArray(R.array.apache_licensed_projects);

        for (String str : basedOnProjects) {
            sb.append("• ").append(str).append("\n");
        }

        sb.append("\n").append(getString(R.string.licenses_subheader));
        sb.append("\n\n").append(getString(R.string.apache_license));
        textView.setText(sb.toString());

        Button closeDialogButton = apacheLicenseDialog.findViewById(R.id.close_dialog_button);

        closeDialogButton.setOnClickListener(view -> apacheLicenseDialog.dismiss());

        closeDialogButton.setOnLongClickListener(v -> {
            apacheLicenseDialog.dismiss();
            Toast.makeText(getActivity(),
                    getActivity().getString(R.string.accelerate_server_unlock),
                    Toast.LENGTH_SHORT).show();
            PreferenceManager.getDefaultSharedPreferences(getActivity())
                    .edit()
                    .putBoolean(Constants.SharedPreferencesKeys.KEY_SHOULD_ENABLE_ACCELERATE_SERVER, true)
                    .apply();
            return true;
        });

        apacheLicenseDialog.show();
    }
}
