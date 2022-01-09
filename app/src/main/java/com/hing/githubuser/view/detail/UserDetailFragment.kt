package com.hing.githubuser.view.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.hing.githubuser.R
import com.hing.githubuser.databinding.FragmentUserDetailBinding
import com.hing.githubuser.domain.userdetail.UserDetailData
import com.hing.githubuser.view.UiState
import com.hing.githubuser.view.extensions.loadImage
import com.hing.githubuser.view.extensions.showToast
import com.hing.githubuser.view.extensions.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserDetailFragment : Fragment() {

    private lateinit var binding: FragmentUserDetailBinding
    private val viewModel by viewModels<UserDetailViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_detail, container, false)
        binding = FragmentUserDetailBinding.bind(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        subscribeUi()

        if (arguments?.containsKey(USER_NAME) == true) {
            arguments?.let {
                viewModel.getUserDetail(it.getString(USER_NAME) ?: "")
            }
        } else {
            handleLoadDataFailed()
        }
    }

    private fun subscribeUi() {
        viewModel.liveData.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Failed -> {
                    binding.progressBarLayout.visible(false)
                    handleLoadDataFailed()
                }
                is UiState.Loading -> {
                    binding.progressBarLayout.visible(true)
                }
                is UiState.Success -> {
                    binding.progressBarLayout.visible(false)
                    bindUserDetailData(it.data.userDetail)
                }
            }
        }
    }

    private fun bindUserDetailData(userDetail: UserDetailData) {
        binding.apply {
            imgvAvatar.loadImage(userDetail.avatarUrl)
            tvDisplayName.text = userDetail.displayName ?: userDetail.name
            tvFollowing.text = "${userDetail.following}"
            tvFollowers.text = "${userDetail.followers}"
            tvPublicRepos.text = "${userDetail.publicRepos}"
            tvLoginName.text = userDetail.name
            tvBio.text = userDetail.bio
            tvGithubHtml.text = userDetail.url

            bioRow.visible(userDetail.bio?.isNotEmpty() == true)
        }
    }

    private fun handleLoadDataFailed() {
        binding.root.context.showToast(R.string.load_data_failed)
        activity?.onBackPressed()
    }

    private fun initToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
    }

    companion object {
        const val USER_NAME = "user-name"
    }
}
