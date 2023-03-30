package com.group.ardiagram.ui.projectsList

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.provider.Settings.Global
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.group.ardiagram.R
import com.group.ardiagram.data.Project
import com.group.ardiagram.databinding.FragmentProjectsListBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.FileOutputStream
import java.io.IOException


class ProjectsListFragment : Fragment() {

    private val viewModel: ProjectsListViewModel by activityViewModels()

    private var _binding: FragmentProjectsListBinding? = null
    private val binding get() = _binding!!

    private var importProjectLauncher: ActivityResultLauncher<Intent>? = null
    private var saveProjectLauncher: ActivityResultLauncher<Intent>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        importProjectLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val fileUri: Uri? = result.data?.data

                fileUri ?: return@registerForActivityResult
                try {
                    val inputStream = activity?.contentResolver?.openInputStream(fileUri)

                    val bufferedReader: BufferedReader? = inputStream?.bufferedReader()

                    val inputString = bufferedReader.use { it?.readText() }

                    inputStream?.close()

                    val project = Gson().fromJson(inputString, Project::class.java)

                    if (viewModel.projectList.value?.find { it == project } != null) {
                        Toast.makeText(
                            context,
                            "Project already exists!",
                            Toast.LENGTH_LONG
                        ).show()
                        return@registerForActivityResult
                    }
                    viewModel.saveProject(project)

                } catch (e: Exception) {
                    Toast.makeText(context, "Cannot read file", Toast.LENGTH_SHORT).show()
                }
            }
        }

        saveProjectLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                lifecycleScope.launch(Dispatchers.IO) {
                    try {
                        val uri = result.data?.data ?: return@launch
                        val fileOutputStream = activity?.contentResolver?.openOutputStream(uri)
                        val jsonProject = viewModel.jsonProject?.toByteArray()
                        fileOutputStream?.write(jsonProject)
                        fileOutputStream?.close()
                    } catch (e: IOException) {
                        Log.e("ERROR", e.message.toString())
                    }
                }
            }
        }

        _binding = FragmentProjectsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setUpProjectList() {
        val recyclerView: RecyclerView = binding.projectList
        val projectsAdapter = ProjectsAdapter(
            onItemClicked = {
                //TODO action
            },
            onEditClicked = { project ->
                val projectParamsChangeDialogFragment =
                    ProjectParamsChangeDialogFragment.newInstance(project)
                projectParamsChangeDialogFragment.show(parentFragmentManager, "nameChange")
            },
            onDeleteClicked = { project ->
                viewModel.deleteProject(project)
            },
            onShareClicked = { project ->
                val projectAsJson = Gson().toJson(project)

                viewModel.jsonProject = projectAsJson
                val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "application/json"
                    putExtra(Intent.EXTRA_TITLE, project.name)
                }
                saveProjectLauncher?.launch(intent)
            }
        )

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = projectsAdapter
        }

        viewModel.projectList.observe(viewLifecycleOwner) {

            projectsAdapter.submitList(it)
            binding.textNotifications.visibility =
                if (it.isEmpty()) View.VISIBLE else View.GONE
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
                return when (menuItem.itemId) {
                    R.id.create_new -> {
                        ProjectCreationDialogFragment().show(
                            parentFragmentManager,
                            "ProjectCreationDialogFragment"
                        )
                        true
                    }

                    R.id.open -> {
                        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                            addCategory(Intent.CATEGORY_OPENABLE)
                            type = "application/json"
                        }
                        importProjectLauncher?.launch(intent)
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        setUpProjectList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}