package com.fullsekurity.theatreblood.utils

import androidx.lifecycle.ViewModelProviders
import com.fullsekurity.theatreblood.activity.MainActivity
import com.fullsekurity.theatreblood.donor.DonorFragment
import com.fullsekurity.theatreblood.donors.DonorsFragment
import com.fullsekurity.theatreblood.donors.DonorsListViewModel
import com.fullsekurity.theatreblood.input.InputFragment
import com.fullsekurity.theatreblood.input.InputViewModel
import com.fullsekurity.theatreblood.logger.LogUtils
import com.fullsekurity.theatreblood.modal.StandardModal
import com.fullsekurity.theatreblood.repository.Repository
import com.fullsekurity.theatreblood.ui.UIViewModel
import com.fullsekurity.theatreblood.ui.UIViewModelFactory
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Singleton
@Component(modules = [RepositoryInjectorModule::class])
interface RepositoryDependencyInjector {
    fun inject(activity: MainActivity)
    fun inject(inputViewModel: InputViewModel)
}

@Singleton
@Component(modules = [MapperInjectorModule::class])
interface MapperDependencyInjector {
    fun inject(viewModel: UIViewModel)
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
class RepositoryInjectorModule {
    @Provides
    @Singleton
    fun repositoryProvider() : Repository {
        val a = Repository()
        LogUtils.D("JIMX", LogUtils.FilterTags.withTags(LogUtils.TagFilter.ANX), String.format("JIMX   REPOSITORY   %s", a))
        return a
    }
}

@Module
class ViewModelInjectorModule(val activity: MainActivity) {
    @Provides
    @Singleton
    fun uiViewModelProvider() : UIViewModel {
        LogUtils.D("JIMX", LogUtils.FilterTags.withTags(LogUtils.TagFilter.ANX), String.format("JIMX   VIEWMODEL"))
        return ViewModelProviders.of(activity, UIViewModelFactory(activity.application)).get(UIViewModel::class.java)
    }

}