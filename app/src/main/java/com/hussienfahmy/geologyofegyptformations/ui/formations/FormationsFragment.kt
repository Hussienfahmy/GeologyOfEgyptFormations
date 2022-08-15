package com.hussienfahmy.geologyofegyptformations.ui.formations

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.hussienfahmy.geologyofegyptformations.R
import com.hussienfahmy.geologyofegyptformations.databinding.FragmentFormationsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FormationsFragment : Fragment() {

    private val viewModel: FormationsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        val binding = FragmentFormationsBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        with(binding.recyclerView) {
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )

            binding.recyclerView.adapter = FormationsNamesAdapter(
                OnFormationItemClickListener { formationName ->
                    findNavController().navigate(
                        FormationsFragmentDirections.actionDataFragmentToFormationDetails(
                            formationName
                        )
                    )
                }
            )
        }
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.data_fragment_menu, menu)
        val searchItem = menu.findItem(R.id.search_menu)
        val searchView = searchItem.actionView as SearchView
        searchView.queryHint = "Formation Name"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.submitFilterQuery(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.submitFilterQuery(newText)
                return true
            }
        })

        searchView.setOnCloseListener {
            viewModel.clearFilter()
            return@setOnCloseListener true
        }
    }
}