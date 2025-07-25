package com.dapoer.rangoe.ui.profile

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.dapoer.rangoe.R
import com.dapoer.rangoe.data.ViewModelFactory
import com.dapoer.rangoe.databinding.FragmentProfileBinding
import com.dapoer.rangoe.ui.aktivasiToko.AktivasiTokoViewModel
import com.dapoer.rangoe.ui.aktivasiToko.UpdateStoreActivity
import com.dapoer.rangoe.ui.listKategori.ListKategoriActivity
import com.dapoer.rangoe.ui.updatePassword.UpdatePasswordActivity
import com.dapoer.rangoe.ui.welcome.WelcomeActivity

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null

    private val viewModels by viewModels<AktivasiTokoViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonLogout.setOnClickListener {
            viewModels.deleteUserToken()
            startActivity(Intent(activity, WelcomeActivity::class.java))
            activity?.finish()
        }

        binding.editToko.setOnClickListener {
            startActivity(Intent(activity, UpdateStoreActivity::class.java))
            activity?.finish()
        }

//        binding.theme.setOnClickListener {
//            startActivity(Intent(activity, TemaActivity::class.java))
//            activity?.finish()
//        }

        binding.updatePassword.setOnClickListener {
            startActivity(Intent(activity, UpdatePasswordActivity::class.java))
        }

        binding.category.setOnClickListener {
            startActivity(Intent(activity, ListKategoriActivity::class.java))
        }

        viewModels.profileResult.observe(viewLifecycleOwner) { data ->
            data?.let {
                binding.tvTokoName.text = it.name.toString()
                binding.tvDesc.text = it.description.toString()
                val baseLink = getString(R.string.generate_base_link)
                val fullLink = "$baseLink${it.link}"
                binding.tvLinks.text = fullLink

                Glide.with(this)
                    .load(it.logo)
                    .into(binding.ivThumbnail)

                // Set click listener untuk membuka link
                binding.tvLinks.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(fullLink))
                    startActivity(intent)
                }

                // Set click listener untuk menyalin link
                binding.btnCopyLink.setOnClickListener {
                    val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("Copied Link", fullLink)
                    clipboard.setPrimaryClip(clip)
                    Toast.makeText(requireContext(), "Tautan disalin ke clipboard", Toast.LENGTH_SHORT).show()
                }
            }
        }
        viewModels.getProfile()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}