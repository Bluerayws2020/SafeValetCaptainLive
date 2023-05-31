package com.example.safevaletcaptain.fragments

import android.os.Bundle
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.safevaletcaptain.databinding.PrograssdiloagBinding

class ProgressDialogFragment : DialogFragment() {

    init {
        d("ayham","ProgressDialogFragment")
    }

    private var binding: PrograssdiloagBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PrograssdiloagBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}