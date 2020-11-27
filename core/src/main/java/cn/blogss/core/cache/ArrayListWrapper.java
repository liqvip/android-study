package cn.blogss.core.cache;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ArrayListWrapper<T> extends ArrayList<T> {
    public int maxSize = 0;
    private boolean canReset = true;

    @Override
    public boolean remove(@Nullable Object o) {
        if(size() > maxSize){
            maxSize = size();
            canReset = false;
        }
        if(size() == 0)
            canReset = true;
        return super.remove(o);
    }

    @Override
    public boolean add(T t) {
        if(canReset){
            maxSize = size() + 1;
        }
        return super.add(t);
    }
}
