package modern.java;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Option {
    public static void main(String[] args) {
        Optional<Integer> optionalValue = IntegerList.get(2);
        //optionalをorElseでnullのときの代替値にできる
        Integer i = optionalValue.orElse(-1) + 100;

        System.out.println(i);
    }
}

/**
 * OptionalはNullになりえる値をラップして返すのに使える
 * Nullチェックを簡潔にし、Nullがあり得ることを明示できる
 */
class IntegerList {
    private final static List<Integer> INTEGER_LIST = Arrays.asList(new Integer[]{1,2,null});

    public static Optional<Integer> get(int index) {
        //ラップして返す
        return Optional.ofNullable(INTEGER_LIST.get(index));
    }
}
