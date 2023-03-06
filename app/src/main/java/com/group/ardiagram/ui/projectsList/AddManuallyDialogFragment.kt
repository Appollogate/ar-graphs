package com.group.ardiagram.ui.projectsList

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.group.ardiagram.R
import com.group.ardiagram.data.Project
import com.group.ardiagram.databinding.AddManuallyDialogBinding


class AddManuallyDialogFragment : DialogFragment() {

    private val viewModel: ProjectsListViewModel by activityViewModels()

    private var _binding: AddManuallyDialogBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val PROJECT_ITEM = "projectItem"
        @JvmStatic
        fun newInstance(project: Project) =
            AddManuallyDialogFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(PROJECT_ITEM, project)
                }
            }
    }

    private var project: Project? = null

    @SuppressLint("UseGetLayoutInflater")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        project = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getSerializable(ImportChooseDialogFragment.PROJECT_ITEM, Project::class.java)
        } else {
            arguments?.getSerializable(ImportChooseDialogFragment.PROJECT_ITEM) as Project
        }

        _binding = AddManuallyDialogBinding.inflate(inflater, container, false)
        val view: View = binding.root

        binding.buttonAddRow.setOnClickListener {
            val row = LayoutInflater.from(context)
                .inflate(R.layout.table_row_with_border, container, false)
            binding.tableRows.addView(row)
        }

        binding.buttonCancel.setOnClickListener {
            dialog?.cancel()
        }

        binding.buttonConfirm.setOnClickListener {

            val rowCount = binding.tableRows.childCount - 1
            for (i in 1..rowCount){
                val row = binding.tableRows.getChildAt(i) as TableRow
                val xStr = (row.getChildAt(0) as TextView).text.toString()
                val yStr = (row.getChildAt(1) as TextView).text.toString()
                val zStr = (row.getChildAt(2) as TextView).text.toString()

                if (xStr.isEmpty() && yStr.isEmpty() && zStr.isEmpty()) continue

                val x = if (xStr.isEmpty()) 0.0f else xStr.toFloat()
                val y = if (yStr.isEmpty()) 0.0f else yStr.toFloat()
                val z = if (zStr.isEmpty()) 0.0f else zStr.toFloat()

                viewModel.addPoint(x, y, z)
            }

            project?.let { newProject -> viewModel.addNewProject(newProject) }

            dialog?.cancel()
        }

        return view
    }
}