package com.datamaster.dao;

import java.util.List;

public class ELectronicsDataDao {
    List<String> allCategories;
    List<String> topCategories;
    public List<String> getAllCategories(){
        return allCategories;
    }
    
    public List<String> getTopCategories(){
        return topCategories;
    }
}
