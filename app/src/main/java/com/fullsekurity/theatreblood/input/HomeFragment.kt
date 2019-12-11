package com.fullsekurity.theatreblood.input

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.fullsekurity.theatreblood.R
import com.fullsekurity.theatreblood.activity.MainActivity
import com.fullsekurity.theatreblood.databinding.TextInputFragmentBinding
import com.fullsekurity.theatreblood.ui.UIViewModel
import com.fullsekurity.theatreblood.utils.DaggerViewModelDependencyInjector
import com.fullsekurity.theatreblood.utils.ViewModelInjectorModule
import javax.inject.Inject

class HomeFragment : Fragment() {

    private lateinit var viewModel: InputViewModel

    companion object {
        fun newInstance(): HomeFragment { return HomeFragment() }
    }

    @Inject
    lateinit var uiViewModel: UIViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        DaggerViewModelDependencyInjector.builder()
            .viewModelInjectorModule(ViewModelInjectorModule(activity as MainActivity))
            .build()
            .inject(this)
        val binding: TextInputFragmentBinding = DataBindingUtil.inflate<ViewDataBinding>(inflater, R.layout.text_input_fragment, container, false) as TextInputFragmentBinding
        binding.lifecycleOwner = this
        viewModel = ViewModelProviders.of(this, InputViewModelFactory(activity as MainActivity)).get(InputViewModel::class.java)
        binding.inputViewModel = viewModel
        binding.uiViewModel = uiViewModel
        uiViewModel.currentTheme = (activity as MainActivity).currentTheme
        return binding.root
    }

}