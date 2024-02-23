package com.joshgm3z.ping.ui.frx

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.joshgm3z.ping.MainActivity
import com.joshgm3z.ping.ui.theme.PingTheme
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignInActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val signInViewModel by viewModel<SignInViewModel>()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                signInViewModel.uiState.collect {
                    setContent {
                        PingTheme {
                            Surface(
                                modifier = Modifier.fillMaxSize(),
                                color = MaterialTheme.colorScheme.background
                            ) {
                                when (it) {
                                    is SignInUiState.SignIn -> SignInContainer {
                                        signInViewModel.onSignInClick(it)
                                    }

                                    is SignInUiState.SignUp -> {}
                                    is SignInUiState.Loading -> LoadingContainer(it.message)
                                    is SignInUiState.GoToHome -> launchHomeActivity()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun launchHomeActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}
