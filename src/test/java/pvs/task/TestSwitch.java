package pvs.task;

public class TestSwitch {

//    public int hhh() {
//        System.out.println(System.currentTimeMillis());
//        return (int) System.currentTimeMillis();
//    }
//
//    public void emptySwitchStatement(int a) {
//        //int a = 0;
//        switch (hhh()) {
//        }
//    }

    public void colonSwitchStatement() {
        for (int i = 0; i < 10; i++) {
            System.out.println("--- sw for " + i + " ---");
            switch (i) {
                default:
                    System.out.println("default");
                case 1:
                    switch (i*2) {
                        case 6:

                            case 7:
                            //efe
                        {{{}
                            i =4;
                            //break;
                            //rg
                        } };break;        //greg
                    }
                    System.out.println("one");
                    break;
                case 2:
                case 3:
                    System.out.println("two or three");
                case 4:
                    System.out.println("four");
                    break;
            }
        }
    }

//    public void stringSwitchStatementColon(String v) {
//        switch (v) {
//            case "one":
//                System.out.println("1");
//                break;
//            default:
//                System.out.println("default");
//            case "undef":
//                System.out.println("undef");
//                break;
//            case "two":
//                System.out.println("2");
//            case "three":
//                System.out.println("3");
//                break;
//            case "":
//                System.out.println("empty");
//        }
//    }

//    public void colonSwitchStatement2() {
//        for (int i = 0; i < 10; i++) {
//            System.out.println("--- sw for " + i + " ---");
//            switch (i) {
//                case 1:
//                    System.out.println("one");
//                case 3:
//                    break;
//                default:
//                    System.out.println("default");
//                case 4:
//                    System.out.println("two or three");
//                    break;
//                case 5:
//                case 2:
//                    System.out.println("three");
//                    break;
//            }
//        }
//    }

//    public int arrowSwitchStatement(int value) {
//        int a = 0;
//        switch (value) {
//            case 1 -> {
//                a = 10;
//                a += 2;
//            }
//            default -> a = 100;
//            case 2, 3 -> a = 20;
//            case 4 -> a += 5;
//        }
//        return a;
//    }


//    public int method3(int value) {
//        return switch (value) {
//            default -> 100;
//            case 1 -> {
//                yield 10;
//            }
//            case 2, 3 -> 20;
//            case 4 -> 25;
//        };
//
//    }

}
