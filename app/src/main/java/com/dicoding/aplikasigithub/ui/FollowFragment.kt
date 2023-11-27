package com.dicoding.aplikasigithub.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.aplikasigithub.R
import com.dicoding.aplikasigithub.adapter.UserAdapter
import com.dicoding.aplikasigithub.databinding.FragmentFollowBinding
import com.dicoding.aplikasigithub.model.DetailViewModel
import com.dicoding.aplikasigithub.response.ItemsItem

class FollowFragment : Fragment() {

    private lateinit var binding: FragmentFollowBinding
    private lateinit var detailUserViewModel: DetailViewModel

    companion object {
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = ""
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var position = 0
        var username = arguments?.getString(ARG_USERNAME)

        Log.d("arguments: position", position.toString())
        Log.d("arguments: usermame", username.toString())

        detailUserViewModel = ViewModelProvider(requireActivity(), ViewModelProvider.AndroidViewModelFactory()).get(
            DetailViewModel::class.java)
        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME)
        }

        if (position == 1) {
            showLoadingUser(true)
            username?.let {
                if (it != null) {
                    detailUserViewModel.getFollowers(it)
                }
            }
            detailUserViewModel.followers.observe(viewLifecycleOwner, {
                setDataFollow(it)
                showLoadingUser(false)
            })
        }
        else {
            showLoadingUser(true)
            username.let {
                if (it != null) {
                    detailUserViewModel.getFollowing(it)
                }
            }
            detailUserViewModel.following.observe(viewLifecycleOwner, {
                setDataFollow(it)
                showLoadingUser(false)
            })
        }
    }

    private fun showLoadingUser(isLoadingFollow: Boolean) {
        if (isLoadingFollow) {
            binding.followProgressBar.visibility = View.VISIBLE
        }
        else {
            binding.followProgressBar.visibility = View.GONE
        }
    }

    private fun setDataFollow(listUser: List<ItemsItem>) {
        binding.apply {
            binding.rvFollow.layoutManager = LinearLayoutManager(requireActivity())
            val adapter = UserAdapter(listUser)
            binding.rvFollow.adapter = adapter
        }
    }
}