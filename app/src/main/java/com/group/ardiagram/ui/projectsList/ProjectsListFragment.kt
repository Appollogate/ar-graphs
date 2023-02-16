package com.group.ardiagram.ui.projectsList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.group.ardiagram.R
import com.group.ardiagram.databinding.FragmentProjectsListBinding

class ProjectsListFragment : Fragment() {

    private val viewModel: ProjectsListViewModel by activityViewModels()

    private var _binding: FragmentProjectsListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProjectsListBinding.inflate(inflater, container, false)
        val root: View = binding.root

        showProjects()

        return root
    }

    private fun showProjects() {
        val recyclerView: RecyclerView = binding.projectList

        viewModel.projectList.observe(viewLifecycleOwner) {

            binding.textNotifications.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
            (recyclerView.adapter as ProjectsAdapter).updateList(it)
        }
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ProjectsAdapter(viewModel.projectList.value ?: arrayListOf())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.top_add_button, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {

                ProjectCreationDialogFragment().show(
                    parentFragmentManager,
                    "ProjectCreationDialogFragment"
                )
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}