package com.joshgm3z.common

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview

@Preview(name = "Dark Mode", uiMode = UI_MODE_NIGHT_YES)
@Preview(name = "Light Mode", showBackground = true, uiMode = UI_MODE_NIGHT_NO)
annotation class ThemePreviews

@Preview(name = "Dark Mode", uiMode = UI_MODE_NIGHT_YES)
annotation class DarkPreview

@Preview(name = "Dark Mode w/ SystemUI", uiMode = UI_MODE_NIGHT_YES, showSystemUi = true)
annotation class DarkPreviewWithSystemUi