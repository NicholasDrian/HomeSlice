package com.company;

public class Mesh {
    Face[] faces;
    String name;
    public Mesh(String Name, Face[] Faces){
        faces = Faces;
        name = Name;
    }
    public static void printMesh(Mesh mesh){
        System.out.println("Mesh: " + mesh.name);
        for (Face face : mesh.faces){
            Face.printFace(face);
        }
    }
}
