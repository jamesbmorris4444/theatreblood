package com.fullsekurity.theatreblood.utils

import androidx.lifecycle.ViewModelProviders
import com.fullsekurity.theatreblood.activity.MainActivity
import com.fullsekurity.theatreblood.donor.DonorFragment
import com.fullsekurity.theatreblood.donor.DonorViewModel
import com.fullsekurity.theatreblood.donors.DonateProductsFragment
import com.fullsekurity.theatreblood.donors.DonateProductsListViewModel
import com.fullsekurity.theatreblood.modal.StandardModal
import com.fullsekurity.theatreblood.repository.Repository
import com.fullsekurity.theatreblood.ui.UIViewModel
import com.fullsekurity.theatreblood.ui.UIViewModelFactory
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Singleton
@Component(modules = [MapperInjectorModule::class])
interface MapperDependencyInjector {
    fun inject(viewModel: UIViewModel)
}

@Singleton
@Component(modules = [ViewModelInjectorModule::class])
interface ViewModelDependencyInjector {
    fun inject(fragment: DonateProductsFragment)
    fun inject(modal: StandardModal)
    fun inject(fragment: DonorFragment)
    fun inject(viewModel: DonateProductsListViewModel)
    fun inject(viewModel: DonorViewModel)
    fun inject(activity: MainActivity)
}

@Module
class MapperInjectorModule {
    @Provides
    @Singleton
    fun colorMapperProvider() : ColorMapper {
        val colorMapper = ColorMapper()
        colorMapper.initialize()
        return colorMapper
    }
    @Provides
    @Singleton
    fun textSizeMapperProvider() : TextSizeMapper {
        val textSizeMapper = TextSizeMapper()
        textSizeMapper.initialize()
        return textSizeMapper
    }
    @Provides
    @Singleton
    fun typefaceMapperProvider() : TypefaceMapper {
        val typefaceMapper = TypefaceMapper()
        typefaceMapper.initialize()
        return typefaceMapper
    }
}

@Module
class ViewModelInjectorModule(val activity: MainActivity) {
    @Provides
    @Singleton
    fun uiViewModelProvider() : UIViewModel {
        return ViewModelProviders.of(activity, UIViewModelFactory(activity.application)).get(UIViewModel::class.java)
    }
    @Provides
    @Singleton
    fun repositoryProvider() : Repository {
        return activity.repository
    }

}