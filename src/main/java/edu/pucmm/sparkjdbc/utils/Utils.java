package edu.pucmm.sparkjdbc.utils;

import edu.pucmm.sparkjdbc.encapsulation.Tag;

import java.util.ArrayList;
import java.util.UUID;

public final class Utils {
    public static ArrayList<Tag> arrayToTagList(String[] tags){
        ArrayList<Tag> tagList = new ArrayList<>();
        Tag newTag;
        String uniqueID;
        for(String tag : tags){
            uniqueID = UUID.randomUUID().toString();
            newTag = new Tag(uniqueID, tag);
            tagList.add(newTag);
        }
        return tagList;
    }

    public static Boolean isTagInArray(Tag tag, ArrayList<Tag> tags){
        Boolean exists = false;
        for(Tag myTag : tags){
            if(myTag.getTag().equalsIgnoreCase(tag.getTag())){
                exists = true;
            }
        }
        return exists;
    }
}
