package com.fullsekurity.theatreblood.barcode

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.fullsekurity.theatreblood.utils.Constants.REQUEST_CODE_ASK_PERMISSIONS
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView

class BarCodeScannerActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {

    private var scannerView: ZXingScannerView? = null
    private var grid11TextViewId = 0
    private var grid11TextView: TextView? = null

    override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        scannerView = ZXingScannerView(this)
        setContentView(scannerView)
    }

    public override fun onResume() {
        super.onResume()
        scannerView?.setResultHandler(this)
        startCamera()
    }

    public override fun onPause() {
        super.onPause()
        scannerView?.stopCamera()
    }

    override fun handleResult(rawResult: Result) {
        // Log.v("tag", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)
        var returnIntent = Intent()
        setResult(Activity.RESULT_OK, returnIntent)
        returnIntent.putExtra("rawResult", rawResult.text)
        finish()
        // If you would like to resume scanning, call this method below:
        //mScannerView.resumeCameraPreview(this);
    }

    private fun startCamera() {
        val hasCameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        if (hasCameraPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CODE_ASK_PERMISSIONS)
            return
        }
        scannerView?.startCamera()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE_ASK_PERMISSIONS -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    scannerView?.startCamera()
                } else {
//                    StandardModal(
//                        this@MainActivity,
//                        modalType = StandardModal.ModalType.STANDARD,
//                        titleText = getString(R.string.std_modal_staging_database_count_title),
//                        bodyText = String.format(getString(R.string.std_modal_staging_database_count_body), response[0] as Int, response[1] as Int),
//                        positiveText = getString(R.string.std_modal_ok),
//                        dialogFinishedListener = object : StandardModal.DialogFinishedListener {
//                            override fun onPositive(password: String) { }
//                            override fun onNegative() { }
//                            override fun onNeutral() { }
//                            override fun onBackPressed() { }
//                        }
//                    ).show(supportFragmentManager, "MODAL")
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
}