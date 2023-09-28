package pvs.task;

public class TestSwitch {

    public void emptySwitchStatement() {
        int a = 0;
        switch (a = 1) {
        }
    }

    public void colonSwitchStatement() {
        for (int i = 0; i < 10; i++) {
            System.out.println("--- sw for " + i + " ---");
            switch (i) {
                default:
                    System.out.println("default");

                case 1:
                    System.out.println("one");
                    break;
                case 2:
                case 3:
                    System.out.println("two or three");
                case 4:
                    System.out.println("three");
                    break;
            }
        }
    }

//    public int arrowSwitchStatement(int value) {
//        int a = 0;
//        switch (value) {
//            case 1 -> {
//                a = 10;
//            }
//            case 2, 3 -> a = 20;
//            case 4 -> a += 5;
//            default -> a = 100;
//        }
//        return a;
//    }

//
//    public int method3(int value) {
//        return switch (value) {
//            case 1 -> {
//                yield 10;
//            }
//            case 2, 3 -> 20;
//            case 4 -> 25;
//            default -> 100;
//        };
//
//    }

}
