package com.group.ardiagram.ui.ar

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
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
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.concurrent.CompletableFuture

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

            if (project?.function != null) {
                placeGraph3D(transformableNode)
            } else {
                placeScatterPlot3D(transformableNode, project?.points ?: listOf())
            }
            placeArrows(transformableNode)
            transformableNode.select()
        }

        if (project?.function != null) {
            loadGraph3DModel(project?.function!!)
        }
        return root
    }

    private fun loadGraph3DModel(function: String) {

        val graph3D = Graph3D(
            function,
            project?.xMin ?: -10.0f,
            project?.xMax ?: 10.0f,
            project?.yMin ?: -10.0f,
            project?.yMax ?: 10.0f,
            project?.zMin ?: -10.0f,
            project?.zMax ?: 10.0f
        )

        val pathFileOutPutObj = File.createTempFile("tempObj", ".obj", context?.cacheDir).path
        val pathFileOutPutGlb = File.createTempFile("tempGlb", ".glb", context?.cacheDir).path


        Toast.makeText(context, "Loading...", Toast.LENGTH_SHORT).show()

        graph3D.writeFile(pathFileOutPutObj, pathFileOutPutGlb)

        ModelRenderable.builder()
            .setSource(context, Uri.parse(pathFileOutPutGlb))
            .setIsFilamentGltf(true)
            .build()
            .thenAccept { renderable ->
                graphModel = renderable
            }
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
        // TODO: fix magic number scaling
        otherNode.localScale = Vector3(0.01f, 0.01f, 0.01f)
        otherNode.renderable = graphModel
    }

    private fun placeArrows(parentNode: Node) {
        addArrow(parentNode, "RedXArrow.glb")
        addArrow(parentNode, "GreenYArrow.glb")
        addArrow(parentNode, "BlueZArrow.glb")
    }

    private fun addArrow(parentNode: Node, modelPath: String) {
        val arrow = Node()
        arrow.parent = parentNode
        ModelRenderable.builder()
            .setSource(context, Uri.parse(modelPath))
            .setIsFilamentGltf(true)
            .build()
            .thenAccept { renderable ->
                arrow.renderable = renderable
            }
        // TODO: fix magic number scaling
        arrow.localScale = Vector3(0.05f, 0.05f, 0.05f)
    }
}