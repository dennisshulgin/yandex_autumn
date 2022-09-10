package com.shulgin.yandex_autumn.entity;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Folder extends Element{
    @OneToMany(mappedBy = "parentId", orphanRemoval = true)
    private List<Element> children;

    public List<Element> getChildren() {
        return children;
    }

    public void setChildren(List<Element> children) {
        this.children = children;
    }
}
