package hr.fer.myspellbuddy.view.fragment

import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint
import hr.fer.myspellbuddy.R
import hr.fer.myspellbuddy.databinding.FragmentResultBinding
import hr.fer.myspellbuddy.model.ResultPair
import hr.fer.myspellbuddy.util.ResultsAdapter
import hr.fer.myspellbuddy.util.extensions.viewBinding
import hr.fer.myspellbuddy.viewModel.SettingsViewModel
import timber.log.Timber
import java.io.File


@AndroidEntryPoint
class ResultFragment : BaseFragment(R.layout.fragment_result) {

    override fun getToolbar(): Toolbar? = null

    private val binding by viewBinding(FragmentResultBinding::bind)

    private lateinit var resultsAdapter: ResultsAdapter

    private val vm: SettingsViewModel by viewModels()

    private val resultList by lazy { ResultFragmentArgs.fromBundle(requireArguments()).result.toList() }

    private val barcodeValue by lazy { ResultFragmentArgs.fromBundle(requireArguments()).barcodeValue }

    private lateinit var text: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        onClickListener()
        getText {
            checkAndParseResult(it)
        }
    }

    private fun checkAndParseResult(solution: List<String>) {
        val resultPairs = arrayListOf<ResultPair>()
        val mistakes = arrayListOf<String>()
        for (i in solution.indices) {
            if (i < resultList.size) {
                if (solution[i] != resultList[i]) {
                    mistakes.add(resultList[i])
                    resultPairs.add(ResultPair(userInput = resultList[i], solution = solution[i]))
                }
            } else {
                //resultPairs.add(ResultPair(userInput = "Missing input", solution = solution[i]))
            }
        }
        val builder = SpannableStringBuilder()
        resultList.forEach {
            val str = SpannableString(it)
            if (mistakes.contains(it)) {
                str.setSpan(ForegroundColorSpan(Color.RED), 0, it.length, 0)
            } else {
                str.setSpan(ForegroundColorSpan(Color.GREEN), 0, it.length, 0)
            }
            builder.append(str)
            builder.append(" ")
        }
        binding.tvSolution.setText(builder, TextView.BufferType.SPANNABLE)

        if (resultPairs.isEmpty()) {
            binding.tvMistakes.visibility = View.GONE
            binding.rvResults.visibility = View.GONE
            binding.tvAllCorrect.visibility = View.VISIBLE
        } else {
            binding.tvMistakes.visibility = View.VISIBLE
            binding.rvResults.visibility = View.VISIBLE
            binding.tvAllCorrect.visibility = View.GONE
            resultsAdapter.setResults(resultPairs)
        }
    }


    private fun getText(onReceive: (List<String>) -> Unit) {
        val storage = FirebaseStorage.getInstance().reference
        val localFile = File.createTempFile(barcodeValue, "txt")
        storage.child("text/$barcodeValue.txt").getFile(localFile)
            .addOnSuccessListener {
                val bufferedReader = localFile.bufferedReader()
                val inputString = bufferedReader.use { it.readText() }
                text = inputString
                binding.tvText.text = text
                val list = inputString.replace("\n", "").replace("\r", "").split(" ")
                list.forEach { it.trim() }
                onReceive(list)
                Timber.d("Text: $list")
            }
    }

    private fun onClickListener() {
        binding.apply {
            btnHome.setOnClickListener {
                svm.navigate(ResultFragmentDirections.actionResultFragmentToHomeFragment())
            }
            btnUploadAgain.setOnClickListener {
                when (vm.getWritingMethod()) {
                    "digital_ink" -> {
                        svm.navigate(
                            ResultFragmentDirections.actionResultFragmentToDigitalInkFragment(
                                barcodeValue
                            )
                        )
                    }
                    "text_recognition" -> {
                        svm.navigate(
                            ResultFragmentDirections.actionResultFragmentToUploadTextFragment(
                                barcodeValue
                            )
                        )
                    }
                }
            }
        }
    }

    private fun initRecycler() {
        resultsAdapter = ResultsAdapter()
        binding.rvResults.apply {
            adapter = resultsAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    outRect.bottom = 16
                }
            })
        }
    }

}