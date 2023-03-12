package com.group.ardiagram.ui.projectsList

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.group.ardiagram.data.Project
import com.group.ardiagram.databinding.ImportTypeDialogBinding

class ImportChooseDialogFragment : DialogFragment() {

    private var _binding: ImportTypeDialogBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val PROJECT_ITEM = "projectItem"
        @JvmStatic
        fun newInstance(project: Project) =
            ImportChooseDialogFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(PROJECT_ITEM, project)
                }
            }
    }

    private var project: Project? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        project = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getSerializable(PROJECT_ITEM, Project::class.java)
        } else {
            arguments?.getSerializable(PROJECT_ITEM) as Project
        }

        _binding = ImportTypeDialogBinding.inflate(inflater, container, false)
        val view: View = binding.root

        binding.buttonImportFile.setOnClickListener {
            val activity = context as FragmentActivity
            val importChooseDialogFragment = FileImportDialogFragment.newInstance(project!!)
            val manager = activity.supportFragmentManager
            importChooseDialogFragment.show(manager, "fileImport")

            dialog?.cancel()
        }

        binding.buttonAddManually.setOnClickListener {
            val activity = context as FragmentActivity
            val addManuallyDialogFragment = AddManuallyDialogFragment.newInstance(project!!)
            val manager = activity.supportFragmentManager
            addManuallyDialogFragment.show(manager, "addManually")

            dialog?.cancel()
        }

        binding.buttonFunction.setOnClickListener {
            val activity = context as FragmentActivity
            val addManuallyDialogFragment = FunctionDialogFragment.newInstance(project!!)
            val manager = activity.supportFragmentManager
            addManuallyDialogFragment.show(manager, "addManually")

            dialog?.cancel()
        }

        binding.buttonCancel.setOnClickListener {
            dialog?.cancel()
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}