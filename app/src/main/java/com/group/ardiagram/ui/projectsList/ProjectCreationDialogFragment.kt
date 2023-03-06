package com.group.ardiagram.ui.projectsList

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import com.group.ardiagram.data.Project
import com.group.ardiagram.databinding.ProjectCreationDialogBinding

class ProjectCreationDialogFragment : DialogFragment() {

    private val viewModel: ProjectsListViewModel by activityViewModels()

    private var _launcher: ActivityResultLauncher<Intent>? = null

    private var _binding: ProjectCreationDialogBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = ProjectCreationDialogBinding.inflate(inflater, container, false)
        val view: View = binding.root

        binding.buttonCancel.setOnClickListener {
            dialog?.cancel()
        }
        binding.buttonNext.setOnClickListener {
            onClickConfirm()
        }

        return view
    }

    private fun onClickConfirm() {
        if (isFieldsCorrect()) {
            val projectName: String = binding.projectNameEditText.text.toString()

//            viewModel.addNewProject(projectName, fileUri)
//            val projectName: String = binding.projectNameEditText.text.toString()
//            val filePath: String = binding.filePathEditText.text.toString()
//            viewModel.addNewProject(projectName, filePath)
//            dialog?.cancel()
            val project = Project(binding.projectNameEditText.text.toString())
            val activity = context as FragmentActivity
            val importChooseDialogFragment = ImportChooseDialogFragment.newInstance(project)
            val manager = activity.supportFragmentManager
            importChooseDialogFragment.show(manager, "importChoose")

            dialog?.cancel()
        }
    }

    private fun isFieldsCorrect(): Boolean {
        return binding.projectNameEditText.text.isNotEmpty() &&
                // Check if project name is unique
                viewModel.projectList.value?.any {
                    it.name == binding.projectNameEditText.text.toString()
                } == false
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}