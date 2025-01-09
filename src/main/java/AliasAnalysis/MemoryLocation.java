package AliasAnalysis;


import sootup.core.types.Type;

import java.util.HashMap;
import java.util.Map;

//represents the object instances any variable may point to
public class MemoryLocation {
    static private int  locationCounter=1;  //to be incremented each time
                                            // there s a new object created
                                            //will be used as the id of each MemoryLocation instance
    final private int lineNumber;
    final private int id;
    final private Map<String,PointsToSet> fields;
    public Object constraintSolverElement;


    MemoryLocation(int lineNumber){
        this.id=locationCounter++;
        this.lineNumber=lineNumber;
        this.constraintSolverElement=null;
        this.fields= new HashMap<>();
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "m"+lineNumber+" ("+id+")";
    }
    public static int getLocationCounter(){return MemoryLocation.locationCounter-1;}

    public void setField(String field , PointsToSet fieldTPSet){
        fields.put(field, fieldTPSet);
    }
    public PointsToSet getField(String field){
        return fields.get(field);
    }
    public boolean existsField(String field){
        return fields.containsKey(field);
    }
}
