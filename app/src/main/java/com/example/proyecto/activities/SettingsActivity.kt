package com.example.proyecto.activities

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import com.example.proyecto.R
import com.example.proyecto.utils.LocaleHelper
import java.util.Locale

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        val toolBar = findViewById<Toolbar>(R.id.appbar_settings)
        setSupportActionBar(toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


    }
    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            val languagePreference = findPreference<ListPreference>("language")

            languagePreference?.setOnPreferenceChangeListener { _, newValue ->
                val selectedLanguage = newValue as String
                updateLanguage(selectedLanguage)
                true
            }

            val fontPreference = findPreference<ListPreference>("font_preference")

            fontPreference?.setOnPreferenceChangeListener { _, newValue ->
                val selectedFont = newValue as String
                applyFont(selectedFont)
                true
            }
        }

        private fun applyFont(fontName: String) {
            val fontResId = when (fontName) {
                "roboto" -> R.font.roboto
                "montserrat" -> R.font.montserrat
                else -> R.font.roboto
            }

            val typeface = ResourcesCompat.getFont(requireContext(), fontResId)

            requireActivity().theme.applyStyle(getThemeFromFont(fontName), true)

            activity?.recreate()
        }

        private fun getThemeFromFont(fontName: String): Int {
            return when (fontName) {
                "roboto" -> R.style.ProjectTheme_Roboto
                "montserrat" -> R.style.ProjectTheme_Montserrat
                else -> com.google.android.material.R.style.Theme_AppCompat
            }
        }

        private fun updateLanguage(languageCode: String) {
            LocaleHelper.persistLanguage(requireContext(), languageCode)

            activity?.recreate()
        }

    }

    override fun attachBaseContext(newBase: Context) {
        val lang = LocaleHelper.getSavedLanguage(newBase)
        super.attachBaseContext(LocaleHelper.setLocale(newBase, lang))
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}