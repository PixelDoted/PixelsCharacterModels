package me.pixeldots.pixelscharactermodels.utils;

import java.util.ArrayList;
import java.util.List;

public class MapModelVectors {
	
	public String meshID = "";
	public List<String> Vertices = new ArrayList<String>();
	public List<String> Faces = new ArrayList<String>();
	public List<String> VertexUVs = new ArrayList<String>();
	public List<String> VertexNormals = new ArrayList<String>();
	
	public List<List<String>> parsedFaces = new ArrayList<List<String>>();
	public List<MapVec2> parsedUVs = new ArrayList<MapVec2>();
	
}
