package com.fullsekurity.theatreblood.ui

class UIDataClass(

    val standardDialogBackground: Int,                    // Standard dialog background drawable resource
    val standardDialogDashedLine: Int,                    // Standard dialog circular header icon drawable resource
    val standardDialogWidth: Float,                       // Standard dialog total width
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

    val editTextNameHintColor: String,                    // Input EditText hint text color
    val editTextNameColor: String,                        // Input EditText text color
    val editTextNameSize: String,                         // Input EditText text size
    val editTextNameBackgroundColor: String,              // Input EditText background color
    val editTextNameUpperHintColor: String,               // Input EditText upper hint text color
    val editTextNameBackground: Int,                      // Input EditText background drawable

    val editTextDisplayModifyHintColor: String,           // Display/Modify EditText hint text color
    val editTextDisplayModifyHintSize: String,            // Display/Modify EditText hint text color (changes to this value should be synchronized with styles.TextInputLayoutForLight/Dark)
    val editTextDisplayModifyColor: String,               // Display/Modify EditText text color
    val editTextDisplayModifySize: String,                // Display/Modify EditText text size
    val editTextDisplayModifyBackgroundColor: String,     // Display/Modify EditText underline color
    val editTextDisplayModifyUpperHintColor: String,      // Display/Modify EditText upper hint text color
    val editTextDisplayModifyBackground: Int,             // Display/Modify EditText background drawable

    val buttonDrawable: Int,                              // Button drawable icon
    val buttonTextColor: String,                          // Button text color
    val buttonTextSize: String,                           // Button text size

    val donorItemTextColor: String,                       // Donor item text color
    val donorItemTextSize: String,                        // Donor item text size

    val largeErrorTextColor: String,                      // Standard large error text color
    val largeErrorTextSize: String,                       // Standard large error text size

    val radioButtonColor:String                           // radio button_light color

)