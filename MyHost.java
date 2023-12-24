// Voicu Catalin Marian 332CC Tema2 APD

import java.util.LinkedList;

public class MyHost extends Host {

    // Coada de task-uri
    private LinkedList<Task> taskQueue = new LinkedList<>();

    // "running" este setat pe 1 pentru ca host-ul sa ruleze
    private volatile int running = 1;

    // "check_running" este folosit pentru a verifica daca un task este in executie
    private volatile int check_running;

    // O variabila auxiliara pentru a retine task-ul care este in executie
    Task task_running;

    @Override
    public void run() {
        // Verific daca un task este in executie
        while (running == 1) {
            // Scot un task din coada
            Task task = taskQueue.poll();
            
            // Verific daca task-ul este null
            if (task != null) {
                // Setez "check_running" pe 1 si task-ul care este in executie
                check_running = 1;
                task_running = task;

                // Astept timpul necesar pentru a rula task-ul
                try {
                    Thread.sleep(task.getDuration());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }

                // Termin task-ul dupa ce a fost executat
                task.finish();
            }
            else
                {
                    // Setez "check_running" pe 0 si task-ul care este in executie pe null
                    check_running = 0;
                    task_running = null;
                }
        }
    }


    @Override
    public void addTask(Task task) {
        // Verific daca task-ul are o prioritate mai mare
        if(task.getPriority() == 200)
            // Daca da, adaug task-ul la inceputul cozii
            taskQueue.addFirst(task);
        else
            // Daca nu, adaug task-ul la sfarsitul cozii
            taskQueue.addLast(task);
    }

    @Override
    public int getQueueSize() {
        // Verific daca un task este in executie
        if(check_running == 1)
            // Daca da, returnez dimensiunea cozii + 1 (si pentru task-ul care este in executie)
            return taskQueue.size() + 1;
        else
            // Daca nu, returnez dimensiunea cozii
            return taskQueue.size();
    }

    @Override
    public long getWorkLeft() {
        long workLeft = 0L;

        // Verific daca un task este in executie si adaug timpul ramas pentru acesta
        if (check_running == 1) {
            workLeft += task_running.getLeft();
        }

        // Adaug timpul ramas pentru fiecare task din coada
        for (Task task : taskQueue) {
            workLeft += task.getDuration();
        }

        // Returnez timpul ramas calculat anterior
        return workLeft;
    }

    @Override
    public void shutdown() {
        // Setez "running" pe 0 pentru a opri host-ul
        running = 0;
    }
}