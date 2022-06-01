package hr.fer.myspellbuddy.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hr.fer.myspellbuddy.databinding.ItemResultBinding
import hr.fer.myspellbuddy.model.ResultPair

class ResultsAdapter(
    private var resultsList: List<ResultPair> = listOf()
) : RecyclerView.Adapter<ResultsAdapter.ResultsVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultsVH {
        val binding =
            ItemResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ResultsVH(binding)
    }

    override fun onBindViewHolder(holder: ResultsVH, position: Int) {
        return holder.bind(resultsList[position])
    }

    override fun getItemCount(): Int = resultsList.size

    fun setResults(newResultPairs: List<ResultPair>) {
        resultsList = newResultPairs
        notifyDataSetChanged()
    }

    inner class ResultsVH(private val binding: ItemResultBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(resultPair: ResultPair) {
            binding.apply {
                tvCorrect.text = resultPair.solution
                tvMistake.text = resultPair.userInput
            }
        }
    }
}