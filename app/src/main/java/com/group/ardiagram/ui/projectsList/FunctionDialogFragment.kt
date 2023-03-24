package com.group.ardiagram.ui.projectsList

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.group.ardiagram.data.Project
import com.group.ardiagram.databinding.FunctionDialogBinding

class FunctionDialogFragment : DialogFragment() {

    private val viewModel: ProjectsListViewModel by activityViewModels()

    private var _binding: FunctionDialogBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val PROJECT_ITEM = "projectItem"

        @JvmStatic
        fun newInstance(project: Project) =
            FunctionDialogFragment().apply {
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
            arguments?.getSerializable(ImportChooseDialogFragment.PROJECT_ITEM, Project::class.java)
        } else {
            arguments?.getSerializable(ImportChooseDialogFragment.PROJECT_ITEM) as Project
        }

        _binding = FunctionDialogBinding.inflate(inflater, container, false)
        val view: View = binding.root

        binding.buttonCancel.setOnClickListener {
            dialog?.cancel()
        }

        binding.buttonConfirm.setOnClickListener {
            val function = binding.functionEditText.text.toString()

            val xMin = binding.xLowEditText.text.toString().toFloat();
            val xMax = binding.xHighEditText.text.toString().toFloat();

            val yMin = binding.yLowEditText.text.toString().toFloat();
            val yMax = binding.yHighEditText.text.toString().toFloat();

            val zMin = binding.zLowEditText.text.toString().toFloat();
            val zMax = binding.zHighEditText.text.toString().toFloat();

            if (xMin <= xMax && yMin <= yMax && zMin <= zMax) {

                val xScope: List<Float> = listOf(xMin, xMax)
                val yScope: List<Float> = listOf(yMin, yMax)
                val zScope: List<Float> = listOf(zMin, zMax)

                viewModel.applyProjectFunction(
                    project,
                    function,
                    xScope,
                    yScope,
                    zScope
                )

                dialog?.cancel()
            }
        }

        return view
    }
}