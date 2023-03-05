package com.group.ardiagram.ui.ar

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.Color
import com.google.ar.sceneform.rendering.MaterialFactory
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import com.group.ardiagram.R
import com.group.ardiagram.data.Project
import com.group.ardiagram.data.ScatterPlot3D
import com.group.ardiagram.databinding.FragmentScreenArBinding
import com.group.ardiagram.ui.projectsList.NameChangeDialogFragment

class ARScreenFragment : Fragment() {
    companion object {
        private const val PROJECT_PARAM = "project"
    }

    private var project: Project? = null

    private var _binding: FragmentScreenArBinding? = null
    private val binding get() = _binding!!

    private lateinit var arFragment: ArFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            project = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arguments?.getSerializable(PROJECT_PARAM, Project::class.java)
            } else {
                arguments?.getSerializable(PROJECT_PARAM) as Project
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScreenArBinding.inflate(inflater, container, false)
        val root: View = binding.root
        arFragment = childFragmentManager.findFragmentById(R.id.arFragment) as ArFragment

        arFragment.setOnTapArPlaneListener { hitResult, _, _ ->
            val anchor = hitResult.createAnchor()
            val anchorNode = AnchorNode(anchor)
            anchorNode.parent = arFragment.arSceneView.scene
            val transformableNode = TransformableNode(arFragment.transformationSystem)
            transformableNode.parent = anchorNode

            // Use Node.addChild(Node!) to add a Node as a subgraph of Nodes where
            // all the necessary renderable objects are already set.
            // Use Node.renderable = <your renderable> to add a single Renderable to the scene.

            if (project?.points.isNullOrEmpty()) {
                Toast.makeText(context, "No data", Toast.LENGTH_LONG).show()
                return@setOnTapArPlaneListener
            }

            placeScatterPlot3D(transformableNode, project?.points ?: listOf())
            transformableNode.select()
        }

        return root
    }

    private fun placeScatterPlot3D(parentNode: Node, listOfPoints: List<Vector3>) {
        Toast.makeText(context, "Show plot: $listOfPoints", Toast.LENGTH_SHORT).show()

        MaterialFactory
            .makeOpaqueWithColor(context, Color(android.graphics.Color.RED))
            .thenAccept {
                val scatterPlot3D = ScatterPlot3D(listOfPoints, it)
                parentNode.addChild(scatterPlot3D)
            }
    }
}