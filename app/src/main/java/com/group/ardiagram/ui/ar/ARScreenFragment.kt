package com.group.ardiagram.ui.ar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.ar.core.Anchor
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.Color
import com.google.ar.sceneform.rendering.MaterialFactory
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.ShapeFactory
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import com.group.ardiagram.R
import com.group.ardiagram.data.ScatterPlot3D
import com.group.ardiagram.databinding.FragmentScreenArBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ARScreenFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ARScreenFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentScreenArBinding? = null
    private val binding get() = _binding!!

    private lateinit var arFragment: ArFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
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
            placeScatterPlot3D(transformableNode)
            //placeCube(transformableNode)

            transformableNode.select()
        }

        return root
    }

    private fun placeCube(node: Node) {
        MaterialFactory
            .makeOpaqueWithColor(context, Color(android.graphics.Color.BLUE))
            .thenAccept {
                val modelRenderable =
                    ShapeFactory.makeCube(Vector3(0.1f, 0.1f, 0.1f), Vector3.zero(), it)
                node.renderable = modelRenderable
            }
    }

    private fun placeScatterPlot3D(parentNode: Node) {
        // TODO: add list of vectors (dot coordinates) as a function parameter
        MaterialFactory
            .makeOpaqueWithColor(context, Color(android.graphics.Color.GREEN))
            .thenAccept {
                val vectorList  = listOf(
                    Vector3.zero(),
                    Vector3(0.02f, 0.02f, 0.0f),
                    Vector3(0.01f, 0.05f, 0.0f)
                )
                val scatterPlot3D = ScatterPlot3D(vectorList, it)
                parentNode.addChild(scatterPlot3D)
            }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ARFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ARScreenFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}