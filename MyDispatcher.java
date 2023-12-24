// Voicu Catalin Marian 332CC Tema2 APD

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MyDispatcher extends Dispatcher {

    // O variabila auxiliara pentru a retine index-ul host-ului care va primi task-ul urmator
    public static AtomicInteger i = new AtomicInteger(0);

    public MyDispatcher(SchedulingAlgorithm algorithm, List<Host> hosts) {
        super(algorithm, hosts);
    }

    @Override
    public void addTask(Task task) {
        // Verific ce algoritm de scheduling este folosit
        switch(algorithm) {
            case ROUND_ROBIN:
                // Calculez index-ul host-ului care va primi task-ul urmator
                int index = (i.getAndIncrement() + 1) % hosts.size();

                // Adaug task-ul in coada host-ului
                hosts.get(index).addTask(task);

                break;
            case SHORTEST_QUEUE:
                // Initial host-ul cu cea mai mica coada este primul host din lista
                Host shortest_queue_host = hosts.get(0);

                // Parcurg lista de host-uri
                for (Host host : hosts) 
                {
                    // Compar coada host-ului curent cu cea a host-ului cu cea mai mica coada
                    if (host.getQueueSize() < shortest_queue_host.getQueueSize()) 
                        // Daca da, atunci host-ul curent devine host-ul cu cea mai mica coada
                        shortest_queue_host = host;
                    else 
                    // Daca coada host-ului curent este egala cu cea a host-ului cu cea mai mica coada
                    if (host.getQueueSize() == shortest_queue_host.getQueueSize())
                        // Compar id-ul host-ului curent cu cel al host-ului cu cea mai mica coada
                        if (host.getId() < shortest_queue_host.getId())
                            // Daca da, atunci host-ul curent devine host-ul cu cea mai mica coada
                            shortest_queue_host = host;
                }

                // Adaug task-ul in coada host-ului cu cea mai mica coada
                shortest_queue_host.addTask(task);

                break;
            case SIZE_INTERVAL_TASK_ASSIGNMENT:
                // Daca task-ul este de tip SHORT
                if(task.getType() == TaskType.SHORT)
                    // Adaug task-ul in coada host-ului cu index-ul 0
                    hosts.get(0).addTask(task);
                else
                // Daca task-ul este de tip MEDIUM
                if(task.getType() == TaskType.MEDIUM)
                    // Adaug task-ul in coada host-ului cu index-ul 1
                    hosts.get(1).addTask(task);
                else
                // Daca task-ul este de tip LONG
                if(task.getType() == TaskType.LONG)
                    // Adaug task-ul in coada host-ului cu index-ul 2
                    hosts.get(2).addTask(task);

                break;
            case LEAST_WORK_LEFT:
                // Initial host-ul cu cel mai mic timp ramas este primul host din lista
                Host least_work_left_host = hosts.get(0);

                // Parcurg lista de host-uri
                for (Host host : hosts) 
                {
                    // Compar timpul ramas al host-ului curent cu cel al host-ului cu cel mai mic timp ramas
                    if (host.getWorkLeft() < least_work_left_host.getWorkLeft()) 
                        // Daca da, atunci host-ul curent devine host-ul cu cel mai mic timp ramas
                        least_work_left_host = host;
                    else 
                    // Daca timpul ramas al host-ului curent este egal cu cel al host-ului cu cel mai mic timp ramas
                    if (host.getWorkLeft() == least_work_left_host.getWorkLeft())
                        // Compar id-ul host-ului curent cu cel al host-ului cu cel mai mic timp ramas
                        if (host.getId() < least_work_left_host.getId())
                            // Daca da, atunci host-ul curent devine host-ul cu cel mai mic timp ramas
                            least_work_left_host = host;
                }

                // Adaug task-ul in coada host-ului cu cel mai mic timp ramas
                least_work_left_host.addTask(task);

                break;
        }
    }
}