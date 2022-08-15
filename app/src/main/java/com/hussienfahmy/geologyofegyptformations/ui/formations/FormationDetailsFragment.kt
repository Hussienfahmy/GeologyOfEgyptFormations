package com.hussienfahmy.geologyofegyptformations.ui.formations

import android.graphics.Paint
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.hussienfahmy.geologyofegyptformations.R
import com.hussienfahmy.geologyofegyptformations.databinding.FragmentFormationDetailsBinding
import com.hussienfahmy.geologyofegyptformations.model.Formation
import com.hussienfahmy.geologyofegyptformations.utils.searchFormationByName
import com.hussienfahmy.geologyofegyptformations.utils.toastNotAvailable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class FormationDetailsFragment : Fragment() {

    private val viewModel: FormationDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFormationDetailsBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }
}