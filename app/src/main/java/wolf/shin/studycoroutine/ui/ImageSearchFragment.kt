package wolf.shin.studycoroutine.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import wolf.shin.studycoroutine.databinding.FragmentMainBinding

class ImageSearchFragment : Fragment() {

    private lateinit var imageSearchViewModel: ImageSearchViewModel
    private val adapter: ImageSearchAdapter = ImageSearchAdapter {
        imageSearchViewModel.toggle(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imageSearchViewModel = ViewModelProvider(this)[ImageSearchViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentMainBinding.inflate(inflater, container, false)
        val root = binding.root

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(context, 4)
        binding.search.setOnClickListener {
            imageSearchViewModel.handleQuery(binding.editText.text.trim().toString())
        }

        viewLifecycleOwner.lifecycleScope.launch {
            imageSearchViewModel.pagingDataFlow.collectLatest { adapter.submitData(it) }
        }

        return root
    }
}