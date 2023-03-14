package com.example.paymentapp.peresentation.splash

import android.animation.Animator
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.paymentapp.R
import com.example.paymentapp.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash) {

    private lateinit var binding: FragmentSplashBinding
    private val viewModel: SplashViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSplashBinding.bind(view)
        lifecycleScope.launch {
            viewModel.updateList()
            viewModel.isReady = true
        }
        binding.animationView.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator) {}
            override fun onAnimationCancel(p0: Animator) {}
            override fun onAnimationRepeat(p0: Animator) {}
            override fun onAnimationEnd(p0: Animator) {
                lifecycleScope.launch {
                    while (!viewModel.isReady) {
                        delay(20)
                    }
                    if (!viewModel.isUsePassword()) {
                        findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToHomeFragment())
                    } else {
                        findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToPasswordFragment())
                    }
                }
            }
        })
    }

}


