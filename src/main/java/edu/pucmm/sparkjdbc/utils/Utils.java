package edu.pucmm.sparkjdbc.utils;

import edu.pucmm.sparkjdbc.encapsulation.Tag;
import edu.pucmm.sparkjdbc.encapsulation.User;

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

    public static String truncate(String value, int length) {
        // Ensure String length is longer than requested size.
        if (value.length() > length) {
            return value.substring(0, length);
        } else {
            return value;
        }
    }

    public static User getCurrentUser(){
        User user = new User("autor01256", "Luis Garcia", "123456", "admin"); //This should be deleted when sessions get implemented
        return user;
    }

}
