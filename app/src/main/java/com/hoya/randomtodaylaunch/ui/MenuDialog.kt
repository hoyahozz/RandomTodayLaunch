package com.hoya.randomtodaylaunch.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoya.randomtodaylaunch.adapter.MenuAdapter
import com.hoya.randomtodaylaunch.databinding.DialogMenuBinding
import com.hoya.randomtodaylaunch.util.RecyclerViewDecoration
import com.hoya.randomtodaylaunch.viewModel.ListViewModel

class MenuDialog(
    private val fName : String
) : DialogFragment() {


//    private val viewModel = ViewModelProvider(this)[ListViewModel::class.java]

    private val viewModel : ListViewModel by viewModels()
//    private val viewModel = (requireActivity() as ListActivity).viewModel

    private lateinit var binding: DialogMenuBinding
    private val adapter: MenuAdapter by lazy { MenuAdapter() }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel.getMenuList(fName)

        viewModel.menuList.observe(this) {
            adapter.submitList(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.apply {
            this.layoutManager = LinearLayoutManager(requireContext())
            this.adapter = this@MenuDialog.adapter
            this.addItemDecoration(RecyclerViewDecoration(15))
        }

        binding.closeBtn.setOnClickListener {
            dismiss()
        }

        binding.fName = fName
    }
}