package graphics.data.objects.loaders

import graphics.data.objects.Mesh
import graphics.data.textures.Material
import launcher.Settings.DEFAULT_COLOR
import launcher.Settings.MODEL_PATH
import org.joml.Vector4f
import org.lwjgl.assimp.AIColor4D
import org.lwjgl.assimp.AIMaterial
import org.lwjgl.assimp.AIMesh
import org.lwjgl.assimp.AIString
import org.lwjgl.assimp.Assimp.*
import java.io.File
import java.nio.IntBuffer
import java.util.*

fun loadStaticMesh(name: String, format: TextureFormat): Array<Mesh> {
    val path = "$MODEL_PATH$name\\$name"
    val file = File("$path.obj")
    val aiScene = aiImportFile(
        file.absolutePath,
        aiProcess_JoinIdenticalVertices +
                aiProcess_Triangulate +
                aiProcess_FixInfacingNormals
    )!!
    val numMaterials = aiScene.mNumMaterials()
    val aiMaterials = aiScene.mMaterials()
    val materials = ArrayList<Material>()
    for (i in 0 until numMaterials) {
        val aiMaterial = AIMaterial.create(aiMaterials!!.get(i))
        processMaterial(aiMaterial, materials, path + format.value)
    }
    val numMeshes = aiScene.mNumMeshes()
    val aiMeshes = aiScene.mMeshes()
    return Array(numMeshes) {
        processMesh(AIMesh.create(aiMeshes!!.get(it)), materials)
    }
}

private fun processMaterial(aiMaterial: AIMaterial, materials: MutableList<Material>, texturePath: String) {
    val color = AIColor4D.create()
    val aiPath = AIString.calloc()
    aiGetMaterialTexture(
        aiMaterial,
        aiTextureType_NONE,
        0,
        aiPath,
        null as IntBuffer?,
        null,
        null,
        null,
        null,
        null
    )
    val texture = TextureCache.texture(texturePath)
    var ambient = DEFAULT_COLOR
    var result = aiGetMaterialColor(aiMaterial, AI_MATKEY_COLOR_AMBIENT, aiTextureType_AMBIENT, 0, color)
    if (result == 0) {
        ambient = Vector4f(color.r(), color.g(), color.b(), color.a())
    }
    var diffuse = DEFAULT_COLOR
    result = aiGetMaterialColor(aiMaterial, AI_MATKEY_COLOR_DIFFUSE, aiTextureType_DIFFUSE, 0, color)
    if (result == 0) {
        diffuse = Vector4f(color.r(), color.g(), color.b(), color.a())
    }
    var specular = DEFAULT_COLOR
    result = aiGetMaterialColor(aiMaterial, AI_MATKEY_COLOR_SPECULAR, aiTextureType_SPECULAR, 0, color)
    if (result == 0) {
        specular = Vector4f(color.r(), color.g(), color.b(), color.a())
    }
    val material = Material(ambient, diffuse, specular, 1.0f)
    material.texture = texture
    materials.add(material)
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