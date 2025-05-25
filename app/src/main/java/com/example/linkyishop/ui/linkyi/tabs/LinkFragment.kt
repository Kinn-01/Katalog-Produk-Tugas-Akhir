package com.example.linkyishop.ui.linkyi.tabs

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.linkyishop.R
import com.example.linkyishop.data.ViewModelFactory
import com.example.linkyishop.databinding.FragmentLinkBinding
import com.example.linkyishop.databinding.FragmentLinkyiBinding
import com.example.linkyishop.ui.linkyi.AddLinkyiActivity
import com.example.linkyishop.ui.linkyi.LinkyiViewModel
import kotlinx.coroutines.launch


class LinkFragment : Fragment() {
    private var _binding: FragmentLinkBinding? = null
    private val viewModel by viewModels<LinkyiViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private var isActive = "1"
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentLinkBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    private fun simpanLink() {
        val link = binding.edEditLink.text.toString().trim()
        val name = binding.edEditName.text.toString().trim()

        // Validasi input kosong
        if (link.isEmpty() || name.isEmpty()) {
            Toast.makeText(requireContext(), "Nama dan link harus diisi.", Toast.LENGTH_SHORT).show()
            return
        }

        // Validasi format URL harus diawali dengan https://
        if (!link.startsWith("https://")) {
            Toast.makeText(requireContext(), "Link harus diawali dengan \"https://\"", Toast.LENGTH_SHORT).show()
            return
        }

        // Optional: validasi apakah benar-benar URL (Regex sederhana)
        val urlPattern = Regex("^https://[\\w.-]+(?:\\.[\\w\\.-]+)+[/#?]?.*\$")
        if (!urlPattern.matches(link)) {
            Toast.makeText(requireContext(), "Format link tidak valid.", Toast.LENGTH_SHORT).show()
            return
        }

        // Jika lolos semua validasi, lanjutkan simpan
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.addLink(link, name, isActive)
            viewModel.linkyiResponse.observe(viewLifecycleOwner) {
                if (it?.success == true) {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    binding.edEditLink.setText("")
                    binding.edEditName.setText("")
                    binding.featureSwitch.isChecked = true
                }
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLinkBinding.bind(view)
        with(binding) {
            featureSwitch.setOnCheckedChangeListener { _, isChecked ->
                isActive = if (isChecked) "1" else "0"
            }

            btnSimpan.setOnClickListener {
                simpanLink()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}