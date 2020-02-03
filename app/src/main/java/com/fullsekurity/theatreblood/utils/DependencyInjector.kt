package com.fullsekurity.theatreblood.utils

import androidx.lifecycle.ViewModelProvider
import com.fullsekurity.theatreblood.activity.MainActivity
import com.fullsekurity.theatreblood.createproducts.CreateProductsFragment
import com.fullsekurity.theatreblood.createproducts.CreateProductsListViewModel
import com.fullsekurity.theatreblood.donateproducts.DonateProductsFragment
import com.fullsekurity.theatreblood.donateproducts.DonateProductsListViewModel
import com.fullsekurity.theatreblood.managedonor.DonorFragment
import com.fullsekurity.theatreblood.managedonor.ManageDonorViewModel
import com.fullsekurity.theatreblood.modal.StandardModal
import com.fullsekurity.theatreblood.reassociateproducts.ReassociateProductsFragment
import com.fullsekurity.theatreblood.reassociateproducts.ReassociateProductsListViewModel
import com.fullsekurity.theatreblood.repository.Repository
import com.fullsekurity.theatreblood.ui.UIViewModel
import com.fullsekurity.theatreblood.ui.UIViewModelFactory
import com.fullsekurity.theatreblood.viewdonorlist.ViewDonorListFragment
import com.fullsekurity.theatreblood.viewdonorlist.ViewDonorListListViewModel
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
    fun inject(viewModelManage: ManageDonorViewModel)
    fun inject(activity: MainActivity)
    fun inject(viewModel: CreateProductsListViewModel)
    fun inject(fragment: CreateProductsFragment)
    fun inject(viewModel: ReassociateProductsListViewModel)
    fun inject(fragment: ReassociateProductsFragment)
    fun inject(fragment: ViewDonorListFragment)
    fun inject(viewModel: ViewDonorListListViewModel)
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
        return ViewModelProvider(activity, UIViewModelFactory(activity.application)).get(UIViewModel::class.java)
    }
    @Provides
    @Singleton
    fun repositoryProvider() : Repository {
        return activity.repository
    }

}