package me.PixelDots.PixelsCharacterModels.client.model.render.mesh;

import java.util.ArrayList;
import java.util.List;

public class MeshFace 
{

	public List<Integer> vertices = new ArrayList<Integer>();
	public List<Integer> vertexTexture = new ArrayList<Integer>();
	public List<Integer> vertexNormal = new ArrayList<Integer>();
	
	public MeshFace() {
		
	}
	
	public MeshFace addVertex(int vertex) {
		vertices.add(vertex);
		return this;
	}
	
	public MeshFace addVertex(String vertex) {
		vertices.add(Integer.valueOf(vertex));
		return this;
	}
	
	public MeshFace addVertexTexture(int vertex) {
		vertexTexture.add(vertex);
		return this;
	}
	
	public MeshFace addVertexTexture(String vertex) {
		vertexTexture.add(Integer.valueOf(vertex));
		return this;
	}
	
	public MeshFace addVertexNormal(int vertex) {
		vertexNormal.add(vertex);
		return this;
	}
	
	public MeshFace addVertexNormal(String vertex) {
		vertexNormal.add(Integer.valueOf(vertex));
		return this;
	}
	
	public int[] toArray() {
		int[] array = new int[vertices.size()];
		for (int i = 0; i < vertices.size(); i++) {
			array[i] = vertices.get(i);
		}
		return array;
 	}
	
}
