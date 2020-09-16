package ua.kpi;

import java.util.ArrayList;
import java.util.List;

public class Model {
    private double tnext;// найближча подыя
    private double tcurr;
    private double t0, // початок подыъ
            t1; // кынець подыъ
    private double delayCreate, delayProcess;
    private int numCreate, numProcess, failure;
    private int state, maxqueue, queue;
    private int nextEvent;

//    private List<Double> waitingTimeList;
//    private List<Double> serviceTimeList;
//    private List<Integer> stateList;
//    private double r;

    public Model(double delay0, double delay1) {
        delayCreate = delay0;
        delayProcess = delay1;
        tnext = 0.0;
        tcurr = tnext;
        t0 = tcurr;
        t1 = Double.MAX_VALUE;
        maxqueue = 0;
//        waitingTimeList = new ArrayList<>();
//        serviceTimeList = new ArrayList<>();
//        stateList = new ArrayList<>();
    }

    public Model(double delay0, double delay1, int maxQ) {
        delayCreate = delay0;
        delayProcess = delay1;
        tnext = 0.0;
        tcurr = tnext;
        t0 = tcurr;
        t1 = Double.MAX_VALUE;
        maxqueue = maxQ;
//        waitingTimeList = new ArrayList<>();
//        serviceTimeList = new ArrayList<>();
//        stateList = new ArrayList<>();
//        r = 0;
    }

    public void simulate(double timeModeling) {
        while (tcurr < timeModeling) { //	доки поточний час менший за час моделювання

//            if (state == 1) {
//                stateList.add(state);
//                serviceTimeList.add(t1 - t0);
//                this.waitingTimeList.add(t0 - tnext);

//            }
            tnext = t0;
            nextEvent = 0;

            if (t1 < tnext) { //3)	знайти найменший із моментів часу «момент надходження вимоги у систему»
                //// та «момент звільнення пристрою обслуговування» і запам’ятати, якій події  він відповідає;
                tnext = t1;
                nextEvent = 1;
            }
            tcurr = tnext;
            switch (nextEvent) {
                case 0:
                    event0();
                    break;
                case 1:
                    event1();
                    break;
            }
            printInfo();

        }
        printStatistic();
//        System.out.println("Average waiting time= " + getAverageWaitingTime());
//        System.out.println("Average service time= " + getAverageServiceTime());
//        System.out.println("Average load= " + getSumWServiceTime(timeModeling));
    }

    public void printStatistic() {
        System.out.println(" numCreate= " + numCreate + " numProcess = " + numProcess + " failure = " + failure);
    }

    public void printInfo() {
        System.out.println(" t= " + tcurr + " state = " + state + " queue = " + queue);
    }

    public void event0() {
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

    public void event1() {
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

//    private Double getAverageWaitingTime() {
//        return waitingTimeList.stream().mapToDouble(i -> i).average().getAsDouble();
//    }
//
//    private Double getAverageServiceTime() {
//        System.out.println(serviceTimeList);
//        return serviceTimeList.stream().mapToDouble(i -> i).average().getAsDouble();
//    }
//
//    private Double getSumWServiceTime(double timeModeling) {
//        double number = 0;
//        for (int i = 0; i < serviceTimeList.size() - 1; i++) {
//            number += serviceTimeList.get(i) * stateList.get(i);
//        }
//        return number / timeModeling;
//    }
}
