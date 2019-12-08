package com.fullsekurity.theatreblood.utils

import android.content.Context
import com.fullsekurity.theatreblood.activity.MainActivity
import com.fullsekurity.theatreblood.donors.DonorsItemViewModel
import com.fullsekurity.theatreblood.home.HomeViewModel
import com.fullsekurity.theatreblood.repository.Repository
import com.fullsekurity.theatreblood.repository.storage.BloodDatabase
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Singleton
@Component(modules = [ContextInjectorModule::class])
interface ContextDependencyInjector {
    fun inject(donorsItemViewModel: DonorsItemViewModel)
    fun inject(homeViewModel: HomeViewModel)
    fun inject(activity: MainActivity)
//    fun inject(viewModel: AccountsViewModel)
//    fun inject(viewModel: RewardsViewModel)
//    fun inject(viewModel: P2pUiViewModel)
//    fun inject(viewModel: DirectDepositUiViewModel)
//    fun inject(generator: GridDashboardFragment.GridDashboardAdapter)
//    fun inject(generator: P2PDashboardFragment.P2PDashboardAdapter)
}

@Singleton
@Component(modules = [ViewModelInjectorModule::class])
interface ViewModelDependencyInjector {
//    fun inject(fragment: SettingsDashboardFragment)
//    fun inject(fragment: PayTransactionFragment)
//    fun inject(fragment: ChallengesMainFragment)
//    fun inject(fragment: ChallengesListFragment)
//    fun inject(fragment: GridDashboardFragment)
//    fun inject(fragment: BalancePagerFragment)
//    fun inject(fragment: P2PDashboardFragment)
//    fun inject(fragment: DirectDepositDetailFragment)
//    fun inject(fragment: ChatBotFragment)
//    fun inject(fragment: MoneyMovementRetailerListFragment)
//    fun inject(fragment: RewardsSendAsGiftSuccessFragment)
//    fun inject(fragment: RewardsSendAsGiftNoteFragment)
//    fun inject(fragment: ContactListFragment)
//    fun inject(fragment: RewardsDashboardFragment)
//    fun inject(fragment: ThemeChoiceFragment)
//    fun inject(fragment: TransactionPageFragment)
//    fun inject(fragment: TransactionsTileFragment)
//    fun inject(fragment: NewBaseTileFragment)
//    fun inject(fragment: P2PNewBaseTileFragment)
//    fun inject(fragment: P2PTransactionPageFragment)
//    fun inject(fragment: P2PTransactionHistoryFragment)
//    fun inject(activity: MainActivity)
//    fun inject(fragment: HomeNavPagerFragment)
//    fun inject(fragment: BottomNavigationFragment)
//    fun inject(fragment: RewardsProductInfoFragment)
//    fun inject(fragment: P2PFundingOptionsFragment)
//    fun inject(fragment: CreditCardOCRFragment)
//    fun inject(fragment: PersonalInfoVerifyFragment)
//    fun inject(fragment: ActivateCardFragment)
//    fun inject(fragment: P2PCardDetailsFragment)
//    fun inject(fragment: ChangePasswordFragment)
//    fun inject(fragment: PersonalInfoFragment)
//    fun inject(fragment: NotificationFragment)
//    fun inject(fragment: ChangeATMPINFragment)
//    fun inject(fragment: SecuritySettingsFragment)
//    fun inject(fragment: ManageCardFragment)
//    fun inject(fragment: TrackCardFragment)
//    fun inject(fragment: P2PeCashRetailerListFragment)
//    fun inject(fragment: P2PRecipientFragment)
//    fun inject(fragment: P2PeCashConfirmFragment)
//    fun inject(fragment: PersonalInfoAddressQASFragment)
//    fun inject(fragment: CardNotReceivedFragment)
//    fun inject(fragment: CardReportLostFragment)
//    fun inject(fragment: VerifyCardDetailsFragment)
//    fun inject(fragment: SupportFragment)
//    fun inject(fragment: SupportFAQFragment)
//    fun inject(fragment: RewardsWalletFragment)
//    fun inject(fragment: RewardPurchaseConfirmationFragment)
//    fun inject(fragment: StandardThemedModal)
//    fun inject(fragment: RewardsSendAsGiftRecipientFragment)
//    fun inject(fragment: DirectDepositFAQDialogFragment)
//    fun inject(fragment: DDEmailEmployerFragment)
//    fun inject(supportFragAdapter: SupportFragment.SupportFragmentAdapter)
}

@Module
class ContextInjectorModule(val context: Context) {
//    @Provides
//    @Singleton
//    fun colorMapperProvider() : ColorMapper {
//        val colorMapper = ColorMapper()
//        colorMapper.initialize()
//        return colorMapper
//    }
//    @Provides
//    @Singleton
//    fun textSizeMapperProvider() : TextSizeMapper {
//        val textSizeMapper = TextSizeMapper()
//        textSizeMapper.initialize()
//        return textSizeMapper
//    }
//    @Provides
//    @Singleton
//    fun typefaceMapperProvider() : TypefaceMapper {
//        val typefaceMapper = TypefaceMapper()
//        typefaceMapper.initialize()
//        return typefaceMapper
//    }
//    @Provides
//    @Singleton
//    fun fileNameMapperProvider() : FileNameMapper {
//        val fileNameMapper = FileNameMapper()
//        fileNameMapper.initialize()
//        return fileNameMapper
//    }
    @Provides
    @Singleton
    fun repositoryProvider() : Repository? {
        val bloodDatabase = BloodDatabase.newInstance(context)
        bloodDatabase ?: return null
        return Repository(bloodDatabase)
    }
}

@Module
class ViewModelInjectorModule(val activity: com.fullsekurity.theatreblood.activity.MainActivity) {
//    @Provides
//    @Singleton
//    fun dashboardViewModelProvider() : DashboardViewModel {
//        return ViewModelProviders.of(activity, DashboardViewModelFactory(activity.application)).get(DashboardViewModel::class.java)
//    }
//    @Provides
//    @Singleton
//    fun rewardsViewModelProvider() : RewardsViewModel {
//        return ViewModelProviders.of(activity, RewardsViewModelFactory(activity.application, "TAG")).get(RewardsViewModel::class.java)
//    }
//    @Provides
//    @Singleton
//    fun bottomNavigationViewModelProvider() : BottomNavigationViewModel {
//        return ViewModelProviders.of(activity, BottomNavigationViewModelFactory(activity.application, "TAG")).get(BottomNavigationViewModel::class.java)
//    }

}