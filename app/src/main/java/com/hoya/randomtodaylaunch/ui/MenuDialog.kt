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

    private val viewModel : ListViewModel by viewModels()

    private lateinit var binding: DialogMenuBinding
    private val adapter: MenuAdapter by lazy { MenuAdapter() }

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

        viewModel.getMenuList(fName)

        viewModel.menuList.observe(this) {
            if (it.isEmpty()) { // 메뉴 리스트가 없으면 안내
                binding.itemEmpty.text = "메뉴가 등록되어 있지 않아요 😭"
            } else {
                binding.itemEmpty.text = ""
            }

            adapter.submitList(it)
        }

        binding.fName = fName
    }
}