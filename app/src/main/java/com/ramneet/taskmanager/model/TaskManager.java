package com.ramneet.taskmanager.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Singleton class to manage all the tasks.
 */
public class TaskManager {
    private List<Task> tasks = new ArrayList<>();

    // Singleton support
    private static List<Task> instance;

    private TaskManager() {
        // private to prevent anyone else from instantiating
    }

    public static List<Task> getInstance() {
        if (instance == null) {
            instance = new TaskManager().getList();
        }
        return instance;
    }

    private List<Task> getList() {
        return tasks;
    }

    public void add(Task task) {
        tasks.add(task);
    }

    public int getSize() {
        return tasks.size();
    }

    public Task get(int i) {
        return tasks.get(i);
    }


//    public void printLenses() {
//        for( Lens lens : lenses) {
//            System.out.println( lens.getMake() + " "
//                    + lens.getMaxAperture() + "mm  F"
//                    + lens.getFocalLength());
//        }
//        //       for (int i = 0; i < lenses.size(); i++) {
//        //         System.out.println( i + ". " + lenses.get(i).getMake()
//        //               + " " + lenses.get(i).getMaxAperture()
//        //             + "mm F" + lenses.get(i).getFocalLength() );
//        //}
//    }

    public Iterator<Task> iterator() {
        return tasks.iterator();
    }


}
