package hr.fer.myspellbuddy.view.fragment

import androidx.appcompat.widget.Toolbar
import dagger.hilt.android.AndroidEntryPoint
import hr.fer.myspellbuddy.R
import hr.fer.myspellbuddy.databinding.FragmentQRCodeScannerBinding
import hr.fer.myspellbuddy.util.extensions.viewBinding

@AndroidEntryPoint
class QRCodeScannerFragment : BaseFragment(R.layout.fragment_q_r_code_scanner) {

    override fun getToolbar(): Toolbar? = null

    private val binding by viewBinding(FragmentQRCodeScannerBinding::bind)


}