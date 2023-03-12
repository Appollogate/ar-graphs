package com.group.ardiagram.ui.home

import android.app.AlertDialog
import android.os.Bundle
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.group.ardiagram.R
import com.group.ardiagram.data.Project
import com.group.ardiagram.databinding.FragmentHomeBinding
import com.group.ardiagram.ui.projectsList.ProjectsListViewModel


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        homeViewModel.projectList.observe(viewLifecycleOwner) { projects ->
            binding.startSessionButton.setOnClickListener {
                showListOfProjects(projects)
            }
        }

        return root
    }

    private fun showListOfProjects(projects: List<Project>) {
        val builder = AlertDialog.Builder(context)
        val projectsNames = projects.map { it.name }

        if (projects.isEmpty()) {
            builder.setTitle("You don't have any project yet")
            builder.setPositiveButton("Ok", null)
            val dialog = builder.create()
            dialog.show()
            return
        } else {
            builder.setTitle("Choose a project")
        }

        builder.setItems(projectsNames.toTypedArray()) { dialog, index ->
            val bundle = bundleOf("project" to projects[index])
            findNavController().navigate(R.id.action_navigation_home_to_ARFragment, bundle)
        }

        val dialog = builder.create()
        dialog.show()
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menu.add(Menu.NONE, 123, Menu.NONE, "Create")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}