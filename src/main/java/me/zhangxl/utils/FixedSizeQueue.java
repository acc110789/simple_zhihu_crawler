package me.zhangxl.utils;

import org.apache.http.annotation.NotThreadSafe;

import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

/**
 * Created by zhangxiaolong on 16/11/5.
 */
@NotThreadSafe
public class FixedSizeQueue<T> extends AbstractQueue<T> {
    //start 是 inclusive，start位置的地方有东西
    private int start = 0;
    //end 是exclusive，end位置的地方没有东西
    private int end = 0;
    private final int maxSize;
    private final Object[] data;

    FixedSizeQueue(int maxSize){
        this.maxSize = maxSize;
        data = new Object[maxSize + 1];
    }

    private void addStart(){
        start = (start + 1) % data.length;
    }

    private void addEnd(){
        end = (end + 1) % data.length;
    }

    private class InnerIterator<T> implements Iterator<T>{

        private int nextPosition = start;

        @Override
        public boolean hasNext() {
            return nextPosition != end;
        }

        @Override
        public T next() {
            //noinspection unchecked
            T result =  (T) data[nextPosition];
            addNextPosition();
            return result;
        }

        private void addNextPosition(){
            nextPosition = (nextPosition + 1) % data.length;
        }

        @Override
        public void remove() {
            throw new IllegalStateException("can remove");
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new InnerIterator<>();
    }

    @Override
    public int size() {
        if(end > start){
            return end - start;
        } else if(end < start) {
            return end + data.length - start;
        } else {
            return 0;
        }
    }

    public boolean full(){
        return size() >= maxSize;
    }

    @Override
    public boolean offer(T t) {
        if(full()) {
            return false;
        } else {
            data[end] = t;
            addEnd();
            return true;
        }
    }

    public boolean offerAll(Collection<? extends T> c) {
        if (c == this)
            throw new IllegalArgumentException();
        boolean modified = false;
        for (T e : c) {
            if (full()) {
                break;
            }
            if (offer(e))
                modified = true;
        }
        return modified;
    }

    @Override
    public T poll() {
        if(isEmpty()) {
            return null;
        } else {
            @SuppressWarnings("unchecked")
            T result = (T) data[start];
            addStart();
            return result;
        }
    }

    @Override
    public T peek() {
        if(isEmpty()) {
            return null;
        } else {
            //noinspection unchecked
            return (T) data[start];
        }
    }


    public static void main(String[] args){
        //测试一下FixedSizeQueue的性能
        Queue<Integer> queue = new FixedSizeQueue<>(5);
        for(int i = 1; i <= 5; i++){
            System.out.println("add " + i + "   " + queue.offer(i));
        }
        System.out.println(queue);
        System.out.println("add " + 6 + "   " + queue.offer(6));
        System.out.println(queue);

        System.out.println("poll " + queue.poll());
        System.out.println(queue);

        System.out.println("add " + 7 + "   " + queue.offer(7));
        System.out.println(queue);
    }
}
