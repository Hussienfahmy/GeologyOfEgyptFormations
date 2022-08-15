package com.hussienfahmy.geologyofegyptformations.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hussienfahmy.geologyofegyptformations.R
import com.mikepenz.aboutlibraries.LibsBuilder
import mehdi.sakout.aboutpage.AboutPage
import mehdi.sakout.aboutpage.Element

class AboutFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val licences = Element()
        licences.title = "licenses"
        licences.onClickListener = View.OnClickListener { displayLicenses() }
        return AboutPage(context, false)
            .setDescription(getString(R.string.aboutLibraries_description_text))
            .isRTL(false)
            .setImage(R.mipmap.ic_launcher_foreground)
            .addItem(licences)
            .create()
    }

    private fun displayLicenses() {
        context?.let {
            LibsBuilder()
                .withAboutAppName("Geology Of Egypt Formations")
                .withAboutVersionShownCode(true)
                .withActivityTitle("Licences")
                .withLicenseShown(true)
                .withLicenseDialog(true)
                .withSortEnabled(true)
                .start(it)
        }
    }
}