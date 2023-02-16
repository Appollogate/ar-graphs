package com.group.ardiagram.ui.projectsList

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.group.ardiagram.databinding.ProjectCreationDialogBinding

class ProjectCreationDialogFragment : DialogFragment() {

    private val viewModel: ProjectsListViewModel by activityViewModels()

    private var _launcher: ActivityResultLauncher<Intent>? = null

    private var _binding: ProjectCreationDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        // Set file path to textView
        _launcher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == RESULT_OK) {
                    val text = result.data?.data
                    binding.filePathEditText.setText(text.toString())
                }
            }
        _binding = ProjectCreationDialogBinding.inflate(inflater, container, false)
        val view: View = binding.root

        binding.buttonCancel.setOnClickListener {
            dialog?.cancel()
        }
        binding.buttonConfirm.setOnClickListener {
            onClickConfirm()
        }
        binding.buttonFilePath.setOnClickListener {
            onClickFilePath()
        }

        return view
    }

    private fun onClickConfirm() {
        if (isFieldsCorrect()) {
            val projectName: String = binding.projectNameEditText.text.toString()
            val filePath: String = binding.filePathEditText.text.toString()
            viewModel.addNewProject(projectName, filePath)
            dialog?.cancel()
        }
    }

    private fun isFieldsCorrect(): Boolean {
        return binding.projectNameEditText.text.isNotEmpty() &&
                binding.filePathEditText.text.isNotEmpty() &&
                // Check if project name is unique
                viewModel.projectList.value?.any {
                    it.name == binding.projectNameEditText.text.toString()
                } == false
    }

    private fun onClickFilePath() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/*"
        }
        _launcher?.launch(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}