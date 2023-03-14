package com.group.ardiagram.ui.projectsList

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.group.ardiagram.data.Project
import com.group.ardiagram.databinding.ProjectParamsChangeDialogBinding

class ProjectParamsChangeDialogFragment : DialogFragment() {

    companion object {
        const val PROJECT_ITEM = "projectItem"

        @JvmStatic
        fun newInstance(project: Project) =
            ProjectParamsChangeDialogFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(PROJECT_ITEM, project)
                }
            }
    }

    private var project: Project? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        project = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getSerializable(PROJECT_ITEM, Project::class.java)
        } else {
            arguments?.getSerializable(PROJECT_ITEM) as Project
        }
    }

    private val viewModel: ProjectsListViewModel by activityViewModels()

    private var _binding: ProjectParamsChangeDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = ProjectParamsChangeDialogBinding.inflate(inflater, container, false)
        val view: View = binding.root

        binding.projectNameEditText.setText(project?.name.orEmpty())

        val inputText = when {
            project?.function != null -> project?.function
            project?.points != null && project?.pathToTableFile?.isNotEmpty() ?: false -> "Points from ${
                project?.pathToTableFile?.split(
                    "/"
                )?.last()
            }"

            else -> "Manual points"
        }

        binding.inputText.text = inputText

        binding.buttonCancel.setOnClickListener {
            dialog?.cancel()
        }

       bindButtons()

        return view
    }


    private fun bindButtons() {
        binding.buttonApplyChange.setOnClickListener {
            val newName = binding.projectNameEditText.text.toString()
            viewModel.changeProjectName(project, newName)
            dialog?.cancel()
        }

        binding.buttonChangeInput.setOnClickListener {
            val importChooseDialogFragment = project?.let { it1 ->
                ImportChooseDialogFragment.newInstance(
                    it1
                )
            }
            importChooseDialogFragment?.show(parentFragmentManager, "importChoose")

            dialog?.cancel()
        }
    }
}