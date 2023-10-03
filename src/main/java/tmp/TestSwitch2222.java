package tmp;


import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TestSwitch2222 {

/*
    public String onlyDefaultSwitchExpression(String value) {
        return switch (value) {
            default -> "kukue " + value;
        };
    }

    public int switchExpression(int value) {
        return //aaa
                switch (value) {
                    default -> 100;
                    case 1 -> {
                        log("143");
                        yield switch (value * 2) {
                            case 0 -> 1;
                            default -> 1;

                        };
                    }
                    case 2, 3 -> 20;
                    case 4 -> {
                        {
                            var a = switch (value) {
                                case 2 -> {
                                    log("two");
                                    yield 2;
                                }
                                default -> 12;
                            };

                            log("asd " + a);
                        }
                        { //
                            {
                                {
                                    {
                                      */   /*wwqd*/ /*
                                    }
                                }
                                //
                                yield 234;//
                            }
                        }
                    }
                };
    }


    public void switchExpressionCase() {
        IntStream.range(0, 10).forEach(i -> log("for " + i + " switch returns " + switchExpression(i)));
    }

    public void switchExpression2() {
        log("aaaaa");

        Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12).forEach(i -> log("aaaa " + i));

        Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12).forEach(i -> log("aaaa " + switch (i) {
            default -> 100;
            case 1 -> {
                yield 10;
            }
            case 2, 3 -> 20;
            case 4 -> 25;
        }));

*/
//
//        // в лямбдах ложка скипает свичи
//        IntStream.range(0, 10).forEach(i -> log("aaaa " + switch (i) {
//            default -> 100;
//            case 1 -> {
//                yield 10;
//            }
//            case 2, 3 -> 20;
//            case 4 -> 25;
//        }));
//       }

//    public void switchExpressionHard() {
//        IntStream.range(0, 10).map(i -> switch (i) {
//            default -> 100;
//            case 1 -> {
//                yield 10;
//            }
//            case 2, 3 -> 20;
//            case 4 -> 25;
//        }).forEach(i -> log("hard case " + i));
//    }

    // кажется оно в превью.
//    public void switchPatternMatching(){
//        final var items = new ArrayList<Object>();
//        items.add("aaaaaaaaaaaaaaa");
//        items.add(Integer.valueOf(100));
//        items.add(new ArrayList<Double>());
//
//        for(var item:items){
//            log(switch (item){
//                case Integer i -> "integer" + i;
//                default -> "a";
//
//            });
//        }
//    }

}
