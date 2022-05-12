package hr.fer.mydictator.view.fragment

import androidx.appcompat.widget.Toolbar
import hr.fer.mydictator.R
import hr.fer.mydictator.databinding.FragmentQRCodeScannerBinding
import hr.fer.mydictator.util.extensions.viewBinding


class QRCodeScannerFragment : BaseFragment(R.layout.fragment_q_r_code_scanner) {

    override fun getToolbar(): Toolbar? = null

    private val binding by viewBinding(FragmentQRCodeScannerBinding::bind)


}