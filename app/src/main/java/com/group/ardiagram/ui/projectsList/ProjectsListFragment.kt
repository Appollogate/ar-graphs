package com.group.ardiagram.ui.projectsList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import com.group.ardiagram.R
import com.group.ardiagram.databinding.FragmentProjectsListBinding

class ProjectsListFragment : Fragment() {

    private var _binding: FragmentProjectsListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var projectsListViewModel: ProjectsListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        projectsListViewModel =
            ViewModelProvider(this)[ProjectsListViewModel::class.java]

        _binding = FragmentProjectsListBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textNotifications
        projectsListViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
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
                projectsListViewModel.createNewProject()
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}