package com.fullsekurity.theatreblood.input

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.fullsekurity.theatreblood.R
import com.fullsekurity.theatreblood.activity.MainActivity
import com.fullsekurity.theatreblood.databinding.TextInputFragmentBinding
import com.fullsekurity.theatreblood.ui.UIViewModel
import com.fullsekurity.theatreblood.utils.DaggerViewModelDependencyInjector
import com.fullsekurity.theatreblood.utils.ViewModelInjectorModule
import javax.inject.Inject


class InputFragment : Fragment() {

    private lateinit var inputViewModel: InputViewModel

    companion object {
        fun newInstance(): InputFragment { return InputFragment() }
    }

    @Inject
    lateinit var uiViewModel: UIViewModel

    override fun onAttach(context: Context) {
        DaggerViewModelDependencyInjector.builder()
            .viewModelInjectorModule(ViewModelInjectorModule(activity as MainActivity))
            .build()
            .inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: TextInputFragmentBinding = DataBindingUtil.inflate<ViewDataBinding>(inflater, R.layout.text_input_fragment, container, false) as TextInputFragmentBinding
        binding.lifecycleOwner = this
        inputViewModel = ViewModelProviders.of(this, InputViewModelFactory(activity as MainActivity)).get(InputViewModel::class.java)
        binding.inputViewModel = inputViewModel
        binding.uiViewModel = uiViewModel
        uiViewModel.currentTheme = (activity as MainActivity).currentTheme
        inputViewModel.donorSearchLiveData.observe(this, Observer {
            (activity as MainActivity).showDonors(it)
        })
        inputViewModel.setRootView(binding.root, uiViewModel)
        return binding.root
    }

}