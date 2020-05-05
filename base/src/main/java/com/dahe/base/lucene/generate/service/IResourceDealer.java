package com.dahe.base.lucene.generate.service;

import java.util.List;

public interface IResourceDealer {

    public void addResource(long id);

    public void addResource(long id, String resType);
    
    public void dealResource(List<Long> idList, String type);
    
    public void index();
    
    public String getIndexType();
    
    public long generateIndexId(long id);
}