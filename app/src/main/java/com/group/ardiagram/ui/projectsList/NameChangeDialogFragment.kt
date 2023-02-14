package com.group.ardiagram.ui.projectsList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.group.ardiagram.data.Project
import com.group.ardiagram.databinding.NameChangeDialogBinding

class NameChangeDialogFragment(project: Project) : DialogFragment() {

    private var _project = project

    private val viewModel: ProjectsListViewModel by activityViewModels()

    private var _binding: NameChangeDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = NameChangeDialogBinding.inflate(inflater, container, false)
        val view: View = binding.root

        binding.projectNameEditText.setText(_project.name)

        binding.buttonCancel.setOnClickListener {
            dialog?.cancel()
        }

        binding.buttonApplyChange.setOnClickListener {
            val index: Int = viewModel.projectList.indexOf(_project)

            _project.name = binding.projectNameEditText.text.toString()
            viewModel.projectList[index] = _project
            viewModel.updateProjectListUI()

            dialog?.cancel()
        }

        return view
    }


}
