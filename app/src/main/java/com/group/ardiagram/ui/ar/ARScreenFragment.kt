package com.group.ardiagram.ui.ar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.ar.core.Anchor
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.Color
import com.google.ar.sceneform.rendering.MaterialFactory
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.ShapeFactory
import com.google.ar.sceneform.ux.ArFragment
import com.group.ardiagram.R
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
            placeCube(hitResult.createAnchor())
        }

        return root
    }

    private fun placeCube(anchor: Anchor) {
        MaterialFactory
            .makeOpaqueWithColor(context, Color(android.graphics.Color.BLUE))
            .thenAccept {
                val modelRenderable =
                    ShapeFactory.makeCube(Vector3(0.1f, 0.1f, 0.1f), Vector3(0.1f, 0.1f, 0.1f), it)
                placeModel(modelRenderable, anchor)
            }
    }

    private fun placeModel(modelRenderable: ModelRenderable, anchor: Anchor) {
        val anchorNode = AnchorNode(anchor)
        anchorNode.renderable = modelRenderable
        arFragment.arSceneView.scene.addChild(anchorNode)
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