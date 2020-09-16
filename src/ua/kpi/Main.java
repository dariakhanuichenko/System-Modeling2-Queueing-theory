package ua.kpi;

public class Main {

    public static void main(String[] args) {
            Model model = new Model(2,1,5);
            model.simulate(1000, true);

        for (int i = 0; i <10 ; i++) {
            model = new Model(1 + Math.random() * 4, 1 + Math.random() * 6, (int) (1 + Math.random() * 9));
            model.simulate(1000, false);
        }
    }
}
