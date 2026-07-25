import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable

@Composable
fun Test() {
    LazyColumn {
        val a = listOf(1, 2, 3)
        items(a.size) { }
    }
}
