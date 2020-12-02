import androidx.compose.desktop.Window
import androidx.compose.desktop.WindowEvents
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.gesture.pressIndicatorGestureFilter
import androidx.compose.ui.gesture.rawPressStartGestureFilter
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.text.Typeface
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.font
import androidx.compose.ui.text.font.fontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.revwalk.RevCommit
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import java.io.File
import androidx.compose.foundation.layout.Box as Box1

class RepositoryInfo(
    val nickname: String = "no name",
    val path: String = "") {
}

class CommitListItemViewModel(val commit: RevCommit) {
    val shortMessage get() = commit.shortMessage
    val commitTime get() = commit.commitTime
    val author get() = "${commit.authorIdent.name} <${commit.authorIdent.emailAddress}>"
    val hash get() = commit.id.name.substring(0, 8)
}

class RepositoryManager(val repoInfo: RepositoryInfo) {
    val file = FileRepositoryBuilder.create(File(repoInfo.path))
    val git = Git(file)

    fun loadLogs(): List<CommitListItemViewModel> {
        val logs = git.log().call()
        return logs.map { CommitListItemViewModel(it) }
//        val refMaster = repo.findRef("master")
//
//        val walk = RevWalk(repo)
//        val commit = walk.parseCommit(refMaster.objectId)
//        val message = commit.fullMessage
    }
}

fun main() = Window(
    title = "Compose for Desktop",
    size = IntSize(500, 500),
) {
    val (repo, setRepo) = remember {
        val repoInfo = RepositoryInfo("サンプル", "D:\\SampleRepo\\.git")
        val repo = RepositoryManager(repoInfo)
        mutableStateOf(repo)
    }
    val (logs, setLogs) = remember {
        mutableStateOf(repo.loadLogs().take(100).toList())
    }
    val (selectedCommit, setSelectedCommit) = remember { mutableStateOf(logs.firstOrNull()) }
    val stateVertical = rememberScrollState(0f)

    MaterialTheme {
        Column {
            Box1(Modifier
                .background(Color(0xcccccccc))
                .padding(vertical = 5.dp)
                .fillMaxWidth()
            ) {
                Row {
                    Button(
                        onClick = {},
                        Modifier.padding(horizontal = 5.dp),
                    ) {
                        Text("🆕クローン")
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Button(
                        onClick = {},
                        Modifier.padding(horizontal = 2.5.dp),
                    ) {
                        Text("💾コミット")
                    }
                    Button(
                        onClick = {},
                        Modifier.padding(horizontal = 2.5.dp),
                    ) {
                        Text("🔽フェッチ")
                    }
                    Button(
                        onClick = {},
                        Modifier.padding(horizontal = 2.5.dp),
                    ) {
                        Text("🔼プッシュ")
                    }
                    Button(
                        onClick = {},
                        Modifier.padding(horizontal = 2.5.dp),
                    ) {
                        Text("❯ターミナル")
                    }
                }
                Button(
                    onClick = {},
                    Modifier
                        .align(Alignment.CenterEnd)
                        .padding(horizontal = 5.dp),
                ) {
                    Text("⚙設定")
                }
            }
            Box1(modifier = Modifier.fillMaxSize().padding(10.dp)) {
                ScrollableColumn(
                    Modifier
                        .fillMaxSize()
                        .padding(end = 12.dp, bottom = 12.dp),
                    scrollState = stateVertical,
                ) {
                    Column {
                        logs.forEachIndexed { i, commit ->
                            val isSelected = selectedCommit == commit
                            val (isHover, setIsHover) = remember { mutableStateOf(false) }
                            var bgColor =
                                if (isSelected) Color(0x88ff88ff)
                                else if (i % 2 == 0) Color(0xffffffff)
                                else Color(0xeeeeeeee)
                            if (isHover) bgColor = bgColor.run { Color(red * 0.9f, green * 0.9f, blue * 0.9f, alpha) }
                            Row(Modifier
                                .background(bgColor)
                                .pressIndicatorGestureFilter(
                                    onStart = { setSelectedCommit(commit) },
                                )
//                                .pointerMoveFilter(
//                                    onEnter = { setIsHover(true); true },
//                                    onExit = { setIsHover(false); true },
//                                )
                            ) {
                                val maxLines = 1
                                val overflow = TextOverflow.Ellipsis
                                Text(commit.shortMessage, maxLines = maxLines, overflow = overflow, modifier = Modifier.weight(1f))
                                Text(commit.commitTime.toString(), maxLines = maxLines, overflow = overflow, modifier = Modifier.width(100.dp))
                                Text(commit.author, maxLines = maxLines, overflow = overflow, modifier = Modifier.width(120.dp))
                                Text(commit.hash, maxLines = maxLines, overflow = overflow, modifier = Modifier.width(80.dp))
                            }
                        }
                    }
                }
                VerticalScrollbar(
                    modifier = Modifier.fillMaxHeight().align(Alignment.TopEnd),
                    adapter = rememberScrollbarAdapter(stateVertical)
                )
//            HorizontalScrollbar(
//                modifier = Modifier.fillMaxWidth().align(Alignment.BottomStart),
//                adapter = rememberScrollbarAdapter(stateHorizontal)
//            )
            }
        }
    }
}

