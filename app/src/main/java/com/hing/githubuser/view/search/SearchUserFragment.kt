package com.hing.githubuser.view.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hing.githubuser.R
import com.hing.githubuser.databinding.FragmentSearchUserListBinding
import com.hing.githubuser.view.UiState
import com.hing.githubuser.view.extensions.showToast
import com.hing.githubuser.view.extensions.visible
import com.hing.githubuser.view.listeners.OnLoadMoreListener
import dagger.hilt.android.AndroidEntryPoint

/**
 * A fragment representing a list of Users with Search.
 */
@AndroidEntryPoint
class SearchUserFragment : Fragment() {

    private lateinit var binding: FragmentSearchUserListBinding
    private lateinit var userListAdapter: UserListAdapter
    private val viewModel by viewModels<SearchUserViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search_user_list, container, false)
        binding = FragmentSearchUserListBinding.bind(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initSearchView()
        subscribeUi()

        viewModel.getUsers()
    }

    override fun onStart() {
        super.onStart()
        addLoadMoreListener()
    }

    override fun onStop() {
        super.onStop()
        removeLoadMoreListener()
    }

    private fun subscribeUi() {
        viewModel.liveData.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Failed -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    binding.progressBar.visible(false)

                    userListAdapter.updateData(mutableListOf())
                    val messagesRes = if (it.error is UserListState.FailedState.EmptyData) {
                        if (userListAdapter.isLoadMore) {
                            R.string.no_data
                        } else {
                            R.string.no_more_data
                        }
                    } else {
                        R.string.load_data_failed
                    }
                    binding.root.context.showToast(messagesRes)
                }
                is UiState.Loading -> {
                    binding.progressBar.visible(true)
                    userListAdapter.updateData(mutableListOf())
                }
                is UiState.Success -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    binding.progressBar.visible(false)
                    userListAdapter.updateData(it.data.users)
                }
            }
        }
        viewModel.isLoadMore.observe(viewLifecycleOwner) {
            userListAdapter.isLoadMore = it
        }
    }

    private fun initSearchView() {
        binding.edtSearch.doAfterTextChanged {
            val text = it?.toString()
            if (text.isNullOrBlank()) {
                viewModel.getUsers()
            } else {
                viewModel.searchUser(text)
            }
        }
    }

    private fun initRecyclerView() {
        userListAdapter = UserListAdapter { pos ->
        }
        binding.recyclerView.apply {
            adapter = userListAdapter
            addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getUsers()
        }
    }

    private fun addLoadMoreListener() {
        binding.recyclerView.addOnScrollListener(loadMoreListener)
    }

    private fun removeLoadMoreListener() {
        binding.recyclerView.removeOnScrollListener(loadMoreListener)
    }

    private val loadMoreListener: OnLoadMoreListener by lazy {
        object : OnLoadMoreListener(
            VISIBLE_THRESHOLD,
            binding.recyclerView.layoutManager as LinearLayoutManager
        ) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                if (viewModel.liveData.value is UiState.Success) {
                    val searchText = binding.edtSearch.text.toString()
                    if (searchText.isBlank()) {
                        val users = (viewModel.liveData.value as UiState.Success).data.users
                        viewModel.getUsers(users.last().id)
                    } else {
                        viewModel.searchUser(searchText, page)
                    }
                }
            }
        }
    }

    companion object {
        private const val VISIBLE_THRESHOLD = 10
    }
}
