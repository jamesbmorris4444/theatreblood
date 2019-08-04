package com.greendot.rewards.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.greendot.rewards.Constants
import com.greendot.rewards.activity.MainActivity
import com.greendot.rewards.databinding.HomeScreenBinding
import com.squareup.picasso.Picasso


class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel

    companion object {
        fun newInstance(): HomeFragment { return HomeFragment() }
        @BindingAdapter("android:src")
        @JvmStatic
        fun setImageUrl(view: ImageView, url: String?) {
            if (url != null) {
                Picasso.get().load(Constants.SMALL_IMAGE_URL_PREFIX + url).into(view)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.unsubscribe()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: HomeScreenBinding = DataBindingUtil.inflate<ViewDataBinding>(inflater, com.greendot.rewards.R.layout.home_screen, container, false) as HomeScreenBinding
        binding.lifecycleOwner = this
        viewModel = ViewModelProviders.of(this, HomeViewModelFactory(activity as MainActivity)).get(HomeViewModel::class.java)
        binding.homeViewModel = viewModel
        viewModel.getArrayListMovie().observe(this, Observer { viewModel.liveDataUpdate() })
        return binding.root
    }

}