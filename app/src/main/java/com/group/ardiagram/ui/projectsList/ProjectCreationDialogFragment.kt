package com.group.ardiagram.ui.projectsList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import com.group.ardiagram.data.Project
import com.group.ardiagram.databinding.ProjectCreationDialogBinding

class ProjectCreationDialogFragment : DialogFragment() {

    private val viewModel: ProjectsListViewModel by activityViewModels()
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
        val projectName = binding.projectNameEditText.text.toString()

        viewModel.checkNameFieldIsCorrect(projectName).also { fieldsAreCorrect ->
            if (fieldsAreCorrect) {
                val project = Project(name = projectName)

                val importChooseDialogFragment = ImportChooseDialogFragment.newInstance(project)
                importChooseDialogFragment.show(parentFragmentManager, "importChoose")

                dialog?.cancel()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}