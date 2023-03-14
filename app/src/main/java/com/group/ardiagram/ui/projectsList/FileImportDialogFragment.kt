package com.group.ardiagram.ui.projectsList

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.group.ardiagram.data.Project
import com.group.ardiagram.databinding.FileImportDialogBinding

class FileImportDialogFragment : DialogFragment() {

    private val viewModel: ProjectsListViewModel by activityViewModels()

    private var _binding: FileImportDialogBinding? = null
    private val binding get() = _binding!!

    private var _launcher: ActivityResultLauncher<Intent>? = null


    companion object {
        const val PROJECT_ITEM = "projectItem"

        @JvmStatic
        fun newInstance(project: Project) =
            FileImportDialogFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(PROJECT_ITEM, project)
                }
            }
    }

    private var project: Project? = null

    private var fileUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Set file path to textView
        _launcher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                fileUri = result.data?.data

                fileUri ?: return@registerForActivityResult
                binding.filePathEditText.setText(context?.let { getFileName(it, fileUri!!) })
            }
        }

        project = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getSerializable(PROJECT_ITEM, Project::class.java)
        } else {
            arguments?.getSerializable(PROJECT_ITEM) as Project
        }

        _binding = FileImportDialogBinding.inflate(inflater, container, false)
        val view: View = binding.root

        binding.buttonFindFile.setOnClickListener {
            onClickFilePath()
        }

        binding.buttonCancel.setOnClickListener {
            dialog?.cancel()
        }

        binding.buttonConfirm.setOnClickListener {
            onClickConfirm()
        }

        return view
    }

    private fun getFileName(context: Context, uri: Uri): String {

        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor.use {
                if (cursor?.moveToFirst() == true) {
                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    return cursor.getString(nameIndex)
                }
            }
        }
        return uri.lastPathSegment.toString()
    }

    private fun onClickFilePath() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/*"
        }
        _launcher?.launch(intent)
    }

    private fun onClickConfirm() {
        val path = binding.filePathEditText.toString()
        if (path.isNotEmpty()) {
            project?.pathToTableFile = path

            viewModel.applyImportProjectData(project, fileUri)

            dialog?.cancel()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}