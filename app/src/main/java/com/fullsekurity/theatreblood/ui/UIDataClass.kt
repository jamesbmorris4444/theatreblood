package com.fullsekurity.theatreblood.ui

class UIDataClass(

    val standardBackground: Int,                          // Main background drawable resource for all fragments
    val backgroundLottieJsonFileName: String,             // Main background lottie file name for all fragments

    val standardDialogBackground: Int,                    // Standard dialog background drawable resource
    val standardDialogDashedLine: Int,                    // Standard dialog circular header icon drawable resource
    val standardDialogDividerColor: String,               // Standard dialog button_light divider line color
    val standardDialogPasswordHintColor: String,          // Standard dialog password hint text color
    val standardDialogTopSpacerHeight: Float,             // Standard dialog spacer height at the top of the modal
    val standardDialogSubTitleHeight: Float,              // Standard dialog spacer height below title text
    val standardDialogTitleTextColor: String,             // Standard dialog title text color
    val standardDialogTitleTextSize: String,              // Standard dialog title text size
    val standardDialogIconHeight: Float,                  // Standard dialog icon drawable height
    val standardDialogIconWidth: Float,                   // Standard dialog icon drawable width
    val standardDialogSubIconHeight: Float,               // Standard dialog spacer height below icon
    val standardDialogSubBodyHeight: Float,               // Standard dialog spacer height below body text
    val standardDialogBodyTextColor: String,              // Standard dialog body text color
    val standardDialogBodyTextSize: String,               // Standard dialog body text size
    val standardDialogButtonHeight: Float,                // Standard dialog button_light height
    val standardDialogButtonTextColor: String,            // Standard dialog positive button_light text color
    val standardDialogButtonTextSize: String,             // Standard dialog positive button_light text size
    val standardDialogPasswordHeight: Float,              // Standard dialog password entry field height
    val standardDialogPasswordTextColor: String,          // Standard dialog password entry field text color
    val standardDialogPasswordTextSize: String,           // Standard dialog password entry field text size
    val standardDialogPasswordToggleColor: String,        // Standard dialog password toggle icon color
    val standardDialogListCenterTextColor: String,        // Standard dialog list header center text color
    val standardDialogListCenterTextSize: String,         // Standard dialog list header center text size
    val standardDialogListCenterTextMarginLeft: Float,    // Standard dialog list header center margin left (between image and text)
    val standardDialogListAmountTextColor: String,        // Standard dialog list header amount text color
    val standardDialogListAmountTextSize: String,         // Standard dialog list header amount text size
    val standardDialogListLongMarginLeft: Float,          // Standard dialog list items long margin left (between left edge of screen and text)
    val standardDialogListTextColor: String,              // Standard dialog list item text color
    val standardDialogListTextSize: String,               // Standard dialog list item text size
    val standardDialogListSmallTextColor: String,         // Standard dialog list item small text color
    val standardDialogListSmallTextSize: String,          // Standard dialog list item small text size
    val standardDialogListTitleMarginTop: Float,          // Standard dialog list title (first item) top margin (between dashed line and text)
    val standardDialogListTitleHeight: Float,             // Standard dialog list title height
    val standardDialogListLine1MarginTop: Float,          // Standard dialog list item (all but first item) top margin (between line 1 text and dashed line above it)
    val standardDialogListLine2MarginTop: Float,          // Standard dialog list item (all but first item) top margin (between line 2 text and line 1 text above it)
    val standardDialogListFooterTextColor: String,        // Standard dialog footer text color
    val standardDialogListFooterTextSize: String,         // Standard dialog footer text size

    val recyclerViewAlternatingColor1: String,            // one of two alternating colors on all lists
    val recyclerViewAlternatingColor2: String,            // the other of two alternating colors on all lists
    val primaryColor: String,                             // primary color used throughout the app
    val secondaryColor: String,                           // secondary color used throughout the app
    val toolbarTextColor: String,                         // toolbar text color

    val editTextCursor: Int,                              // hammer cursor drawable for edit texts

    val editTextLabelColor: String,                       // EditText hint text color
    val editTextLabelSize: String,                        // EditText hint text size (changes to this value should be synchronized with styles.TextInputLayoutForLight/Dark)
    val editTextColor: String,                            // EditText text color
    val editTextSize: String,                             // EditText text size
    val editTextUpperHintColor: String,                   // EditText upper hint text color (hint text color when it is elevated above the EditText, focused or text-filled)
    val editTextLowerHintColor: String,                   // EditText lower hint text color (hint text color when it is inside the EditText, not focused and not text-filled)
    val editTextBackground: Int,                          // EditText background drawable (used to provide rounded corners only)

    val buttonDrawable: Int,                              // Button drawable icon
    val buttonTextColor: String,                          // Button text color
    val buttonTextSize: String,                           // Button text size

    val donorItemTextColor: String,                       // Donor item text color
    val donorItemTextSize: String,                        // Donor item text size

    val largeErrorTextColor: String,                      // Standard large error text color
    val largeErrorTextSize: String,                       // Standard large error text size

    val radioButtonColor: String,                         // radio button_light color
    val dropdownBackground: Int,                          // dropdown background drawable used to set background color

    val productItemTextColor: String,                     // Product item text color
    val productItemTextSize: String,                      // Product item text size

    val productGridBackgroundDrawable11: Int,             // Product grid background drawable upper left
    val productGridBackgroundDrawable12: Int,             // Product grid background drawable upper right
    val productGridBackgroundDrawable21: Int,             // Product grid background drawable lower left
    val productGridBackgroundDrawable22: Int,             // Product grid background drawable lower right

    val editTextProductHintColor: String,                 // Product EditText hint text color
    val editTextProductColor: String,                     // Product EditText text color
    val editTextAboRhProductColor: String,                // Product EditText text color for ABO Rh entry only
    val editTextProductSize: String,                      // Product EditText text size
    val editTextProductBackground: Int,                   // Product EditText background drawable resource

    val productIdTextColor: String,                       // Product grid id label text color
    val productIdTextSize: String,                        // Product grid id label text size

    val incorrectDonorTextColor: String,                  // Reassociate donor label text color
    val incorrectDonorTextSize: String,                   // Reassociate donor label text size

    val dropdownTextColor: String,                        // aboRh/branch dropdown label text color
    val dropdownTextSize: String                          // aboRh/branch dropdown label text size
)