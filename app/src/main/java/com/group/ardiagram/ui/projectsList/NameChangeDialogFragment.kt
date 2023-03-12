package com.group.ardiagram.ui.projectsList

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.group.ardiagram.data.Project
import com.group.ardiagram.databinding.NameChangeDialogBinding

class NameChangeDialogFragment : DialogFragment() {

    companion object {
        const val PROJECT_ITEM = "projectItem"
        @JvmStatic
        fun newInstance(project: Project) =
            NameChangeDialogFragment().apply {
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

    private var _binding: NameChangeDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = NameChangeDialogBinding.inflate(inflater, container, false)
        val view: View = binding.root

        binding.projectNameEditText.setText(project?.name.orEmpty())

        binding.buttonCancel.setOnClickListener {
            dialog?.cancel()
        }

        binding.buttonApplyChange.setOnClickListener {
            val newName = binding.projectNameEditText.text.toString()
            viewModel.changeProjectName(project, newName)
            dialog?.cancel()
        }

        return view
    }
}
