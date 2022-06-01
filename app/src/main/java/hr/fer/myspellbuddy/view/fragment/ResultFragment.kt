package hr.fer.myspellbuddy.view.fragment

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint
import hr.fer.myspellbuddy.R
import hr.fer.myspellbuddy.databinding.FragmentResultBinding
import hr.fer.myspellbuddy.model.ResultPair
import hr.fer.myspellbuddy.util.ResultsAdapter
import hr.fer.myspellbuddy.util.extensions.viewBinding
import timber.log.Timber
import java.io.File

@AndroidEntryPoint
class ResultFragment : BaseFragment(R.layout.fragment_result) {

    override fun getToolbar(): Toolbar? = null

    private val binding by viewBinding(FragmentResultBinding::bind)

    private lateinit var resultsAdapter: ResultsAdapter

    private val resultList by lazy { ResultFragmentArgs.fromBundle(requireArguments()).result.toList() }

    private val barcodeValue by lazy { ResultFragmentArgs.fromBundle(requireArguments()).barcodeValue }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        onClickListener()
        getText {
            checkResult(it)
        }
    }

    private fun checkResult(solution: List<String>) {
        val mistakes = arrayListOf<ResultPair>()
        for (i in 0..solution.size) {
            if (resultList.size > i)
                if (solution[i] != resultList[i])
                    mistakes.add(ResultPair(userInput = resultList[i], solution = solution[i]))
        }
        if (mistakes.isEmpty()) {
            binding.rvResults.visibility = View.GONE
            binding.tvMistakes.visibility = View.GONE
            binding.tvAllCorrect.visibility = View.VISIBLE
        } else {
            binding.rvResults.visibility = View.VISIBLE
            binding.tvMistakes.visibility = View.VISIBLE
            binding.tvAllCorrect.visibility = View.GONE
            resultsAdapter.setResults(mistakes)
        }
    }

    private fun getText(onReceive: (List<String>) -> Unit) {
        val storage = FirebaseStorage.getInstance().reference
        val localFile = File.createTempFile(barcodeValue, "txt")
        storage.child("text/$barcodeValue.txt").getFile(localFile)
            .addOnSuccessListener {
                val bufferedReader = localFile.bufferedReader()
                val inputString = bufferedReader.use { it.readText() }
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
                svm.navigate(
                    ResultFragmentDirections.actionResultFragmentToUploadTextFragment(
                        barcodeValue
                    )
                )
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