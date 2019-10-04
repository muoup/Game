package com.Util.Other;

import com.Util.Exceptions.ObjectNotFoundException;

import java.util.AbstractList;

// TODO: Either fix GameList or get rid of it.
/**
 * Alternative to ArrayList. Trying to solve ConcurrentModificationException
 * by recreating ArrayList (probably less efficiently) that can have objects
 * removed from it without it throwing errors
 * NOT WORKING
 * @param <E> The Type of the Object
 */
public class GameList<E> extends AbstractList<E> implements Iterable<E> {
    public Object[] objects;
    int iteration = 0;
    private static final Object removeObject = new GameList<GameList<Object>>();

    public GameList() {
        objects = new Object[0];
    }

    /**
     * Adds an object to the GameList
     * This is were ConcurrentModificationError will be eliminated. I really do not
     * care about the indexes of the objects, so the error is fairly pointless I think.
     * @param object Object to add to the array
     * @return
     */
    public boolean add(E object) {
        Object[] tempArray = objects.clone();
        objects = new Object[objects.length + 1];

        for (int i = 0; i < tempArray.length; i++) {
            objects[i] = tempArray[i];
        }

        return true;
    }

    /**
     * Returns the object at index;
     * @param index Index of object
     * @return Object at specified index
     */
    public E get(int index) {
        return (E) objects[index];
    }

    /**
     * Removes an object from the GameList. If it is not found, an ObjectNotFoundException
     * will be thrown. If multiple of the same object are in the GameList, only one will be removed.
     * @param object Object to remove from the GameList.
     * @return
     */
    public boolean remove(Object object) {
        int index = -1;
        for (int i = 0; i < objects.length; i++) {
            if (objects[i].equals(object)) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            try {
                throw new ObjectNotFoundException(object.toString() + " cannot be found.");
            } catch (ObjectNotFoundException e) {
                e.printStackTrace();
            }
            return false;
        }

        remove(index);

        return true;
    }

    /**
     * Determines if GameList is empty.
     * @return If the GameLIst is empty.
     */
    public boolean isEmpty() {
        return objects.length == 0;
    }

    /**
     *
     * @param array
     */
    public void removeAll(GameList<E> array) {
       for (E e : array) {

       }
    }

    public E next() {
        E obj = (E) objects[iteration];
        iteration++;
        return obj;
    }

    public E previous() {
        E obj = (E) objects[iteration];
        iteration++;
        return obj;
    }

    /**
     * Removes an object from the GameList at the specified index.
     * @param index Object to remove from the GameList.
     * @return
     */
    public E remove(int index) {
        /*
         * Making part of the array null will not work if other parts of the array
         * are null. Instead, I am using the most nonsensical object I can. This works
         * as long as get(index) is never called as this cannot be casted to E.
         */
        Object ret = objects[index];
        objects[index] = removeObject;

        Object[] tempArray = new Object[objects.length - 1];

        int mod = 0;

        for (int i = 0; i < objects.length; i++) {
            if (objects[i] == removeObject) {
                mod = -1;
            } else {
                tempArray[i + mod] = objects[i];
            }
        }

        objects = tempArray;
        return (E) ret;
    }

    @Override
    public int size() {
        return objects.length;
    }
}