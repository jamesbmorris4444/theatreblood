package com.fullsekurity.theatreblood.utils

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import com.fullsekurity.theatreblood.activity.MainActivity
import com.fullsekurity.theatreblood.donor.DonorFragment
import com.fullsekurity.theatreblood.donors.DonorsFragment
import com.fullsekurity.theatreblood.donors.DonorsItemViewModel
import com.fullsekurity.theatreblood.donors.DonorsListViewModel
import com.fullsekurity.theatreblood.input.InputFragment
import com.fullsekurity.theatreblood.modal.StandardModal
import com.fullsekurity.theatreblood.repository.Repository
import com.fullsekurity.theatreblood.repository.storage.BloodDatabase
import com.fullsekurity.theatreblood.ui.UIViewModel
import com.fullsekurity.theatreblood.ui.UIViewModelFactory
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Singleton
@Component(modules = [ContextInjectorModule::class])
interface ContextDependencyInjector {
    fun inject(donorsItemViewModel: DonorsItemViewModel)
    fun inject(activity: MainActivity)
}

@Singleton
@Component(modules = [MapperInjectorModule::class])
interface MapperDependencyInjector {
    fun inject(viewModel: UIViewModel)

}

@Module
class MapperInjectorModule(val context: Context) {
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

@Singleton
@Component(modules = [ViewModelInjectorModule::class])
interface ViewModelDependencyInjector {
    fun inject(fragment: InputFragment)
    fun inject(fragment: DonorsFragment)
    fun inject(modal: StandardModal)
    fun inject(fragment: DonorFragment)
    fun inject(viewModel: DonorsListViewModel)
}

@Module
class ContextInjectorModule(val context: Context) {
    @Provides
    @Singleton
    fun repositoryProvider() : Repository? {
        val bloodDatabase = BloodDatabase.newInstance(context)
        bloodDatabase ?: return null
        return Repository(bloodDatabase)
    }
}

@Module
class ViewModelInjectorModule(val activity: MainActivity) {
    @Provides
    @Singleton
    fun uiViewModelProvider() : UIViewModel {
        return ViewModelProviders.of(activity, UIViewModelFactory(activity.application)).get(UIViewModel::class.java)
    }

}