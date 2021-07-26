package com.company;

public class ToGCode {
     public static void ToGCode (Mesh mesh){
         for (Face face : mesh.faces){

         }
     }
}

/*public class ToGCode {
    Map<Float, List<PolyLine>> polyLines = new HashMap<>();

    public ToGCode(List<Line[]> lines){
        for (Line[] linesWithinLines : lines){
            for (Line line : linesWithinLines){
                if (polyLines.containsKey(line.start.dimensions[2])){
                    List<PolyLine> polyLineArrayList = polyLines.get(line.start.dimensions[2]);
                    Iterator it = polyLineArrayList.iterator();
                    boolean lineAdded = false;
                    while (it.hasNext()){
                        PolyLine polyLine = (PolyLine) it.next();
                        if (polyLine.add(line)){
                            lineAdded = true;
                            break;
                        }
                    } if (!lineAdded){
                        PolyLine temp = new PolyLine();
                        temp.add(line);
                        polyLineArrayList.add(temp);
                    }
                } else {
                    //level key doesnt exist in polylines hashmap.
                    PolyLine temp = new PolyLine();
                    temp.add(line);
                    List<PolyLine> listOfPolylines = new ArrayList<PolyLine>();
                    listOfPolylines.add(temp);
                    polyLines.put(line.start.dimensions[2], listOfPolylines);
                }
            }
        }
    }
}
*/