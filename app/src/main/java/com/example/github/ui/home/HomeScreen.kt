package com.example.github.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapLayoutInfoProvider
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.github.data.model.ApiStatus.ERROR
import com.example.github.data.model.ApiStatus.IDLE
import com.example.github.data.model.ApiStatus.LOADING
import com.example.github.data.model.ApiStatus.SUCCESS
import com.example.github.data.model.CommitModelLocal
import com.example.github.data.model.RepoModelLocal
import com.example.github.ui.vm.HomeViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel


@Composable
fun HomeScreen(vm: HomeViewModel = getViewModel()) {

    val firstFiveRepos = vm.firstFiveRepos.collectAsState()
    val bottomList = vm.bottomList.collectAsState()

    var showBottomSheet by remember { mutableStateOf(false) }

    var selectedRepo by remember {
        mutableStateOf(RepoModelLocal())
    }
    var selectedRepoCommits by remember {
        mutableStateOf(listOf(CommitModelLocal()))
    }

    Surface {

        //Detail BottomSheet
        if (showBottomSheet) {
            BottomSheet(
                selectedRepo = selectedRepo,
                selectedRepoCommits = selectedRepoCommits,
            ) {
                showBottomSheet = it
            }
        }

        //Main Layout
        Column {
            Spacer(modifier = Modifier.padding(16.dp))
            HorizontalCarousal(repoModelLocalList = firstFiveRepos.value)
            Spacer(modifier = Modifier.padding(16.dp))
            VerticalList(vm = vm, list = bottomList.value) { repo, commits ->
                selectedRepo = repo
                selectedRepoCommits = commits
                showBottomSheet = true
            }
            Spacer(modifier = Modifier.padding(16.dp))
        }

    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    selectedRepo: RepoModelLocal,
    selectedRepoCommits: List<CommitModelLocal>,
    callback: (show: Boolean) -> Unit = {}
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        modifier = Modifier
            .navigationBarsPadding()
            .wrapContentHeight(),
        onDismissRequest = {

        },
        sheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.Expanded,
            confirmValueChange = { it != SheetValue.PartiallyExpanded },
            skipHiddenState = false
        )
    ) {

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopEnd) {
            Column {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = selectedRepo.name.toString() + " Repository",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Image(
                        modifier = Modifier
                            .padding(16.dp)
                            .clickable {
                                scope
                                    .launch { sheetState.hide() }
                                    .invokeOnCompletion {
                                        if (!sheetState.isVisible) {
                                            callback(false)
                                        }
                                    }
                            },
                        painter = painterResource(id = android.R.drawable.ic_menu_close_clear_cancel),
                        contentDescription = ""
                    )
                }


                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    selectedRepoCommits.forEach {
                        Text(text = buildAnnotatedString {
                            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("Commit: ") }
                            append(it.message?.trim())
                        })
                    }
                }

                Spacer(modifier = Modifier.padding(16.dp))
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HorizontalCarousal(
    modifier: Modifier = Modifier,
    repoModelLocalList: List<RepoModelLocal>
) {
    val listState = rememberLazyListState(
        Int.MAX_VALUE / 5
    )
    val snappingLayout = remember(listState) { SnapLayoutInfoProvider(listState) }
    val flingBehavior = rememberSnapFlingBehavior(snappingLayout)

    repoModelLocalList.takeIf { it.isNotEmpty() }?.let {
        LazyRow(
            state = listState,
            modifier = modifier.fillMaxWidth(),
            flingBehavior = flingBehavior

        ) {
            items(
                count = Int.MAX_VALUE,
                itemContent = {
                    val index = it % 5
                    Card(
                        modifier
                            .fillParentMaxWidth(0.9f)
                            .padding(horizontal = 8.dp)
                    ) {
                        CarousalItem(repoModelLocalList[index], listState, index)
                    }
                }
            )
        }
    }
}

@Composable
private fun CarousalItem(
    repoModelLocal: RepoModelLocal,
    listState: LazyListState,
    index: Int
) {

    val isCentered = remember { mutableStateOf(false) }

    val itemInfo =
        remember { listState.layoutInfo.visibleItemsInfo.firstOrNull { it.index == index } }

    itemInfo?.let {
        val center = listState.layoutInfo.viewportEndOffset / 2
        val childCenter = it.offset + it.size / 2
        val target = childCenter - center
        if (index == target) isCentered.value = true
    }

    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(100.dp)
    ) {
        Text(
            text = "${repoModelLocal.name}",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
    }

}

@Composable
private fun VerticalList(
    vm: HomeViewModel,
    list: List<RepoModelLocal>,
    onItemClickListener: (repo: RepoModelLocal, commits: List<CommitModelLocal>) -> Unit = { _, _ -> }
) {
    val listState = rememberLazyListState()
    LazyColumn(state = listState) {
        items(
            count = list.size,
            itemContent = { index ->
                val repo = list[index]
                VerticalListItem(repo, vm, onItemClickListener)
            }
        )
    }
}

@Composable
private fun VerticalListItem(
    repo: RepoModelLocal,
    vm: HomeViewModel,
    itemClickListener: (repo: RepoModelLocal, commits: List<CommitModelLocal>) -> Unit = { _, _ -> }
) {

    var commits by remember(repo) {
        mutableStateOf(listOf<CommitModelLocal>())
    }

    var isLoading by remember {
        mutableStateOf(false)
    }

    var error by remember {
        mutableStateOf("")
    }

    LaunchedEffect(Unit) {
        vm.getCommitsForRepo(repo.name ?: "", 3).collectLatest {
            when (it.status) {
                IDLE -> {}

                SUCCESS -> {
                    isLoading = false
                    it.data?.let { list -> if(list.isNotEmpty()) commits = list }
                }

                ERROR -> {
                    it.message?.let { error = it }
                    isLoading = false
                }

                LOADING -> isLoading = true
            }

        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .defaultMinSize(100.dp)
            .clickable {
                itemClickListener(repo, commits)
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = "${repo.name}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.padding(top = 8.dp))

                commits.forEachIndexed { _, commitModelItem ->

                    Text(text = buildAnnotatedString {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("Commit: ") }
                        append(commitModelItem.message?.trim())
                    })
                }

                when {
                    isLoading ->
                        CircularProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .wrapContentWidth(Alignment.CenterHorizontally)
                        )

                    commits.isEmpty() && !isLoading ->
                        Text(text = "No Commits for Repository.")

                            !isLoading && error != "" ->
                        Text(text = "Error fetching from network : $error")
                }
            }
        }
    }

}

