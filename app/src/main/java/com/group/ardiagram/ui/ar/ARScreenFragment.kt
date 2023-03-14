package com.group.ardiagram.ui.ar

import android.net.Uri
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
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.Renderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import com.group.ardiagram.R
import com.group.ardiagram.data.Graph3D
import com.group.ardiagram.data.Project
import com.group.ardiagram.data.ScatterPlot3D
import com.group.ardiagram.databinding.FragmentScreenArBinding

class ARScreenFragment : Fragment() {
    companion object {
        private const val PROJECT_PARAM = "project"
    }

    private var project: Project? = null
    private var graphModel: Renderable? = null

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

//            if (project?.points.isNullOrEmpty()) {
//                Toast.makeText(context, "No data", Toast.LENGTH_LONG).show()
//                return@setOnTapArPlaneListener
//            }
//
//            placeScatterPlot3D(transformableNode, project?.points ?: listOf())

            placeGraph3D(transformableNode)
            transformableNode.select()
        }
        // todo: asynchronous call / coroutine ???
        loadGraph3DModel()
        return root
    }

    private fun loadGraph3DModel() {

        val graph3D = Graph3D(
            "sin(x)*cos(y)",
            -10.0f, 10.0f,
            -10.0f, 10.0f,
            0.0f, 0.0f
        )

        // todo: write files so that they are saved correctly
        // graph3D.writeFile("model.obj", "model.glb")

        ModelRenderable.builder()
            .setSource(context, Uri.parse("temp.glb")) // todo: change to valid .glb model path
            .setIsFilamentGltf(true)
            .build()
            .thenAccept { renderable -> graphModel = renderable }
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

    private fun placeGraph3D(parentNode: Node) {
        val otherNode = Node()
        otherNode.parent = parentNode
        // the model is usually quite large so it's scaled down (temporary fix)
        otherNode.localScale = Vector3(0.01f, 0.01f, 0.01f)
        otherNode.renderable = graphModel
    }
}