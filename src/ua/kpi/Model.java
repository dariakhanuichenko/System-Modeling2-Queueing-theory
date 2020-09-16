package ua.kpi;

import java.util.ArrayList;
import java.util.List;

public class Model {
    private double tnext;
    private double tcurr;
    private double t0,
                   t1;
    private double delayCreate, delayProcess;
    private int numCreate, numProcess, failure;
    private int state, maxqueue, queue;
    private int nextEvent;

    private List<Double> waitingTimeList; // список для обчислення середнього часу очікування черзі
    private List<Double> deltaTList;  // список для обчислення середнього   обчислення середнього завантаження пристрою
    private List<Integer> lengthQueueList; // список довжин черг в момент tcurr

    public Model(double delay0, double delay1) {
        delayCreate = delay0;
        delayProcess = delay1;
        tnext = 0.0;
        tcurr = tnext;
        t0 = tcurr;
        t1 = Double.MAX_VALUE;
        maxqueue = 0;
        waitingTimeList = new ArrayList<>();
        deltaTList = new ArrayList<>();
    }

    public Model(double delay0, double delay1, int maxQ) {
        delayCreate = delay0;
        delayProcess = delay1;
        tnext = 0.0;
        tcurr = tnext;
        t0 = tcurr;
        t1 = Double.MAX_VALUE;
        maxqueue = maxQ;
        waitingTimeList = new ArrayList<>();
        deltaTList = new ArrayList<>();
        lengthQueueList = new ArrayList<>();

    }

    public void simulate(double timeModeling, boolean flag) {
        while (tcurr < timeModeling) {


            tnext = t0;
            nextEvent = 0; // надходження заявки

            if (t1 < tnext) {
                tnext = t1;
                nextEvent = 1;
            }

            waitingTimeList.add((tnext - tcurr) * queue);
            deltaTList.add((tnext - tcurr) * state);

            tcurr = tnext;
            switch (nextEvent) {
                case 0:
                    event0();
                    break;
                case 1:
                    event1();
                    break;
            }
            if (flag)
                printInfo();
            lengthQueueList.add(state);
        }
        if (flag)
            System.out.println("-----------------------------------------------VERIFICATION---------------------------------------------------");
        printStatistic(timeModeling);
    }

    public void printStatistic(double timeModeling) {
        System.out.printf(" numCreate= %3d;  numProcess = %3d;  failure = %3d;  average waiting time=%5.10f;  Average device=%5.10f;  probability of failure = %5.10f;  average queue length = %5.10f;\n",
                numCreate, numProcess, failure, getSumWaitingTime() / numProcess, getSumDeltaT() / timeModeling, getProbabilityOfFailure(), getSumWaitingTime() / timeModeling);
    }

    public void printInfo() {
        System.out.println(" t= " + tcurr + " state = " + state + " queue = " + queue);
    }

    public void event0() { // start service
        t0 = tcurr + getDelayOfCreate();
        numCreate++;

        if (state == 0) {
            state = 1;
            t1 = tcurr + getDelayOfProcess();
        } else {
            if (queue < maxqueue)
                queue++;
            else
                failure++;
        }
    }

    public void event1() { // end of service

        t1 = Double.MAX_VALUE;
        state = 0;
        if (queue > 0) {
            queue--;
            state = 1;
            t1 = tcurr + getDelayOfProcess();
        }
        numProcess++;
    }


    private double getDelayOfCreate() {
        return FunRand.Exp(delayCreate);
    }

    private double getDelayOfProcess() {
        return FunRand.Exp(delayProcess);
    }

    private Double getSumWaitingTime() {
        return waitingTimeList.stream().mapToDouble(i -> i).sum();
    }

    private Double getSumDeltaT() {
        return deltaTList.stream().mapToDouble(i -> i).sum();
    }

    private double getProbabilityOfFailure() {
        return (double) failure / numCreate;
    }

}
