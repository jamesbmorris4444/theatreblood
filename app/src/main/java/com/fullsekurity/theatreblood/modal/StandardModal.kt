package com.fullsekurity.theatreblood.modal

import android.app.Dialog
import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fullsekurity.theatreblood.R
import com.fullsekurity.theatreblood.activity.Callbacks
import com.fullsekurity.theatreblood.databinding.StandardModalBinding
import com.fullsekurity.theatreblood.databinding.StandardModalFooterItemBinding
import com.fullsekurity.theatreblood.databinding.StandardModalHeaderItemBinding
import com.fullsekurity.theatreblood.databinding.StandardModalListItemBinding
import com.fullsekurity.theatreblood.ui.UIViewModel
import com.fullsekurity.theatreblood.utils.DaggerViewModelDependencyInjector
import com.fullsekurity.theatreblood.utils.ViewModelInjectorModule
import javax.inject.Inject

class StandardModal (

    /**
         * Creates a standard modal for all modals throughout the app.
         * All current modals should be converted to use StandardModal, and then the current modals should be deleted.
         * See TransactionsDetailDialog for an example of creating LIST type modals
         * @param callbacks is the interface that allows the fetching of context and activity, if needed.
         * @param modalType designates the type of the modal, and is optional; default value is STANDARD. See types below under ModalType.
         * @param iconType designates the type of icon to be displayed, and is optional; default value is NONE, so if no modalType parameter is supplied, no icon will appear.
         * @param titleText is optional, default value is NO title (there must be either a title or a body present, except for LIST type modals which normally do not have a title or body).
         * @param bodyText is optional, default value is NO body (there must be either a title or a body present, except for LIST type modals which normally do not have a title or body).
         * @param bodyGravity is optional, default value is Gravity.LEFT, which will left-justify the body. Use Gravity.CENTER to center the body.
         * @param hintText is optional, default value is NO hint (hintText is used only in modals like password modals, etc.).
         * @param positiveText is required, and is the text that appears on the rightmost bottom button_light (1, 2, 3 buttons can be present).
         * @param negativeText is optional, and is the text that appears on the bottom button_light to the left of the positiveText button_light (2 or 3 buttons can be present if a negativeText button_light appears).
         * @param neutralText is optional, and is the text that appears on the bottom button_light to the left of the negativeText button_light (3 buttons must be present if a neutralText button_light appears).
         * @param transactionIcon is optional and is used in LIST type modals. See TransactionsDetailDialog for examples.
         * @param modalItemList is a list of ModalItems that will appear in the modal as a list. See TransactionsDetailDialog for examples.
         * @param dialogFinishedListener is a required interface that provides methods for handling modal button_light clicks.
         *
         * @returns a DialogFragment which can be shown using a statement like
         *      fragment.fragmentManager?.let { dialog.show(it, TAG) }, where dialog is the object created by the StandardModal constructor
         *      An example of the usage of Standard  Modal is:
         *      val dialog = StandardModal(
                                 modalType = StandardModal.ModalType.PASSWORD,
                                 titleText = fragment.getString(R.string.std_themed_modal_password_authentication_title),
                                 hintText = fragment.getString(R.string.std_themed_modal_password_authentication_hint),
                                 positiveText = fragment.getString(R.string.std_themed_modal_password_authentication_submit),
                                 negativeText = fragment.getString(R.string.std_themed_modal_password_authentication_cancel),
                                 dialogFinishedListener = object : StandardModal.DialogFinishedListener {

                                     override fun onPositive(password: String) {
                                         validatePassword(fragment, password, validateSuccess = {
                                             validateSuccess.invoke(it)
                                         }, validateFailed = {
                                             validateFailed.invoke(it)
                                         })
                                     }

                                     override fun onNegative() {
                                         onCancel?.invoke()
                                     }

                                     override fun onNeutral() {
                                     }

                                     override fun onBackPressed() {
                                     }
                                 }
                )
                fragment.fragmentManager?.let { dialog.show(it, TAG) }
         */

    private val callbacks: Callbacks,
    private var modalType: ModalType = ModalType.STANDARD,
    private var iconType: IconType = IconType.NONE,
    titleText: String = "",
    bodyText: String = "",
    bodyGravity: Int = Gravity.LEFT,
    hintText: String = "",
    positiveText: String = "",
    negativeText: String = "",
    neutralText: String = "",
    private val transactionIcon: Int = 0,
    val modalItemList: List<ModalItem> = arrayListOf(),
    val dialogFinishedListener : DialogFinishedListener? = null

) : DialogFragment() {

    enum class ModalType {
        STANDARD,
        PASSWORD,
        LIST
    }

    enum class IconType {
        NONE,
        ERROR
    }

    enum class ModalListType {
        HEADER_ITEM, LIST_ITEM, FOOTER_ITEM
    }

    val transIcon: ObservableField<Drawable> = ObservableField()
    val icon: ObservableField<Drawable> = ObservableField()
    val titleText: ObservableField<String> = ObservableField(titleText)
    val bodyText: ObservableField<String> = ObservableField(bodyText)
    val bodyGravity: ObservableField<Int> = ObservableField(bodyGravity)
    val hintText: ObservableField<String> = ObservableField(hintText)
    val positiveText: ObservableField<String> = ObservableField(positiveText)
    val negativeText: ObservableField<String> = ObservableField(negativeText)
    val neutralText: ObservableField<String> = ObservableField(neutralText)
    val iconVisibility: ObservableField<Int> = ObservableField(View.VISIBLE)
    val titleVisibility: ObservableField<Int> = ObservableField(if (titleText.isEmpty()) View.GONE else View.VISIBLE)
    val titleSpacerVisibility: ObservableField<Int> = ObservableField(if (titleText.isEmpty() || bodyText.isEmpty()) View.GONE else View.VISIBLE)
    val bodyVisibility: ObservableField<Int> = ObservableField(if (bodyText.isEmpty()) View.GONE else View.VISIBLE)
    val bodySpacerVisibility: ObservableField<Int> = ObservableField(View.GONE)
    val positiveButtonVisibility: ObservableField<Int> = ObservableField(if (positiveText.isEmpty()) View.GONE else View.VISIBLE)
    val negativeButtonVisibility: ObservableField<Int> = ObservableField(if (negativeText.isEmpty()) View.GONE else View.VISIBLE)
    val neutralButtonVisibility: ObservableField<Int> = ObservableField(if (neutralText.isEmpty()) View.GONE else View.VISIBLE)
    val passwordVisibility: ObservableField<Int> = ObservableField(View.GONE)
    val listVisibility: ObservableField<Int> = ObservableField(View.GONE)
    lateinit var recyclerView: RecyclerView

    // observable used for two-way donations binding. Values set into this field will show in view.
    // Text typed into EditText in view will be stored into this field after each character is typed.
    var editTextInput: ObservableField<String> = ObservableField("")

    private var onPositive: (View) -> Unit = { _ ->
        if (modalType == ModalType.PASSWORD) {
            editTextInput.get()?.let {
                dialogFinishedListener?.onPositive(it.trim())
            }
        } else {
            dialogFinishedListener?.onPositive("")
        }
        dismiss()
    }

    private var onNegative: (View) -> Unit = { _ ->
        dialogFinishedListener?.onNegative()
        dismiss()
    }

    private var onNeutral: (View) -> Unit = { _ ->
        dialogFinishedListener?.onNeutral()
        dismiss()
    }

    private lateinit var binding: StandardModalBinding

    @Inject
    lateinit var uiViewModel: UIViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        DaggerViewModelDependencyInjector.builder()
                .viewModelInjectorModule(ViewModelInjectorModule(callbacks.fetchActivity()))
                .build()
                .inject(this)

        bodyText.get()?.let { bText -> titleText.get()?.let { tText -> positiveText.get()?.let { pText ->
            if (modalType == ModalType.LIST) {
                if (transactionIcon == 0) {
                    modalType = ModalType.STANDARD
                    iconType = IconType.NONE
                    titleText.set("ERROR: transaction icon must be supplied for LIST type modals")
                    titleVisibility.set(View.VISIBLE)
                }
            } else {
                if (bText.isEmpty() && tText.isEmpty()) {
                    modalType = ModalType.STANDARD
                    iconType = IconType.NONE
                    titleText.set("ERROR: bodyText and titleText cannot both be empty")
                    titleVisibility.set(View.VISIBLE)
                } else if (pText.isEmpty()) {
                    modalType = ModalType.STANDARD
                    iconType = IconType.NONE
                    titleText.set("ERROR: positiveText must always be present")
                    bodyText.set("")
                    positiveText.set("positiveText ERROR")
                    titleVisibility.set(View.VISIBLE)
                }
            }
        }}}

        when (modalType) {
            ModalType.STANDARD -> {
                binding =  DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.standard_modal, container, false)
                setIcon()
                passwordVisibility.set(View.GONE)
            }
            ModalType.PASSWORD -> {
                binding =  DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.standard_modal, container, false)
                setIcon()
                passwordVisibility.set(View.VISIBLE)
                bodySpacerVisibility.set(View.VISIBLE)
            }
            ModalType.LIST -> {
                binding =  DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.standard_modal, container, false)
                transIcon.set(ContextCompat.getDrawable(binding.root.context, transactionIcon))
                recyclerView = binding.root.findViewById(R.id.modal_list_view)
                listVisibility.set(View.VISIBLE)
                iconVisibility.set(View.GONE)
                val resultLayoutManager = LinearLayoutManager(activity)
                lateinit var adapter: ModalListAdapter
                context?.let {
                    adapter = ModalListAdapter(callbacks, uiViewModel,this, it)
                }
                recyclerView.adapter = adapter
                recyclerView.layoutManager = resultLayoutManager
                resultLayoutManager?.let{
                    recyclerView.addItemDecoration(ModalItemDecoration(it))
                }
                adapter.updateData(modalItemList)
            }
        }

        binding.lifecycleOwner = this
        binding.standardModal = this
        binding.uiViewModel = uiViewModel
        val positive: TextView = binding.root.findViewById(R.id.dialog_positive)
        positive.setOnClickListener(onPositive)
        val negative: TextView = binding.root.findViewById(R.id.dialog_negative)
        negative.setOnClickListener(onNegative)
        val neutral: TextView = binding.root.findViewById(R.id.dialog_neutral)
        neutral.setOnClickListener(onNeutral)
        return binding.root
    }

    private fun setIcon() {
        iconVisibility.set(View.VISIBLE)
        when (iconType) {
            IconType.ERROR -> {
                icon.set(uiViewModel.modalErrorIcon)
            }
            IconType.NONE -> {
                iconVisibility.set(View.GONE)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.StandardDialog)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object: Dialog(callbacks.fetchActivity(), theme) {
            override fun onBackPressed() {
                dismiss()
                dialogFinishedListener?.onBackPressed()
                super.onBackPressed()
            }
        }
    }

    interface DialogFinishedListener {
        fun onPositive(string: String)
        fun onNegative()
        fun onNeutral()
        fun onBackPressed()
    }

//    var textHint: ObservableField<String> = ObservableField("HINT") // Hint value initialized into EditText field (This is a hint, not initialized text that is initialized into the view field)
//    private var dataStorageAreaOutsideOfObservableField: String = "HINT TEXT"
//
//    fun readTestEditFieldValue() { // click Locations button_light, observes current value of observable testEdit field that has been typed by the user
//    }
//
//    fun transferTestEditToLocalVariable() { // click Direct Deposit button_light
//        dataStorageAreaOutsideOfObservableField = editTextInput.get() ?: return
//    }
//
//    fun forceTestEditToASpecificValue() { // click MoneyBot button_light
//        editTextInput.set("") // Store "" to clear the observable EditText field
//    }

    fun onTextChanged(string: CharSequence, start: Int, before: Int, count: Int) {
        // within "string", the "count" characters beginning at index "start" have just replaced old text that had length "before"
    }

    class ModalItem(val type: ModalListType,
                    val title: String,
                    val amount: String,
                    val line1: String,
                    val line2: String)

    class ModalListAdapter(private val callbacks: Callbacks, val uiViewModel: UIViewModel, val standardModal: StandardModal, val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private val TAG = ModalListAdapter::class.java.simpleName
        private var itemList = arrayListOf<ModalItem>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            when (viewType) {
                ModalListType.HEADER_ITEM.ordinal -> {
                    val binding: StandardModalHeaderItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.standard_modal_header_item, parent, false)
                    binding.uiViewModel = uiViewModel
                    binding.standardModal = standardModal
                    uiViewModel.currentTheme = callbacks.fetchActivity().currentTheme
                    return HeaderViewHolder(binding.root)
                }
                ModalListType.LIST_ITEM.ordinal -> {
                    val binding: StandardModalListItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.standard_modal_list_item, parent, false)
                    binding.uiViewModel = uiViewModel
                    uiViewModel.currentTheme = callbacks.fetchActivity().currentTheme
                    return ListViewHolder(binding.root)
                }
                ModalListType.FOOTER_ITEM.ordinal -> {
                    val binding: StandardModalFooterItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.standard_modal_footer_item, parent, false)
                    binding.uiViewModel = uiViewModel
                    uiViewModel.currentTheme = callbacks.fetchActivity().currentTheme
                    return FooterViewHolder(binding.root)
                }
                else -> {
                    val binding: StandardModalFooterItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.standard_modal_footer_item, parent, false)
                    return FooterViewHolder(binding.root)
                }
            }
        }

        override fun getItemViewType(position: Int): Int {
            return itemList[position].type.ordinal
        }

        override fun getItemCount(): Int {
            return itemList.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val item = itemList[position]
            when (getItemViewType(position)) {
                ModalListType.HEADER_ITEM.ordinal -> {
                    (holder as HeaderViewHolder).centerText?.text = item.title
                    holder.amountText?.text = item.amount
                }
                ModalListType.LIST_ITEM.ordinal -> {
                    if (position == 1) {
                        (holder as ListViewHolder).titleText?.text = item.title
                        holder.titleText?.visibility = View.VISIBLE
                    } else {
                        (holder as ListViewHolder).titleText?.visibility = View.GONE
                    }
                    holder.line1Text?.text = item.line1
                    holder.line2Text?.text = item.line2
                }
                ModalListType.FOOTER_ITEM.ordinal -> {
                    (holder as FooterViewHolder).footerText?.text = item.line1
                }
            }
        }

        fun updateData(list: List<ModalItem>) {
            itemList.clear()
            itemList.addAll(list)
            notifyDataSetChanged()
        }

        class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val centerText: TextView? = itemView.findViewById(R.id.center_text)
            val amountText: TextView? = itemView.findViewById(R.id.amount_text)
        }

        class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var titleText: TextView? = itemView.findViewById(R.id.modal_text_list_title)
            val line1Text: TextView? = itemView.findViewById(R.id.modal_text_line_1)
            val line2Text: TextView? = itemView.findViewById(R.id.modal_text_line_2)
        }

        class FooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var footerText: TextView? = itemView.findViewById(R.id.modal_text_footer)
        }

    }

    class ModalItemDecoration(val layoutManager: LinearLayoutManager) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            val position = (view.layoutParams as RecyclerView.LayoutParams).viewAdapterPosition
            if (position == 0) {
                outRect.bottom = 0
            } else if (position == 1) {
                outRect.top = 12
            } else {
                outRect.top = 30
            }
        }
    }

}


