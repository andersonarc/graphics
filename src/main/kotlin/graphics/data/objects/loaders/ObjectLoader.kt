package graphics.data.objects.loaders

import graphics.data.objects.Mesh
import graphics.data.objects.animations.*
import graphics.data.textures.Material
import launcher.Settings.DEFAULT_COLOR
import launcher.Settings.MAX_WEIGHTS
import org.joml.Matrix4f
import org.joml.Quaternionf
import org.joml.Vector4f
import org.lwjgl.assimp.*
import org.lwjgl.assimp.Assimp.*
import java.nio.IntBuffer
import java.util.*


fun loadStaticMesh(path: String, texturesDirectory: String): Array<Mesh> {
    val aiScene = aiImportFile(
        path,
        aiProcess_JoinIdenticalVertices or
                aiProcess_Triangulate or
                aiProcess_FixInfacingNormals
    )!!
    val numMaterials = aiScene.mNumMaterials()
    val aiMaterials = aiScene.mMaterials()
    val materials = ArrayList<Material>(numMaterials)
    for (i in 0 until numMaterials) {
        val aiMaterial = AIMaterial.create(aiMaterials!!.get(i))
        processMaterial(aiMaterial, materials, texturesDirectory)
    }
    val numMeshes = aiScene.mNumMeshes()
    val aiMeshes = aiScene.mMeshes()
    return Array(numMeshes) {
        processMesh(AIMesh.create(aiMeshes!!.get(it)), materials)
    }
}

private fun processMaterial(aiMaterial: AIMaterial, materials: MutableList<Material>, texturesDirectory: String) {
    val color = AIColor4D.create()
    val path = AIString.calloc()
    aiGetMaterialTexture(
        aiMaterial,
        aiTextureType_DIFFUSE,
        0,
        path,
        null as IntBuffer?,
        null,
        null,
        null,
        null,
        null
    )
    val textPath = path.dataString()
    val texture = if (textPath.isNotEmpty()) TextureCache.texture("$texturesDirectory\\$textPath") else null
    val ambient = if (aiGetMaterialColor(aiMaterial, AI_MATKEY_COLOR_AMBIENT, aiTextureType_NONE, 0, color) == 0) {
        Vector4f(color.r(), color.g(), color.b(), color.a())
    } else DEFAULT_COLOR
    val diffuse = if (aiGetMaterialColor(aiMaterial, AI_MATKEY_COLOR_DIFFUSE, aiTextureType_NONE, 0, color) == 0) {
        Vector4f(color.r(), color.g(), color.b(), color.a())
    } else DEFAULT_COLOR
    val specular = if (aiGetMaterialColor(aiMaterial, AI_MATKEY_COLOR_SPECULAR, aiTextureType_NONE, 0, color) == 0) {
        Vector4f(color.r(), color.g(), color.b(), color.a())
    } else DEFAULT_COLOR
    materials.add(Material(ambient, diffuse, specular, 1.0f, texture))
}

private fun processMesh(aiMesh: AIMesh, materials: List<Material>): Mesh {
    val vertices = ArrayList<Float>()
    val textures = ArrayList<Float>()
    val normals = ArrayList<Float>()
    val indices = ArrayList<Int>()
    processVertices(aiMesh, vertices)
    processNormals(aiMesh, normals)
    processTextCoords(aiMesh, textures)
    processIndices(aiMesh, indices)
    val materialIdx = aiMesh.mMaterialIndex()
    val material = if (materialIdx >= 0 && materialIdx < materials.size) {
        materials[materialIdx]
    } else {
        Material()
    }
    return Mesh(
        vertices.toFloatArray(),
        textures.toFloatArray(),
        normals.toFloatArray(),
        indices.toIntArray(),
        material
    )
}

private fun processVertices(aiMesh: AIMesh, vertices: MutableList<Float>) {
    val aiVertices = aiMesh.mVertices()
    while (aiVertices.remaining() > 0) {
        val aiVertex = aiVertices.get()
        vertices.add(aiVertex.x())
        vertices.add(aiVertex.y())
        vertices.add(aiVertex.z())
    }
}

private fun processNormals(aiMesh: AIMesh, normals: MutableList<Float>) {
    val aiNormals = aiMesh.mNormals()
    while (aiNormals != null && aiNormals.remaining() > 0) {
        val aiNormal = aiNormals.get()
        normals.add(aiNormal.x())
        normals.add(aiNormal.y())
        normals.add(aiNormal.z())
    }
}

private fun processTextCoords(aiMesh: AIMesh, textures: MutableList<Float>) {
    val textCoords = aiMesh.mTextureCoords(0)
    val numTextCoords = textCoords?.remaining() ?: 0
    for (i in 0 until numTextCoords) {
        val textCoord = textCoords!!.get()
        textures.add(textCoord.x())
        textures.add(1 - textCoord.y())
    }
}

private fun processIndices(aiMesh: AIMesh, indices: MutableList<Int>) {
    val numFaces = aiMesh.mNumFaces()
    val aiFaces = aiMesh.mFaces()
    for (i in 0 until numFaces) {
        val aiFace = aiFaces.get(i)
        val buffer = aiFace.mIndices()
        while (buffer.remaining() > 0) {
            indices.add(buffer.get())
        }
    }
}

private fun buildTransFormationMatrices(aiNodeAnim: AINodeAnim, node: Node) {
    val numFrames = aiNodeAnim.mNumPositionKeys()
    val positionKeys = aiNodeAnim.mPositionKeys()
    val scalingKeys = aiNodeAnim.mScalingKeys()
    val rotationKeys = aiNodeAnim.mRotationKeys()
    for (i in 0 until numFrames) {
        var aiVecKey = positionKeys!!.get(i)
        var vec = aiVecKey.mValue()
        val transMatrix = Matrix4f().translate(vec.x(), vec.y(), vec.z())
        val quaternionKey = rotationKeys!!.get(i)
        val aiQuaternion = quaternionKey.mValue()
        val quaternion = Quaternionf(aiQuaternion.x(), aiQuaternion.y(), aiQuaternion.z(), aiQuaternion.w())
        transMatrix.rotate(quaternion)
        if (i < aiNodeAnim.mNumScalingKeys()) {
            aiVecKey = scalingKeys!!.get(i)
            vec = aiVecKey.mValue()
            transMatrix.scale(vec.x(), vec.y(), vec.z())
        }
        node.addTransformation(transMatrix)
    }
}

fun loadAnimatedObject(path: String, texturesDirectory: String): AnimatedObject {
    val aiScene = aiImportFile(
        path,
        aiProcess_GenSmoothNormals or
                aiProcess_JoinIdenticalVertices or
                aiProcess_Triangulate or
                aiProcess_FixInfacingNormals or
                aiProcess_LimitBoneWeights
    )!!
    val numMaterials = aiScene.mNumMaterials()
    val aiMaterials = aiScene.mMaterials()
    val materials = ArrayList<Material>()
    for (i in 0 until numMaterials) {
        val aiMaterial = AIMaterial.create(aiMaterials!!.get(i))
        processMaterial(aiMaterial, materials, texturesDirectory)
    }
    val boneList = ArrayList<Bone>()
    val numMeshes = aiScene.mNumMeshes()
    val aiMeshes = aiScene.mMeshes()
    val get = MeshCache.animatedMesh(path)
    val meshes = if (get != null) get else {
        val array = Array(numMeshes) {
            processMesh(AIMesh.create(aiMeshes!!.get(it)), materials, boneList)
        }
        MeshCache.putAnimatedMesh(path, array)
        array
    }
    val aiRootNode = aiScene.mRootNode()!!
    val rootTransformation = toMatrix(aiRootNode.mTransformation())
    val rootNode = processNodesHierarchy(aiRootNode, null)
    val animations = processAnimations(aiScene, boneList, rootNode, rootTransformation)
    return AnimatedObject(meshes, animations)
}

private fun buildAnimationFrames(
    boneList: List<Bone>,
    rootNode: Node,
    rootTransformation: Matrix4f
): List<AnimatedFrame> {
    val numFrames = rootNode.animationFrames
    val frameList = ArrayList<AnimatedFrame>()
    for (i in 0 until numFrames) {
        val frame = AnimatedFrame()
        frameList.add(frame)
        for (j in boneList.indices) {
            val bone = boneList[j]
            val node = rootNode.findByName(bone.boneName)
            var boneMatrix = node?.getParentTransforms(i) ?: Matrix4f()
            boneMatrix.mul(bone.offsetMatrix)
            boneMatrix = Matrix4f(rootTransformation).mul(boneMatrix)
            frame.setMatrix(j, boneMatrix)
        }
    }
    return frameList
}

private fun processAnimations(
    aiScene: AIScene, boneList: List<Bone>,
    rootNode: Node, rootTransformation: Matrix4f
): Map<String, Animation> {
    val animations = HashMap<String, Animation>()
    val numAnimations = aiScene.mNumAnimations()
    val aiAnimations = aiScene.mAnimations()
    for (i in 0 until numAnimations) {
        val aiAnimation = AIAnimation.create(aiAnimations!!.get(i))
        val numChannels = aiAnimation.mNumChannels()
        val aiChannels = aiAnimation.mChannels()
        for (j in 0 until numChannels) {
            val aiNodeAnim = AINodeAnim.create(aiChannels!!.get(j))
            val nodeName = aiNodeAnim.mNodeName().dataString()
            val node = rootNode.findByName(nodeName)
            if (node != null) {
                buildTransFormationMatrices(aiNodeAnim, node)
            }
        }
        val frames = buildAnimationFrames(boneList, rootNode, rootTransformation)
        val animation = Animation(frames, aiAnimation.mName().dataString(), aiAnimation.mDuration())
        animations[animation.name] = animation
    }
    return animations
}

private fun processBones(
    aiMesh: AIMesh, boneList: MutableList<Bone>, boneIds: MutableList<Int>,
    weights: MutableList<Float>
) {
    val weightSet = HashMap<Int, List<VertexWeight>>()
    val numBones = aiMesh.mNumBones()
    val aiBones = aiMesh.mBones()
    for (i in 0 until numBones) {
        val aiBone = AIBone.create(aiBones!!.get(i))
        val id = boneList.size
        val bone = Bone(id, aiBone.mName().dataString(), toMatrix(aiBone.mOffsetMatrix()))
        boneList.add(bone)
        val numWeights = aiBone.mNumWeights()
        val aiWeights = aiBone.mWeights()
        for (j in 0 until numWeights) {
            val aiWeight = aiWeights.get(j)
            val vw = VertexWeight(
                bone.boneId, aiWeight.mVertexId(),
                aiWeight.mWeight()
            )
            var vertexWeightList: MutableList<VertexWeight>? = weightSet[vw.vertexId] as MutableList<VertexWeight>?
            if (vertexWeightList == null) {
                vertexWeightList = ArrayList()
                weightSet[vw.vertexId] = vertexWeightList
            }
            vertexWeightList.add(vw)
        }
    }
    val numVertices = aiMesh.mNumVertices()
    for (i in 0 until numVertices) {
        val vertexWeightList = weightSet[i]
        val size = vertexWeightList?.size ?: 0
        for (j in 0 until MAX_WEIGHTS) {
            if (j < size) {
                val vw = vertexWeightList!![j]
                weights.add(vw.weight)
                boneIds.add(vw.boneId)
            } else {
                weights.add(0.0f)
                boneIds.add(0)
            }
        }
    }
}

private fun processMesh(aiMesh: AIMesh, materials: List<Material>, boneList: MutableList<Bone>): Mesh {
    val vertices = ArrayList<Float>()
    val textures = ArrayList<Float>()
    val normals = ArrayList<Float>()
    val indices = ArrayList<Int>()
    val boneIds = ArrayList<Int>()
    val weights = ArrayList<Float>()

    processVertices(aiMesh, vertices)
    processNormals(aiMesh, normals)
    processTextCoords(aiMesh, textures)
    processIndices(aiMesh, indices)
    processBones(aiMesh, boneList, boneIds, weights)

    val mesh = Mesh(
        vertices.toFloatArray(), textures.toFloatArray(),
        normals.toFloatArray(), indices.toIntArray(),
        boneIds.toIntArray(), weights.toFloatArray()
    )
    val material: Material
    val materialIdx = aiMesh.mMaterialIndex()
    material = if (materialIdx >= 0 && materialIdx < materials.size) {
        materials[materialIdx]
    } else {
        Material()
    }
    mesh.material = material

    return mesh
}

private fun processNodesHierarchy(aiNode: AINode, parentNode: Node?): Node {
    val nodeName = aiNode.mName().dataString()
    val node = Node(nodeName, parentNode)

    val numChildren = aiNode.mNumChildren()
    val aiChildren = aiNode.mChildren()
    for (i in 0 until numChildren) {
        val aiChildNode = AINode.create(aiChildren!!.get(i))
        val childNode = processNodesHierarchy(aiChildNode, node)
        node.addChild(childNode)
    }

    return node
}

private fun toMatrix(aiMatrix4x4: AIMatrix4x4): Matrix4f {
    val result = Matrix4f()
    result.m00(aiMatrix4x4.a1())
    result.m10(aiMatrix4x4.a2())
    result.m20(aiMatrix4x4.a3())
    result.m30(aiMatrix4x4.a4())
    result.m01(aiMatrix4x4.b1())
    result.m11(aiMatrix4x4.b2())
    result.m21(aiMatrix4x4.b3())
    result.m31(aiMatrix4x4.b4())
    result.m02(aiMatrix4x4.c1())
    result.m12(aiMatrix4x4.c2())
    result.m22(aiMatrix4x4.c3())
    result.m32(aiMatrix4x4.c4())
    result.m03(aiMatrix4x4.d1())
    result.m13(aiMatrix4x4.d2())
    result.m23(aiMatrix4x4.d3())
    result.m33(aiMatrix4x4.d4())
    return result
}