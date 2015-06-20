package OnlineStore;

import java.util.Random;

public class Supplier {

    private String name;

    public Supplier(String name) {
        this.name = name;
    }

    public void supply(Pair<Product,Integer> p) {

        try {
            Thread.sleep(new Random().nextInt(10000) + 1);
            p.setR(p.getR() + (new Random().nextInt(5) + 1));
            p.getL().setAvailability(true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("produto "+ p.getL().getName() +" restocado");
    }

    public String getName() {
        return name;
    }
}
